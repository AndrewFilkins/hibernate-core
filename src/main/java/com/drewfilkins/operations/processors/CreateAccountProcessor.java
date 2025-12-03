package com.drewfilkins.operations.processors;

import com.drewfilkins.operations.ConsoleOperationType;
import com.drewfilkins.service.AccountService;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class CreateAccountProcessor implements OperationCommandProcessor {

    private final Scanner scanner;
    private final AccountService accountService;

    public CreateAccountProcessor(Scanner scanner, AccountService accountService) {
        this.scanner = scanner;
        this.accountService = accountService;
    }

    @Override
    public void process() {
        System.out.println("Enter the user id for which to create an account:");
        int accountId;
        try {
            accountId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Account id should be an integer!");
        }
        accountService.createAccountByUserId(accountId);
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_CREATE;
    }
}
