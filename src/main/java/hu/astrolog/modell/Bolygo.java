package hu.astrolog.modell;

import hu.astrolog.kivetel.ErvenytelenAdatException;

public class Bolygo extends Egitest {
    private final int fazisSzazalek;

    public Bolygo(String nev, double magnitudo, int fazisSzazalek) throws ErvenytelenAdatException {
        super(nev, magnitudo);
        if (fazisSzazalek < 0 || fazisSzazalek > 100) {
            throw new ErvenytelenAdatException("A fazis 0 es 100% kozott lehet!");
        }
        this.fazisSzazalek = fazisSzazalek;
    }

    public int getFazisSzazalek() { return fazisSzazalek; }

    @Override
    public String getTipus() { return "Bolygo"; }

    @Override
    public double alapPont() {
        return 10.0 + (100 - fazisSzazalek) * 0.1;
    }

    @Override
    public String toString() {
        return super.toString() + " - Fazis: " + fazisSzazalek + "%";
    }
}