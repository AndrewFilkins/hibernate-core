package com.drewfilkins.model;

import jakarta.persistence.*;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(cascade = {CascadeType.MERGE}, fetch= FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User user;
    private Integer moneyAmount;

    public Account() {
    }

    public Account(User user, Integer moneyAmount) {
        this.user = user;
        this.moneyAmount = moneyAmount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(Integer moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", userId=" + user.getId() +
                ", moneyAmount=" + moneyAmount +
                '}';
    }
}
