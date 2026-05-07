package com.pao.laboratory09.exercise3;

import com.pao.laboratory09.exercise1.Tranzactie;
import java.util.LinkedList;
import java.util.Queue;

public class CoadaTranzactii {
    private final Queue<Tranzactie> banda = new LinkedList<>();
    private final int capacitateMax = 5;

    public synchronized void adauga(Tranzactie t, int atmId) throws InterruptedException {
        while (banda.size() == capacitateMax) {
            System.out.println("[ATM-" + atmId + "] astept loc...");
            wait();
        }
        banda.add(t);
        notifyAll();
    }

    public synchronized Tranzactie extrage() throws InterruptedException {
        while (banda.isEmpty()) {
            wait();
        }
        Tranzactie t = banda.poll();
        notifyAll();
        return t;
    }

    public synchronized int getDimensiune() {
        return banda.size();
    }
}