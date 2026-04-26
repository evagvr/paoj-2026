package com.pao.project.bankingApp.service;

import com.pao.project.bankingApp.model.enums.TransactionStatus;
import com.pao.project.bankingApp.model.transaction.Transaction;

import java.util.*;

public class TransactionService {
    private HashMap<String, Transaction> transactionsById;
    private HashMap<String, List<Transaction>> transactionsByAccount;
    private PriorityQueue<Transaction> transactionsByAmount;
    private static TransactionService instance = null;

    private TransactionService(){
        this.transactionsById = new HashMap<>();
        this.transactionsByAccount = new HashMap<>();
        this.transactionsByAmount = new PriorityQueue<>(Comparator.comparingDouble(Transaction::getAmount).reversed());
    }
    public static TransactionService getInstance(){
        if (instance == null){
            instance = new TransactionService();
        }
        return instance;
    }
    public void addTransaction(Transaction transaction){
        transactionsById.put(transaction.getTransactionId(),transaction);
        transactionsByAccount
                .computeIfAbsent(transaction.getIban(), k -> new ArrayList<>())
                .add(transaction);
        transactionsByAmount.add(transaction);
    }
    public Transaction getTransactionById(String id) {
        return transactionsById.get(id);
    }
    public List<Transaction> getTransactionsByAccount(String iban) {
        return transactionsByAccount.getOrDefault(iban, new ArrayList<>());
    }
    public List<Transaction> getTransactionsByAccountAndType(String iban, Class<?> type) {
        List<Transaction> all = getTransactionsByAccount(iban);
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : all) {
            if (type.isInstance(t)) {
                result.add(t);
            }
        }
        return result;
    }
    public List<Transaction> getTopNTransactions(int n) {
        PriorityQueue<Transaction> copy = new PriorityQueue<>(transactionsByAmount);
        List<Transaction> result = new ArrayList<>();
        while (!copy.isEmpty() && result.size() < n) {
            Transaction t = copy.poll();
            if (t.getStatus() == TransactionStatus.COMPLETED) {
                result.add(t);
            }
        }
        return result;
    }
    public void removeTransaction(String id) {
        Transaction transaction = getTransactionById(id);
        if (transaction != null) {
            transactionsById.remove(id);
            transactionsByAmount.remove(transaction);
            List<Transaction> accountTransactions = transactionsByAccount
                    .get(transaction.getIban());
            if (accountTransactions != null) {
                accountTransactions.remove(transaction);
            }
        }
    }
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactionsById.values());
    }
    public int getTransactionCount() {
        return transactionsById.size();
    }
}
