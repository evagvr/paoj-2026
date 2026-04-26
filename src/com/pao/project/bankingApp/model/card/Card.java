package com.pao.project.bankingApp.model.card;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

public abstract class Card {
    private final String cardNumber;
    private final String iban;
    private final String cardHolderName;
    private final LocalDate expiryDate;
    private boolean isActive;
    private double dailyLimit;
    private final LocalDateTime createdAt;
    public Card(String iban, String cardHolderName, double dailyLimit){
        this.cardNumber = generateCardNumber();
        this.iban = iban;
        this.cardHolderName = cardHolderName;
        this.expiryDate = LocalDate.now().plusYears(4);
        this.isActive = true;
        this.dailyLimit = dailyLimit;
        this.createdAt = LocalDateTime.now();
    }
    public String getIban(){
        return iban;
    }
    public String getCardNumber(){
        return cardNumber;
    }
    public String getCardHolderName() {
        return cardHolderName;
    }
    public LocalDate getExpiryDate(){
        return expiryDate;
    }
    public boolean getIsActive(){
        return isActive;
    }
    public double getDailyLimit(){
        return dailyLimit;
    }
    public void setIsActive(boolean isActive){
        this.isActive = isActive;
    }
    public void setDailyLimit(double dailyLimit){
        this.dailyLimit = dailyLimit;
    }
    public String getMaskedCardNumber(){
        return "**** **** **** " + cardNumber.substring(12);
    }
    public boolean isExpired(){
        return LocalDate.now().isAfter(expiryDate);
    }

    private static String generateCardNumber(){
        Random random = new Random();
        String number = "";
        for (int i = 0; i < 16; i++)
            number += random.nextInt(10);
        return number;
    }
    public abstract String getCardDetails();
    @Override
    public String toString() {
        return getCardDetails();
    }
}
