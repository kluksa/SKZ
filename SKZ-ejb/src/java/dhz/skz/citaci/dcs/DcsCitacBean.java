/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.dcs;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.NivoValidacije;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.facades.IzvorPodatakaFacade;
import dhz.skz.aqdb.facades.PodatakFacade;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacadeLocal;
import dhz.skz.citaci.CitacIzvora;
import dhz.skz.citaci.weblogger.util.Flag;
import dhz.skz.citaci.weblogger.util.NizOcitanja;
import dhz.skz.citaci.weblogger.util.Status;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
    private ProgramMjerenjaFacadeLocal programMjerenjaFacade;

    private static final Logger log = Logger.getLogger(DcsCitacBean.class.getName());

    @Resource(name = "dcs")
    private DataSource dcsDS;

    @EJB
    private PodatakFacade podatakFacade;
    @EJB
    private IzvorPodatakaFacade izvorPodatakaFacade;
    

    private final String selektSql = "SELECT endtime, value, validity, opestatus, errstatus FROM sirovi_2 WHERE station=? AND component=? AND vrijeme > ?";

    @Override
    public Map<ProgramMjerenja, NizOcitanja> procitaj(IzvorPodataka izvor) {
        for (ProgramMjerenja pm : programMjerenjaFacade.find(izvor)) {
            Date zadnji = podatakFacade.getZadnjiPodatak(pm);
            try (Connection con = dcsDS.getConnection()) {
                List<Podatak> podaci = getPodaci(con, pm, zadnji);
                podatakFacade.spremi(podaci);
            } catch (SQLException ex) {
                log.log(Level.SEVERE, null, ex);
            }

        }
        return null;
    }

    @Override
    public Map<ProgramMjerenja, NizOcitanja> procitaj(IzvorPodataka izvor, Map<ProgramMjerenja, Podatak> zadnjiPodatak) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private List<Podatak> getPodaci(Connection con, ProgramMjerenja pm, Date zadnji) throws SQLException {
        List<Podatak> podaci = new ArrayList<>();
        Integer station = Integer.parseInt(pm.getIzvorProgramKljuceviMap().getPKljuc());
        Integer component = Integer.parseInt(pm.getIzvorProgramKljuceviMap().getKKljuc());
        try (PreparedStatement prepStmt = con.prepareStatement(selektSql)) {
            prepStmt.setInt(1, station);
            prepStmt.setInt(2, component);
            prepStmt.setTimestamp(3, new Timestamp(zadnji.getTime()));
            try (ResultSet rs = prepStmt.executeQuery()) {
                while (rs.next()) {
                    Timestamp timestamp = rs.getTimestamp(1);
                    float aFloat = rs.getFloat(2);
                    short obuhvat = rs.getShort(3);
                    String statusStr = rs.getString(4);
                    String greskaStr = rs.getString(5);
                    
                    Integer status = getStatus(statusStr, greskaStr, obuhvat);
                    Podatak p = new Podatak();
                    p.setProgramMjerenjaId(pm);
                    p.setNivoValidacijeId(new NivoValidacije((short) 0));
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

    private Integer getStatus(String statusStr, String greskaStr, Short obuhvat) {
        Status s = new Status();

        if (greskaStr != null && greskaStr.trim().isEmpty()) {
            s.dodajFlag(Flag.FAULT);
        }
        if (statusStr != null && !statusStr.trim().isEmpty()) {
            s.dodajFlag(Flag.UPOZORENJE);
            if (statusStr.contains("S")) {
                s.dodajFlag(Flag.SPAN);
            }
            if (statusStr.contains("Z")) {
                s.dodajFlag(Flag.ZERO);
            }
            if (statusStr.contains("M")) {
                s.dodajFlag(Flag.MAINTENENCE);
            }
        }
        if (obuhvat < 75) {
            s.dodajFlag(Flag.OBUHVAT);
        }
        return s.getStatus();
    }

    @Override
    public Collection<PodatakSirovi> dohvatiSirove(ProgramMjerenja program, Date pocetak, Date kraj, boolean p, boolean k) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
