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
package dhz.skz.util.eksportpodataka;

import com.csvreader.CsvWriter;
import dhz.skz.aqdb.entity.Komponenta;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.util.OperStatus;
import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author kraljevic
 */
public class CsvExportSiroki implements PodatakEksport {
    
    private SortedMap<Date, SortedMap<Postaja, SortedMap<Komponenta,Podatak>>> podaciT;
    
    private SortedMap<String, Integer> stupacMapa;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddTHH:mm");
    private static final DecimalFormat df2 = new DecimalFormat(",##");
    
    
    @Override
    public void write(Writer writer, Collection<Podatak> pod) throws IOException {
        
        
        CsvWriter csv = new CsvWriter(writer, ';');
        
        podaciT = transformirajPodatke(pod);
        
        csv.write("vrijeme");
        csv.write("postaja");
        for (String k : stupacMapa.keySet()){
            csv.write(k + ".vrijednost");
            csv.write(k + ".status");
        }
        csv.endRecord();

        for ( Date d : podaciT.keySet()){
            csv.write(sdf.format(d));
            SortedMap<Postaja, SortedMap<Komponenta, Podatak>> get = podaciT.get(d);
            for ( Postaja po : get.keySet()){
                SortedMap<Komponenta, Podatak> get1 = get.get(po);
                csv.write(po.getNazivPostaje());
                for ( Komponenta k : get1.keySet()){
                    Podatak p = get1.get(k);
                    csv.write(df2.format(p.getVrijednost()));
                    csv.write(OperStatus.isValid(p) ? "0" : "-1");
                }
                csv.endRecord();
            }
        }
        csv.close();
    }

    
    private void transformacija(Collection<Podatak> pod) {
        for ( Podatak p : pod) {
            Date vrijeme  = p.getVrijeme();
            
            if ( ! podaciT.containsKey(vrijeme)) {
                podaciT.put(vrijeme, new TreeMap<>());
            }
            SortedMap<Postaja, SortedMap<Komponenta,Podatak>> postajaMap = podaciT.get(vrijeme);
            Postaja postaja = p.getProgramMjerenjaId().getPostajaId();
            if ( ! postajaMap.containsKey(postaja)) {
                postajaMap.put(postaja, new TreeMap<>());
            }
            SortedMap<Komponenta, Podatak> komponentaMap = postajaMap.get(postaja);
            
            
        }
    }
    private NavigableMap<Date, SortedMap<Postaja, SortedMap<Komponenta, Podatak>>> transformirajPodatke(Collection<Podatak> pod) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
