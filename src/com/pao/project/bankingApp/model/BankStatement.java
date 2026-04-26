package com.pao.project.bankingApp.model;

import com.pao.project.bankingApp.model.enums.AtmOperationType;
import com.pao.project.bankingApp.model.enums.TransactionStatus;
import com.pao.project.bankingApp.model.transaction.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class BankStatement {
    private final String statementId;
    private final String accountIban;
    private final String customerId;
    private final LocalDate periodStart;
    private final LocalDate periodEnd;
    private final List<Transaction> transactions;
    private final double openingBalance;
    private final double closingBalance;

    public BankStatement(String accountIban, String customerId, LocalDate periodStart,
                         LocalDate periodEnd, double openingBalance, List<Transaction> transactions) {
        this.statementId = UUID.randomUUID().toString();
        this.accountIban = accountIban;
        this.customerId = customerId;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.openingBalance = openingBalance;
        this.transactions = Collections.unmodifiableList(new ArrayList<>(transactions));
        this.closingBalance = calculateClosingBalance();
    }

    public String getStatementId() { return statementId; }
    public String getAccountIban() { return accountIban; }
    public String getCustomerId() { return customerId; }
    public LocalDate getPeriodStart() { return periodStart; }
    public LocalDate getPeriodEnd() { return periodEnd; }
    public List<Transaction> getTransactions() { return transactions; }
    public double getOpeningBalance() { return openingBalance; }
    public double getClosingBalance() { return closingBalance; }

    public double getTotalInflows() {
        double total = 0;
        for (Transaction t : transactions) {
            if (t.getStatus() != TransactionStatus.COMPLETED) continue;
            if (t instanceof ATMTransaction atm) {
                if (atm.getAtmOperationType() == AtmOperationType.DEPOSIT) {
                    total += t.getAmount();
                }
            } else if (t instanceof TransferTransaction transfer) {
                if (transfer.getDestinationIban().equals(this.accountIban)) {
                    total += t.getAmount();
                }
            }
        }
        return total;
    }

    public double getTotalOutflows() {
        double total = 0;
        for (Transaction t : transactions) {
            if (t.getStatus() != TransactionStatus.COMPLETED) continue;
            if (t instanceof ATMTransaction atm) {
                if (atm.getAtmOperationType() == AtmOperationType.WITHDRAWAL) {
                    total += t.getAmount();
                }
            } else if (t instanceof PosTransaction) {
                total += t.getAmount();
            } else if (t instanceof OnlineTransaction) {
                total += t.getAmount();
            } else if (t instanceof TransferTransaction transfer) {
                if (transfer.getIban().equals(this.accountIban)) {
                    total += t.getAmount();
                }
            }
        }
        return total;
    }

    public double getNetPosition() {
        return getTotalInflows() - getTotalOutflows();
    }

    public int getTransactionCount() {
        return transactions.size();
    }

    private double calculateClosingBalance() {
        return this.openingBalance + getNetPosition();
    }
    @Override
    public String toString() {
        return "BankStatement{" +
                "statementId='" + statementId + '\'' +
                ", accountIban='" + accountIban + '\'' +
                ", period=" + periodStart + " to " + periodEnd +
                ", openingBalance=" + openingBalance +
                ", closingBalance=" + closingBalance +
                ", transactions(" + getTransactionCount()+ " transactions)" +
                '}';
    }
}