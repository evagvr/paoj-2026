package com.pao.project.bankingApp.model.account;

import com.pao.project.bankingApp.model.enums.Currency;
import com.pao.project.bankingApp.exception.InsufficientFundsException;

public class CurrentAccount extends Account {
    private final double monthlyFee;
    private final double overdraftLimit;
    public CurrentAccount(String clientId, Currency currency, double overdraftLimit, double monthlyFee){
        super( clientId, currency);
        this.overdraftLimit = overdraftLimit;
        this.monthlyFee = monthlyFee;
    }
    public void applyMonthlyFee(){
        balance -= monthlyFee;
    }
    public boolean isInOverdraft(){
        return balance < 0;
    }
    public double getAvailableBalance(){
        return balance + overdraftLimit;
    }
    @Override
    public boolean withdraw(double amount){
        if (balance + overdraftLimit < amount) {
            throw new InsufficientFundsException(balance + overdraftLimit, amount);
        }
        balance -= amount;
        return true;
    }
    @Override
    public String getAccountSummary(){
        return "Current Account " +
                " IBAN " + getIban() +
                " Balance " + balance+
                " Overdraft "+ overdraftLimit +
                " Monthly fee " + monthlyFee;
    }
}
