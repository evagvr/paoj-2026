package com.pao.project.bankingApp;

import com.pao.project.bankingApp.exception.*;
import com.pao.project.bankingApp.model.BankStatement;
import com.pao.project.bankingApp.model.Customer;
import com.pao.project.bankingApp.model.Loan;
import com.pao.project.bankingApp.model.account.Account;
import com.pao.project.bankingApp.model.account.CurrentAccount;
import com.pao.project.bankingApp.model.account.SavingsAccount;
import com.pao.project.bankingApp.model.card.CreditCard;
import com.pao.project.bankingApp.model.enums.Currency;
import com.pao.project.bankingApp.model.enums.InsuranceType;
import com.pao.project.bankingApp.model.enums.MerchantCategory;
import com.pao.project.bankingApp.model.transaction.PosTransaction;
import com.pao.project.bankingApp.model.transaction.Transaction;
import com.pao.project.bankingApp.service.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        SystemOperationsService sysOps = SystemOperationsService.getInstance();
        AnalyticsService analytics = AnalyticsService.getInstance();
        LoanService loanService = LoanService.getInstance();
        DepositService depositService = DepositService.getInstance();
        BankStatementService statementService = BankStatementService.getInstance();
        TransactionService transactionService = TransactionService.getInstance();
        AccountService accountService = AccountService.getInstance();
        CardService cardService = CardService.getInstance();

        // Actiunea 1: Inregistreaza un client nou in sistem
        System.out.println("Actiunea 1: Inregistrare clienti");
        Customer laura = sysOps.registerCustomer(
                "2900515223344", "Laura", "Stanescu",
                LocalDate.of(1990, 5, 15),
                "laura.stanescu@email.ro", "0722111222", Currency.RON
        );

        Customer robert = sysOps.registerCustomer(
                "1871203334455", "Robert", "Badoi",
                LocalDate.of(1987, 12, 3),
                "robert.badoi@email.ro", "0733222333", Currency.RON
        );

        SavingsAccount lauraSavings = new SavingsAccount(laura.getUserId(), Currency.RON, 0.05, 3);
        accountService.addAccount(lauraSavings);
        lauraSavings.deposit(5000);

        CurrentAccount lauraEurAccount = new CurrentAccount(laura.getUserId(), Currency.EUR, 200, 5);
        accountService.addAccount(lauraEurAccount);
        lauraEurAccount.deposit(2000);

        Account lauraCurrent = accountService.getAccountsByCustomer(laura.getUserId()).getFirst();
        lauraCurrent.deposit(10000);

        Account robertCurrent = accountService.getAccountsByCustomer(robert.getUserId()).getFirst();
        robertCurrent.deposit(3000);

        CreditCard lauraCreditCard = new CreditCard(
                lauraCurrent.getIban(), laura.getFullName(),
                2000, 5000, 100, LocalDate.now().plusMonths(1));
        cardService.addCard(lauraCreditCard);
        lauraCreditCard.spend(1500);

        System.out.println("\nClienti inregistrati(in ordine alfabetica): ");
        CustomerService.getInstance().getAllCustomers()
                .forEach(c -> System.out.println("  " + c.getFullName()));
        System.out.println("Cont curent Laura: " + lauraCurrent.getAccountSummary());
        System.out.println("Cont economii Laura: " + lauraSavings.getAccountSummary());

        // WithdrawalLimitException
        System.out.println("WithdrawalLimitException:");
        System.out.println("\nRetrageri din contul de economii al Laurei:");
        try {
            lauraSavings.withdraw(100);
            System.out.println("Retragere 1 reusita. Retrageri efectuate: " + lauraSavings.getWithdrawals() + "/" + lauraSavings.getWithdrawalLimit());
            lauraSavings.withdraw(100);
            System.out.println("Retragere 2 reusita. Retrageri efectuate: " + lauraSavings.getWithdrawals() + "/" + lauraSavings.getWithdrawalLimit());
            lauraSavings.withdraw(100);
            System.out.println("Retragere 3 reusita. Retrageri efectuate: " + lauraSavings.getWithdrawals() + "/" + lauraSavings.getWithdrawalLimit());
            lauraSavings.withdraw(100);
        } catch (WithdrawalLimitExceededException e) {
            System.out.println("Eroare: " + e.getMessage());
        }

        // Actiunea 2: Efectueaza un transfer bancar intre doua conturi
        System.out.println("\nActiunea 2: Transfer bancar intre 2 conturi");
        System.out.println("\nTransfer de la Laura catre Robert:");
        sysOps.processTransfer(lauraCurrent.getIban(), robertCurrent.getIban(), 1500, Currency.RON);

        System.out.println("Transfer cu fonduri insuficiente:");
        sysOps.processTransfer(robertCurrent.getIban(), lauraCurrent.getIban(), 99999, Currency.RON);

        // InsufficientFundsException
        System.out.println("InsufficientFundsException:");
        try {
            lauraCurrent.withdraw(999999);
        } catch (InsufficientFundsException e) {
            System.out.println("Eroare: " + e.getMessage());
        }

        // Actiunea 3: Plateste cu cardul la un comerciant
        System.out.println("\nActiunea 3: Plati cu cardul");
        System.out.println("\nPlati cu cardul Laurei:");
        String lauraCardNumber = cardService.getCardsByIban(lauraCurrent.getIban()).getFirst().getCardNumber();
        sysOps.processCardPayment(lauraCardNumber, 250, "Kaufland", MerchantCategory.GROCERIES);
        sysOps.processCardPayment(lauraCardNumber, 120, "Netflix", MerchantCategory.ENTERTAINMENT);

        // CardNotFoundException
        System.out.println("CardNotFoundException:");
        try {
            sysOps.processCardPayment("0000000000000000", 100, "Magazin", MerchantCategory.GROCERIES);
        } catch (CardNotFoundException e) {
            System.out.println("Eroare: " + e.getMessage());
        }

        //Actiunea 4: Aplica pentru un credit si achita rate lunare
        System.out.println("\nActiunea 4: Credit si rate");
        System.out.println("\nCerere credit Laura:");
        double sumaImprumut = 20000;
        double scor = analytics.scoreLoanApplication(laura.getUserId(), sumaImprumut);
        System.out.printf("Scor bonitate: %.1f/100 pentru suma de %.0f RON%n", scor, sumaImprumut);

        Loan lauraLoan = null;
        if (scor >= 50) {
            lauraLoan = new Loan(
                    laura.getUserId(), lauraCurrent.getIban(),
                    sumaImprumut, 0.07,
                    LocalDate.now(), LocalDate.now().plusYears(3));
            try {
                loanService.applyForLoan(lauraLoan);
                lauraCurrent.deposit(sumaImprumut);
                System.out.println("Credit aprobat: " + lauraLoan);
            } catch (CustomerNotFoundException e) {
                System.out.println("Eroare: " + e.getMessage());
            }
        }

        // LoanNotFoundException
        System.out.println("LoanNotFoundException:");
        try {
            loanService.payInstalment("id-inexistent");
        } catch (LoanNotFoundException e) {
            System.out.println("Eroare: " + e.getMessage());
        }

        if (lauraLoan != null) {
            try {
                loanService.payInstalment(lauraLoan.getLoanId());
                System.out.printf("Rata achitata. Datorie ramasa: %.2f RON%n", lauraLoan.getRemainingDebt());
                System.out.println("Plati la timp: " + laura.getCreditHistory().getOnTimePayments());
            } catch (LoanAlreadyRepaidException e) {
                System.out.println("Eroare: " + e.getMessage());
            }
        }

        // LoanAlreadyRepaidException
        System.out.println("LoanAlreadyRepaidException:");
        try {
            Loan creditScurt = new Loan(laura.getUserId(), lauraCurrent.getIban(),
                    100, 0.0, LocalDate.now(), LocalDate.now().plusMonths(1));
            loanService.applyForLoan(creditScurt);
            loanService.payInstalment(creditScurt.getLoanId());
            loanService.payInstalment(creditScurt.getLoanId());
        } catch (LoanAlreadyRepaidException e) {
            System.out.println("Eroare: " + e.getMessage());
        }

        // Actiunea 5: Deschide un depozit la termen
        System.out.println("\nActiunea 5: Deschidere depozit");
        System.out.println("\nDeschidere depozit pentru Laura:");
        sysOps.openDeposit(laura.getUserId(), lauraCurrent.getIban(),
                3000, 0.06, Currency.RON, LocalDate.now().plusYears(1), 0.30);

        sysOps.openDeposit(robert.getUserId(), robertCurrent.getIban(),
                999999, 0.05, Currency.RON, LocalDate.now().plusYears(1), 0.20);

        // DepositNotFoundException
        System.out.println("DepositNotFoundException:");
        try {
            depositService.getDepositById("id-inexistent");
        } catch (DepositNotFoundException e) {
            System.out.println("Eroare: " + e.getMessage());
        }

        // Actiunea 6: Achizitioneaza o polita de asigurare
        System.out.println("\nActiunea 6: Achizitie asigurare");
        System.out.println("\nAchizitie asigurare pentru Laura:");
        sysOps.purchaseInsurance(laura.getUserId(), InsuranceType.HEALTH,
                50, 100000, LocalDate.now(), LocalDate.now().plusYears(1));

        // InsuranceNotFoundException
        System.out.println("InsuranceNotFoundException:");
        try {
            InsuranceService.getInstance().cancelInsurance("id-inexistent");
        } catch (InsuranceNotFoundException e) {
            System.out.println("Eroare: " + e.getMessage());
        }

        // Actiunea 7: Genereaza un extras de cont pentru o perioada data
        System.out.println("\nActiunea 7: Extras de cont");
        System.out.println("\nExtras de cont Laura:");
        try {
            BankStatement extras = statementService.generateStatement(
                    lauraCurrent.getIban(),
                    LocalDate.now().minusDays(30),
                    LocalDate.now());
            System.out.println(extras);
            System.out.printf("Sold initial: %.2f RON | Sold final: %.2f RON%n",
                    extras.getOpeningBalance(), extras.getClosingBalance());
            System.out.printf("Total intrari: %.2f RON | Total iesiri: %.2f RON%n",
                    extras.getTotalInflows(), extras.getTotalOutflows());
        } catch (AccountNotFoundException e) {
            System.out.println("Eroare: " + e.getMessage());
        }

        // AccountNotFoundException
        System.out.println("AccountNotFoundException:");
        try {
            accountService.getAccountByIban("RO00XXXXXX00000000000");
        } catch (AccountNotFoundException e) {
            System.out.println("Eroare: " + e.getMessage());
        }

        // BankStatementNotFoundException
        System.out.println("BankStatementNotFoundException:");
        try {
            statementService.getStatementById("id-inexistent");
        } catch (StatementNotFoundException e) {
            System.out.println("Eroare: " + e.getMessage());
        }

        //Actiunea 8: Detecteaza tranzactiile suspecte ale unui cont
        System.out.println("\nActiunea 8: Detectare tranzactii suspecte");
        Customer ada = sysOps.registerCustomer(
                "2000303456789", "Ada", "Marin",
                LocalDate.of(2000, 3, 3),
                "ada.marin@email.ro", "0745323894", Currency.RON
        );
        Account adaCurrent = accountService.getAccountsByCustomer(ada.getUserId()).getFirst();
        adaCurrent.deposit(50000);

        int[] cumparaturi = {50, 80, 60, 45, 70, 90, 55, 65, 75, 85, 55, 79, 85};
        for (int suma : cumparaturi) {
            PosTransaction t = new PosTransaction(adaCurrent.getIban(), suma, Currency.RON,
                    "Cumparaturi uzuale", "Supermarket", MerchantCategory.GROCERIES);
            transactionService.addTransaction(t);
            t.complete();
            adaCurrent.withdraw(suma);
        }

        PosTransaction t1 = new PosTransaction(adaCurrent.getIban(), 8000, Currency.RON,
                "Achizitie de valoare mare", "Magazin Electrocasnice", MerchantCategory.ELECTRONICS);
        transactionService.addTransaction(t1);
        t1.complete();
        adaCurrent.withdraw(8000);

        PosTransaction t2 = new PosTransaction(adaCurrent.getIban(), 12000, Currency.RON,
                "Achizitie de valoare mare", "Bijuterii Lux", MerchantCategory.ENTERTAINMENT);
        transactionService.addTransaction(t2);
        t2.complete();
        adaCurrent.withdraw(12000);

        System.out.println("\nTranzactii suspecte detectate pentru Ada:");
        List<Transaction> suspecte = analytics.detectAnomalousTransactions(adaCurrent.getIban());
        if (suspecte.isEmpty()) {
            System.out.println("Nicio tranzactie suspecta.");
        } else {
            suspecte.forEach(t -> System.out.printf("SUSPECT: %.2f RON - %s%n", t.getAmount(), t.getDescription()));
        }

        //Actiunea 9: Afiseaza cheltuielile unui client grupate pe categorii
        System.out.println("\nActiunea 9: Cheltuieli pe categorii");
        System.out.println("\nCheltuieli pe categorii pentru Laura:");
        HashMap<MerchantCategory, Double> cheltuieli = analytics.getSpendingBreakdownByCategory(lauraCurrent.getIban());
        if (cheltuieli.isEmpty()) {
            System.out.println("Nu exista tranzactii finalizate.");
        } else {
            cheltuieli.forEach((cat, suma) -> System.out.printf("  %s: %.2f RON%n", cat, suma));
        }

        //Actiunea 10: Calculeaza averea neta a unui client
        System.out.println("\nActiunea 10: Avere neta client");
        System.out.printf("%nAvere neta Laura: %.2f RON%n", analytics.computeNetWorth(laura.getUserId()));

        // Actiunea 11: Calculeaza gradul de utilizare a cardurilor de credit
        System.out.println("\nActiunea 11: Grad utilizare carduri de credit");
        System.out.println("\nGrad utilizare carduri de credit Laura:");
        analytics.computeCreditCardUtilization(laura.getUserId())
                .forEach((card, grad) -> System.out.printf("  %s  %.1f%% utilizat%n", card, grad));

        // Actiunea 12: Identifica clientii cu risc ridicat
        System.out.println("\nActiunea 12: Clienti cu risc ridicat");
        System.out.println("\nClienti cu risc ridicat:");
        List<Customer> riscRidicat = analytics.flagHighRiskCustomers();
        if (riscRidicat.isEmpty()) {
            System.out.println("Niciun client cu risc ridicat.");
        } else {
            riscRidicat.forEach(c -> System.out.println("  " + c.getFullName()));
        }

        // Actiunea 13: Afiseaza top N tranzactii dupa valoare
        System.out.println("\nActiunea 13: Top tranzactii dupa valoare");
        System.out.println("\nTop 3 tranzactii dupa valoare:");
        analytics.getTopNTransactions(3)
                .forEach(t -> System.out.printf("  %.2f RON - %s%n", t.getAmount(), t.getDescription()));

        // Actiunea 14: Grupeaza toate tranzactiile din sistem pe categorii
        System.out.println("\nActiunea 14: Tranzactii generale pe categorii");
        analytics.groupTransactionsByCategory()
                .forEach((cat, lista) -> System.out.printf("  %s: %d tranzactii%n", cat, lista.size()));

        // Actiunea 15: Ruleaza procesarea de sfarsit de luna
        System.out.println("\n Actiunea 15: Procesare sfarsit de luna");
        sysOps.runEndOfMonthBatch();

        // Actiunea 16: Genereaza un raport financiar complet pentru un client
        System.out.println("\nActiunea 16: Genereaza un raport financiar complet");
        sysOps.generateCustomerReport(laura.getUserId());

        //Actiunea 17: Sterge un client din sistem
        System.out.println("\nActiunea 17: Stergere client");
        System.out.println("\nStergere client Robert Badoi:");
        sysOps.offboardCustomer(robert.getUserId());

        //CustomerNotFoundException
        System.out.println("CustomerNotFoundException: ");
        try {
            CustomerService.getInstance().getCustomerById(robert.getUserId());
        } catch (CustomerNotFoundException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }
}
