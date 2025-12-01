package com.drewfilkins.operations.processors;

import com.drewfilkins.operations.ConsoleOperationType;
import com.drewfilkins.service.AccountService;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class WithdrawAccountProcessor implements OperationCommandProcessor {

    private final Scanner scanner;
    private final AccountService accountService;

    public WithdrawAccountProcessor(Scanner scanner, AccountService accountService) {
        this.scanner = scanner;
        this.accountService = accountService;
    }

    @Override
    public void process() {
        System.out.println("Enter account ID:");
        int accountId;
        try {
            accountId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Account id should be an integer!");
        }
        System.out.println("Enter amount to withdraw:");
        int amount;
        try {
            amount = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Account id should be an integer!");
        }
        accountService.accountWithdraw(accountId, amount);
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_WITHDRAW;
    }
}
