package hu.astrolog.modell;

import hu.astrolog.kivetel.ErvenytelenAdatException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EszlelesiAlkalom {
    private final LocalDate datum;
    private final String helyszin;
    private final int bortle;
    private final Tavcso tavcso;
    private final List<Egitest> egitestek = new ArrayList<>();

    public EszlelesiAlkalom(LocalDate datum, String helyszin, int bortle, Tavcso tavcso) throws ErvenytelenAdatException {
        if (helyszin == null || helyszin.isBlank()) {
            throw new ErvenytelenAdatException("A helyszin nem lehet ures!");
        }
        if (bortle < 1 || bortle > 9) {
            throw new ErvenytelenAdatException("A Bortle-skala 1 es 9 kozotti szam lehet!");
        }
        this.datum = datum;
        this.helyszin = helyszin.trim();
        this.bortle = bortle;
        this.tavcso = tavcso;
    }

    public void hozzaad(Egitest e) { egitestek.add(e); }
    public LocalDate getDatum() { return datum; }
    public String getHelyszin() { return helyszin; }
    public int getBortle() { return bortle; }
    public Tavcso getTavcso() { return tavcso; }
    public List<Egitest> getEgitestek() { return Collections.unmodifiableList(egitestek); }

    @Override
    public String toString() {
        return String.format("%s | %s (Bortle: %d) | Tavcso: %s | %d db objektum",
                datum, helyszin, bortle, tavcso, egitestek.size());
    }
}