/*
 * Copyright (C) 2017 kraljevic
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
package dhz.skz.diseminacija.upozorenja.poruka;

import dhz.skz.aqdb.entity.Obavijesti;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.PrekoracenjaUpozorenjaResult;
import dhz.skz.diseminacija.upozorenja.slanje.VrstaUpozorenja;
import java.util.Collection;

/**
 *
 * @author kraljevic
 */
public class SlanjePoruka {

    public void salji(Obavijesti obavijest, PrekoracenjaUpozorenjaResult pur, Podatak pod, Collection<Podatak> podaci, VrstaUpozorenja vrsta) {
        String xml = parseObavijest(obavijest.getPredlozakTeksta());
        
        
        
        
    }

    private String parseObavijest(String predlozakTeksta) {
//        body = body.replaceAll("\\$\\{OPIS\\}", opis);
//        body = body.replaceAll("\\$\\{POSTAJA\\}", postaja);
//        body = body.replaceAll("\\$\\{KOMPONENTA\\}", komponenta);
//        body = body.replaceAll("\\$\\{MAKSIMALNA_VRIJEDNOST\\}", maksimalnaVrijednost.toString());
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void salji(EmailPoruka email){
        
    }
    
    private void salji(SmsPoruka sms) {
        
    }
}
