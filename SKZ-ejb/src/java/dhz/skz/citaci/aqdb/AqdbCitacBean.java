/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.aqdb;

import dhz.skz.aqdb.facades.PodatakFacade;
import dhz.skz.aqdb.facades.PodatakSiroviFacade;
import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.NivoValidacije;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.citaci.CitacIzvora;
import dhz.skz.config.Config;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.sql.DataSource;

/**
 *
 * @author kraljevic
 */
@Stateless
@LocalBean
public class AqdbCitacBean implements CitacIzvora {
    @Resource(name = "alcinaDB")
    private DataSource alcinaDB;

    private static final Logger log = Logger.getLogger(AqdbCitacBean.class.getName());
    protected static final short MIN_OBUHVAT = 75;
    

    @EJB
    private PodatakSiroviFacade podatakSiroviFacade;
    @EJB
    private PodatakFacade podatakFacade;
    @Inject @Config private TimeZone tzone;

    @Override
    public void napraviSatne(IzvorPodataka izvor) {
        log.log(Level.INFO, "POCETAK CITANJA");
        for (ProgramMjerenja program : izvor.getProgramMjerenjaCollection()) {
            Date zadnjiSatni = podatakFacade.getZadnjiPodatak(program, new NivoValidacije(0));
            Date zadnjiSirovi = podatakSiroviFacade.getZadnjiPodatak(program);
            procitaj();
//            SatniIterator sat = new SatniIterator(zadnjiSatni, zadnjiSirovi, tzone);
//            while (sat.next()) {
//            }
        }
        log.log(Level.INFO, "KRAJ CITANJA");
    }

    private void procitaj() {
        
        try (Connection con = alcinaDB.getConnection()) {
            
        } catch (SQLException ex) {
            log.log(Level.SEVERE, null, ex);
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
