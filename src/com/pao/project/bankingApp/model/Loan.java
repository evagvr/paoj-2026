package com.pao.project.bankingApp.model;

import com.pao.project.bankingApp.model.enums.LoanStatus;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Loan {
    private final String loanId;
    private final String customerId;
    private final String accountIban;
    private final double loanAmount;
    private final double interestRate;
    private final double monthlyInstalment;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final int totalInstalments;
    private int instalmentsPaid;
    private LoanStatus status;

    public Loan(String customerId, String accountIban, double loanAmount, double interestRate, LocalDate startDate, LocalDate endDate){
        this.loanId = java.util.UUID.randomUUID().toString();
        this.customerId = customerId;
        this.accountIban = accountIban;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.totalInstalments = (int) ChronoUnit.MONTHS.between(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
        this.instalmentsPaid = 0;
        this.monthlyInstalment = calculateMonthlyInstalment();
        this.status = LoanStatus.ACTIVE;
    }
    public double calculateMonthlyInstalment() {
        if (interestRate == 0) return loanAmount / totalInstalments;
        double monthlyRate = interestRate / 12;
        return (loanAmount * monthlyRate) / (1 - Math.pow((1 + monthlyRate), -totalInstalments));
    }
    public void setStatus(LoanStatus status){
        this.status = status;
    }
    public String getLoanId() {
        return loanId;
    }
    public String getCustomerId() {
        return customerId;
    }
    public String getAccountIban() {
        return accountIban;
    }

    public double getLoanAmount() {
        return loanAmount;
    }
    public double getInterestRate() {
        return interestRate;
    }
    public double getMonthlyInstalment() {
        return monthlyInstalment;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public int getTotalInstalments() {
        return totalInstalments;
    }
    public int getInstalmentsPaid() {
        return instalmentsPaid;
    }
    public LoanStatus getStatus() {
        return status;
    }
    public double getRemainingDebt() {
        int remaining = totalInstalments - instalmentsPaid;
        return remaining * monthlyInstalment;
    }
    public boolean isFullyRepaid(){
        return instalmentsPaid >= totalInstalments;
    }
    public void payInstalment(){
        if (!isFullyRepaid()){
            instalmentsPaid += 1;
            if (isFullyRepaid()){
                setStatus(LoanStatus.COMPLETED);
            }
        }
    }
    @Override
    public String toString() {
        return "Loan{" +
                "loanId='" + loanId + '\'' +
                ", amount=" + loanAmount +
                ", monthlyInstalment=" + monthlyInstalment +
                ", instalmentsPaid=" + instalmentsPaid + "/" + totalInstalments +
                ", status=" + status +
                '}';
    }
}
