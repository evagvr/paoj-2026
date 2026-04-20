package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public interface IOOperatiiScriereCitire {
    void citeste(Scanner in);
    void afiseaza();
    String tipContract();
    default boolean areBonus(){
        return false;
    }
}
