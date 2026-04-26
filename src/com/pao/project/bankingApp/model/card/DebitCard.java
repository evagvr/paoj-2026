package com.pao.project.bankingApp.model.card;

public class DebitCard extends Card {
    public DebitCard(String iban, String cardHolderName, double dailyLimit){
        super(iban, cardHolderName, dailyLimit);
    }
    @Override
    public String getCardDetails(){
        return "Debit Card "+
                " Card Number " + getMaskedCardNumber() +
                " Holder " + getCardHolderName() +
                " Expires " + getExpiryDate() +
                " Limit " + getDailyLimit() + "/day";
    }
}
