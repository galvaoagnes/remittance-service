package com.inter.remittance.application.service;

import com.inter.remittance.application.command.CreateWithdrawalCommand;
import com.inter.remittance.domain.commom.ErrorCatalog;
import com.inter.remittance.domain.entities.Account;
import com.inter.remittance.domain.entities.Transaction;
import com.inter.remittance.domain.entities.Wallet;
import com.inter.remittance.domain.enums.TransactionStatus;
import com.inter.remittance.domain.enums.TransactionType;
import com.inter.remittance.domain.repositories.AccountRepository;
import com.inter.remittance.domain.repositories.TransactionRepository;
import com.inter.remittance.domain.repositories.WalletRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateWithdrawalService {
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public CreateWithdrawalService(WalletRepository walletRepository, TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }


    public Transaction process(CreateWithdrawalCommand command) {
        Account account = accountRepository.findWithDetailsById(command.sourceAccountId());
        Wallet walletToUpdateBalance = account.wallets().stream().filter(
                        wallet -> wallet.currency().equals(command.sourceCurrency()))
                .findFirst().orElseThrow(
                        () -> new RuntimeException(ErrorCatalog.WALLET_NOT_FOUND)
                );


        Transaction transaction = transactionRepository.save(Transaction.createNewTransaction(
                        walletToUpdateBalance,
                        null,
                        command.amount(),
                        TransactionType.WITHDRAWAL
        ));

        transaction = transaction.transitionStatusTo(transaction, TransactionStatus.PENDING);

        Transaction updated;
        try {

            Wallet updatedWallet = walletToUpdateBalance.withdraw(command.amount());
            updated = transaction.transitionStatusTo(transaction, TransactionStatus.FINISHED_WITH_SUCCESS);
            walletRepository.update(updatedWallet);
        } catch (Exception ex){
            updated = transaction.transitionStatusTo(transaction, TransactionStatus.FINISHED_WITH_FAILURE);
            transactionRepository.save(updated);
            throw ex;
        }

        return transactionRepository.save(updated);
    }
}
