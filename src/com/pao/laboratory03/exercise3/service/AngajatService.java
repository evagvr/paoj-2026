package com.pao.laboratory03.exercise3.service;

import com.pao.laboratory03.exercise3.model.Angajat;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Completează cele 3 metode.
 * Folosește ArrayList — nu mai e nevoie de redimensionare manuală.
 */
public class AngajatService {
    private List<Angajat> angajati;

    public AngajatService() {
        this.angajati = new ArrayList<>();
    }

    /** TODO: angajati.add(a); println("Angajat adăugat: " + a.getName()); */
    public void addAngajat(Angajat a) {
        // TODO
        angajati.add(a);
        System.out.println("Angajat adăugat: " + a.getName());
    }

    /** TODO: dacă goală → mesaj; altfel parcurge cu index și afișează (i+1) + ". " + angajat */
    public void listAll() {
        // TODO
        if (angajati.isEmpty()){
            System.out.println("Lista de angajati este goala");
        }
        else{
            for (int i = 0; i < angajati.size(); i++) {
                System.out.println((i + 1) + ". " + angajati.get(i));
            }
        }
    }

    /** TODO: parcurge lista, sumează a.salariuTotal(), returnează totalul. */
    public double totalSalarii() {
        if (angajati.isEmpty()){
            return 0.0;
        }
        else{
            double total = 0;
            for (Angajat a: angajati){
                total += a.salariuTotal();                        // TODO
            }
            return total;
        }
    }
}
