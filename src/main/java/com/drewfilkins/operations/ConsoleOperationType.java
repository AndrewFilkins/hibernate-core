package com.drewfilkins.operations;

public enum ConsoleOperationType {

    USER_CREATE ("Создание нового пользователя."),
    SHOW_ALL_USERS ("Отображение списка всех пользователей."),
    ACCOUNT_CREATE ("Создание нового счета для пользователя."),
    ACCOUNT_CLOSE ("Закрытие счета."),
    ACCOUNT_DEPOSIT ("Пополнение счета."),
    ACCOUNT_TRANSFER ("Перевод средств между счетами."),
    ACCOUNT_WITHDRAW ("Снятие средств со счета.");

    private final String description;

    ConsoleOperationType(String description) {
        this.description = description;
    }

    public String getName() {
        return this.name();
    }

    public String getDescription() {
        return description;
    }
}