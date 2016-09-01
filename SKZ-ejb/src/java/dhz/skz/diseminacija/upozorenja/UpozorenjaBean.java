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
package dhz.skz.diseminacija.upozorenja;

import dhz.skz.aqdb.entity.Komponenta;
import dhz.skz.aqdb.entity.KomponentaUpozorenja;
import dhz.skz.aqdb.entity.PrimateljProgramKljuceviMap;
import dhz.skz.aqdb.entity.PrimateljiPodataka;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.diseminacija.DiseminatorPodataka;
import dhz.skz.diseminacija.EmailSessionBean;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author kraljevic
 */
@Stateless
@LocalBean
public class UpozorenjaBean implements DiseminatorPodataka {
    private static final Logger log = Logger.getLogger(UpozorenjaBean.class.getName());

    @EJB
    private EmailSessionBean esb;
    
    @Override
    public void salji(PrimateljiPodataka primatelj) {
//        for ( PrimateljProgramKljuceviMap ppkm : primatelj.getPrimateljProgramKljuceviMapCollection()){
//            ProgramMjerenja pm = ppkm.getProgramMjerenja();
//            Komponenta k = pm.getKomponentaId();
//            for ( KomponentaUpozorenja u : k.getUpozorenja()){
//                if (isPrekoracen(pm)) {
//                    saljiMail(primatelj, u);
//                }
//            }
//        }
    }

    @Override
    public void nadoknadi(PrimateljiPodataka primatelj, Collection<ProgramMjerenja> program, Date pocetak, Date kraj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    private void saljiMail(PrimateljiPodataka primatelj, KomponentaUpozorenja u) {
        String tekst = u.getPredlozakTeksta();
        String uri  = primatelj.getUrl();
        String naziv = u.getNaziv();
        
    }
}
