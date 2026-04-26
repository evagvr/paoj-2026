package com.pao.project.bankingApp.exception;

public class WithdrawalLimitExceededException extends RuntimeException {
    public WithdrawalLimitExceededException(int limit) {
        super("Withdrawal limit of " + limit + " per period has been exceeded.");
    }
}