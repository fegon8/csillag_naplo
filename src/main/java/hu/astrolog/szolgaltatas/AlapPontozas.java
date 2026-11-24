package hu.astrolog.szolgaltatas;

import hu.astrolog.modell.Egitest;

public class AlapPontozas implements PontozasiStrategia {
    @Override
    public String getNev() { return "Alap pontozas"; }

    @Override
    public double szamitPont(Egitest egitest, int bortle) {
        return Math.round(egitest.alapPont() * 10.0) / 10.0;
    }
}