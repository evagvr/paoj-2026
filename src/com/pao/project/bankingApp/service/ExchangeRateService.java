package com.pao.project.bankingApp.service;

import com.pao.project.bankingApp.model.enums.Currency;

import java.util.HashMap;
import java.util.Map;

public class ExchangeRateService {
    private Map<Currency, Double> rates;
    private static ExchangeRateService instance = null;
    public static ExchangeRateService getInstance(){
        if (instance == null){
            instance = new ExchangeRateService();
        }
        return instance;
    }
    private ExchangeRateService(){
        rates = new HashMap<>();
        rates.put(Currency.RON, 1.0);
        rates.put(Currency.EUR, 4.97);
        rates.put(Currency.GBP, 5.80);
        rates.put(Currency.USD, 4.60);
    }
    public double convert(double amount, Currency initialCurrency, Currency finalCurrency){
        if (initialCurrency == finalCurrency){
            return  amount;
        }
        double initialRate = rates.get(initialCurrency);
        double finalRate = rates.get(finalCurrency);
        return amount*initialRate/finalRate;
    }
}
