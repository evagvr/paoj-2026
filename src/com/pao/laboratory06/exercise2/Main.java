package com.pao.laboratory06.exercise2;

import java.util.*;

public class Main {
    private static void afiseazaSumar(List<Colaborator> lista, String tip) {
        double suma = 0;
        int numar = 0;
        for (Colaborator c : lista) {
            if (c.tipContract().equals(tip)) {
                suma += c.calculeazaVenitNetAnual();
                numar++;
            }
        }
        if (numar > 0) {
            System.out.printf("%s: suma = %.2f lei, număr = %d\n", tip, suma, numar);
        }
    }
    public static void main(String[] args) {
        // Vezi Readme.md pentru cerințe
        Scanner scanner = new Scanner(System.in);
        List<Colaborator> listaColaboratori = new ArrayList<>();
        int numarColaboratori = scanner.nextInt();
        for (int i = 0; i < numarColaboratori; i++) {
            String tip = scanner.next();
            Colaborator colaborator = switch (tip) {
                case "CIM" -> new CIMColaborator();
                case "PFA" -> new PFAColaborator();
                case "SRL" -> new SRLColaborator();
                default -> null;
            };
            if (colaborator != null) {
                colaborator.citeste(scanner);
                listaColaboratori.add(colaborator);
            }
        }
        listaColaboratori.sort(
                Comparator.comparing(Colaborator::tipContract)
                        .thenComparing(Comparator.comparing(Colaborator::calculeazaVenitNetAnual).reversed())
        );
        for (Colaborator c : listaColaboratori) {
            c.afiseaza();
        }
        System.out.println();
//        listaColaboratori.sort(Comparator.comparing(Colaborator::calculeazaVenitNetAnual).reversed());
//        for (Colaborator c : listaColaboratori) {
//            c.afiseaza();
//        }
        Colaborator maxim = Collections.max(listaColaboratori, Comparator.comparing(Colaborator::calculeazaVenitNetAnual));
        System.out.print("Colaborator cu venit net maxim: ");
        maxim.afiseaza();
        System.out.println();

        System.out.println("Colaboratori persoane juridice:");
        for (Colaborator c : listaColaboratori) {
            if (c instanceof PersoanaJuridica) {
                c.afiseaza();
            }
        }
        System.out.println();

        System.out.println("Sume și număr colaboratori pe tip:");
        afiseazaSumar(listaColaboratori, TipColaborator.CIM.name());
        afiseazaSumar(listaColaboratori, TipColaborator.PFA.name());
        afiseazaSumar(listaColaboratori, TipColaborator.SRL.name());
        scanner.close();
    }
}