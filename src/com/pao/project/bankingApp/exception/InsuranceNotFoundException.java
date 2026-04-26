package com.pao.project.bankingApp.exception;

public class InsuranceNotFoundException extends RuntimeException {
    public InsuranceNotFoundException(String insuranceId) {
        super("The insurance with id "+ insuranceId + " could not be found.");
    }
}
