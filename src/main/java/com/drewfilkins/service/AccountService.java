package com.drewfilkins.service;

import com.drewfilkins.TransactionHelper;
import com.drewfilkins.model.Account;
import com.drewfilkins.model.User;
import com.drewfilkins.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    UserService userService;
    AccountRepository accountRepository;
    private final TransactionHelper transactionHelper;

    @Value("${account.default-amount}")
    private Integer defaultMoneyAmount;

    @Value("${account.transfer-commission}")
    private Integer transferCommission;

    public AccountService(AccountRepository accountRepository, TransactionHelper transactionHelper) {
        this.accountRepository = accountRepository;
        this.transactionHelper = transactionHelper;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public Account createAccount(User user) {
        return transactionHelper.executeInTransaction(session -> {
            Account account = new Account(user, defaultMoneyAmount);
            session.persist(account);
            return account;
        });
    }

    public Account createAccountByUserId(int userId) {
        return transactionHelper.executeInTransaction(session -> {
            Optional<User> user = userService.getUserById(session, userId);
            Account account = null;
            if (user.isPresent()) {
                User currUser = user.get();
                account = createAccount(currUser);
                System.out.printf("New account created with ID: %d for user: %s%n", account.getId(), user.get().getLogin());
                session.persist(account);
            } else {
                throw new IllegalArgumentException("There's no user with id=%d".formatted(userId));
            }
            return account;
        });
    }

    public void accountDeposit(int accountId, int amount) {
        if (amount > 0) {
            transactionHelper.executeInTransaction(session -> {
                Optional<Account> account = accountRepository.findById(session, accountId);
                if (account.isPresent()) {
                    Account currAccount = account.get();
                    currAccount.setMoneyAmount(currAccount.getMoneyAmount() + amount);
                    session.persist(currAccount);
                    System.out.printf("Amount %d deposited to account ID: %d%n", amount, accountId);
                } else {
                    throw new IllegalArgumentException("There's no account with id=%d".formatted(accountId));
                }
            });
        } else {
            throw new IllegalArgumentException("Amount should be greater than zero!");
        }
    }

    public void accountWithdraw(int accountId, int amountToWithdraw) {
        if (amountToWithdraw > 0) {
            transactionHelper.executeInTransaction(session -> {
                Optional<Account> account = accountRepository.findById(session, accountId);
                if (account.isPresent()) {
                    Account currAccount = account.get();
                    int currAmount = currAccount.getMoneyAmount();
                    if (currAmount >= amountToWithdraw) {
                        currAccount.setMoneyAmount(currAmount - amountToWithdraw);
                        System.out.printf("Amount %d withdrawn from account ID: %d%n", amountToWithdraw, accountId);
                    } else {
                        throw new IllegalArgumentException("Not enough funds to withdraw: account id=%d, amount=%d, attemptedWithdraw=%d%n".formatted(accountId, currAmount, amountToWithdraw));
                    }
                } else {
                    throw new IllegalArgumentException("There's no account with id=%d".formatted(accountId));
                }
            });
        } else {
            throw new IllegalArgumentException("Amount should be greater than zero!");
        }
    }

    public void accountClose(int accountId) {
        transactionHelper.executeInTransaction(session -> {
            Optional<Account> account = accountRepository.findById(session, accountId);
            if (account.isPresent()) {
                Account currAccount = account.get();
                List<Account> accountList = currAccount.getUser().getAccountList();
                if (accountList.size() > 1) {
                    accountList.sort(Comparator.comparingInt(Account::getId));
                    Account firstAccount = accountList.get(0);
                    if (firstAccount.equals(currAccount)) {
                        firstAccount = accountList.get(1);
                    }
                    firstAccount.setMoneyAmount(firstAccount.getMoneyAmount() + currAccount.getMoneyAmount());
                    accountRepository.deleteAccount(session, currAccount);
                    System.out.printf("Account with ID=%d has been closed.%n", accountId);
                } else {
                    throw new IllegalArgumentException("Could not close account with ID=%d because it's the only account for user with ID=%d%n".formatted(accountId, currAccount.getUser().getId()));
                }

            } else {
                throw new IllegalArgumentException("There's no account with id=%d".formatted(accountId));
            }
        });
    }

    public void accountTransfer(int sourceAccountId, int targetAccountId, int amountToTransfer) {
        transactionHelper.executeInTransaction(session -> {
            Optional<Account> sourceAccount = accountRepository.findById(session, sourceAccountId);
            Optional<Account> targetAccount = accountRepository.findById(session, targetAccountId);
            if (sourceAccount.isPresent()) {
                if (targetAccount.isPresent()) {
                    Account actualSourceAccount = sourceAccount.get();
                    Account actualTargetAccount = targetAccount.get();

                    int actualSourceAccountMoneyAmount = actualSourceAccount.getMoneyAmount();
                    if (actualSourceAccount.getUser().equals(actualTargetAccount.getUser())) {
                        if (actualSourceAccountMoneyAmount >= amountToTransfer) {
                            actualSourceAccount.setMoneyAmount(actualSourceAccountMoneyAmount - amountToTransfer);
                        } else {
                            throw new IllegalArgumentException("Not enough funds to transfer: account id=%d, amount=%d, amountToTransfer=%d%n".formatted(sourceAccountId, actualSourceAccount.getMoneyAmount(), amountToTransfer));
                        }
                    } else {
                        if (actualSourceAccountMoneyAmount >= amountToTransfer + amountToTransfer * transferCommission / 100) {
                            actualSourceAccount.setMoneyAmount(actualSourceAccountMoneyAmount - amountToTransfer - amountToTransfer * transferCommission / 100);
                        } else {
                            throw new IllegalArgumentException("Not enough funds to transfer: account id=%d, amount=%d, amountToTransfer=%d%n".formatted(sourceAccountId, actualSourceAccount.getMoneyAmount(), amountToTransfer));
                        }
                    }
                    actualTargetAccount.setMoneyAmount(actualTargetAccount.getMoneyAmount() + amountToTransfer);
                    System.out.printf("Amount %d transferred from account ID %d to account ID %d.%n", amountToTransfer, sourceAccountId, targetAccountId);

                } else {
                    throw new IllegalArgumentException("There's no account with ID=%d!%n".formatted(targetAccountId));
                }
            } else {
                throw new IllegalArgumentException("There's no account with ID=%d!%n".formatted(sourceAccountId));
            }
        });
    }

}
