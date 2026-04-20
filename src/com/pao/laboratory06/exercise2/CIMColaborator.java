package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class CIMColaborator extends Colaborator implements PersoanaFizica{
    private boolean bonus;
    public CIMColaborator(){
        super();
    }
    public CIMColaborator(String nume, String prenume, double venitBrutLunar, boolean bonus){
        super(nume, prenume, venitBrutLunar);
        this.bonus = bonus;
    }
    @Override
    public boolean areBonus(){
        return this.bonus;
    }
    @Override
    public String tipContract(){
        return TipColaborator.CIM.name();
    }
    @Override
    public void citeste(Scanner in){
        super.citeste(in);
        String restLinie = in.nextLine().trim();
        this.bonus = restLinie.equals("DA");
    }
    @Override
    public void afiseaza() {
        System.out.printf("CIM: %s %s, venit net anual: %.2f lei\n", getNume(), getPrenume(), calculeazaVenitNetAnual());
    }
    @Override
    public double calculeazaVenitNetAnual(){
        double venitNetAnual = venitBrutLunar * 12 * 0.55;
        if (bonus)
            venitNetAnual += 0.1 * venitNetAnual;
        return venitNetAnual;
    }
}
