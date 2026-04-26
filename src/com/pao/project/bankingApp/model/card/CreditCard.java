package com.pao.project.bankingApp.model.card;

import java.time.LocalDate;

public class CreditCard extends Card {
    private double creditLimit;
    private double currentBalance;
    private double minimumPayment;
    private LocalDate billingDate;
    public CreditCard(String iban,String cardHolderName, double dailyLimit, double creditLimit, double minimumPayment, LocalDate billingDate){
        super(iban, cardHolderName, dailyLimit);
        this.creditLimit = creditLimit;
        this.currentBalance = 0.0;
        this.minimumPayment = minimumPayment;
        this.billingDate = billingDate;
    }
    public double getAvailableCredit(){
        return creditLimit - currentBalance;
    }
    public double getUtilizationRate(){
        if (creditLimit == 0)
            return 0;
        return currentBalance / creditLimit *100;
    }
    public boolean repay(double amount) {
        if (amount < 0) throw new IllegalArgumentException("Amount cannot be negative!");
        currentBalance = Math.max(0, currentBalance - amount);
        return true;
    }
    public boolean spend(double amount){
        if (currentBalance + amount <= creditLimit){
            currentBalance += amount;
            return true;
        }
        return false;

    }
    @Override
    public String getCardDetails(){
        return "Credit Card "+
                " Card Number " + getCardNumber()+
                " Holder " + getCardHolderName() +
                " Limit " + creditLimit +
                " Used " + currentBalance;
    }
}
