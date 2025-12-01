package com.drewfilkins;

import com.drewfilkins.model.Account;
import com.drewfilkins.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class HibernateConfiguration {

    @Bean
    public SessionFactory sessionFactory() {
        Configuration configuration = new Configuration();
        configuration
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Account.class)
                .addPackage("com.drewfilkins")
                .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
                .setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5433/bank_db")
                .setProperty("hibernate.connection.username", "postgres")
                .setProperty("hibernate.connection.password", "admin")
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.hbm2ddl.auto", "update")
                .setProperty("hibernate.current_session_context_class", "thread");

        return configuration.buildSessionFactory();
    }
}
