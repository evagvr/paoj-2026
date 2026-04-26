package com.pao.project.bankingApp.exception;

public class DepositNotFoundException extends RuntimeException {
    public DepositNotFoundException(String depositId) {
        super("The deposit with id "+ depositId+ " could not be found.");
    }
}
