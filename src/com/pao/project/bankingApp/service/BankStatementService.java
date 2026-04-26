package com.pao.project.bankingApp.service;

import com.pao.project.bankingApp.model.enums.AtmOperationType;
import com.pao.project.bankingApp.model.enums.TransactionStatus;
import com.pao.project.bankingApp.exception.AccountNotFoundException;
import com.pao.project.bankingApp.exception.StatementNotFoundException;
import com.pao.project.bankingApp.model.BankStatement;
import com.pao.project.bankingApp.model.account.Account;
import com.pao.project.bankingApp.model.transaction.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BankStatementService {
    private HashMap<String, BankStatement> statementsById;
    private HashMap<String, List<BankStatement>> statementsByAccount;
    private static BankStatementService instance = null;

    private BankStatementService() {
        this.statementsById = new HashMap<>();
        this.statementsByAccount = new HashMap<>();
    }

    public static BankStatementService getInstance() {
        if (instance == null) {
            instance = new BankStatementService();
        }
        return instance;
    }

    public BankStatement generateStatement(String accountIban, LocalDate from, LocalDate to)
            throws AccountNotFoundException {
        Account account = AccountService.getInstance().getAccountByIban(accountIban);

        List<Transaction> all = TransactionService.getInstance()
                .getTransactionsByAccount(accountIban);

        List<Transaction> inPeriod = new ArrayList<>();
        for (Transaction t : all) {
            LocalDate date = t.getTimestamp().toLocalDate();
            if (!date.isBefore(from) && !date.isAfter(to) && t.getStatus() == TransactionStatus.COMPLETED) {
                inPeriod.add(t);
            }
        }
        double openingBalance = account.getBalance();

        for (Transaction t : all) {
            if (t.getStatus() != TransactionStatus.COMPLETED) continue;
            LocalDate date = t.getTimestamp().toLocalDate();

            if (!date.isBefore(from)) {
                if (t instanceof ATMTransaction atm) {
                    if (atm.getAtmOperationType() == AtmOperationType.DEPOSIT)
                        openingBalance -= t.getAmount();
                    else
                        openingBalance += t.getAmount();
                } else if (t instanceof TransferTransaction transfer) {
                    if (transfer.getDestinationIban().equals(accountIban))
                        openingBalance -= t.getAmount();
                    else
                        openingBalance += t.getAmount();
                } else if (t instanceof PosTransaction || t instanceof OnlineTransaction) {
                    openingBalance += t.getAmount();
                }
            }
        }
        BankStatement statement = new BankStatement(
                accountIban,
                account.getCustomerId(),
                from,
                to,
                openingBalance,
                inPeriod
        );

        statementsById.put(statement.getStatementId(), statement);
        statementsByAccount
                .computeIfAbsent(accountIban, k -> new ArrayList<>())
                .add(statement);

        return statement;
    }

    public BankStatement getStatementById(String statementId) throws StatementNotFoundException {
        BankStatement statement = statementsById.get(statementId);
        if (statement == null) throw new StatementNotFoundException(statementId);
        return statement;
    }

    public List<BankStatement> getStatementsByAccount(String iban) {
        return statementsByAccount.getOrDefault(iban, new ArrayList<>());
    }
    public void removeStatement(String statementId) {
        BankStatement statement = getStatementById(statementId);
        statementsByAccount.get(statement.getAccountIban()).remove(statement);
        statementsById.remove(statementId);
    }
    public List<BankStatement> getAllStatements() {
        return new ArrayList<>(statementsById.values());
    }
}