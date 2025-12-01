package com.drewfilkins.operations.processors;

import com.drewfilkins.operations.ConsoleOperationType;

public interface OperationCommandProcessor {

    void process();

    ConsoleOperationType getOperationType();
}
