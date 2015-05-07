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
public class CitacMainBean  {
    private static final Logger log = Logger.getLogger(CitacMainBean.class.getName());
    @EJB
    private PodatakSiroviFacadeLocal podatakSiroviFacade;
    @EJB
    private IzvorPodatakaFacade izvorPodatakaFacade;

    public void pokreniCitace() {
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
}
