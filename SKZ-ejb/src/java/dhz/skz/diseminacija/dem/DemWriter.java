/*
 * Copyright (C) 2016 kraljevic
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dhz.skz.diseminacija.dem;

import dhz.skz.aqdb.entity.Komponenta;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.citaci.SatniIterator;
import dhz.skz.util.OperStatus;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 *
 * @author kraljevic
 */
public class DemWriter implements PodatakWriter, Closeable, Flushable {

    private static final Logger log = Logger.getLogger(DemWriter.class.getName());

    private final PrintWriter pw;
    private final SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd HH:mm");
    private final TimeZone tzone;
    private Date zadnjeVrijeme;
    private final Komponenta komponenta;
    private boolean prviPoziv = true;
    private final Map<ProgramMjerenja, Date> zadnjiZapis = new HashMap<>();

    public DemWriter(Writer wr, Komponenta k) {
        if (k == null) {
            throw new IllegalArgumentException("k == null");
        }
        pw = new PrintWriter(wr);
        tzone = LokalnaZona.getZone();
        sdf.setTimeZone(tzone);

        komponenta = k;
    }

    public void printPostajaPodaci(ProgramMjerenja program, Collection<Podatak> podaci) throws PodatakWriterException {
        if (program == null) {
            throw new IllegalArgumentException("postaja == null");
        }
        if (!program.getKomponentaId().equals(komponenta)) {
            throw new IllegalArgumentException("ProgramMjerenja.komponentaId != komponenta");
        }
        if (podaci == null) {
            throw new IllegalArgumentException("podaci == null");
        }
        if (prviPoziv) {
            pw.printf("COMPONENT %s, %s\n", komponenta.getNazivEng(), "hour");
        }
        prviPoziv = false;
        SortedMap<Date, Podatak> podMap = transformirajPodatke(podaci).get(program);
        if (podMap.keySet().isEmpty()) {
            return;
        }

        pw.printf("STATION %s\n", program.getPostajaId().getOznakaPostaje());
        log.info(program.getPostajaId().getNazivPostaje());

        SatniIterator sat = new SatniIterator(podMap.firstKey(), podMap.lastKey(), tzone);

        while (sat.next()) {
            Date vr = sat.getVrijeme();
            if (podMap.containsKey(vr)) {
                Podatak pod = podMap.get(vr);
                if (!program.equals(pod.getProgramMjerenjaId())) {
                    throw new PodatakWriterException("Program: " + program.getId() + " != " + pod.getProgramMjerenjaId().getId());
                }
                if (OperStatus.isValid(pod)) {
                    printLinija(vr, pod.getVrijednost(), -1);
                } else {
                    printLinija(vr, -999., 0);
                }
                zadnjiZapis.put(program, vr);
            } else {
                printLinija(vr, -999., 0);
            }
        }
    }

    private void printLinija(Date vrijeme, Double vrijednost, Integer status) {
        pw.printf("%s, %8.1f, %d\n", sdf.format(vrijeme), vrijednost, status);
    }

    private Map<ProgramMjerenja, SortedMap<Date, Podatak>> transformirajPodatke(Collection<Podatak> podaci) {
        Map<ProgramMjerenja, SortedMap<Date, Podatak>> mapa2 = new HashMap<>();
        podaci.stream().forEach((p) -> {
            if ( ! mapa2.containsKey(p.getProgramMjerenjaId())){
                mapa2.put(p.getProgramMjerenjaId(), new TreeMap<>());
            }
            SortedMap<Date, Podatak> mapa = mapa2.get(p.getProgramMjerenjaId());
            mapa.put(p.getVrijeme(), p);
        });
        return mapa2;
    }

    public Map<ProgramMjerenja,Date> getZadnjiZapis() {
        return zadnjiZapis;
    }

    @Override
    public void write(Collection<Podatak> podaci) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void flush() throws IOException {
        pw.flush();
    }

    @Override
    public void close() throws IOException {
        pw.close();
        zadnjiZapis.clear();
    }
}
