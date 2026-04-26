package com.pao.project.bankingApp.exception;

public class LoanAlreadyRepaidException extends RuntimeException {
    public LoanAlreadyRepaidException(String loanId) {
        super("The instalment cannot be paid as the loan with id: "+ loanId + " has already been repaid");
    }
}
