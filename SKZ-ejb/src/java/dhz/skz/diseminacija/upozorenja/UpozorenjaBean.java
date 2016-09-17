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

import dhz.skz.aqdb.entity.Granice;
import dhz.skz.aqdb.entity.Komponenta;
import dhz.skz.aqdb.entity.Obavijesti;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.PrekoracenjaUpozorenjaResult;
import dhz.skz.aqdb.entity.PrimateljProgramKljuceviMap;
import dhz.skz.aqdb.entity.PrimateljiPodataka;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.facades.GraniceFacade;
import dhz.skz.aqdb.facades.ObavijestiFacade;
import dhz.skz.aqdb.facades.PodatakFacade;
import dhz.skz.aqdb.facades.PrekoracenjaUpozorenjaResultFacade;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacade;
import dhz.skz.config.Config;
import dhz.skz.diseminacija.DiseminatorPodataka;
import dhz.skz.diseminacija.upozorenja.slanje.MailUpozorenje;
import dhz.skz.diseminacija.upozorenja.slanje.SlanjeUpozorenja;
import dhz.skz.diseminacija.upozorenja.slanje.UpozorenjeSlanjeFactory;
import dhz.skz.util.OperStatus;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.inject.Inject;

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
    private GraniceFacade graniceFac;

    @EJB
    private PrekoracenjaUpozorenjaResultFacade prFac;

    @EJB
    private ProgramMjerenjaFacade pmfac;
    @Inject
    @Config
    private TimeZone tzone;

    private Date sada;

    @Override
    public void salji(PrimateljiPodataka primatelj) {
        sada = new Date();
        UpozorenjeSlanjeFactory suf = new UpozorenjeSlanjeFactory();
        for (Obavijesti o : obf.findAll(primatelj)) {
            for (PrekoracenjaUpozorenjaResult pur : prFac.findAll(o, sada)) {
                ProgramMjerenja pm = pmfac.find(pur.getProgramMjerenjaId());
                SlanjeUpozorenja up = suf.getSender(o);
                Collection<Podatak> podaci = pokupiPodatke(primatelj);
                up.saljiUpozorenje(o, pm, podaci);
            }
        }
    }

    @Override
    public void nadoknadi(PrimateljiPodataka primatelj, Collection<ProgramMjerenja> program, Date pocetak, Date kraj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Collection<Podatak> pokupiPodatke(PrimateljiPodataka primatelj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}