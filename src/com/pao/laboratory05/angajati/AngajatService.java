package com.pao.laboratory05.angajati;

import java.util.Arrays;

public class AngajatService {
    private Angajat[] angajati;
    private static class Holder{
        private static final AngajatService INSTANCE = new AngajatService();
    }
    public static AngajatService getInstance(){
        return Holder.INSTANCE;
    }
    private AngajatService(){
        this.angajati = new Angajat[0];
    }
    public void addAngajat(Angajat angajat){
        Angajat[] newAngajati = new Angajat[angajati.length + 1];
        System.arraycopy(angajati, 0, newAngajati, 0, angajati.length);
        newAngajati[angajati.length] = angajat;
        this.angajati = newAngajati;
        System.out.println("Angajat adăugat cu succes: " + angajat.getNume());
    }
    public void printAll(){
        for( Angajat a: angajati){
            System.out.println(a);
        }
    }
    public void listBySalary(){
        Angajat[] copy = angajati.clone();
        Arrays.sort(copy);
        for (Angajat a: copy){
            System.out.println(a);
        }
    }
    public void findByDepartament(String numeDept){
        boolean gasit = false;
        for (Angajat a: angajati){
            if (a.getDepartament().nume().equalsIgnoreCase(numeDept)){
                gasit = true;
                System.out.println(a);
            }
        }
        if (!gasit){
            System.out.println("Niciun angajat în departamentul: "+ numeDept);
        }
    }

}
