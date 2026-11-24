package hu.astrolog.szolgaltatas;

import hu.astrolog.modell.Egitest;

public interface PontozasiStrategia {
    String getNev();
    double szamitPont(Egitest egitest, int bortle);
}