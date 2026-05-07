package com.pao.laboratory10.exercise3;

import com.pao.laboratory10.exercise1.TipTranzactie;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        // Vezi Readme.md pentru cerințe

        List<Tranzactie> tranzactii = Arrays.asList(
                new Tranzactie(1, 1500.0, "2026-01-10", TipTranzactie.CREDIT, "RO01"),
                new Tranzactie(2, 200.0, "2026-01-15", TipTranzactie.DEBIT, "RO01"),
                new Tranzactie(3, 50.0, "2026-01-20", TipTranzactie.DEBIT, "RO02"),
                new Tranzactie(4, 3000.0, "2026-02-05", TipTranzactie.CREDIT, "RO03"),
                new Tranzactie(5, 750.0, "2026-02-14", TipTranzactie.DEBIT, "RO01"),
                new Tranzactie(6, 120.0, "2026-02-28", TipTranzactie.DEBIT, "RO04"),
                new Tranzactie(7, 450.0, "2026-03-02", TipTranzactie.CREDIT, "RO02"),
                new Tranzactie(8, 800.0, "2026-03-10", TipTranzactie.DEBIT, "RO03"),
                new Tranzactie(9, 100.0, "2026-03-15", TipTranzactie.DEBIT, "RO02"),
                new Tranzactie(10, 2100.0, "2026-03-25", TipTranzactie.CREDIT, "RO01")
        );

        System.out.println("\n# 1. Lista tuturor tranzactiilor CREDIT:");
        tranzactii.stream()
                .filter(t -> t.getTip() == TipTranzactie.CREDIT)
                .forEach(System.out::println);

        double total = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .sum();
        System.out.printf("\n# 2. Total procesat: %.2f RON%n", total);

        System.out.println("\n# 3. Per luna (Suma totala):");
        Map<String, Double> sumaPerLuna = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new,
                        Collectors.summingDouble(Tranzactie::getSuma)
                ));
        sumaPerLuna.forEach((luna, suma) -> System.out.printf("%s: %.2f RON%n", luna, suma));

        System.out.println("\n# 4. Top 3 tranzactii:");
        tranzactii.stream()
                .sorted(Comparator.comparingDouble(Tranzactie::getSuma).reversed())
                .limit(3)
                .forEach(System.out::println);

        List<String> surseUnice = tranzactii.stream()
                .map(Tranzactie::getContSursa)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("\n# 5. Conturi sursa unice: " + surseUnice);

        double medie = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .average()
                .orElse(0.0);
        System.out.printf("\n# 6. Suma medie: %.2f RON%n", medie);

        System.out.println("\n# 7. EXTRASA DE CONT LUNARE:");
        Map<String, List<Tranzactie>> grupate = tranzactii.stream()
                .collect(Collectors.groupingBy(t -> t.getData().substring(0, 7), TreeMap::new, Collectors.toList()));

        grupate.forEach((luna, lista) -> {
            double totalLuna = lista.stream().mapToDouble(Tranzactie::getSuma).sum();
            System.out.printf("EXTRAS DE CONT - %s: %d tranzactii, total: %.2f RON%n",
                    luna, lista.size(), totalLuna);
        });
    }
}
