package com.pao.project.bankingApp.service;

import com.pao.project.bankingApp.exception.AccountNotFoundException;
import com.pao.project.bankingApp.exception.DepositNotFoundException;
import com.pao.project.bankingApp.model.enums.DepositStatus;
import com.pao.project.bankingApp.model.Deposit;
import com.pao.project.bankingApp.model.account.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DepositService {
    private HashMap<String, Deposit> depositsById;
    private HashMap<String, List<Deposit>> depositsByCustomer;
    private static DepositService instance = null;

    private DepositService() {
        this.depositsById = new HashMap<>();
        this.depositsByCustomer = new HashMap<>();
    }

    public static DepositService getInstance() {
        if (instance == null) {
            instance = new DepositService();
        }
        return instance;
    }

    public void openDeposit(Deposit deposit) {
        depositsById.put(deposit.getDepositId(), deposit);
        depositsByCustomer
                .computeIfAbsent(deposit.getCustomerId(), k -> new ArrayList<>())
                .add(deposit);
    }

    public Deposit getDepositById(String depositId) throws DepositNotFoundException {
        Deposit deposit = depositsById.get(depositId);
        if (deposit == null) throw new DepositNotFoundException(depositId);
        return deposit;
    }

    public List<Deposit> getDepositsByCustomer(String customerId) {
        return depositsByCustomer.getOrDefault(customerId, new ArrayList<>());
    }

    public double closeDeposit(String depositId) throws DepositNotFoundException {
        Deposit deposit = getDepositById(depositId);
        deposit.setStatus(DepositStatus.BROKEN);
        double amount = deposit.calculateEarlyWithdrawalAmount();
        AccountService.getInstance().getAccountByIban(deposit.getLinkedAccountIban()).deposit(amount);
        return amount;
    }

    public double matureDeposit(String depositId) throws DepositNotFoundException {
        Deposit deposit = getDepositById(depositId);
        if (!deposit.isMatured()) {
            System.out.println("Deposit has not matured yet. Days remaining: "
                    + deposit.getDaysRemaining());
            return 0;
        }
        deposit.setStatus(DepositStatus.MATURED);
        double amount = deposit.calculateMaturityAmount();
        AccountService.getInstance().getAccountByIban(deposit.getLinkedAccountIban()).deposit(amount);
        return  amount;
    }

    public List<Deposit> getAllDeposits() {
        return new ArrayList<>(depositsById.values());
    }

    public List<Deposit> getActiveDeposits() {
        List<Deposit> active = new ArrayList<>();
        for (Deposit deposit : depositsById.values()) {
            if (deposit.getStatus() == DepositStatus.ACTIVE) {
                active.add(deposit);
            }
        }
        return active;
    }
    public HashMap<String, Double> getDepositSummaryForCustomer(String customerId) {
        List<Deposit> deposits = getDepositsByCustomer(customerId);
        HashMap<String, Double> summary = new HashMap<>();
        for (Deposit deposit : deposits) {
            summary.put(deposit.getDepositId(), deposit.calculateMaturityAmount());
        }
        return summary;
    }
    public void removeDeposit(String depositId) {
        Deposit deposit = getDepositById(depositId);
        depositsByCustomer.get(deposit.getCustomerId()).remove(deposit);
        depositsById.remove(depositId);
    }
}