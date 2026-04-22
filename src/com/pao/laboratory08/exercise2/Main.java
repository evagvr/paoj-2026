package com.pao.laboratory08.exercise2;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import com.pao.laboratory08.exercise1.Student;
import com.pao.laboratory08.exercise1.Adresa;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";
    private static final String OUTPUT_FILE = "rezultate.txt";
    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește studenții din FILE_PATH cu BufferedReader
        List<Student> studenti = new ArrayList<>();
        BufferedReader fin = new BufferedReader(new FileReader(FILE_PATH));
        String linie;
        while((linie = fin.readLine()) != null){
            String[] date = linie.split(",");
            if(date.length== 4){
                Adresa adr = new Adresa(date[2].trim(), date[3].trim());
                studenti.add( new Student(date[0].trim(), Integer.parseInt(date[1].trim()), adr));
            }
        }
        fin.close();
        // 2. Citește pragul de vârstă din stdin cu Scanner
        Scanner scanner = new Scanner(System.in);
        Integer prag = scanner.nextInt();
        // 3. Filtrează studenții cu varsta >= prag
        List<Student> studentiFiltrati = studenti.stream()
                .filter(s -> s.getVarsta() >= prag)
                .collect(Collectors.toList());
        // 4. Scrie filtrații în "rezultate.txt" cu BufferedWriter
        BufferedWriter fout = new BufferedWriter(new FileWriter(OUTPUT_FILE));
        System.out.println("Filtru: varsta >= " + prag);
        System.out.println("Rezultate: " + studentiFiltrati.size() + " studenti");
        for (Student s : studentiFiltrati) {
            String output = s.toString();
            fout.write(output);
            fout.newLine();
            System.out.println(output);
        }
        fout.close();
        System.out.println("Scris in: " + OUTPUT_FILE);
        // 5. Afișează sumarul la consolă

    }
}

