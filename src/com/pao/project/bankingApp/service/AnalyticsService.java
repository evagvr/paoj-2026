package com.pao.project.bankingApp.service;

import com.pao.project.bankingApp.model.enums.Currency;
import com.pao.project.bankingApp.model.enums.MerchantCategory;
import com.pao.project.bankingApp.model.enums.TransactionStatus;
import com.pao.project.bankingApp.exception.AccountNotFoundException;
import com.pao.project.bankingApp.model.BankStatement;
import com.pao.project.bankingApp.model.Customer;
import com.pao.project.bankingApp.model.Deposit;
import com.pao.project.bankingApp.model.Insurance;
import com.pao.project.bankingApp.model.account.Account;
import com.pao.project.bankingApp.model.account.SavingsAccount;
import com.pao.project.bankingApp.model.card.Card;
import com.pao.project.bankingApp.model.card.CreditCard;
import com.pao.project.bankingApp.model.transaction.OnlineTransaction;
import com.pao.project.bankingApp.model.transaction.PosTransaction;
import com.pao.project.bankingApp.model.transaction.Transaction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class AnalyticsService {
    private static AnalyticsService instance = null;

    private AnalyticsService() {
    }

    public static AnalyticsService getInstance() {
        if (instance == null)
            instance = new AnalyticsService();
        return instance;
    }

    public double computeNetWorth(String customerId) {
        List<Account> accounts = AccountService.getInstance().getAccountsByCustomer(customerId);
        double total = 0;
        for (Account account : accounts) {
            total += ExchangeRateService.getInstance().convert(
                    account.getBalance(),
                    account.getCurrency(),
                    Currency.RON
            );
        }
        return total;
    }

    public List<Transaction> detectAnomalousTransactions(String accountIban) {
        List<Transaction> all = TransactionService.getInstance().getTransactionsByAccount(accountIban);
        List<Transaction> completed = new ArrayList<>();
        for (Transaction t : all) {
            if (t.getStatus() == TransactionStatus.COMPLETED) {
                completed.add(t);
            }
        }

        if (completed.isEmpty()) return new ArrayList<>();
        if (completed.size() < 10) {
            System.out.println("Not enough transactions for anomaly detection (minimum 10 required, found " + completed.size() + ")");
            return new ArrayList<>();
        }
        double sum = 0;
        for (Transaction t : completed) {
            sum += t.getAmount();
        }
        double mean = sum / completed.size();

        double varianceSum = 0;
        for (Transaction t : completed) {
            varianceSum += Math.pow(t.getAmount() - mean, 2);
        }
        double standardDeviation = Math.sqrt(varianceSum / completed.size());
        double threshold = mean + 2 * standardDeviation;

        List<Transaction> anomalous = new ArrayList<>();
        for (Transaction t : completed) {
            if (t.getAmount() > threshold) {
                anomalous.add(t);
            }
        }
        return anomalous;
    }

    public HashMap<MerchantCategory, Double> getSpendingBreakdownByCategory(String accountIban) {
        List<Transaction> all = TransactionService.getInstance().getTransactionsByAccount(accountIban);
        HashMap<MerchantCategory, Double> breakdown = new HashMap<>();

        for (Transaction t : all) {
            if (t.getStatus() != TransactionStatus.COMPLETED) continue;
            if (t instanceof PosTransaction pos) {
                breakdown.merge(pos.getMerchantCategory(), t.getAmount(), Double::sum);
            } else if (t instanceof OnlineTransaction online) {
                breakdown.merge(online.getMerchantCategory(), t.getAmount(), Double::sum);
            }
        }
        return breakdown;
    }

    public List<Transaction> getTopNTransactions(int n) {
        return TransactionService.getInstance().getTopNTransactions(n);
    }

    public HashMap<String, Double> computeCreditCardUtilization(String customerId) {
        List<Account> accounts = AccountService.getInstance().getAccountsByCustomer(customerId);
        HashMap<String, Double> utilization = new HashMap<>();

        for (Account account : accounts) {
            List<Card> cards = CardService.getInstance().getCardsByIban(account.getIban());
            for (Card card : cards) {
                if (card instanceof CreditCard creditCard) {
                    utilization.put(
                            creditCard.getMaskedCardNumber(),
                            creditCard.getUtilizationRate()
                    );
                }
            }
        }
        return utilization;
    }

    public List<Customer> flagHighRiskCustomers() {
        List<Customer> highRisk = new ArrayList<>();

        for (Customer customer : CustomerService.getInstance().getAllCustomers()) {
            double missedPaymentRate = customer.getCreditHistory().getMissedPaymentRate();
            double netWorth = computeNetWorth(customer.getUserId());
            double totalDebt = customer.getCreditHistory().getTotalCurrentDebt();

            double debtToWorthRatio = 0;
            if (netWorth > 0) {
                debtToWorthRatio = totalDebt / netWorth;
            }

            if (missedPaymentRate > 0.3 || debtToWorthRatio > 0.5) {
                highRisk.add(customer);
            }
        }
        return highRisk;
    }

    public HashMap<MerchantCategory, List<Transaction>> groupTransactionsByCategory() {
        List<Transaction> all = TransactionService.getInstance().getAllTransactions();
        HashMap<MerchantCategory, List<Transaction>> grouped = new HashMap<>();

        for (Transaction t : all) {
            if (t instanceof PosTransaction pos) {
                grouped.computeIfAbsent(pos.getMerchantCategory(), k -> new ArrayList<>()).add(pos);
            } else if (t instanceof OnlineTransaction online) {
                grouped.computeIfAbsent(online.getMerchantCategory(), k -> new ArrayList<>()).add(online);
            }
        }
        return grouped;
    }

    public double calculateAverageDailyBalance(String accountIban, LocalDate from, LocalDate to) {
        List<Transaction> all = TransactionService.getInstance().getTransactionsByAccount(accountIban);

        List<Transaction> inPeriod = new ArrayList<>();
        for (Transaction t : all) {
            LocalDate date = t.getTimestamp().toLocalDate();
            if (t.getStatus() == TransactionStatus.COMPLETED
                    && !date.isBefore(from)
                    && !date.isAfter(to)) {
                inPeriod.add(t);
            }
        }
        inPeriod.sort(Comparator.comparing(Transaction::getTimestamp));

        Account account = AccountService.getInstance().getAccountByIban(accountIban);
        double runningBalance = account.getBalance();
        for (Transaction t : inPeriod) {
            runningBalance -= t.getAmount();
        }

        double balanceSum = 0;
        long totalDays = ChronoUnit.DAYS.between(from, to);
        if (totalDays == 0) return runningBalance;

        LocalDate current = from;
        int transactionIndex = 0;

        while (!current.isAfter(to)) {
            while (transactionIndex < inPeriod.size() &&
                    inPeriod.get(transactionIndex).getTimestamp().toLocalDate().equals(current)) {
                runningBalance += inPeriod.get(transactionIndex).getAmount();
                transactionIndex++;
            }
            balanceSum += runningBalance;
            current = current.plusDays(1);
        }

        return balanceSum / totalDays;
    }

    public List<Account> findDormantAccounts(int days) {
        List<Account> allAccounts = AccountService.getInstance().getAllAccounts();
        List<Account> dormant = new ArrayList<>();

        for (Account account : allAccounts) {
            List<Transaction> transactions = TransactionService.getInstance()
                    .getTransactionsByAccount(account.getIban());

            if (transactions.isEmpty()) {
                dormant.add(account);
                continue;
            }

            LocalDateTime mostRecent = transactions.get(0).getTimestamp();
            for (Transaction t : transactions) {
                if (t.getTimestamp().isAfter(mostRecent)) {
                    mostRecent = t.getTimestamp();
                }
            }

            if (ChronoUnit.DAYS.between(mostRecent, LocalDateTime.now()) > days) {
                dormant.add(account);
            }
        }
        return dormant;
    }

    public double scoreLoanApplication(String customerId, double requestedAmount) {
        Customer customer = CustomerService.getInstance().getCustomerById(customerId);
        double score = 100;
        double missedPaymentRate = customer.getCreditHistory().getMissedPaymentRate();
        score -= missedPaymentRate * 40;

        double totalCurrentDebt = customer.getCreditHistory().getTotalCurrentDebt();
        double debtRatio = requestedAmount > 0 ? totalCurrentDebt / requestedAmount : 0;
        if (debtRatio > 1) {
            score -= 30;
        } else if (debtRatio > 0.5) {
            score -= 15;
        }

        double netWorth = computeNetWorth(customerId);
        if (netWorth < requestedAmount) {
            score -= 20;
        }

        int activeLoans = customer.getCreditHistory().getActiveLoansCount();
        if (activeLoans > 2) {
            score -= 10;
        }

        return Math.max(0, Math.min(100, score));
    }
}
