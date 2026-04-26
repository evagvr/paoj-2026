package com.pao.project.bankingApp.model.transaction;


import com.pao.project.bankingApp.model.enums.Currency;
import com.pao.project.bankingApp.model.enums.TransactionStatus;


import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Transaction {
    private TransactionStatus status;
    private final String transactionId;
    private final String iban;
    private final double amount;
    private final Currency currency;
    private final LocalDateTime timestamp;
    private final String description;
    private boolean isForeign;
    private Double exchangeRateApplied;

    public Transaction(String iban, double amount, Currency currency,String description) {
        this.status = TransactionStatus.PENDING;
        this.transactionId = java.util.UUID.randomUUID().toString();
        this.iban = iban;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.currency = currency;
        this.description = description;
        this.isForeign = false;
        this.exchangeRateApplied = null;
    }
    public String getTransactionId() {
        return transactionId;
    }
    public String getIban() {
        return iban;
    }
    public double getAmount() {
        return amount;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public TransactionStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public void complete() {
        status = TransactionStatus.COMPLETED;
    }
    public void fail(){
        status = TransactionStatus.FAILED;
    }
    public void reverse(){
        if (status == TransactionStatus.COMPLETED){
            status = TransactionStatus.REVERSED;
        }
    }
    public void setIsForeign(boolean isForeign){
        this.isForeign = isForeign;
    }
    public void setExchangeRateApplied(Double rate){
        exchangeRateApplied = rate;
    }
    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", accountIban='" + iban + '\'' +
                ", amount=" + amount +
                ", currency=" + currency +
                ", status=" + status +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction transaction)) return false;
        return transactionId.equals(transaction.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }
}
