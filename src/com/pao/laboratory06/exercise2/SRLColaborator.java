package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class SRLColaborator extends Colaborator implements PersoanaJuridica{
    private double cheltuieliLunare;
    public SRLColaborator(){
        super();
    }
    public SRLColaborator(String nume, String prenume, double venitBrutLunar, double cheltuieliLunare){
        super(nume, prenume, venitBrutLunar);
        this.cheltuieliLunare = cheltuieliLunare;
    }
    @Override
    public void citeste(Scanner in){
        super.citeste(in);
        this.cheltuieliLunare = in.nextDouble();
    }
    @Override
    public double calculeazaVenitNetAnual(){
        double venitNetAnual =( venitBrutLunar - cheltuieliLunare)* 12 * 0.84;
        return  venitNetAnual;
    }
    @Override
    public String tipContract() {
        return TipColaborator.SRL.name();
    }
    @Override
    public void afiseaza() {
        System.out.printf("%s: %s %s, venit net anual: %.2f lei\n", tipContract(), getNume(), getPrenume(), calculeazaVenitNetAnual());
    }
}
