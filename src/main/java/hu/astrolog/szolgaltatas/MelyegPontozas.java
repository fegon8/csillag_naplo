package hu.astrolog.szolgaltatas;

import hu.astrolog.modell.Egitest;

public class MelyegPontozas implements PontozasiStrategia {
    @Override
    public String getNev() { return "Sotet egbolt bonusz"; }

    @Override
    public double szamitPont(Egitest egitest, int bortle) {
        return Math.round((egitest.alapPont() + (10 - bortle) * 2.0) * 10.0) / 10.0;
    }
}
