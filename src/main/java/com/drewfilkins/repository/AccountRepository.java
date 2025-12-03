package com.drewfilkins.repository;


import com.drewfilkins.model.Account;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AccountRepository {

    public Optional<Account> findById(Session session, int accountId) {
        return Optional.ofNullable(session.get(Account.class, accountId));
    }

    public void deleteAccount(Session session, Account account) {
        session.remove(account);
    }
}
