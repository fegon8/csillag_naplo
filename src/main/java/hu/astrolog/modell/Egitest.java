package hu.astrolog.modell;

import hu.astrolog.kivetel.ErvenytelenAdatException;

public abstract class Egitest {
    private final String nev;
    private final double magnitudo;

    public Egitest(String nev, double magnitudo) throws ErvenytelenAdatException {
        if (nev == null || nev.isBlank()) {
            throw new ErvenytelenAdatException("Az egitest neve nem lehet ures!");
        }
        if (magnitudo < -30.0 || magnitudo > 35.0) {
            throw new ErvenytelenAdatException("A magnitudo -30 es +35 kozott lehet!");
        }
        this.nev = nev.trim();
        this.magnitudo = magnitudo;
    }

    public abstract String getTipus();
    public abstract double alapPont();

    public String getNev() { return nev; }
    public double getMagnitudo() { return magnitudo; }

    @Override
    public String toString() {
        return String.format("[%s] %s (mag: %.1f)", getTipus(), nev, magnitudo);
    }
}