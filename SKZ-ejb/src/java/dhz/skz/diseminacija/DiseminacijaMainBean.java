/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.diseminacija;

import dhz.skz.aqdb.entity.PrimateljiPodataka;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.facades.PrimateljiPodatakaFacadeLocal;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author kraljevic
 */
@Stateless
public class DiseminacijaMainBean  {

    private static final Logger log = Logger.getLogger(DiseminacijaMainBean.class.getName());

    @EJB
    private PrimateljiPodatakaFacadeLocal dao;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void pokreni() {
        log.log(Level.INFO, "DISEMINACIJA");
        try {
            InitialContext ctx = new InitialContext();
            String str = "java:module/";

            for (PrimateljiPodataka pr : dao.getAktivniPrimatelji()) {
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
        } catch (Exception ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void nadoknadiPodatke(PrimateljiPodataka primatelj, Collection<ProgramMjerenja> program, Date pocetak, Date kraj) {
        try {
            InitialContext ctx = new InitialContext();
            String str = "java:module/";
            String naziv = str + primatelj.getTip().trim();
            log.log(Level.INFO, "JNDI: {0}", naziv);
            try {
                DiseminatorPodataka diseminator = (DiseminatorPodataka) ctx.lookup(naziv);
                diseminator.nadoknadi(primatelj, program, pocetak, kraj);
            } catch (NamingException ex) {
                log.log(Level.SEVERE, null, ex);
            }
        } catch (NamingException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }
}
