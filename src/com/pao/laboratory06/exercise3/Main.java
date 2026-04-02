package com.pao.laboratory06.exercise3;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // Vezi Readme.md pentru cerințe
        Inginer[] ingineri = {
                new Inginer("Ionescu", "Ion", "0725341546", 6000, 10000),
                new Inginer("Ganescu", "Ana", "0723411222", 8000, 5000),
                new Inginer("Zaharia", "Andreea", null, 4500, 2000),
                new Inginer("Ilie", "Robert", "076575478", 5000, 3000),
                new Inginer("Marin", "Sebastian", "07293904", 9000, 7000)
        };
        System.out.println("Lista initiala ingineri:");
        for (Inginer i : ingineri)
            System.out.println(i);
        Arrays.sort(ingineri);
        System.out.println("\nSortare naturala (alfabetica):");
        for (Inginer i : ingineri)
            System.out.println(i);

        Arrays.sort(ingineri, new ComparatorInginerSalariu());
        System.out.println("\nSortare descrescatoare dupa salariu:");
        for (Inginer i : ingineri)
            System.out.println(i);

        System.out.println("\nautentificare inginer si consultare sold prin upcast:");
        PlataOnline referintaPlata = ingineri[0];
        referintaPlata.autentificare("admin", "1234");
        System.out.println("Sold disponibil: " + referintaPlata.consultareSold());

        System.out.println("\ntrimitere sms pt persoana juridica prin upcast:");
        PersoanaJuridica persJuridicaValid = new PersoanaJuridica("TechSRL", "", "0799888777", 50000);
        PlataOnlineSMS referintaSMS = persJuridicaValid;
        System.out.println("\nse trimite sms valid:");
        referintaSMS.trimiteSMS("Plata de 100 RON a fost confirmata.");
        boolean smsGol = referintaSMS.trimiteSMS("");
        System.out.println("\nse trimite sms cu mesaj gol:");
        System.out.println("trimiteSms returneaza: " + smsGol);
        System.out.println("Istoric SMS-uri TechSRL: " + persJuridicaValid.getSmsTrimise());

        PersoanaJuridica persJuridicaFaraTelefon = new PersoanaJuridica("NoPhoneSRL", "", null, 20000);
        boolean trimis = persJuridicaFaraTelefon.trimiteSMS("Mesaj test");
        System.out.println("\ntrimiteSMS cu telefon null:");
        System.out.println("functia trimiteSMS returneaza:" + trimis);

        System.out.println("\nValoarea TVA-ului curent este: " + ConstanteFinanciare.TVA.getValoare() * 100 + "%");

        try {
            PlataOnline inginerPlata = new Inginer("Fara", "SMS", "074654789", 5000, 100);
            System.out.println("\nIncercam trimiterea SMS unui Inginer");
            inginerPlata.trimiteSMS("Salut!");
        } catch (UnsupportedOperationException e) {
            System.out.println("Exceptie prinsa cu succes: " + e.getMessage());
        }
        try {
            System.out.println("\nIncercam autentificarea cu user null");
            referintaPlata.autentificare(null, "parola");
        } catch (IllegalArgumentException e) {
            System.out.println("Exceptie prinsa cu succes: " + e.getMessage());
        }
    }
}
