package com.pao.project.bankingApp.model;

import java.util.ArrayList;
import java.util.List;

public class CreditHistory {
    private List<Loan> loans;
    private int missedPayments;
    private int onTimePayments;
    private double totalDebtRepaid;
    public CreditHistory(){
        this.loans = new ArrayList<>();
        this.missedPayments = 0;
        this.onTimePayments = 0;
        this.totalDebtRepaid = 0.0;
    }
    public List<Loan> getLoans() { return loans; }
    public int getMissedPayments() { return missedPayments; }
    public int getOnTimePayments() { return onTimePayments; }
    public double getTotalDebtRepaid() { return totalDebtRepaid; }
    public void addLoan(Loan loan) {
        loans.add(loan);
    }

    public void recordPayment(boolean wasOnTime, double amount) {
        if (wasOnTime) {
            onTimePayments++;
        } else {
            missedPayments++;
        }
        totalDebtRepaid += amount;

    }
    public double getMissedPaymentRate() {
        int total = missedPayments + onTimePayments;
        if (total == 0) return 0;
        return missedPayments / (double) total;
    }

    public double getTotalCurrentDebt() {
        double total = 0;
        for (Loan loan : loans) {
            if (!loan.isFullyRepaid()) {
                total += loan.getRemainingDebt();
            }
        }
        return total;
    }
    public int getActiveLoansCount() {
        int count = 0;
        for (Loan loan : loans) {
            if (!loan.isFullyRepaid()) {
                count++;
            }
        }
        return count;
    }
    @Override
    public String toString() {
        return "Loans: " + getActiveLoansCount() +
                " | Total Current Debt: "+getTotalCurrentDebt()+
                " | Total Repaid: "+ totalDebtRepaid +
                " | Payments: " + onTimePayments+" on time, "+missedPayments+" missed ("+ getMissedPaymentRate()*100 +"% missed rate)";
    }

}
