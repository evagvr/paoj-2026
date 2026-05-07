package com.pao.laboratory09.exercise3;

public class Main {
    public static void main(String[] args) throws InterruptedException{
        CoadaTranzactii bandaPartajata = new CoadaTranzactii();

        ATMThread atm1 = new ATMThread(1, bandaPartajata);
        ATMThread atm2 = new ATMThread(2, bandaPartajata);
        ATMThread atm3 = new ATMThread(3, bandaPartajata);

        ProcessorThread processorLogic = new ProcessorThread(bandaPartajata);
        Thread processorThread = new Thread(processorLogic);

        processorThread.start();
        atm1.start();
        atm2.start();
        atm3.start();

        atm1.join();
        atm2.join();
        atm3.join();

        processorLogic.activ = false;
        synchronized (bandaPartajata) {
            bandaPartajata.notifyAll();
        }

        processorThread.join();

        System.out.println("--------------------------------------------------");
        System.out.println("Toate tranzactiile procesate. Total: " + processorLogic.getTotalProcesate());
    }
}
