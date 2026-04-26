package com.pao.project.bankingApp.model.transaction;

import com.pao.project.bankingApp.model.enums.Currency;
import com.pao.project.bankingApp.model.enums.MerchantCategory;

public class OnlineTransaction extends Transaction {
    private final String merchantName;
    private final MerchantCategory merchantCategory;
    private final String website;
    public OnlineTransaction(String iban, double amount, Currency currency, String description, String merchantName, MerchantCategory merchantCategory, String website){
        super(iban, amount, currency, description);
        this.merchantName = merchantName;
        this.merchantCategory = merchantCategory;
        this.website = website;
    }
    public String getMerchantName(){
        return merchantName;
    }
    public MerchantCategory getMerchantCategory() {
        return merchantCategory;
    }
    public String getWebsite(){
        return website;
    }
}
