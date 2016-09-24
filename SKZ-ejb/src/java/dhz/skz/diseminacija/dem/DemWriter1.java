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
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.citaci.SatniIterator;
import dhz.skz.config.Config;
import dhz.skz.util.OperStatus;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.inject.Inject;

/**
 *
 * @author kraljevic
 */
public class DemWriter1 implements PodatakWriter {
    private static final Logger log = Logger.getLogger(DemWriter1.class.getName());

    private final PrintWriter pw;
    private final SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd HH:mm");
    private final TimeZone tzone;
    private Date zadnjeVrijeme;

    public DemWriter1(OutputStream os) {
        pw = new PrintWriter(os);
        tzone = LokalnaZona.getZone();
    }

    public DemWriter1(Writer wr) {
        pw = new PrintWriter(wr);
        tzone = LokalnaZona.getZone();
    }
    

    public void print(Komponenta k) {
        if ( k == null ) throw new IllegalArgumentException("k == null");
        pw.printf("COMPONENT %s, %s\n", k.getNazivEng(), "hour");

    }

    public void printPostajaPodaci(Postaja postaja, Collection<Podatak> podaci) {
        if ( postaja == null ) throw new IllegalArgumentException("postaja == null");
        if ( podaci == null ) throw new IllegalArgumentException("podaci == null");
        
        SortedMap<Date, Podatak> podMap = transformirajPodatke(podaci);
        if ( podMap.keySet().isEmpty()) return;
        
        sdf.setTimeZone(tzone);

        
        pw.printf("STATION %s\n", postaja.getOznakaPostaje());
        log.info(postaja.getNazivPostaje());

        SatniIterator sat = new SatniIterator(podMap.firstKey(), podMap.lastKey(), tzone);
        
        
        while (sat.next()) {
            Integer status;
            Date vr = sat.getVrijeme();
            Double vrijednost;
            if (podMap.containsKey(vr)) {
                Podatak pod = podMap.get(vr);
                if (OperStatus.isValid(pod)) {
                    status = -1;
                    vrijednost = pod.getVrijednost();
                } else {
                    status = 0;
                    vrijednost = -999.;
                }
            } else {
                status = 0;
                vrijednost = -999.;
            }
            pw.printf("%s, %8.1f, %d\n", sdf.format(vr), vrijednost, status);
            zadnjeVrijeme = vr;
        }
    }

    private SortedMap<Date, Podatak> transformirajPodatke(Collection<Podatak> podaci) {
        if ( podaci == null) throw new IllegalArgumentException("podaci == null");
        
        SortedMap<Date, Podatak> mapa = new TreeMap<>();
        podaci.stream().forEach((p) -> {
            mapa.put(p.getVrijeme(), p);
        });
        return mapa;
    }

    public Date getZadnjeVrijeme() {
        return zadnjeVrijeme;
    }

    @Override
    public void write(Collection<Podatak> podaci) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
