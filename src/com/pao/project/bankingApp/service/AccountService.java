package com.pao.project.bankingApp.service;

import com.pao.project.bankingApp.exception.AccountNotFoundException;
import com.pao.project.bankingApp.model.account.Account;
import com.pao.project.bankingApp.model.account.SavingsAccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AccountService {
    private HashMap<String, Account> accountsByIban;
    private HashMap<String, List<Account>> accountsByCustomer;
    private static AccountService instance = null;

    private AccountService() {
        this.accountsByIban = new HashMap<>();
        this.accountsByCustomer = new HashMap<>();
    }

    public static AccountService getInstance() {
        if (instance == null) {
            instance = new AccountService();
        }
        return instance;
    }
    public void addAccount(Account account) {
        accountsByIban.put(account.getIban(), account);
        accountsByCustomer
                .computeIfAbsent(account.getCustomerId(), k -> new ArrayList<>())
                .add(account);

    }

    public Account getAccountByIban(String iban) throws AccountNotFoundException{
        Account account = accountsByIban.get(iban);
        if (account == null)
            throw new AccountNotFoundException(iban);
        return account;
    }

    public List<Account> getAccountsByCustomer(String customerId) {
        return accountsByCustomer.getOrDefault(customerId, new ArrayList<>());
    }

    public void removeAccount(String iban) {
        Account account = getAccountByIban(iban);
        accountsByIban.remove(iban);
        List<Account> customerAccounts = accountsByCustomer.get(account.getCustomerId());
        if (customerAccounts != null) {
            customerAccounts.remove(account);
        }

    }

    public double getTotalBalanceForCustomer(String customerId) {
        List<Account> accounts = getAccountsByCustomer(customerId);
        double total = 0;
        for (Account account : accounts) {
            total += account.getBalance();
        }
        return total;
    }
    public List<Account> getAllAccounts() {
        return new ArrayList<>(accountsByIban.values());
    }

    public int getAccountCount() {
        return accountsByIban.size();
    }
    public HashMap<String, Double> calculateSavingsAccountInterest(String customerId) {
        List<Account> accounts = getAccountsByCustomer(customerId);
        HashMap<String, Double> interestMap = new HashMap<>();
        for (Account account : accounts) {
            if (account instanceof SavingsAccount savings) {
                interestMap.put(savings.getIban(), savings.calculateMonthlyInterest());
            }
        }
        return interestMap;
    }
}
