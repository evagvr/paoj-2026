package com.pao.laboratory09.exercise3;

import com.pao.laboratory09.exercise1.Tranzactie;

public class ProcessorThread implements Runnable {
    private final CoadaTranzactii coada;
    public volatile boolean activ = true;
    private int totalProcesate = 0;

    public ProcessorThread(CoadaTranzactii coada) {
        this.coada = coada;
    }

    @Override
    public void run() {
        try {
            while (activ || coada.getDimensiune() > 0) {
                synchronized (coada) {
                    if (!activ && coada.getDimensiune() == 0) break;
                }

                Tranzactie t = coada.extrage();
                System.out.println("[Processor] Factura #" + t.getId() + " - " + t.toString());
                totalProcesate++;
                Thread.sleep(80);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getTotalProcesate() { return totalProcesate; }
}