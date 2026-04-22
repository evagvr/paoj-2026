package com.pao.laboratory08.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    // Calea către fișierul cu date — relativă la rădăcina proiectului
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește studenții din FILE_PATH cu BufferedReader
        List<Student> studenti = new ArrayList<>();
        BufferedReader fin = new BufferedReader(new FileReader(FILE_PATH));
        String linie;
        while ((linie = fin.readLine()) != null) {
            String[] date = linie.split(",");
            if (date.length == 4) {
                Adresa adr = new Adresa(date[2].trim(), date[3].trim());
                studenti.add(new Student(date[0].trim(), Integer.parseInt(date[1].trim()), adr));
            }
        }
        fin.close();

        // 2. Citește comanda din stdin: PRINT, SHALLOW <nume> sau DEEP <nume>
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String[] parts = input.split(" ", 2);
        String comanda = parts[0];
        // 3. Execută comanda:
        //    - PRINT → afișează toți studenții
        //    - SHALLOW <nume> → shallow clone + modifică orașul clonei la "MODIFICAT" + afișează
        //    - DEEP <nume> → deep clone + modifică orașul clonei la "MODIFICAT" + afișează

        switch (comanda) {
            case "PRINT":
                studenti.forEach(System.out::println);
                break;

            case "SHALLOW":
                executaClonare(studenti, parts[1], false);
                break;

            case "DEEP":
                executaClonare(studenti, parts[1], true);
                break;
        }
    }
    private static void executaClonare (List<Student> studenti, String nume,boolean isDeep) throws CloneNotSupportedException {
        for (Student s : studenti) {
            if (s.getNume().equalsIgnoreCase(nume)) {
                Student clona = isDeep ? s.deepClone() : (Student) s.clone();
                clona.getAdresa().setOras("MODIFICAT");
                System.out.println("Original: " + s);
                System.out.println("Clona: " + clona);
                return;
            }
        }
    }

}
