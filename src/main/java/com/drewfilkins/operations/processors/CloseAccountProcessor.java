package com.drewfilkins.operations.processors;

import com.drewfilkins.operations.ConsoleOperationType;
import com.drewfilkins.service.AccountService;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class CloseAccountProcessor implements OperationCommandProcessor {

    private final Scanner scanner;
    private final AccountService accountService;

    public CloseAccountProcessor(Scanner scanner, AccountService accountService) {
        this.scanner = scanner;
        this.accountService = accountService;
    }

    @Override
    public void process() {
        System.out.println("Enter account ID to close:");
        int accountId;
        try {
            accountId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Account id should be an integer!");
        }
        accountService.accountClose(accountId);
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_CLOSE;
    }
}
