package com.drewfilkins.repository;

import com.drewfilkins.model.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserRepository {

    public Optional<User> findByLogin(Session session, String login) {
        Query query = session.createQuery("from User where login = :login");
        query.setParameter("login", login);
        if (!query.list().isEmpty())
            return Optional.of((User) query.list().get(0));
        return Optional.empty();
    }

    public Optional<User> findById(Session session, int id) {
        return Optional.ofNullable(session.get(User.class, id));
    }

    public List<User> findAll(Session session) {
        try (session) {
            Query query = session.createQuery("from User");
            return query.list();
        }
    }
}
