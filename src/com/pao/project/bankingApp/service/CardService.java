package com.pao.project.bankingApp.service;

import com.pao.project.bankingApp.exception.CardNotFoundException;
import com.pao.project.bankingApp.model.card.Card;
import com.pao.project.bankingApp.model.card.CreditCard;
import java.util.*;

public class CardService {
    private HashMap<String, Card> cardsByNumber;
    private HashMap<String, List<Card>> cardsByIban;
    private static CardService instance = null;

    private CardService() {
        this.cardsByNumber = new HashMap<>();
        this.cardsByIban = new HashMap<>();
    }

    public static CardService getInstance() {
        if (instance == null) {
            instance = new CardService();
        }
        return instance;
    }

    public void addCard(Card card) {
        cardsByNumber.put(card.getCardNumber(), card);
        cardsByIban
                .computeIfAbsent(card.getIban(), k -> new ArrayList<>())
                .add(card);
    }

    public Card getCardByNumber(String cardNumber) throws CardNotFoundException{
        Card card = cardsByNumber.get(cardNumber);
        if (card == null)
            throw new CardNotFoundException(cardNumber);
        return card;
    }

    public List<Card> getCardsByIban(String iban) {
        return cardsByIban.getOrDefault(iban, new ArrayList<>());
    }

    public List<Card> getAllCards() {
        return new ArrayList<>(cardsByNumber.values());
    }

    public void deactivateCard(String cardNumber) {
        Card card = getCardByNumber(cardNumber);
        card.setIsActive(false);
    }

    public void removeCard(String cardNumber) {
        Card card = getCardByNumber(cardNumber);
        cardsByNumber.remove(cardNumber);
        List<Card> ibanCards = cardsByIban.get(card.getIban());
        if (ibanCards != null) {
            ibanCards.remove(card);
            if (ibanCards.isEmpty()) {
                cardsByIban.remove(card.getIban());
            }
        }
    }

    public double getCreditCardUtilization(String cardNumber) {
        Card card = getCardByNumber(cardNumber);
        if (card instanceof CreditCard creditCard) {
            return creditCard.getUtilizationRate();
        }
        return 0;
    }

    public int getCardCount() {
        return cardsByNumber.size();
    }
}