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
import dhz.skz.diseminacija.upozorenja.UpozorenjaBean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * @author kraljevic
 */
@Singleton
@Startup
@TransactionManagement(TransactionManagementType.BEAN)
public class DiseminacijaGlavniBean extends Scheduler implements DiseminacijaGlavniBeanRemote {

    private static final Logger log = Logger.getLogger(DiseminacijaGlavniBean.class.getName());

    @EJB
    private PrimateljiPodatakaFacade primateljPodatakaFacade;

    @EJB
    private UpozorenjaBean upozorenjeB;

    private boolean aktivan = false;

    @Inject
    @Config(vrijednost = "23")
    private Integer minuta;

    @Resource
    private EJBContext context;

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
        if (false) {
            pokretniTest();
        } else {
            if (!aktivan) {
                try {
                    aktivan = true;
                    InitialContext ctx = new InitialContext();
                    String str = "java:module/";
                    UserTransaction utx = context.getUserTransaction();

                    for (PrimateljiPodataka pr : primateljPodatakaFacade.findAll()) {
                        if (pr.getAktivan() < 1) {
                            log.log(Level.INFO, "Primatelj: {0} nije aktivan", new Object[]{pr.getNaziv()});
                        } else {
                            String tip = pr.getTip();
                            if (pr.getTip() != null || !pr.getTip().isEmpty()) {
//                            if (pr.getTip() != null && !(pr.getTip().equals("UpozorenjaBean"))) {
                                if (pr.getTip().equals("UpozorenjaBean")) {
                                    upozorenjeB.salji(pr);
                                } else {
                                    String naziv = str + pr.getTip().trim();
                                    log.log(Level.INFO, "JNDI: {0}", naziv);
                                    try {
                                        // ovo nije dobro jer u slucaju da lookup ne uspije ispada exception iz transakcije i 
                                        // sve se raspada
                                        DiseminatorPodataka diseminator = (DiseminatorPodataka) ctx.lookup(naziv);
                                        utx.begin();
                                        diseminator.salji(pr);
                                        utx.commit();
                                    } catch (NotSupportedException ex) {
                                        log.log(Level.SEVERE, null, ex);
                                    } catch (SystemException ex) {
                                        log.log(Level.SEVERE, null, ex);
                                    } catch (NamingException ex) {
                                        log.log(Level.SEVERE, null, ex);
                                    } catch (RollbackException ex) {
                                        log.log(Level.SEVERE, null, ex);
                                    } catch (HeuristicMixedException ex) {
                                        log.log(Level.SEVERE, null, ex);
                                    } catch (HeuristicRollbackException ex) {
                                        log.log(Level.SEVERE, null, ex);
                                    } catch (SecurityException ex) {
                                        log.log(Level.SEVERE, null, ex);
                                    } catch (IllegalStateException ex) {
                                        log.log(Level.SEVERE, null, ex);
                                    } catch (RuntimeException ex) {
                                        log.log(Level.SEVERE, null, ex);
                                    } catch (Exception ex) {
                                        log.log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                        }
                    }

                    aktivan = false;
                    log.log(Level.INFO, "Kraj pokretanja diseminacije");
                } catch (NamingException ex) {
                    log.log(Level.SEVERE, null, ex);
                }
            } else {
                log.log(Level.INFO, "Prethodna diseminacija jos nije zavrsila");
            }
        }
    }

    private void pokretniTest() {
        try {
            InitialContext ctx = new InitialContext();
            String str = "java:module/";

            PrimateljiPodataka pr = primateljPodatakaFacade.find(1);
            if (pr.getTip() != null) {
                String naziv = str + pr.getTip().trim();
                log.log(Level.INFO, "JNDI: {0}", naziv);
                try {
                    DiseminatorPodataka diseminator = (DiseminatorPodataka) ctx.lookup(naziv);
                    diseminator.salji(pr);
                } catch (Exception ex) {
                    log.log(Level.SEVERE, null, ex);
                }
            }
        } catch (NamingException ex) {
            log.log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }
}
