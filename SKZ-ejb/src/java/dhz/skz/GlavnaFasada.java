/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.PrimateljiPodataka;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.facades.IzvorPodatakaFacade;
import dhz.skz.aqdb.facades.PrimateljiPodatakaFacade;
import dhz.skz.citaci.CitacIzvora;
import dhz.skz.diseminacija.DiseminatorPodataka;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author kraljevic
 */
@Stateless
public class GlavnaFasada implements GlavnaFasadaRemote {
    @EJB
    private IzvorPodatakaFacade izvorPodatakaFacade;
    @EJB
    private PrimateljiPodatakaFacade primateljPodatakaFacade;

    private static final Logger log = Logger.getLogger(GlavnaFasada.class.getName());

    @Override
    public void pokreniCitanje() {
        log.log(Level.INFO, "Pokrecem citace");
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
        }
        log.log(Level.INFO, "Kraj pokretanja citaca");
    }

    @Override
    public void pokreniDiseminaciju() {
        log.log(Level.INFO, "Pokrecem diseminaciju");
        try {
            InitialContext ctx = new InitialContext();
            String str = "java:module/";

            for (PrimateljiPodataka pr : primateljPodatakaFacade.getAktivniPrimatelji()) {
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

    @Override
    public void nadoknadiPodatke(PrimateljiPodataka primatelj, Collection<ProgramMjerenja> program, Date pocetak, Date kraj) {
        log.log(Level.INFO, "Nadoknadjujem podatke");
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
