package com.pao.project.bankingApp.service;

import com.pao.project.bankingApp.exception.CustomerNotFoundException;
import com.pao.project.bankingApp.exception.LoanAlreadyRepaidException;
import com.pao.project.bankingApp.exception.LoanNotFoundException;
import com.pao.project.bankingApp.model.Customer;
import com.pao.project.bankingApp.model.Loan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoanService {
    private HashMap<String, Loan> loansById;
    private HashMap<String, List<Loan>> loansByCustomer;
    private static LoanService instance = null;

    private LoanService() {
        this.loansById = new HashMap<>();
        this.loansByCustomer = new HashMap<>();
    }

    public static LoanService getInstance() {
        if (instance == null) {
            instance = new LoanService();
        }
        return instance;
    }

    public void applyForLoan(Loan loan) throws CustomerNotFoundException {
        Customer customer = CustomerService.getInstance().getCustomerById(loan.getCustomerId());
        if (loansById.containsKey(loan.getLoanId())) return;
        loansById.put(loan.getLoanId(), loan);
        loansByCustomer
                .computeIfAbsent(loan.getCustomerId(), k -> new ArrayList<>())
                .add(loan);
        customer.getCreditHistory().addLoan(loan);
    }

    public Loan getLoanById(String loanId) throws LoanNotFoundException {
        Loan loan = loansById.get(loanId);
        if (loan == null) throw new LoanNotFoundException(loanId);
        return loan;
    }

    public List<Loan> getLoansByCustomer(String customerId) {
        return loansByCustomer.getOrDefault(customerId, new ArrayList<>());
    }

    public void payInstalment(String loanId) throws LoanNotFoundException, LoanAlreadyRepaidException {
        Loan loan = getLoanById(loanId);
        if (loan.isFullyRepaid()) throw new LoanAlreadyRepaidException(loanId);
        loan.payInstalment();
        Customer customer = CustomerService.getInstance().getCustomerById(loan.getCustomerId());
        customer.getCreditHistory().recordPayment(true, loan.getMonthlyInstalment());
    }

    public List<Loan> getAllLoans() {
        return new ArrayList<>(loansById.values());
    }

    public List<Loan> getActiveLoans() {
        List<Loan> active = new ArrayList<>();
        for (Loan loan : loansById.values()) {
            if (!loan.isFullyRepaid()) {
                active.add(loan);
            }
        }
        return active;
    }
    public void removeLoan(String loanId) {
        Loan loan = getLoanById(loanId);
        loansByCustomer.get(loan.getCustomerId()).remove(loan);
        loansById.remove(loanId);
    }
}