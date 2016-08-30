/*
 * Copyright (C) 2015 kraljevic
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
package dhz.skz;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.facades.IzvorPodatakaFacade;
import dhz.skz.aqdb.facades.PodatakFacade;
import dhz.skz.aqdb.facades.PodatakSiroviFacade;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacade;
import dhz.skz.citaci.CitacIzvora;
import dhz.skz.citaci.MinutniUSatne;
import dhz.skz.config.Config;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author kraljevic
 */
@Singleton
@Startup
public class CitaciGlavniBean extends Scheduler implements CitaciGlavniBeanRemote {

    private static final Logger log = Logger.getLogger(CitaciGlavniBean.class.getName());

    @EJB
    private IzvorPodatakaFacade izvorPodatakaFacade;
    @EJB
    private MinutniUSatne minutniUSatne;
    @EJB
    private ProgramMjerenjaFacade pmf;
    @EJB
    private PodatakFacade pf;
    @EJB
    private PodatakSiroviFacade psf;

    private boolean aktivan = false;

    public CitaciGlavniBean() {
        super("CitacGlavniTimer");
    }

    @Inject
    @Config(vrijednost = "14")
    private Integer minuta;

    @PostConstruct
    public void init() {
        schedule(minuta);
    }

    @Timeout
    @Override
    public void pokreni() {
        if (false) {
            try {
                InitialContext ctx = new InitialContext();
                IzvorPodataka ip = izvorPodatakaFacade.findByName("WebLogger");
                CitacIzvora citac = (CitacIzvora) ctx.lookup("java:module/WebloggerCitacBean");
                citac.napraviSatne(ip);
            } catch (Throwable ex) {
                log.log(Level.SEVERE, "POGRESKA KOD CITANJA IZVORA");
                log.log(Level.SEVERE, null, ex);
            }
        } else {
            if (!aktivan) {
//            HashMap<String, Future<Boolean>> citaciFuture = new HashMap<>();
                log.log(Level.INFO, "Pokrecem citace");
                aktivan = true;
                try { // sto god da se desi, idemo na slijedeci izvor

                    InitialContext ctx = new InitialContext();
                    String str = "java:module/";
                    for (IzvorPodataka ip : izvorPodatakaFacade.getAktivniIzvori()) {
                        String naziv = str + ip.getBean().trim();
                        log.log(Level.INFO, "JNDI: {0}", naziv);
                        try {
                            CitacIzvora citac = (CitacIzvora) ctx.lookup(naziv);
                            citac.napraviSatne(ip);
//                        citaciFuture.put(naziv, citac.napraviSatne(ip));
                        } catch (Throwable ex) {
                            log.log(Level.SEVERE, "POGRESKA KOD CITANJA IZVORA {0}:{1}", new Object[]{ip.getId(), ip.getNaziv()});
                            log.log(Level.SEVERE, null, ex);
                        }
                    }
//                log.log(Level.INFO, "PRIJE CEKANJA");
//                while (!checkIsDone(citaciFuture)) {
//                    Thread.sleep(1000);
//                }
                    log.log(Level.SEVERE, "CITACI ZAVRSILI POCINJE AGREGACIJA");
                    minutniUSatne.napraviSatne(0);
                    log.log(Level.SEVERE, "KRAJ AGREGACIJE");
                } catch (NamingException ex) {
                    log.log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    log.log(Level.SEVERE, null, ex);
                }
                aktivan = false;
                log.log(Level.INFO, "Kraj pokretanja citaca");
            } else {
                log.log(Level.INFO, "Prethodni citac jos nije zavrsio");
            }
        }
    }

    boolean checkIsDone(Map<String, Future<Boolean>> citaciFuture) {
        Boolean status = true;
        for (String st : citaciFuture.keySet()) {
            status &= citaciFuture.get(st).isDone();
        }
        return status;
    }

    @Override
    public Map<String, String> opisiStatus(PodatakSirovi ps) throws NamingException {
        InitialContext ctx = new InitialContext();
        String str = "java:module/";
        IzvorPodataka izvorPodatakaId = ps.getProgramMjerenjaId().getIzvorPodatakaId();
        String naziv = str + izvorPodatakaId.getBean().trim();
        CitacIzvora citac = (CitacIzvora) ctx.lookup(naziv);
        return citac.opisiStatus(ps);
    }

    @Override
    public void agregirajProgramOdDo(final ProgramMjerenja program,
            final Integer nivo,
            final Date pocetak,
            final Date kraj
    ) {
        log.log(Level.INFO, "Spremam pm={0}; od={1}; do={2}", new Object[]{program.getId(), pocetak, kraj});
        minutniUSatne.spremiSatneIzSirovih(program, nivo, pocetak, kraj);
    }

    @Override
    public void dodajOdPocetka() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
