package com.pao.project.bankingApp.exception;

public class LoanNotFoundException extends RuntimeException {
    public LoanNotFoundException(String loanId) {
        super("The loan with id: " + loanId + " does not exist.");
    }
}
