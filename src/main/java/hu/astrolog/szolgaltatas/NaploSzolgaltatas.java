package hu.astrolog.szolgaltatas;

import hu.astrolog.kivetel.ErvenytelenAdatException;
import hu.astrolog.modell.*;
import hu.astrolog.perzisztencia.FajlKezelo;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class NaploSzolgaltatas {
    private final List<EszlelesiAlkalom> alkalmak = new ArrayList<>();
    private final Map<String, LocalDate> feloldottEgitestek = new LinkedHashMap<>();
    private final FajlKezelo fajlKezelo = new FajlKezelo();

    private EszlelesiAlkalom aktivAlkalom;
    private Tavcso defaultTavcso;
    private PontozasiStrategia strategia = new AlapPontozas();

    public NaploSzolgaltatas() {
        try {
            this.defaultTavcso = new Tavcso("SkyWatcher 150/750", 150);
            fajlKezelo.betolt(alkalmak, feloldottEgitestek);
        } catch (Exception e) {
            System.out.println("Info: Uj naplo inditva.");
        }
    }

    public Tavcso getDefaultTavcso() { return defaultTavcso; }
    public void setDefaultTavcso(Tavcso t) { this.defaultTavcso = t; }
    public boolean vanAktivAlkalom() { return aktivAlkalom != null; }
    public EszlelesiAlkalom getAktivAlkalom() { return aktivAlkalom; }
    public PontozasiStrategia getStrategia() { return strategia; }
    public void setStrategia(PontozasiStrategia s) { this.strategia = s; }
    public List<EszlelesiAlkalom> getAlkalmak() { return alkalmak; }
    public Map<String, LocalDate> getFeloldottEgitestek() { return feloldottEgitestek; }

    public void inditAlkalom(LocalDate datum, String helyszin, int bortle, Tavcso tavcso) throws ErvenytelenAdatException {
        if (aktivAlkalom != null) throw new ErvenytelenAdatException("Mar fut egy session!");
        aktivAlkalom = new EszlelesiAlkalom(datum, helyszin, bortle, tavcso);
    }

    // Visszaadja, hogy UJ feloldas-e (eloszor latta-e valaha)
    public boolean rogzitEgitest(Egitest egitest) throws ErvenytelenAdatException {
        if (aktivAlkalom == null) throw new ErvenytelenAdatException("Nincs aktiv session!");
        aktivAlkalom.hozzaad(egitest);

        String kulcs = egitest.getNev().toUpperCase();
        if (!feloldottEgitestek.containsKey(kulcs)) {
            feloldottEgitestek.put(kulcs, aktivAlkalom.getDatum());
            return true;
        }
        return false;
    }

    public void lezarEsMent() throws ErvenytelenAdatException, IOException {
        if (aktivAlkalom == null) throw new ErvenytelenAdatException("Nincs aktiv session!");
        alkalmak.add(aktivAlkalom);
        aktivAlkalom = null;
        fajlKezelo.ment(alkalmak, feloldottEgitestek);
    }

    public List<String> rangsoroltEszlelesek() {
        List<String> sorok = new ArrayList<>();
        record Tetel(double pont, String szoveg) {}
        List<Tetel> tetelek = new ArrayList<>();

        for (EszlelesiAlkalom a : alkalmak) {
            for (Egitest e : a.getEgitestek()) {
                double p = strategia.szamitPont(e, a.getBortle());
                tetelek.add(new Tetel(p, String.format("[%.1f pont] %s (%s) - %s", p, e.getNev(), a.getDatum(), e)));
            }
        }
        tetelek.sort((a, b) -> Double.compare(b.pont(), a.pont()));
        for (Tetel t : tetelek) sorok.add(t.szoveg());
        return sorok;
    }
}