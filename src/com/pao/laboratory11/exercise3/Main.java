package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // TODO: Manual demo for bonus requirements.
        List<Transaction> data = Arrays.asList(
                new Transaction(1, new BigDecimal("5000.00"), "RO", "IT"),
                new Transaction(2, new BigDecimal("1200.00"), "RO", "FOOD"),
                new Transaction(3, new BigDecimal("800.00"), "FR", "IT"),
                new Transaction(4, new BigDecimal("2500.00"), "FR", "AUTO"),
                new Transaction(5, new BigDecimal("100.00"), "DE", "FOOD")
        );

        AuditSnapshot snap = data.stream().collect(CustomCollectors.toAuditSnapshot());

        System.out.println("RAPORT ANALITIC: ");

        System.out.println("Valoare medie tranzactie: " + snap.getAverageValue());

        System.out.println("Volum pe categorii:");
        snap.getAmountByCategory().entrySet().stream()
                        .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                        .forEach(e -> System.out.println(e.getKey() + " -> " + e.getValue()));
        System.out.println("Top Tranzactie per Tara:");
        snap.getTopTransactionByCountry().forEach((country, tx) ->
                System.out.println(" - " + country + ": ID " + tx.getId() + " cu suma " + tx.getAmount()));
        System.out.println("\nTranzactii High-Value identificate:");
        snap.getHighValueTransactions().forEach(t ->
                System.out.println("ID: " + t.getId() + " | Suma: " + t.getAmount()));
    }
}
