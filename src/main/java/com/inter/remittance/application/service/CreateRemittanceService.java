package com.inter.remittance.application.service;

import com.inter.remittance.application.command.CreateRemittanceCommand;
import com.inter.remittance.domain.commom.ErrorCatalog;
import com.inter.remittance.domain.commom.Exceptions.BusinessException;
import com.inter.remittance.domain.entities.*;
import com.inter.remittance.domain.enums.Currency;
import com.inter.remittance.domain.enums.TransactionStatus;
import com.inter.remittance.domain.enums.TransactionType;
import com.inter.remittance.domain.repositories.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static com.inter.remittance.domain.entities.Wallet.DEFAULT_ORIGIN_CURRENCY;

@Service
public class CreateRemittanceService {
    private final RemittanceRepository remittanceRepository;
    private final AccountRepository accountRepository;
    private final CurrencyConversionService currencyConversionService;
    private final GetCachedValuesService getCachedValuesService;
    private final UpdateExchangeRateService updateExchangeRateService;
    private final WalletRepository walletRepository;
    private final DailyTransactionLimitRepository dailyTransactionLimitRepository;
    private final TransactionRepository transactionRepository;

    public CreateRemittanceService(
            RemittanceRepository remittanceRepository,
            AccountRepository accountRepository,
            CurrencyConversionService currencyConversionService,
            GetCachedValuesService getCachedValuesService,
            UpdateExchangeRateService updateExchangeRateService,
            WalletRepository walletRepository1,
            DailyTransactionLimitRepository dailyTransactionLimitRepository,
            TransactionRepository transactionRepository
    ) {
        this.remittanceRepository = remittanceRepository;
        this.accountRepository = accountRepository;
        this.currencyConversionService = currencyConversionService;
        this.getCachedValuesService = getCachedValuesService;
        this.updateExchangeRateService = updateExchangeRateService;
        this.walletRepository = walletRepository1;
        this.dailyTransactionLimitRepository = dailyTransactionLimitRepository;
        this.transactionRepository = transactionRepository;
    }
    private static final Logger log = LoggerFactory.getLogger(CreateRemittanceService.class);

