package com.pao.project.bankingApp.service;

import com.pao.project.bankingApp.model.enums.Currency;
import com.pao.project.bankingApp.model.enums.InsuranceType;
import com.pao.project.bankingApp.model.enums.MerchantCategory;
import com.pao.project.bankingApp.exception.CustomerNotFoundException;
import com.pao.project.bankingApp.exception.InsufficientFundsException;
import com.pao.project.bankingApp.model.Customer;
import com.pao.project.bankingApp.model.Deposit;
import com.pao.project.bankingApp.model.Insurance;
import com.pao.project.bankingApp.model.Loan;
import com.pao.project.bankingApp.model.account.Account;
import com.pao.project.bankingApp.model.account.CurrentAccount;
import com.pao.project.bankingApp.model.account.SavingsAccount;
import com.pao.project.bankingApp.model.card.Card;
import com.pao.project.bankingApp.model.card.DebitCard;
import com.pao.project.bankingApp.model.transaction.PosTransaction;
import com.pao.project.bankingApp.model.transaction.TransferTransaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SystemOperationsService {
    private static SystemOperationsService instance = null;

    private SystemOperationsService() {}

    public static SystemOperationsService getInstance() {
        if (instance == null) {
            instance = new SystemOperationsService();
        }
        return instance;
    }

    public void generateCustomerReport(String customerId) {
        try {
            Customer customer = CustomerService.getInstance().getCustomerById(customerId);
            System.out.println("--- REPORT FOR: " + customer.getFullName() + " ---");
            System.out.println("Total Accounts Balance: " + AccountService.getInstance().getTotalBalanceForCustomer(customerId));
            System.out.println("Deposits: " + DepositService.getInstance().getDepositSummaryForCustomer(customerId));
            System.out.println("Insurances: " + InsuranceService.getInstance().getInsuranceCoverageAnalysis(customerId));
            System.out.println("Active Loans: " + LoanService.getInstance().getLoansByCustomer(customerId).size());
            System.out.println("Credit History: "+ customer.getCreditHistory());
        } catch (CustomerNotFoundException e) {
            System.out.println("Cannot generate report. " + e.getMessage());
        }
    }
    public void runEndOfMonthBatch() {
        System.out.println("--- RUNNING END OF MONTH BATCH ---");
        List<Account> allAccounts = AccountService.getInstance().getAllAccounts();
        for (Account account : allAccounts) {
            if (account instanceof CurrentAccount current) {
                current.applyMonthlyFee();
            } else if (account instanceof SavingsAccount savings) {
                savings.resetWithdrawals();
                savings.addInterest();
            }
        }
        System.out.println("Applied monthly fees, added interest, and reset withdrawal limits.");
    }
    public void offboardCustomer(String customerId) {
        System.out.println("--- OFFBOARDING CUSTOMER ---");
        List<Loan> loans = new ArrayList<>(LoanService.getInstance().getLoansByCustomer(customerId));
        for (Loan loan : loans) {
            LoanService.getInstance().removeLoan(loan.getLoanId());
        }
        List<Deposit> deposits = new ArrayList<>(DepositService.getInstance().getDepositsByCustomer(customerId));
        for (Deposit deposit : deposits) {
            DepositService.getInstance().removeDeposit(deposit.getDepositId());
        }
        List<Insurance> insurances = new ArrayList<>(InsuranceService.getInstance().getInsurancesByCustomer(customerId));
        for (Insurance insurance : insurances) {
            InsuranceService.getInstance().removeInsurance(insurance.getInsuranceId());
        }
        List<Account> accounts = new ArrayList<>(AccountService.getInstance().getAccountsByCustomer(customerId));
        for (Account acc : accounts) {
            List<Card> cards = new ArrayList<>(CardService.getInstance().getCardsByIban(acc.getIban()));
            for (Card card : cards) {
                CardService.getInstance().removeCard(card.getCardNumber());
            }
            AccountService.getInstance().removeAccount(acc.getIban());
        }
        CustomerService.getInstance().removeCustomer(customerId);
        System.out.println("Customer and all associated loans, insurances, deposits and accounts/cards removed successfully.");
    }
    public void processTransfer(String fromIban, String toIban, double amount, Currency currency) {
        System.out.println("--- PROCESSING TRANSFER ---");
        Account source = AccountService.getInstance().getAccountByIban(fromIban);
        Account destination = AccountService.getInstance().getAccountByIban(toIban);

        TransferTransaction transaction = new TransferTransaction(fromIban, amount, currency, "Transfer to " + toIban, toIban);
        TransactionService.getInstance().addTransaction(transaction);

        try {
            source.withdraw(amount);
            destination.deposit(amount);
            transaction.complete();
            System.out.println("Transfer completed successfully.");
        } catch (InsufficientFundsException e) {
            transaction.fail();
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }
    public void processCardPayment(String cardNumber, double amount, String merchantName, MerchantCategory category) {
        System.out.println("--- PROCESSING CARD PAYMENT ---");
        Card card = CardService.getInstance().getCardByNumber(cardNumber);

        if (!card.getIsActive()) {
            System.out.println("Payment failed: card is not active.");
            return;
        }
        if (amount > card.getDailyLimit()) {
            System.out.println("Payment failed: amount exceeds daily limit.");
            return;
        }

        Account account = AccountService.getInstance().getAccountByIban(card.getIban());
        PosTransaction transaction = new PosTransaction(card.getIban(), amount, account.getCurrency(), "POS payment at " + merchantName, merchantName, category);
        TransactionService.getInstance().addTransaction(transaction);

        try {
            account.withdraw(amount);
            transaction.complete();
            System.out.println("Card payment completed: " + amount + " at " + merchantName);
        } catch (InsufficientFundsException e) {
            transaction.fail();
            System.out.println("Payment failed: " + e.getMessage());
        }
    }
    public void openDeposit(String customerId, String iban, double amount, double interestRate, Currency currency, LocalDate maturityDate, double penaltyRate) {
        System.out.println("--- OPENING DEPOSIT ---");
        Account account = AccountService.getInstance().getAccountByIban(iban);

        try {
            account.withdraw(amount);
            Deposit deposit = new Deposit(customerId, iban, amount, interestRate, currency, LocalDate.now(), maturityDate, penaltyRate);
            DepositService.getInstance().openDeposit(deposit);
            System.out.println("Deposit opened: " + deposit);
        } catch (InsufficientFundsException e) {
            System.out.println("Cannot open deposit: " + e.getMessage());
        }
    }
    public void purchaseInsurance(String customerId, InsuranceType type, double premium, double coverage, LocalDate startDate, LocalDate expiryDate) {
        System.out.println("--- PURCHASING INSURANCE ---");
        Customer customer = CustomerService.getInstance().getCustomerById(customerId);
        Insurance insurance = new Insurance(customerId, type, premium, coverage, startDate, expiryDate);
        InsuranceService.getInstance().addInsurance(insurance);
        customer.addInsurance(insurance);
        System.out.println("Insurance purchased: " + insurance);
    }
    public Customer registerCustomer(String cnp, String firstName, String lastName, LocalDate dob, String email, String phone, Currency currency) {
        System.out.println("--- REGISTERING NEW CUSTOMER ---");
        Customer customer = new Customer(cnp, firstName, lastName, dob, email, phone);
        CustomerService.getInstance().addCustomer(customer);

        CurrentAccount account = new CurrentAccount(customer.getUserId(), currency, 500, 10);
        AccountService.getInstance().addAccount(account);

        DebitCard card = new DebitCard(account.getIban(), customer.getFullName(), 1000);
        CardService.getInstance().addCard(card);

        System.out.println("Customer registered: " + customer);
        System.out.println("Account created: " + account.getAccountSummary());
        System.out.println("Card issued: " + card);
        return customer;
    }
}