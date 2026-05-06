package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class PFAColaborator extends Colaborator implements PersoanaFizica{
    private double cheltuieliLunare;
    public PFAColaborator(){
        super();
    }
    public PFAColaborator(String nume, String prenume, double venitBrutLunar, double cheltuieliLunare){
        super(nume, prenume, venitBrutLunar);
        this.cheltuieliLunare = cheltuieliLunare;
    }
    @Override
    public String tipContract(){
        return TipColaborator.PFA.name();
    }
    @Override
    public void citeste(Scanner in){
        super.citeste(in);
        this.cheltuieliLunare = in.nextDouble();
    }
    @Override
    public void afiseaza() {
        System.out.printf("PFA: %s %s, venit net anual: %.2f lei\n", getNume(), getPrenume(), calculeazaVenitNetAnual());
    }
    @Override
    public double calculeazaVenitNetAnual(){
        double salariuMinimBrut = 4050;
        double venitNet = (venitBrutLunar - cheltuieliLunare) * 12;
        double impozitPeVenit = 0.1 * venitNet;
        double cass;
        if (venitNet < 6 * salariuMinimBrut)
            cass = 0.1*(6*salariuMinimBrut);
        else if (venitNet >= 6*salariuMinimBrut && venitNet <= 72*salariuMinimBrut)
            cass = 0.1 *venitNet;
        else
            cass = 0.1 *(72*salariuMinimBrut);
        double cas;
        if (venitNet < 12 * salariuMinimBrut)
            cas = 0;
        else if (venitNet >= 12 * salariuMinimBrut && venitNet <= 24*salariuMinimBrut)
            cas = 0.25 * (12 * salariuMinimBrut);
        else
            cas = 0.25 * (24 * salariuMinimBrut);
        double venitNetAnual = venitNet - impozitPeVenit - cass - cas;
        return venitNetAnual;
    }
}
