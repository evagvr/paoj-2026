package com.pao.project.bankingApp.model.account;

import com.pao.project.bankingApp.model.enums.Currency;
import com.pao.project.bankingApp.exception.InsufficientFundsException;
import com.pao.project.bankingApp.exception.WithdrawalLimitExceededException;

public class SavingsAccount extends Account {
    private final double interestRate;
    private final int withdrawalLimit;
    private int withdrawals;
    public SavingsAccount( String customerId, Currency currency, double interestRate, int withdrawalLimit){
        super(customerId, currency);
        this.interestRate = interestRate;
        this.withdrawalLimit = withdrawalLimit;
        this.withdrawals = 0;
    }
    public double getInterestRate(){
        return interestRate;
    }
    public int getWithdrawalLimit(){
        return withdrawalLimit;
    }
    public int getWithdrawals(){
        return withdrawals;
    }
    public void resetWithdrawals(){
        withdrawals = 0;
    }
    @Override
    public boolean withdraw(double amount) {
        if (balance < amount) throw new InsufficientFundsException(balance, amount);
        if (withdrawals >= withdrawalLimit) throw new WithdrawalLimitExceededException(withdrawalLimit);
        balance -= amount;
        withdrawals++;
        return true;
    }
    public void addInterest(){
        balance += balance*interestRate;
    }
    public double calculateMonthlyInterest() {
        return balance * interestRate / 12;
    }
    @Override
    public String getAccountSummary(){
        return "Savings Account " +
                " IBAN " + getIban() +
                " Balance " + balance +
                " Rate " + interestRate +
                " Withdrawals left " + (withdrawalLimit - withdrawals) + "/" + withdrawalLimit;
    }
}
