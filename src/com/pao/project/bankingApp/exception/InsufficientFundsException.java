package com.pao.project.bankingApp.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(double available, double requested) {
        super("Insufficient funds. Available: " + available + ", Requested: " + requested);
    }
}
