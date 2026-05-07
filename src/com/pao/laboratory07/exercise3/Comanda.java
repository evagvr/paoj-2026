package com.pao.laboratory07.exercise3;

import com.pao.laboratory07.exercise1.OrderState;

public abstract sealed class Comanda permits ComandaStandard, ComandaRedusa, ComandaGratuita {
    protected String nume;
    protected String client;
    protected OrderState stare;
    public Comanda(String nume, String client){
        this.nume = nume;
        this.client = client;
        this.stare = OrderState.PLACED;
    }
    public String getClient(){
        return client;
    }
    public abstract double pretFinal();
    public abstract String descriere();
}
