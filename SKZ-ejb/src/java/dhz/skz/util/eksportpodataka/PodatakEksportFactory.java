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

import dhz.skz.util.eksportpodataka.exceptions.InvalidPodatakExportException;
import dhz.skz.aqdb.entity.PrimateljiPodataka;

/**
 *
 * @author kraljevic
 */
public class PodatakEksportFactory {
    public PodatakEksport getPodatakEksport(PrimateljiPodataka pp) throws InvalidPodatakExportException {
        switch (pp.getXsd()) {
            case "CsvSiroki":
                return new CsvExportSiroki();
            case "CsvUski":
                return new CsvExportSiroki();
            case "CsvSirokiKomponenta":
                return new CsvExportSiroki();
            case "CsvSirokiPostaja":
                return new CsvExportSiroki();
            default:
                throw new InvalidPodatakExportException();
        }
        
    }
}
