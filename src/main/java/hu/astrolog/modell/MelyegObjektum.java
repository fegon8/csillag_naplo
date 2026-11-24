package hu.astrolog.modell;

import hu.astrolog.kivetel.ErvenytelenAdatException;

public class MelyegObjektum extends Egitest {
    private final String kategoria;

    public MelyegObjektum(String nev, double magnitudo, String kategoria) throws ErvenytelenAdatException {
        super(nev, magnitudo);
        this.kategoria = (kategoria == null || kategoria.isBlank()) ? "Galaxis/Kod" : kategoria.trim();
    }

    public String getKategoria() { return kategoria; }

    @Override
    public String getTipus() { return "Melyeg"; }

    @Override
    public double alapPont() {
        return Math.max(5.0, getMagnitudo() * 2.0);
    }

    @Override
    public String toString() {
        return super.toString() + " - " + kategoria;
    }
}