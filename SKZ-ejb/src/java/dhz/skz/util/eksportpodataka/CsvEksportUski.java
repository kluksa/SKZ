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
import dhz.skz.util.OperStatus;
import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author kraljevic
 */
public class CsvEksportUski implements PodatakEksport {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddTHH:mm");
    private static final DecimalFormat df2 = new DecimalFormat(",##");

    @Override
    public void write(Writer writer, Collection<Podatak> pod) throws IOException {
        
        CsvWriter csv = new CsvWriter(writer, ';');
        ArrayList<Podatak> podaci = new ArrayList<>(pod);

        Collections.sort(podaci, (Podatak c1, Podatak c2) -> {
            if (c2.getVrijeme().getTime() == c1.getVrijeme().getTime()) {
                return 0;
            }
            return c2.getVrijeme().getTime() < c1.getVrijeme().getTime() ? -1 : 1;
        });
        
        csv.writeRecord(new String[]{"vrijeme", "postaja", "komponenta", "vrijednost", "status"});
        for (Podatak p : podaci) {
            csv.writeRecord(new String[]{sdf.format(p.getVrijeme()),
                p.getProgramMjerenjaId().getPostajaId().getNazivPostaje(),
                p.getProgramMjerenjaId().getKomponentaId().getFormula(),
                df2.format(p.getVrijednost()),
                OperStatus.isValid(p) ? "0" : "-1"
            });
        }
        csv.close();
    }
}
