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

import dhz.skz.aqdb.entity.Obavijesti;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.PrekoracenjaUpozorenjaResult;
import dhz.skz.aqdb.entity.PrimateljiPodataka;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.facades.ObavijestiFacade;
import dhz.skz.aqdb.facades.PodatakFacade;
import dhz.skz.aqdb.facades.PrekoracenjaUpozorenjaResultFacade;
import dhz.skz.diseminacija.DiseminatorPodataka;
import dhz.skz.diseminacija.upozorenja.slanje.SlanjeUpozorenja;
import dhz.skz.diseminacija.upozorenja.slanje.UpozorenjeSlanjeFactory;
import dhz.skz.diseminacija.upozorenja.slanje.VrstaUpozorenja;
import dhz.skz.util.OperStatus;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
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
    private PodatakFacade pf;

    @EJB
    private ObavijestiFacade obf;

    @EJB
    private PrekoracenjaUpozorenjaResultFacade prFac;

    @Override
    public void salji(PrimateljiPodataka primatelj) {
        OffsetDateTime odt_sada = OffsetDateTime.now();
                // OffsetDateTime.parse("2016-09-30T15:59:00+02:00");
        UpozorenjeSlanjeFactory suf = new UpozorenjeSlanjeFactory();
        
        for (Obavijesti o : obf.findAll(primatelj)) {
            Integer granicniBroj = o.getGranica().getDozvoljeniBrojPrekoracenja();
            
            for (PrekoracenjaUpozorenjaResult pur : prFac.findAll(o, odt_sada, granicniBroj)) {
                SlanjeUpozorenja up = suf.getSender(o);
                Collection<Podatak> podaci = pokupiPodatke(primatelj, odt_sada);
                if ( Objects.equals(pur.getBrojPojavljivanja(), granicniBroj)) {
                    up.saljiUpozorenje(o, pur.getProgramMjerenja(), pur.getMaksimalnaVrijednost(), podaci, VrstaUpozorenja.UPOZORENJE);
                } else {
                    Podatak pod = pf.getPodatakZaSat(pur.getProgramMjerenja(),odt_sada);
                    if ( pod != null && pod.getVrijednost() > o.getGranica().getVrijednost() && OperStatus.isValid(pod)){
                        up.saljiUpozorenje(o, pur.getProgramMjerenja(), pod.getVrijednost(), podaci, VrstaUpozorenja.OBAVIJEST);
                    }
                }
            }
        }
    }

    @Override
    public void nadoknadi(PrimateljiPodataka primatelj, Collection<ProgramMjerenja> program, Date pocetak, Date kraj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Collection<Podatak> pokupiPodatke(PrimateljiPodataka primatelj, OffsetDateTime sada) {
        OffsetDateTime pocetak = sada.minusHours(6);
        return pf.getVrijemePrimatelj(primatelj, pocetak, sada);
    }
}
