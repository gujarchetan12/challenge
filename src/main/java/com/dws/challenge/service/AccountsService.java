package com.dws.challenge.service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.repository.AccountsRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountsService {

    @Getter
    private final AccountsRepository accountsRepository;

    @Getter
    NotificationService notificationService;

    @Autowired
    public AccountsService(AccountsRepository accountsRepository,NotificationService notificationService) {
        this.accountsRepository = accountsRepository;
        this.notificationService = notificationService;
    }

    public void createAccount(Account account) {
        this.accountsRepository.createAccount(account);
    }

    public Account getAccount(String accountId) {
        return this.accountsRepository.getAccount(accountId);
    }

     // we can use @Transactional if we are performing database operations
    public void transferMoney(Account fromAccount, Account toAccount, BigDecimal transferAmount) {

        if (fromAccount == null || toAccount == null) {
            throw new IllegalArgumentException("Invalid account ID");
        }

        if (fromAccount.getAccountId().equals(toAccount.getAccountId())) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        // Lock this portion based on accountId
        Account firstLock = fromAccount.getAccountId().compareTo(toAccount.getAccountId()) < 0 ? fromAccount : toAccount;
        Account secondLock = fromAccount.getAccountId().compareTo(toAccount.getAccountId()) < 0 ? toAccount : fromAccount;

        firstLock.getLock().lock();
        try {
            secondLock.getLock().lock();
            try {
                if (fromAccount.getBalance().compareTo(transferAmount) < 0) {
                    throw new IllegalArgumentException("Insufficient balance");
                }
                fromAccount.setBalance(fromAccount.getBalance().subtract(transferAmount));
                toAccount.setBalance(toAccount.getBalance().add(transferAmount));
                this.accountsRepository.updateBalance(fromAccount);
                this.accountsRepository.updateBalance(toAccount);
            } finally {
                secondLock.getLock().unlock();
            }
        } finally {
            firstLock.getLock().unlock();
        }
        //we can call this in callable or in completable future to run asynch
        notificationService.notifyAboutTransfer(fromAccount,transferAmount + " rs has debited from " + fromAccount.getAccountId());
        notificationService.notifyAboutTransfer(toAccount,transferAmount + " rs has credited to " + toAccount.getAccountId());

    }
}
