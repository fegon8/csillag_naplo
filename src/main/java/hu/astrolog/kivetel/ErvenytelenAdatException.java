package hu.astrolog.kivetel;

public class ErvenytelenAdatException extends Exception {
    public ErvenytelenAdatException(String uzenet) {
        super(uzenet);
    }
}