/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.dcs;

import dhz.skz.aqdb.facades.IzvorPodatakaFacade;
import dhz.skz.aqdb.facades.PodatakFacade;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacade;
import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.NivoValidacije;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.citaci.CitacIzvora;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.sql.DataSource;

/**
 *
 * @author kraljevic
 */
@Stateless
@LocalBean
public class DcsCitacBean implements CitacIzvora {
    @EJB
    private ProgramMjerenjaFacade programMjerenjaFacade;

    private static final Logger log = Logger.getLogger(DcsCitacBean.class.getName());

    @Resource(name = "dcs")
    private DataSource dcsDS;

    @EJB
    private PodatakFacade podatakFacade;
    @EJB
    private IzvorPodatakaFacade izvorPodatakaFacade;
    

    private final String selektSql = "SELECT endtime, value, validity, opestatus, errstatus FROM sirovi_2 WHERE station=? AND component=? AND vrijeme > ?";

    @Override
    public void napraviSatne(IzvorPodataka izvor) {
        for (ProgramMjerenja pm : programMjerenjaFacade.find(izvor)) {
            Date zadnji = podatakFacade.getZadnjiPodatak(pm);
            try (Connection con = dcsDS.getConnection()) {
                List<Podatak> podaci = getPodaci(con, pm, zadnji);
                podatakFacade.spremi(podaci);
            } catch (SQLException ex) {
                log.log(Level.SEVERE, null, ex);
            }

        }
    }

    private List<Podatak> getPodaci(Connection con, ProgramMjerenja pm, Date zadnji) throws SQLException {
        List<Podatak> podaci = new ArrayList<>();
        Integer station = Integer.parseInt(pm.getIzvorProgramKljuceviMap().getPKljuc());
        Integer component = Integer.parseInt(pm.getIzvorProgramKljuceviMap().getKKljuc());
        NivoValidacije nv = new NivoValidacije(0);
        
        try (PreparedStatement prepStmt = con.prepareStatement(selektSql)) {
            prepStmt.setInt(1, station);
            prepStmt.setInt(2, component);
            prepStmt.setTimestamp(3, new Timestamp(zadnji.getTime()));
            try (ResultSet rs = prepStmt.executeQuery()) {
                while (rs.next()) {
                    Timestamp timestamp = rs.getTimestamp(1);
                    double aFloat = rs.getDouble(2);
                    int obuhvat = rs.getInt(3);
                    String statusStr = rs.getString(4);
                    String greskaStr = rs.getString(5);
                    
                    Integer status = getStatus(statusStr, greskaStr, obuhvat);
                    Podatak p = new Podatak();
                    p.setProgramMjerenjaId(pm);
                    p.setNivoValidacijeId(nv);
                    p.setObuhvat(obuhvat);
                    p.setStatus(status);
                    p.setVrijeme(new Date(timestamp.getTime()));
                    p.setVrijednost(aFloat);
                    podaci.add(p);
                }
            }
        }
        return podaci;
    }

    private Integer getStatus(String statusStr, String greskaStr, Integer obuhvat) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
