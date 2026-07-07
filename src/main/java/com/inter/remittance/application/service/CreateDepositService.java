package com.inter.remittance.application.service;

import com.inter.remittance.application.command.CreateDepositCommand;
import com.inter.remittance.domain.commom.ErrorCatalog;
import com.inter.remittance.domain.entities.Account;
import com.inter.remittance.domain.entities.Transaction;
import com.inter.remittance.domain.entities.Wallet;
import com.inter.remittance.domain.enums.TransactionStatus;
import com.inter.remittance.domain.enums.TransactionType;
import com.inter.remittance.domain.repositories.AccountRepository;
import com.inter.remittance.domain.repositories.TransactionRepository;
import com.inter.remittance.domain.repositories.WalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreateDepositService {
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public CreateDepositService(WalletRepository walletRepository, TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(CreateDepositService.class);

    public Transaction process(CreateDepositCommand command) {
        log.info("Processing deposit transaction for account {} with amount {} and currency {}",
                command.destinationAccountId(),
                command.amount(),
                command.destinationCurrency()
        );
        Account account = accountRepository.findWithDetailsById(command.destinationAccountId());
        Wallet walletToUpdateBalance = account.wallets().stream().filter(
                        wallet -> wallet.currency().equals(command.destinationCurrency()))
                .findFirst().orElseThrow(
                        () -> new RuntimeException(ErrorCatalog.WALLET_NOT_FOUND)
                );

        Transaction transaction = transactionRepository.save(
                Transaction.createNewTransaction(
                        null,
                        walletToUpdateBalance,
                        command.amount(),
                        TransactionType.DEPOSIT
                )
        );

        transaction = transaction.transitionStatusTo(transaction, TransactionStatus.PENDING);

        Transaction updated;

        try {
            Wallet updatedWallet = walletToUpdateBalance.addBalance(command.amount());
            walletRepository.update(updatedWallet);
            updated = transaction.transitionStatusTo(transaction, TransactionStatus.FINISHED_WITH_SUCCESS);
        } catch (Exception ex){
            updated = transaction.transitionStatusTo(transaction, TransactionStatus.FINISHED_WITH_FAILURE);
        }

        Transaction persistedTransaction =  transactionRepository.save(updated);
        log.info("Deposit transaction processed for account {} with amount {} and currency {}. Transaction status: {}",
                command.destinationAccountId(),
                command.amount(),
                command.destinationCurrency(),
                persistedTransaction.status()
        );

        return  persistedTransaction;
    }
}
