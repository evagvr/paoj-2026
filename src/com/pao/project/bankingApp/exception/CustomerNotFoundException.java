package com.pao.project.bankingApp.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String customerId) {
        super("The customer with id "+customerId+" could not be found!");
    }
}
