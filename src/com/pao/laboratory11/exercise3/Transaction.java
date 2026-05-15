package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;

public final class Transaction {
    private final int id;
    private final BigDecimal amount;
    private final String country;
    private final String category;

    public Transaction(int id, BigDecimal amount, String country, String category) {
        this.id = id;
        this.amount = amount;
        this.country = country;
        this.category = category;
    }
    public int getId() { return id; }
    public BigDecimal getAmount() { return amount; }
    public String getCountry() { return country; }
    public String getCategory() { return category; }
}
