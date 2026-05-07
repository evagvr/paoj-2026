package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

public class TranzactieRecord {
    public int id;
    public double suma;
    public String data;
    public TipTranzactie tip;
    public int status;

    public static String statusToString(int status) {
        return switch (status) {
            case 1 -> "PROCESSED";
            case 2 -> "REJECTED";
            default -> "PENDING";
        };
    }

    public static int statusToByte(String statusStr) {
        return switch (statusStr) {
            case "PROCESSED" -> 1;
            case "REJECTED" -> 2;
            default -> 0;
        };
    }
}