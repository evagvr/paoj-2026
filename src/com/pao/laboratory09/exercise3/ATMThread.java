package com.pao.laboratory09.exercise3;

import com.pao.laboratory09.exercise1.TipTranzactie;
import com.pao.laboratory09.exercise1.Tranzactie;

public class ATMThread extends Thread {
    private final int atmId;
    private final CoadaTranzactii coada;

    public ATMThread(int atmId, CoadaTranzactii coada) {
        this.atmId = atmId;
        this.coada = coada;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= 4; i++) {
                int idTranzactie = atmId * 100 + i;
                double suma = 100 + Math.random() * 900;
                Tranzactie t = new Tranzactie(idTranzactie, suma, "2026-05-06", "RO_ATM_" + atmId, "RO_BANK", TipTranzactie.CREDIT);

                System.out.printf("[ATM-%d] trimite: Tranzactie #%d %.2f RON%n", atmId, idTranzactie, suma);
                coada.adauga(t, atmId);
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}