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
package dhz.skz.validatori;

import dhz.skz.aqdb.entity.PodatakSirovi;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author kraljevic
 */
public class NulValidator implements Validator{

    @Override
    public void validiraj(PodatakSirovi ps) {
    }

    @Override
    public void setPodaciUmjeravanja(Double a, Double b, Double ldl, Double opseg) {
    }

    @Override
    public void setTemperatura(Double temperatura) {
    }

    @Override
    public Collection<String> opisStatusa(String statusStr) {
        List<String> l = new ArrayList<String>();
        l.add("Nema opisa");
        return l;
    }

    @Override
    public int provjeraStatusa(String statusStr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
