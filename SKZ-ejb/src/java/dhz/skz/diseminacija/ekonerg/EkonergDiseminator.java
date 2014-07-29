/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.diseminacija.ekonerg;

import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.PrimateljProgramKljuceviMap;
import dhz.skz.aqdb.entity.PrimateljiPodataka;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ZeroSpan;
import dhz.skz.aqdb.facades.PodatakFacade;
import dhz.skz.aqdb.facades.ZeroSpanFacade;
import dhz.skz.diseminacija.DiseminatorPodataka;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author kraljevic
 */
@Stateless
@LocalBean
public class EkonergDiseminator implements DiseminatorPodataka {

    private static final Logger log = Logger.getLogger(EkonergDiseminator.class.getName());
    private PrimateljiPodataka primatelj;
    private Connection con;
    @EJB
    private PodatakFacade dao;
    @EJB
    private ZeroSpanFacade zsDao;

    private Date pocetak, kraj;

    @Override
    public void salji(PrimateljiPodataka primatelj) {
        this.primatelj = primatelj;

        odrediPocetakKraj();

        try {
            otvoriKonekciju();
            PreparedStatement prepStmt = con.prepareStatement(
                    "INSERT INTO mjerenja ("
                    + "postaja_oznaka_azo, komponenta_iso, vrijeme, "
                    + "vrijednost, status, obuhvat) "
                    + "VALUES (?,?,?,?,?,?)");

            PreparedStatement zsStmt = con.prepareStatement(
                    "INSERT INTO zero_span ("
                    + "postaja_azo, komponenta_iso, vrijeme, "
                    + "vrijednost, vrsta) "
                    + "VALUES (?,?,?,?,?)");

            for (PrimateljProgramKljuceviMap pm : primatelj.getPrimateljProgramKljuceviMapCollection()) {
                log.log(Level.INFO, "Slati cu: {0};{1};{2};{3};{4};{5}", new Object[]{pm.getProgramMjerenja().getPostajaId().getNazivPostaje(),
                    pm.getPKljuc(), pm.getProgramMjerenja().getKomponentaId().getFormula(), pm.getPKljuc(), pm.getUKljuc(), pm.getNKljuc()});
                List<Podatak> l = dao.getPodatak(pm.getProgramMjerenja(), pocetak, kraj);
                if (!l.isEmpty()) {
                    spremi(prepStmt, pm, l.get(0));
                } else {
                    log.log(Level.INFO, "Nema podataka za: {0},{1} od {2} do {3}", new Object[]{pm.getProgramMjerenja().getPostajaId().getNazivPostaje(),
                        pm.getProgramMjerenja().getKomponentaId().getFormula(),
                        pocetak, kraj});
                }
                List<ZeroSpan> zs = zsDao.getZeroSpan(pm.getProgramMjerenja(), pocetak, kraj);
                if (!zs.isEmpty()) {
                    spremiZS(zsStmt, pm, zs);
                } else {
                    log.log(Level.INFO, "Nema zero/span za: {0},{1} od {2} do {3}", new Object[]{pm.getProgramMjerenja().getPostajaId().getNazivPostaje(),
                        pm.getProgramMjerenja().getKomponentaId().getFormula(),
                        pocetak, kraj});
                }
            }
            prepStmt.executeBatch();
            prepStmt.close();
            zsStmt.executeBatch();
            zsStmt.close();
            zatvoriKonekciju();
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "", ex);
        }
    }

    private void otvoriKonekciju() throws SQLException {
        Properties connectionProps = new Properties();
        connectionProps.put("user", "automat");
        connectionProps.put("password", "pasvord@auto");

        connectionProps.put("useJDBCCompliantTimeZoneShift", "true:");
        connectionProps.put("serverTimeZone", "UTC");
        connectionProps.put("useGMTMillisForDateTime", "true");
        connectionProps.put("useLegacyDateTimeCode", "false");
        connectionProps.put("useTimeZone", "true");
        String conStr = "jdbc:" + primatelj.getUrl();
        log.log(Level.INFO, conStr);
        con = DriverManager.getConnection(conStr, connectionProps);
    }

    private void spremi(PreparedStatement prepStmt, PrimateljProgramKljuceviMap pm, Podatak p) throws SQLException {
        log.log(Level.INFO, "Spremam {0}:::{1}::{2}::{3}::{4}", new Object[]{p.getVrijeme(),
            p.getProgramMjerenjaId().getId(), pm.getPKljuc(), pm.getKKljuc(), p.getVrijednost()});
        prepStmt.setString(1, pm.getPKljuc());
        prepStmt.setString(2, pm.getKKljuc());
        prepStmt.setTimestamp(3, new Timestamp(p.getVrijeme().getTime()));
        prepStmt.setFloat(4, p.getVrijednost());
        prepStmt.setInt(5, p.getStatus());
        prepStmt.setInt(6, p.getObuhvat());
        prepStmt.addBatch();
    }

    private void zatvoriKonekciju() {
        try {
            con.close();
        } catch (SQLException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

    private void spremiZS(PreparedStatement prepStmt, PrimateljProgramKljuceviMap pm, List<ZeroSpan> zsList) throws SQLException {
        for (ZeroSpan zs : zsList) {
            log.log(Level.INFO, "Spremam zs {0}:::{1}::{2}::{3}::{4}", new Object[]{zs.getVrijeme(),
                pm.getPKljuc(), pm.getKKljuc(), zs.getVrijednost(), zs.getVrsta()});
            prepStmt.setString(1, pm.getPKljuc());
            prepStmt.setString(2, pm.getKKljuc());
            prepStmt.setTimestamp(3, new Timestamp(zs.getVrijeme().getTime()));
            prepStmt.setFloat(4, zs.getVrijednost());
            prepStmt.setString(5, zs.getVrsta());
            prepStmt.addBatch();
        }
    }

    private void odrediPocetakKraj() {
        Calendar trenutni_termin = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        trenutni_termin.set(Calendar.MINUTE, 0);
        trenutni_termin.set(Calendar.SECOND, 0);
        trenutni_termin.set(Calendar.MILLISECOND, 0);
        kraj = trenutni_termin.getTime();
        trenutni_termin.add(Calendar.HOUR, -1);
        pocetak = trenutni_termin.getTime();

    }

    @Override
    public void nadoknadi(PrimateljiPodataka primatelj, Collection<ProgramMjerenja> program, Date pocetak, Date kraj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
