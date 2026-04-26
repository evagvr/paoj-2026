package com.pao.project.bankingApp.model.transaction;

import com.pao.project.bankingApp.model.enums.AtmOperationType;
import com.pao.project.bankingApp.model.enums.Currency;

public class ATMTransaction extends Transaction {
    private final AtmOperationType atmOperationType;
    public ATMTransaction(String iban, double amount, Currency currency, String description, AtmOperationType atmOperationType){
        super(iban, amount, currency, description);
        this.atmOperationType = atmOperationType;
    }
    public AtmOperationType getAtmOperationType(){
        return atmOperationType;
    }
}
