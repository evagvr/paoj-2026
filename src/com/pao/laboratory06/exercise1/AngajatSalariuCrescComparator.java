package com.pao.laboratory06.exercise1;

import java.util.Comparator;

public class AngajatSalariuCrescComparator implements Comparator<Angajat> {
    @Override
    public int compare(Angajat a1, Angajat a2){
        return Double.compare(a1.getSalariu(), a2.getSalariu());
    }
}