    public Map<Transaction, Remittance> process(CreateRemittanceCommand command) {
        validateCreateRemittanceCommand(command);
        log.info("Processing remittance transaction from account {} to account {} with non converted amount {}",
                command.sourceAccountId(),
                command.destinationAccountId(),
                command.amount()
        );

        Currency destinationCurrency = getDestinationCurrency(command.destinationCurrency());

        Account sourceAccount = getAccountById(command.sourceAccountId());
        Account destinationAccount = getAccountById(command.destinationAccountId());

        BigDecimal transactionOriginCurrencyAmount = command.amount();

        DailyTransactionLimit transactionSourceDailyLimit = sourceAccount.person().dailyTransactionLimits().stream().filter(
                dailyTransactionLimit -> dailyTransactionLimit.currency().equals(DEFAULT_ORIGIN_CURRENCY)
        ).findFirst().orElseThrow(() -> new BusinessException(ErrorCatalog.DAILY_LIMIT_EXCEEDED));

        transactionSourceDailyLimit.validateAvailableDailyLimit(transactionOriginCurrencyAmount);

        Wallet sourceWallet = getWalletByAccountAndCurrency(sourceAccount, DEFAULT_ORIGIN_CURRENCY);

        Wallet destinationWallet = getWalletByAccountAndCurrency(destinationAccount, destinationCurrency);

        sourceWallet.validateAvailableBalance(transactionOriginCurrencyAmount);

        BigDecimal exchangeRate = exchangeRate(destinationWallet.currency());

        BigDecimal convertedAmount = currencyConversionService.convert(
                transactionOriginCurrencyAmount,
                sourceWallet.currency(),
                destinationCurrency,
                exchangeRate
        );

        Transaction transaction = Transaction.createNewTransaction(
                sourceWallet,
                destinationWallet,
                transactionOriginCurrencyAmount,
                TransactionType.REMITTANCE
        );

        transaction = transaction.transitionStatusTo(transaction, TransactionStatus.PENDING);

        Remittance remittance = Remittance.createNew(
                transaction,
                exchangeRate,
                convertedAmount,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Transaction updated;

        try {
            log.info("Updating monetary amounts for remittance transaction from account {} to account {} with non converted amount {}",
                    command.sourceAccountId(),
                    command.destinationAccountId(),
                    command.amount()
            );
            updateMonetaryAmounts(
                     sourceWallet,
                     destinationWallet,
                     transactionOriginCurrencyAmount,
                     convertedAmount,
                     transactionSourceDailyLimit
            );
            updated = transaction.transitionStatusTo(transaction, TransactionStatus.FINISHED_WITH_SUCCESS);
        } catch (Exception ex) {
            log.error("Error processing remittance transaction from account {} to account {} with non converted amount {}",
                    command.sourceAccountId(),
                    command.destinationAccountId(),
                    command.amount(),
                    ex
            );
            updated = transaction.transitionStatusTo(transaction, TransactionStatus.FINISHED_WITH_FAILURE);
            transactionRepository.save(updated);
            remittanceRepository.save(remittance, sourceWallet.id());
            throw ex;
        }

        Transaction persistedTransaction = transactionRepository.save(updated);
        Remittance persistedRemittance = remittanceRepository.save(remittance, sourceWallet.id());

        log.info("Remittance transaction processed from account {} to account {} with non converted amount {}. Transaction status: {}",
                command.sourceAccountId(),
                command.destinationAccountId(),
                command.amount(),
                persistedTransaction.status()
        );

        return Map.of(persistedTransaction, persistedRemittance);
    }


    @Transactional
    void updateMonetaryAmounts(
            Wallet sourceWallet,
            Wallet destinationWallet,
            BigDecimal amountToDebitFromSource,
            BigDecimal amountToAddOnDestination,
            DailyTransactionLimit dailyTransactionLimit
    ){

        Wallet sourceUpdated = sourceWallet.withdraw(amountToDebitFromSource);
        walletRepository.update(sourceUpdated);

        DailyTransactionLimit limitUpdated = dailyTransactionLimit.debit(amountToDebitFromSource);
        dailyTransactionLimitRepository.update(limitUpdated);

        Wallet destinationUpdated = destinationWallet.addBalance(amountToAddOnDestination);
        walletRepository.update(destinationUpdated);
    }

    private Account getAccountById(UUID accountId){
        return accountRepository.findWithDetailsById(accountId);
    }

    private BigDecimal exchangeRate(Currency to){
        return getConversionRate(to);
    }

    private BigDecimal getConversionRate(Currency to){
        BigDecimal exchangeRate = getCachedValuesService
                .getCachedValue("exchangeRate","#currency", BigDecimal.class);
        return exchangeRate == null ?
                updateExchangeRateService.execute(to) :
                exchangeRate;
    }

    private Wallet getWalletByAccountAndCurrency(Account account, Currency currency){
        return account.wallets().stream()
                .filter(wallet -> wallet.currency().equals(currency))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    private Currency getDestinationCurrency(Currency currency){
        return currency == null
                ? Currency.USD
                : currency;
    }

    private void validateCreateRemittanceCommand(CreateRemittanceCommand command) {
        if (command.sourceAccountId() == null) {
            throw new BusinessException(ErrorCatalog.INVALID_REMITTANCE_SOURCE_ACCOUNT_ID);
        }
        if (command.destinationAccountId() == null) {
            throw new BusinessException(ErrorCatalog.INVALID_REMITTANCE_DESTINATION_ACCOUNT_ID);
        }
        if (command.amount() == null || command.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ErrorCatalog.INVALID_REMITTANCE_DESTINATION_AMOUNT);
        }

        if (
                command.sourceCurrency().equals(command.destinationCurrency())
        ) {
            throw new BusinessException(ErrorCatalog.INVALID_REMITTANCE);
        }
    }
}
