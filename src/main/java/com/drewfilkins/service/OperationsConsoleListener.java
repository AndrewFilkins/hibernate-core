package com.drewfilkins.service;

import com.drewfilkins.operations.ConsoleOperationType;
import com.drewfilkins.operations.processors.OperationCommandProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class OperationsConsoleListener {

    private final Scanner scanner;
    private final Map<ConsoleOperationType, OperationCommandProcessor> processorMap;

    public OperationsConsoleListener(Scanner scanner, List<OperationCommandProcessor> operationCommandProcessorList) {
        this.scanner = scanner;
        this.processorMap = operationCommandProcessorList
                .stream()
                .collect(
                        Collectors.toMap(
                                OperationCommandProcessor::getOperationType,
                                proccessor -> proccessor
                        )
                );
    }

    public void start() {
        System.out.println("Console listener is stated!");
    }

    public void endListen() {
        System.out.println("Console listener is stopped!");
    }

    public void listenToUpdates() {
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println("\nPlease enter one of operation type:");
            printAllAvailableOperations();
            var operationType = listenNextOperation();
            proccessNextOperation(operationType);
        }

    }

    private void printAllAvailableOperations() {
        processorMap.keySet().forEach(command -> System.out.println("- " + command));
    }

    private ConsoleOperationType listenNextOperation() {
        while (!Thread.currentThread().isInterrupted()) {
            var nextOperation = scanner.nextLine();
            try {
                return ConsoleOperationType.valueOf(nextOperation);
            } catch (IllegalArgumentException e) {
                System.out.println("No such operation!");
            }
        }
        return null;
    }

    private void proccessNextOperation(ConsoleOperationType operationType) {
        try {
            var processor = processorMap.get(operationType);
            processor.process();
        } catch (Exception e) {
            System.out.printf("Error executing operation %s: error = %s%n", operationType, e.getMessage());
        }
    }

}