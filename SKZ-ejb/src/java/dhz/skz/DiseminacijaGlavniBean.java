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

import dhz.skz.aqdb.facades.PrimateljiPodatakaFacade;
import dhz.skz.aqdb.entity.PrimateljiPodataka;
import dhz.skz.config.Config;
import dhz.skz.diseminacija.DiseminatorPodataka;
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
public class DiseminacijaGlavniBean extends Scheduler implements DiseminacijaGlavniBeanRemote {

    private static final Logger log = Logger.getLogger(DiseminacijaGlavniBean.class.getName());

    @EJB
    private PrimateljiPodatakaFacade primateljPodatakaFacade;

    private boolean aktivan = false;

    @Inject
    @Config(vrijednost = "23")
    private Integer minuta;

    public DiseminacijaGlavniBean() {
        super("DiseminacijaGlavniTimer");
    }

    @PostConstruct
    public void init() {
        schedule(minuta);
    }

    @Override
    @Timeout
    public void pokreni() {
        log.log(Level.INFO, "Pokrecem diseminaciju");
        if (!aktivan) {
            aktivan = true;
            try {
                InitialContext ctx = new InitialContext();
                String str = "java:module/";

                for (PrimateljiPodataka pr : primateljPodatakaFacade.findAll()) {
                    if (pr.getAktivan() < 1) {
                        log.log(Level.INFO, "Primatelj: {0} nije aktivan", new Object[]{pr.getNaziv()});

                    } else {
                        if (pr.getTip() != null) {
                            String naziv = str + pr.getTip().trim();
                            log.log(Level.INFO, "JNDI: {0}", naziv);
                            try {
                                DiseminatorPodataka diseminator = (DiseminatorPodataka) ctx.lookup(naziv);
                                diseminator.salji(pr);
                            } catch (NamingException ex) {
                                log.log(Level.SEVERE, null, ex);
                            } catch (Exception ex) {
                                log.log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            } catch (NamingException ex) {
                log.log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                log.log(Level.SEVERE, null, ex);
            }
            aktivan = false;
            log.log(Level.INFO, "Kraj pokretanja diseminacije");
        } else {
            log.log(Level.INFO, "Prethodna diseminacija jos nije zavrsila");
        }
    }
}
