package hu.astrolog.ui;

import hu.astrolog.kivetel.ErvenytelenAdatException;
import hu.astrolog.modell.*;
import hu.astrolog.szolgaltatas.*;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final NaploSzolgaltatas naplo = new NaploSzolgaltatas();

    public static void main(String[] args) {
        while (true) {
            try {
                if (!naplo.vanAktivAlkalom()) {
                    fomenu();
                } else {
                    sessionMenu();
                }
            } catch (ErvenytelenAdatException e) {
                System.out.println("HIBA: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Hibas bemenet! Probald ujra.");
            }
        }
    }

    private static void fomenu() throws Exception {
        System.out.println("\n=== ASTROLOG FOMENU ===");
        System.out.println("Default tavcso: " + naplo.getDefaultTavcso() + " | Strategia: " + naplo.getStrategia().getNev());
        System.out.println("1. Uj eszlelesi session inditasa");
        System.out.println("2. Korabbi sessionok megtekintese");
        System.out.println("3. Feloldott (unlockolt) egitestek");
        System.out.println("4. Eszlelesek rangsorolasa / Strategia valtas");
        System.out.println("5. Default tavcso modositasa");
        System.out.println("0. Kilepes");
        System.out.print("Valasztas: ");

        switch (sc.nextLine().trim()) {
            case "1" -> {
                System.out.print("Helyszin: ");
                String hely = sc.nextLine();
                System.out.print("Bortle-skala (1-9): ");
                int bortle = Integer.parseInt(sc.nextLine());
                naplo.inditAlkalom(LocalDate.now(), hely, bortle, naplo.getDefaultTavcso());
                System.out.println("Session elindult!");
            }
            case "2" -> {
                for (EszlelesiAlkalom a : naplo.getAlkalmak()) {
                    System.out.println("\n" + a);
                    for (Egitest e : a.getEgitestek()) System.out.println("  - " + e);
                }
            }
            case "3" -> {
                System.out.println("\n--- Feloldott egitestek (legeloszor latva) ---");
                naplo.getFeloldottEgitestek().forEach((nev, datum) -> System.out.println(nev + " -> " + datum));
            }
            case "4" -> {
                naplo.setStrategia(naplo.getStrategia() instanceof AlapPontozas ? new MelyegPontozas() : new AlapPontozas());
                System.out.println("Uj strategia: " + naplo.getStrategia().getNev());
                naplo.rangsoroltEszlelesek().forEach(System.out::println);
            }
            case "5" -> {
                System.out.print("Uj tavcso neve: ");
                String nev = sc.nextLine();
                System.out.print("Atmero (mm): ");
                int mm = Integer.parseInt(sc.nextLine());
                naplo.setDefaultTavcso(new Tavcso(nev, mm));
            }
            case "0" -> System.exit(0);
        }
    }

    private static void sessionMenu() throws Exception {
        EszlelesiAlkalom aktiv = naplo.getAktivAlkalom();
        System.out.println("\n=== AKTIV SESSION (" + aktiv.getHelyszin() + " | Eddig: " + aktiv.getEgitestek().size() + " db) ===");
        System.out.println("1. Bolygo rogzitese");
        System.out.println("2. Melyeg-objektum rogzitese");
        System.out.println("3. Session lezarasa es mentese");
        System.out.print("Valasztas: ");

        switch (sc.nextLine().trim()) {
            case "1" -> {
                System.out.print("Bolygo neve (pl. Jupiter): ");
                String nev = sc.nextLine();
                System.out.print("Magnitudo: ");
                double mag = Double.parseDouble(sc.nextLine());
                System.out.print("Fazis (0-100%): ");
                int fazis = Integer.parseInt(sc.nextLine());
                boolean uj = naplo.rogzitEgitest(new Bolygo(nev, mag, fazis));
                if (uj) System.out.println("UJ EGITEST FELOLDVA: " + nev.toUpperCase() + "!");
            }
            case "2" -> {
                System.out.print("Objektum neve (pl. M31): ");
                String nev = sc.nextLine();
                System.out.print("Magnitudo: ");
                double mag = Double.parseDouble(sc.nextLine());
                System.out.print("Kategoria (pl. Galaxis, Kod): ");
                String kat = sc.nextLine();
                boolean uj = naplo.rogzitEgitest(new MelyegObjektum(nev, mag, kat));
                if (uj) System.out.println("UJ EGITEST FELOLDVA: " + nev.toUpperCase() + "!");
            }
            case "3" -> {
                naplo.lezarEsMent();
                System.out.println("Session elmentve!");
            }
        }
    }
}