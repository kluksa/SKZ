/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.diseminacija;

import dhz.skz.aqdb.entity.PrimateljiPodataka;
import dhz.skz.aqdb.facades.PodatakFacade;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author kraljevic
 */
@Stateless
public class DiseminacijaMainBean implements DiseminacijaMain {

    private static final Logger log = Logger.getLogger(DiseminacijaMainBean.class.getName());

    @EJB
    private PodatakFacade dao;
//    @PersistenceContext(unitName = "LIKZ-ejbPU")
//    private EntityManager em;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public void pokreniDiseminaciju() {
        log.log(Level.INFO, "DISEMINACIJA");
        try {
            InitialContext ctx = new InitialContext();
            String str = "java:module/";

            for (PrimateljiPodataka pr : dao.getAktivniPrimatelji()) {
//                em.refresh(pr);
                if (pr.getTip() != null) {
                    String naziv = str + pr.getTip().trim();
                    log.log(Level.INFO, "JNDI: {0}", naziv);
                    try {
                        DiseminatorPodataka diseminator = (DiseminatorPodataka) ctx.lookup(naziv);

                        diseminator.salji(pr);

                    } catch (NamingException ex) {
                        log.log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (NamingException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void nadoknadiPodatke(PrimateljiPodataka primatelj, Date pocetak, Date kraj) {
        try {
            InitialContext ctx = new InitialContext();
            String str = "java:module/";

//            em.refresh(primatelj);
            String naziv = str + primatelj.getTip().trim();
            log.log(Level.INFO, "JNDI: {0}", naziv);
            try {
                DiseminatorPodataka diseminator = (DiseminatorPodataka) ctx.lookup(naziv);

                    diseminator.nadoknadi(primatelj, pocetak, kraj);
            } catch (NamingException ex) {
                log.log(Level.SEVERE, null, ex);
            }
        } catch (NamingException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void nadoknadiPodatke(Long primateljId, Date pocetak, Date kraj) {
        log.log(Level.INFO, "NADOKNADJUKEM!!!!!!!!!!!!!!!!");
    }

}
