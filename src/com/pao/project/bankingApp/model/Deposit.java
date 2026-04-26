package com.pao.project.bankingApp.model;

import com.pao.project.bankingApp.model.enums.Currency;
import com.pao.project.bankingApp.model.enums.DepositStatus;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Deposit {
    private final String depositId;
    private final String customerId;
    private final String linkedAccountIban;
    private final double principalAmount;
    private final double interestRate;
    private final Currency currency;
    private final LocalDate startDate;
    private final LocalDate maturityDate;
    private DepositStatus status;
    private final double penaltyRate;
    public Deposit(String customerId, String linkedAccountIban, double principalAmount, double interestRate, Currency currency, LocalDate startDate, LocalDate maturityDate, double penaltyRate){
        this.depositId = java.util.UUID.randomUUID().toString();
        this.customerId = customerId;
        this.linkedAccountIban = linkedAccountIban;
        this.principalAmount = principalAmount;
        this.interestRate = interestRate;
        this.currency = currency;
        this.startDate = startDate;
        this.maturityDate = maturityDate;
        this.status = DepositStatus.ACTIVE;
        this.penaltyRate = penaltyRate;
    }

    public String getDepositId() {
        return depositId;
    }
    public double getPenaltyRate() {
        return penaltyRate;
    }
    public DepositStatus getStatus() {
        return status;
    }
    public LocalDate getMaturityDate() {
        return maturityDate;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public Currency getCurrency() {
        return currency;
    }
    public double getInterestRate() {
        return interestRate;
    }
    public double getPrincipalAmount() {
        return principalAmount;
    }
    public String getLinkedAccountIban() {
        return linkedAccountIban;
    }
    public String getCustomerId() {
        return customerId;
    }

    public void setStatus(DepositStatus status) {
        this.status = status;
    }

    public double calculateMaturityAmount(){
        double years = ChronoUnit.DAYS.between(startDate, maturityDate) / 365.0;
        return principalAmount * (1 + interestRate * years);
    }
    public double calculateEarlyWithdrawalAmount() {
        double daysElapsed = ChronoUnit.DAYS.between(startDate, LocalDate.now());
        double years = daysElapsed / 365.0;
        double earnedInterest = principalAmount * interestRate * years;
        return principalAmount + (earnedInterest * (1 - penaltyRate));
    }
    public long getDaysRemaining() {
        if (isMatured()) return 0;
        return ChronoUnit.DAYS.between(LocalDate.now(), maturityDate);
    }
    public boolean isMatured() {
        return LocalDate.now().isAfter(maturityDate);
    }
    @Override
    public String toString() {
        return "Deposit{" +
                "depositId='" + depositId + '\'' +
                ", principal=" + principalAmount +
                ", currency=" + currency +
                ", status=" + status +
                ", maturityDate=" + maturityDate +
                '}';
    }
}
