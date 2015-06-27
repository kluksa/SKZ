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

import dhz.skz.aqdb.facades.IzvorPodatakaFacade;
import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.citaci.CitacIzvora;
import dhz.skz.citaci.MinutniUSatne;
import dhz.skz.config.Config;
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

        if (!aktivan) {
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
                    } catch (Throwable ex) {
                        log.log(Level.SEVERE, "POGRESKA KOD CITANJA IZVORA {0}:{1}", new Object[]{ip.getId(), ip.getNaziv()});
                        log.log(Level.SEVERE, null, ex);
                    }
                }
            } catch (NamingException ex) {
                log.log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                log.log(Level.SEVERE, null, ex);
            }
            // TODO ovo odkomentirati kada izbacim agregaciju iz citaca, Ideja je da citaci samo citaju, a da se onda naknadno svi podaci agregiraju
            minutniUSatne.napraviSatne(0);
            aktivan = false;
            log.log(Level.INFO, "Kraj pokretanja citaca");
        } else {
            log.log(Level.INFO, "Prethodni citac jos nije zavrsio");
        }
    }
}
