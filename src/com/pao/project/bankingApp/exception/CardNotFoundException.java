package com.pao.project.bankingApp.exception;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(String cardNumber) {
        super("The card with number: " + cardNumber + " does not exist.");
    }
}
