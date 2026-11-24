package hu.astrolog.modell;

import hu.astrolog.kivetel.ErvenytelenAdatException;

public class Tavcso {
    private final String nev;
    private final int atmeroMm;

    public Tavcso(String nev, int atmeroMm) throws ErvenytelenAdatException {
        if (nev == null || nev.isBlank() || atmeroMm <= 0) {
            throw new ErvenytelenAdatException("Ervenytelen tavcso adatok!");
        }
        this.nev = nev.trim();
        this.atmeroMm = atmeroMm;
    }

    public String getNev() { return nev; }
    public int getAtmeroMm() { return atmeroMm; }

    @Override
    public String toString() {
        return nev + " (" + atmeroMm + " mm)";
    }
}