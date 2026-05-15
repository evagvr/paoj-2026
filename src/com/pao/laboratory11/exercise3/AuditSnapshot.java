package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AuditSnapshot {
    private final BigDecimal totalAmount;
    private final Map<String, BigDecimal> amountByCategory;
    private final Map<String, Transaction> topTransactionByCountry;
    private final List<Transaction> highValueTransactions;
    private final double averageValue;
    public AuditSnapshot(BigDecimal totalAmount, Map<String, BigDecimal> amountByCategory, Map<String, Transaction> topTransactionByCountry, List<Transaction> highValueTransactions, double averageValue){
        this.totalAmount = totalAmount;
        this.amountByCategory = Collections.unmodifiableMap(new HashMap<>(amountByCategory));
        this.topTransactionByCountry = Collections.unmodifiableMap(new HashMap<>(topTransactionByCountry));
        this.highValueTransactions = List.copyOf(highValueTransactions);
        this.averageValue = averageValue;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public Map<String, BigDecimal> getAmountByCategory() {
        return amountByCategory;
    }

    public Map<String, Transaction> getTopTransactionByCountry() {
        return topTransactionByCountry;
    }

    public List<Transaction> getHighValueTransactions() {
        return highValueTransactions;
    }

    public double getAverageValue() {
        return averageValue;
    }
}
