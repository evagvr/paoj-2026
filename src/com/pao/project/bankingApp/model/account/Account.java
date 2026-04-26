package com.pao.project.bankingApp.model.account;

import com.pao.project.bankingApp.model.enums.Currency;
import com.pao.project.bankingApp.exception.InsufficientFundsException;

import java.util.Random;
import java.time.LocalDateTime;

public abstract class Account {
    private final String iban;
    private final String customerId;
    protected double balance;
    private final Currency currency;
    private final LocalDateTime openedAt;
    private boolean active;

    public Account ( String customerId, Currency currency){
        this.iban = generateIban();
        this.customerId = customerId;
        this.balance = 0.0;
        this.currency = currency;
        this.openedAt = LocalDateTime.now();
        this.active = true;
    }
    private static String generateIban(){
        String lastDigits = "";
        Random random = new Random();
        for (int i = 0; i < 16; i++)
            lastDigits += random.nextInt(10);
        return "RO" + String.format("%02d", random.nextInt(97)+ 2) + "BSFB" + lastDigits;
    }
    public String getIban(){
        return iban;
    }
    public String getCustomerId() {
        return customerId;
    }
    public LocalDateTime getOpenedAt() {
        return openedAt;
    }
    public boolean isActive() {
        return active;
    }
    public double getBalance() {
        return balance;
    }
    public Currency getCurrency() {
        return currency;
    }
    public void setActive(boolean active){
        this.active = active;
    }
    public void deposit(double amount) {
        if (amount > 0)
            balance += amount;
    }
    public abstract boolean withdraw(double amount);
    public abstract String getAccountSummary();
}
