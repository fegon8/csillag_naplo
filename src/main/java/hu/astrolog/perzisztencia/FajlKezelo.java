package hu.astrolog.perzisztencia;

import hu.astrolog.kivetel.ErvenytelenAdatException;
import hu.astrolog.modell.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class FajlKezelo {
    private static final String FAJL = "naplo.csv";

    public void ment(List<EszlelesiAlkalom> alkalmak, Map<String, LocalDate> feloldottak) throws IOException {
        try (BufferedWriter iro = new BufferedWriter(new FileWriter(FAJL, StandardCharsets.UTF_8))) {
            for (Map.Entry<String, LocalDate> entry : feloldottak.entrySet()) {
                iro.write("UNLOCK;" + entry.getKey() + ";" + entry.getValue());
                iro.newLine();
            }
            for (EszlelesiAlkalom a : alkalmak) {
                iro.write(String.join(";", "SESSION", a.getDatum().toString(), a.getHelyszin(),
                        String.valueOf(a.getBortle()), a.getTavcso().getNev(), String.valueOf(a.getTavcso().getAtmeroMm())));
                iro.newLine();
                for (Egitest e : a.getEgitestek()) {
                    String extra = (e instanceof Bolygo b) ? String.valueOf(b.getFazisSzazalek()) : ((MelyegObjektum) e).getKategoria();
                    iro.write(String.join(";", "OBJ", e.getTipus(), e.getNev(), String.valueOf(e.getMagnitudo()), extra));
                    iro.newLine();
                }
            }
        }
    }

    public void betolt(List<EszlelesiAlkalom> alkalmak, Map<String, LocalDate> feloldottak)
            throws IOException, ErvenytelenAdatException {
        File f = new File(FAJL);
        if (!f.exists()) return;

        try (BufferedReader olvaso = new BufferedReader(new FileReader(f, StandardCharsets.UTF_8))) {
            String sor;
            EszlelesiAlkalom aktualis = null;
            while ((sor = olvaso.readLine()) != null) {
                String[] p = sor.split(";");
                if ("UNLOCK".equals(p[0])) {
                    feloldottak.put(p[1], LocalDate.parse(p[2]));
                } else if ("SESSION".equals(p[0])) {
                    aktualis = new EszlelesiAlkalom(LocalDate.parse(p[1]), p[2], Integer.parseInt(p[3]),
                            new Tavcso(p[4], Integer.parseInt(p[5])));
                    alkalmak.add(aktualis);
                } else if ("OBJ".equals(p[0]) && aktualis != null) {
                    Egitest e = "Bolygo".equals(p[1])
                            ? new Bolygo(p[2], Double.parseDouble(p[3]), Integer.parseInt(p[4]))
                            : new MelyegObjektum(p[2], Double.parseDouble(p[3]), p[4]);
                    aktualis.hozzaad(e);
                }
            }
        }
    }
}