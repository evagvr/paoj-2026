package com.pao.laboratory07.exercise3;


import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        // Vezi Readme.md pentru cerințe

        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        List<Comanda> comenzi = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String tip = sc.next();
            String nume = sc.next();

            switch (tip) {
                case "STANDARD" -> {
                    double pret = sc.nextDouble();
                    String client = sc.next();
                    comenzi.add(new ComandaStandard(nume, pret, client));
                }
                case "DISCOUNTED" -> {
                    double pret = sc.nextDouble();
                    int discount = sc.nextInt();
                    String client = sc.next();
                    comenzi.add(new ComandaRedusa(nume, pret, discount, client));
                }
                case "GIFT" -> {
                    String client = sc.next();
                    comenzi.add(new ComandaGratuita(nume, client));
                }
            }
        }
        comenzi.forEach(c -> System.out.println(c.descriere()));
        while (sc.hasNext()) {
            String command = sc.next();
            if (command.equals("QUIT")) {
                break;
            }
            switch (command) {
                case "STATS" -> {
                    System.out.println("--- STATS ---");
                    Map<String, Double> medii = comenzi.stream()
                            .collect(Collectors.groupingBy(
                                    c -> {
                                        if (c instanceof ComandaStandard) return "STANDARD";
                                        if (c instanceof ComandaRedusa) return "DISCOUNTED";
                                        return "GIFT";
                                    },
                                    Collectors.averagingDouble(Comanda::pretFinal)
                            ));
                    if (medii.containsKey("STANDARD")) System.out.printf( "STANDARD: medie = %.2f lei\n", medii.get("STANDARD"));
                    if (medii.containsKey("DISCOUNTED")) System.out.printf("DISCOUNTED: medie = %.2f lei\n", medii.get("DISCOUNTED"));
                    if (medii.containsKey("GIFT")) System.out.printf("GIFT: medie = %.2f lei\n", medii.get("GIFT"));
                }
                case "FILTER" -> {
                    double threshold = sc.nextDouble();
                    System.out.printf("--- FILTER (>= %.2f) ---\n", threshold);
                    List<Comanda> filtered = comenzi.stream()
                            .filter(c -> c.pretFinal() >= threshold).toList();
                    filtered.forEach(c -> System.out.println(c.descriere()));
                }
                case "SORT" -> {
                    System.out.println("--- SORT (by client, then by pret) ---");
                    comenzi.stream()
                            .sorted(Comparator.comparing(Comanda::getClient)
                                    .thenComparing(Comanda::pretFinal))
                            .forEach(c -> System.out.println(c.descriere()));
                }
                case "SPECIAL" -> {
                    System.out.println("--- SPECIAL (discount > 15%) ---");
                    comenzi.stream()
                            .filter(c -> c instanceof ComandaRedusa cr && cr.getDiscountProcent() > 15)
                            .forEach(c -> System.out.println(c.descriere()));
                }
            }
        }
    }
}
