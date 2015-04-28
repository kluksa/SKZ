/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.aqdb;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ZeroSpan;
import dhz.skz.aqdb.facades.PodatakFacade;
import dhz.skz.aqdb.facades.PodatakSiroviFacadeLocal;
import dhz.skz.citaci.CitacIzvora;
import dhz.skz.citaci.SatniIterator;
import dhz.skz.config.Config;
import dhz.skz.validatori.Validator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.NavigableMap;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author kraljevic
 */
@Stateless
@LocalBean
public class AqdbCitacBean implements CitacIzvora {

    private static final Logger log = Logger.getLogger(AqdbCitacBean.class.getName());
    protected static final short MIN_OBUHVAT = 75;

    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;
    @EJB
    private PodatakSiroviFacadeLocal podatakSiroviFacade;
    @EJB
    private PodatakFacade podatakFacade;
    @Inject @Config private TimeZone tzone;

    private NavigableMap<Date, Validator> validatori;
    private List<ZeroSpan> zslista = new ArrayList<>();

    @Override
    public void napraviSatne(IzvorPodataka izvor) {
        log.log(Level.INFO, "POCETAK CITANJA");
        em.refresh(izvor);
        for (ProgramMjerenja program : izvor.getProgramMjerenjaCollection()) {
            Date zadnjiSatni = podatakFacade.getZadnjiPodatak(program);
            Date zadnjiSirovi = podatakSiroviFacade.getZadnjiPodatak(program);
            SatniIterator sat = new SatniIterator(zadnjiSatni, zadnjiSirovi, tzone);
            while (sat.next()) {
            }
        }
        log.log(Level.INFO, "KRAJ CITANJA");
    }

    private void obradiSat(ProgramMjerenja program, Collection<PodatakSirovi> pod, Calendar cal) {
        if (pod == null || pod.isEmpty()) {
            return;
        }

        Validator v = validatori.floorEntry(cal.getTime()).getValue();
        Podatak podatak = new Podatak();

        float kum_sum = 0, zskum_sum = 0;
        int count = 0, zscount = 0;

        podatak.setProgramMjerenjaId(program);

        boolean zero = false;
        boolean span = false;
        zslista.clear();
        String zsvrsta = "";
    }
}
