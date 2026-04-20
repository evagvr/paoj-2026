package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public abstract class Colaborator implements IOOperatiiScriereCitire{
    private String nume;
    private String prenume;
    protected double venitBrutLunar;
    public Colaborator(){}
    public Colaborator(String nume, String prenume, double venitBrutLunar){
        this.nume = nume;
        this.prenume = prenume;
        this.venitBrutLunar = venitBrutLunar;
    }
    public String getNume(){
        return nume;
    }
    public String getPrenume(){
        return prenume;
    }
    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitBrutLunar = in.nextDouble();
    }
    public abstract double calculeazaVenitNetAnual();
}
