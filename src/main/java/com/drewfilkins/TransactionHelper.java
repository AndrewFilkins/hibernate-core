package com.drewfilkins;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Function;

@Component
public class TransactionHelper {

    private final SessionFactory sessionFactory;

    public TransactionHelper(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void executeInTransaction(Consumer<Session> action) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.getCurrentSession();
            System.out.println("session = " + session);
            transaction = session.getTransaction();

            if (!transaction.getStatus().equals(TransactionStatus.NOT_ACTIVE)) {
                action.accept(session);
            }

            transaction.begin();

            action.accept(session);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public <T> T executeInTransaction(Function<Session, T> action) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.getCurrentSession();
            System.out.println("session = " + session);
            transaction = session.getTransaction();

            if (!transaction.getStatus().equals(TransactionStatus.NOT_ACTIVE)) {
                return action.apply(session);
            }

            transaction.begin();

            var result = action.apply(session);

            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            if (session != null && (transaction != null && transaction.getStatus().equals(TransactionStatus.NOT_ACTIVE))) {
                session.close();
            }
        }
    }
}
