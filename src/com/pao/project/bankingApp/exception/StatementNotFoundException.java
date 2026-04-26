package com.pao.project.bankingApp.exception;

public class StatementNotFoundException extends RuntimeException {
    public StatementNotFoundException(String statementId) {
        super("The bank statement with id "+ statementId + " does not exist.");
    }
}
