package com.wallet.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import java.util.Date;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int accountNumber;

    @Min(value = 10000, message = "Minimum balance must be at least 10,000 Rials")
    private double balance;

    private Date creationDate;

    private String shabaNumber;

    // getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getShabaNumber() {
        return shabaNumber;
    }

    public void setShabaNumber(String shabaNumber) {
        this.shabaNumber = shabaNumber;
    }
}