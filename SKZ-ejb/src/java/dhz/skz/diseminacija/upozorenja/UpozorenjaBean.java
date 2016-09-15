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
import dhz.skz.aqdb.entity.PrimateljProgramKljuceviMap;
import dhz.skz.aqdb.entity.PrimateljiPodataka;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.facades.ObavijestiFacade;
import dhz.skz.aqdb.facades.PodatakFacade;
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
    
    @Inject
    @Config
    private TimeZone tzone;

    private Date sada;
    
    @Override
    public void salji(PrimateljiPodataka primatelj) {
        sada = getPrvi(0).getTime();
        UpozorenjeSlanjeFactory suf = new UpozorenjeSlanjeFactory();
        
        for (PrimateljProgramKljuceviMap ppkm : primatelj.getPrimateljProgramKljuceviMapCollection()) {
            ProgramMjerenja pm = ppkm.getProgramMjerenja();
            Komponenta k = pm.getKomponentaId();
            for (Obavijesti o : obf.findAll(k, primatelj)){
                if (isPrekoracen(pm, o.getGranica())) {
                    SlanjeUpozorenja up = suf.getSender(o);
                    Collection<Podatak> podaci = pokupiPodatke(primatelj);
                    up.saljiUpozorenje(o, pm, podaci);
                }
            }
        }
    }

    @Override
    public void nadoknadi(PrimateljiPodataka primatelj, Collection<ProgramMjerenja> program, Date pocetak, Date kraj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Calendar getPrvi(int broj_prethodnih_sati) {
        Calendar trenutni_termin = new GregorianCalendar(tzone);
        trenutni_termin.setTime(new Date());
        trenutni_termin.set(Calendar.MINUTE, 0);
        trenutni_termin.set(Calendar.SECOND, 0);
        trenutni_termin.set(Calendar.MILLISECOND, 0);
        trenutni_termin.add(Calendar.HOUR, -broj_prethodnih_sati);
        return trenutni_termin;
    }

    private boolean isPrekoracen(ProgramMjerenja pm, Granice o) {
        Calendar pocetak = getPrvi(o.getDozvoljeniBrojPrekoracenja());
        List<Podatak> pod = pf.getPodatak(pm, pocetak.getTime(), sada, 0, true, true);
        boolean test = false;
        if (pod.size() > o.getDozvoljeniBrojPrekoracenja()) {
            for (int i = 0; i <= o.getDozvoljeniBrojPrekoracenja(); i++) {
                Podatak podatak = pod.get(i);
                if (OperStatus.isValid(podatak) && podatak.getVrijednost() > o.getVrijednost()) {
                    test = true;
                } else {
                    test = false;
                    break;
                }
            }
        }
        return test;
    }

    private Collection<Podatak> pokupiPodatke(PrimateljiPodataka primatelj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
