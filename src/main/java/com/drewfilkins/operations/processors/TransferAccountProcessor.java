package com.drewfilkins.operations.processors;

import com.drewfilkins.operations.ConsoleOperationType;
import com.drewfilkins.service.AccountService;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class TransferAccountProcessor implements OperationCommandProcessor {

    private final Scanner scanner;
    private final AccountService accountService;

    public TransferAccountProcessor(Scanner scanner, AccountService accountService) {
        this.scanner = scanner;
        this.accountService = accountService;
    }

    @Override
    public void process() {
        System.out.println("Enter source account ID:");
        int sourceAccountId;
        try {
            sourceAccountId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Account id should be an integer!");
        }
        System.out.println("Enter target account ID:");
        int targetAccountId;
        try {
            targetAccountId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Account id should be an integer!");
        }
        System.out.println("Enter amount to transfer:");
        int amount;
        try {
            amount = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Account id should be an integer!");
        }
        accountService.accountTransfer(sourceAccountId, targetAccountId, amount);
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_TRANSFER;
    }
}
