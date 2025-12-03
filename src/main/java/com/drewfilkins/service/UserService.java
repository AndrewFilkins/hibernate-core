package com.drewfilkins.service;

import com.drewfilkins.TransactionHelper;
import com.drewfilkins.model.User;
import com.drewfilkins.repository.AccountRepository;
import com.drewfilkins.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final AccountService accountService;
    private final UserRepository userRepository;
    private final SessionFactory sessionFactory;
    private final TransactionHelper transactionHelper;

    @PostConstruct
    public void init() {
        accountService.setUserService(this);
    }

    public UserService(AccountService accountService, UserRepository userRepository, AccountRepository accountRepository, SessionFactory sessionFactory, TransactionHelper transactionHelper) {
        this.accountService = accountService;
        this.userRepository = userRepository;
        this.sessionFactory = sessionFactory;
        this.transactionHelper = transactionHelper;
    }

    public Optional<User> getUserById(Session session, int id) {
        return userRepository.findById(session, id);
    }

    public void userCreate(String login) {
        Integer createdUserId = transactionHelper.executeInTransaction(session -> {
            Integer id;
            Optional<User> user = userRepository.findByLogin(session, login);
            if (user.isPresent()) {
                throw new IllegalArgumentException("User with login=%s already exists!%n".formatted(login));
            } else {
                User newUser = new User(login);
                session.persist(newUser);
                accountService.createAccount(newUser);
                id = newUser.getId();
            }
            return id;
        });
        System.out.println("User created: " + getUserById(sessionFactory.openSession(), createdUserId));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll(sessionFactory.openSession());
    }
}
