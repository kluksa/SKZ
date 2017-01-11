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
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.util.OperStatus;
import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author kraljevic
 */
public class CsvExportSiroki implements PodatakEksport {
    
    private SortedMap<Date, Map<ProgramMjerenja,Podatak>> podaciT;
    private HashMap<ProgramMjerenja, Integer> programiMapa;
    
    private SortedMap<String, Integer> stupacMapa;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final DecimalFormat df2 = new DecimalFormat(",##");
    
    
    @Override
    public void write(Writer writer, Collection<Podatak> pod) throws IOException {
        
        
        CsvWriter csv = new CsvWriter(writer, ';');
        
        transformirajPodatke(pod);
        
        csv.write("vrijeme");
        for (ProgramMjerenja pm : programiMapa.keySet()){
            csv.write(program2text(pm) + ".vrijednost");
            csv.write(program2text(pm) + ".status");
        }
        csv.endRecord();

        for ( Date d : podaciT.keySet()){
            csv.writeRecord(getRecord(d, podaciT.get(d)));
        }
        csv.close();
    }

    
    private void transformirajPodatke(Collection<Podatak> podaci) {
        podaciT = new TreeMap<>();
        SortedSet<ProgramMjerenja> programi = new TreeSet<>((o1, o2) -> program2text(o1).compareTo(program2text(o2)));
        
        for (Podatak p : podaci) {
            if ( ! podaciT.containsKey(p.getVrijeme())) {
                podaciT.put(p.getVrijeme(), new HashMap<>());
            }
            podaciT.get(p.getVrijeme()).put(p.getProgramMjerenjaId(), p);
            programi.add(p.getProgramMjerenjaId());
        }
        programiMapa = new HashMap<>();
        int i = 0;
        for (ProgramMjerenja pm : programi) {
            programiMapa.put(pm, i++);
        }
        System.out.println("e");
    }
    
    private String program2text(ProgramMjerenja pm) {
        StringBuilder sb = new StringBuilder();
        sb.append(pm.getPostajaId().getNazivPostaje());
        sb.append(":");
        sb.append(pm.getKomponentaId().getFormula());
        sb.append(":");
        sb.append(pm.getUsporednoMjerenje());
        return sb.toString();
    } 
    
    private String[] getRecord(Date vrijeme, Map<ProgramMjerenja,Podatak> podaci) {
        String[] s = new String[2*programiMapa.keySet().size()+1];
        s[0] = sdf.format(vrijeme);
        for ( ProgramMjerenja pm : podaci.keySet()) {
            Integer get = programiMapa.get(pm);
            s[2*programiMapa.get(pm)+1] = df2.format(podaci.get(pm).getVrijednost());
            s[2*programiMapa.get(pm)+2] = OperStatus.isValid(podaci.get(pm)) ? "0" : "-1";
        }
        return s;
    }
}
