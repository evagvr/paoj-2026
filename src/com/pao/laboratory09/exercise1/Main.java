package com.pao.laboratory09.exercise1;

import java.io.*;
import java.util.*;


public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data contSursa contDestinatie tip)
        // 2. Setează câmpul note = "procesat" pe fiecare tranzacție înainte de serializare
        // 3. Serializează lista de tranzacții în OUTPUT_FILE cu ObjectOutputStream (try-with-resources)
        // 4. Deserializează lista din OUTPUT_FILE cu ObjectInputStream (try-with-resources)
        // 5. Procesează comenzile din stdin până la EOF:
        //    - LIST          → afișează toate tranzacțiile, câte una pe linie
        //    - FILTER yyyy-MM → afișează tranzacțiile cu data care începe cu yyyy-MM
        //                       sau "Niciun rezultat." dacă nu există
        //    - NOTE id        → afișează "NOTE[id]: <valoarea câmpului note>"
        //                       sau "NOTE[id]: not found" dacă id-ul nu există
        //
        // Format linie tranzacție:
        //   [id] data tip: suma RON | contSursa -> contDestinatie
        //   Ex: [1] 2024-01-15 CREDIT: 1500.00 RON | RO01SRC1 -> RO01DST1

        Scanner scanner = new Scanner(System.in);

        new File("output").mkdirs();

        if (!scanner.hasNextInt()) return;
        int n = scanner.nextInt();
        List<Tranzactie> transactions = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int id = scanner.nextInt();
            double suma = scanner.nextDouble();
            String data = scanner.next();
            String contSursa = scanner.next();
            String contDestinatie = scanner.next();
            TipTranzactie tip = TipTranzactie.valueOf(scanner.next());

            Tranzactie t = new Tranzactie(id, suma, data, contSursa, contDestinatie, tip);
            t.setNote("procesat");
            transactions.add(t);
        }

        serializeTransactions(transactions);

        List<Tranzactie> deserializedList = deserializeTransactions();

        while (scanner.hasNext()) {
            String command = scanner.next();
            switch (command) {
                case "LIST":
                    deserializedList.forEach(System.out::println);
                    break;

                case "FILTER":
                    String prefix = scanner.next();
                    boolean found = false;
                    for (Tranzactie t : deserializedList) {
                        if (t.getData().startsWith(prefix)) {
                            System.out.println(t);
                            found = true;
                        }
                    }
                    if (!found) System.out.println("Niciun rezultat.");
                    break;

                case "NOTE":
                    int searchId = scanner.nextInt();
                    Tranzactie foundT = deserializedList.stream()
                            .filter(t -> t.getId() == searchId)
                            .findFirst()
                            .orElse(null);
                    if (foundT != null) {
                        System.out.println("NOTE[" + searchId + "]: " + foundT.getNote());
                    } else {
                        System.out.println("NOTE[" + searchId + "]: not found");
                    }
                    break;
            }
        }
        scanner.close();
    }
    private static void serializeTransactions(List<Tranzactie> list) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            oos.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private static List<Tranzactie> deserializeTransactions() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(OUTPUT_FILE))) {
            return (List<Tranzactie>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}
