/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.facades.IzvorPodatakaFacade;
import dhz.skz.aqdb.facades.PodatakSiroviFacadeLocal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author kraljevic
 */
@Stateless
@LocalBean
@TransactionAttribute(NOT_SUPPORTED)
public class CitacMainBean  {
    @EJB
    private PodatakSiroviFacadeLocal podatakSiroviFacade;

    private static final Logger log = Logger.getLogger(CitacMainBean.class.getName());
    @EJB
    private IzvorPodatakaFacade izvorPodatakaFacade;

    @TransactionAttribute(NOT_SUPPORTED)
    public void pokreniCitace() {
        log.log(Level.INFO, "Pokrecem citace");
        try {
            InitialContext ctx = new InitialContext();
            String str = "java:module/";
            for (IzvorPodataka ip : izvorPodatakaFacade.getAktivniIzvori()) {
                String naziv = str + ip.getBean().trim();
                log.log(Level.INFO, "JNDI: {0}", naziv);
                try {
                    CitacIzvora citac = (CitacIzvora) ctx.lookup(naziv);
                    citac.napraviSatne(ip);
                } catch (NamingException ex) {
                    log.log(Level.SEVERE, null, ex);
                }
            }
        } catch (NamingException ex) {
            log.log(Level.SEVERE, null, ex);
        }
        log.log(Level.INFO, "Kraj pokretanja citaca");
    }
}
