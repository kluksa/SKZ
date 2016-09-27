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
    private final SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd HH:mm");

    private final PrintWriter writer;
    
    private final TimeZone tzone;
    private final Komponenta komponenta;
    private final Map<ProgramMjerenja, Date> zadnjiZapis = new HashMap<>();

    public DemWriter(Writer wr, Komponenta k) {
        if (k == null) {
            throw new IllegalArgumentException("k == null");
        }
        writer = new PrintWriter(wr);
        tzone = LokalnaZona.getZone();
        sdf.setTimeZone(tzone);

        komponenta = k;
    }

    private Map<ProgramMjerenja, SortedMap<Date, Podatak>> transformirajPodatke(Collection<Podatak> podaci) throws PodatakWriterException {
        Map<ProgramMjerenja, SortedMap<Date, Podatak>> mapa2 = new HashMap<>();
        for (Podatak p : podaci) {
            if (!p.getProgramMjerenjaId().getKomponentaId().equals(komponenta)) {
                throw new PodatakWriterException();
            }
            if (!mapa2.containsKey(p.getProgramMjerenjaId())) {
                mapa2.put(p.getProgramMjerenjaId(), new TreeMap<>());
            }
            SortedMap<Date, Podatak> mapa = mapa2.get(p.getProgramMjerenjaId());
            mapa.put(p.getVrijeme(), p);
        }
        return mapa2;
    }

    public Map<ProgramMjerenja, Date> getZadnjiZapis() {
        return zadnjiZapis;
    }

    @Override
    public void write(Collection<Podatak> podaci) throws PodatakWriterException {
        if (podaci == null || podaci.isEmpty()) {
            return;
        }
        Map<ProgramMjerenja, SortedMap<Date, Podatak>> podatakMasterMap = transformirajPodatke(podaci);
        if (podatakMasterMap.keySet().isEmpty()) {
            return;
        }
        writer.printf("COMPONENT %s, %s\n", komponenta.getNazivEng(), "hour");
        for (ProgramMjerenja program : podatakMasterMap.keySet()) {
            SortedMap<Date, Podatak> podMap = podatakMasterMap.get(program);
            if (!podMap.isEmpty()) {
                writeStation(program, podMap);
            }
        }

    }
    
    private void writeStation(ProgramMjerenja program, SortedMap<Date, Podatak> podMap) {

        writer.printf("STATION %s\n", program.getPostajaId().getOznakaPostaje());
        SatniIterator sat = new SatniIterator(podMap.firstKey(), podMap.lastKey(), tzone);
        while (sat.next()) {
            Date vr = sat.getVrijeme();
            if (podMap.containsKey(vr)) {
                Podatak pod = podMap.get(vr);
                if (OperStatus.isValid(pod)) {
                    writeLinija(vr, pod.getVrijednost(), -1);
                } else {
                    writeLinija(vr, -999., 0);
                }
                zadnjiZapis.put(program, vr);
            } else {
                writeLinija(vr, -999., 0);
            }
        }
    }

    private void writeLinija(Date vrijeme, Double vrijednost, Integer status) {
        writer.printf("%s, %8.1f, %d\n", sdf.format(vrijeme), vrijednost, status);
    }

    @Override
    public void flush() throws IOException {
        writer.flush();
    }

    @Override
    public void close() throws IOException {
        writer.close();
        zadnjiZapis.clear();
    }


}
