package com.pao.laboratory06.exercise3;

public class Inginer extends Angajat implements PlataOnline, Comparable<Inginer>{
    private double sold;
    public Inginer(String nume, String prenume, String telefon, double salariu, double sold){
        super(nume, prenume, telefon, salariu);
        this.sold = sold;
    }
    @Override
    public void autentificare(String user, String parola){
        if (user == null || user.isBlank() || parola == null || parola.isBlank()) {
            throw new IllegalArgumentException("User sau parola nu pot fi null sau goale.");
        }
        System.out.println("Inginerul " + getNume() + " s-a autentificat cu succes.");
    }
    @Override
    public double consultareSold(){
        return sold;
    }
    @Override
    public boolean efectuarePlata(double suma){
        if (suma <= 0) {
            throw new IllegalArgumentException("suma de plata trebuie sa fie pozitiva.");
        }
        if (sold >= suma) {
            sold -= suma;
            System.out.println("Plata reusita, noul sold: " + sold);
            return true;
        }
        System.out.println("Fonduri insuficiente.");
        return false;
    }
    @Override
    public int compareTo(Inginer other){
        return this.getNume().compareTo(other.getNume());
    }
    @Override
    public String toString() {
        return String.format("Inginer: %s %s | Salariu: %.2f", getNume(), getPrenume(), getSalariu());
    }
}
