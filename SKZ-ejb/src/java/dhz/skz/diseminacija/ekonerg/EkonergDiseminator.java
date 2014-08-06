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
import dhz.skz.aqdb.facades.PrimateljProgramKljuceviMapFacadeLocal;
import dhz.skz.aqdb.facades.ZeroSpanFacade;
import dhz.skz.diseminacija.DiseminatorPodataka;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
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
public class EkonergDiseminator implements DiseminatorPodataka {
    @Resource(name = "diseminacija")
    private DataSource diseminacija;
    
 
    private static final Logger log = Logger.getLogger(EkonergDiseminator.class.getName());
    

    private PrimateljiPodataka primatelj;
    @EJB
    private PrimateljProgramKljuceviMapFacadeLocal ppmFac;
    @EJB
    private PodatakFacade dao;
    @EJB
    private ZeroSpanFacade zsDao;

    private Date pocetak, kraj;

    private PreparedStatement prepStmt, zsStmt;

    private final Properties connectionProps = new Properties();

    private final String podatakInsertSql = "INSERT INTO mjerenja (postaja_oznaka_azo, komponenta_iso, vrijeme, vrijednost, status, obuhvat) VALUES (?,?,?,?,?,?)";
    private final String zsInsertSql = "INSERT INTO zero_span (postaja_azo, komponenta_iso, vrijeme, vrijednost, vrsta) VALUES (?,?,?,?,?)";

 
    @Override
    public void salji(PrimateljiPodataka primatelj) {
        
        this.primatelj = primatelj;
        String conStr = "jdbc:" + primatelj.getUrl();
        log.log(Level.INFO, conStr);
//        try (Connection con = DriverManager.getConnection(conStr, connectionProps)) {
        try (Connection con = diseminacija.getConnection()) {
            prepStmt = con.prepareStatement(podatakInsertSql);
            zsStmt = con.prepareStatement(zsInsertSql);

            odrediPocetakKraj();

            for (PrimateljProgramKljuceviMap pm : primatelj.getPrimateljProgramKljuceviMapCollection()) {
                prebaciMjerenja(pm);
                prebaciZS(pm);
            }
            prepStmt.executeBatch();
            prepStmt.close();
            zsStmt.executeBatch();
            zsStmt.close();
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "", ex);
        }
    }

    private void prebaciMjerenja(PrimateljProgramKljuceviMap pm) throws SQLException {
        for (Podatak p : dao.getPodatak(pm.getProgramMjerenja(), pocetak, kraj, true, true)) {
            log.log(Level.FINE, "Spremam {0}:::{1}::{2}::{3}::{4}", new Object[]{p.getVrijeme(),
            p.getProgramMjerenjaId().getId(), pm.getPKljuc(), pm.getKKljuc(), p.getVrijednost()});

            prepStmt.setString(1, pm.getPKljuc());
            prepStmt.setString(2, pm.getKKljuc());
            prepStmt.setTimestamp(3, new java.sql.Timestamp(p.getVrijeme().getTime()));
            prepStmt.setFloat(4, p.getVrijednost());
            prepStmt.setInt(5, p.getStatus());
            prepStmt.setInt(6, p.getObuhvat());
            prepStmt.addBatch();
        }
    }

    private void prebaciZS(PrimateljProgramKljuceviMap pm) throws SQLException {
        for (ZeroSpan zs : zsDao.getZeroSpan(pm.getProgramMjerenja(), pocetak, kraj)) {
            log.log(Level.FINE, "Spremam zs {0}:::{1}::{2}::{3}::{4}", new Object[]{zs.getVrijeme(),
                pm.getPKljuc(), pm.getKKljuc(), zs.getVrijednost(), zs.getVrsta()});
            zsStmt.setString(1, pm.getPKljuc());
            zsStmt.setString(2, pm.getKKljuc());
            zsStmt.setTimestamp(3, new Timestamp(zs.getVrijeme().getTime()));
            zsStmt.setFloat(4, zs.getVrijednost());
            zsStmt.setString(5, zs.getVrsta());
            zsStmt.addBatch();
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
    public void nadoknadi(PrimateljiPodataka primatelj, Collection<ProgramMjerenja> programi, Date d1, Date d2) {
        this.primatelj = primatelj;
        this.pocetak = d1;
        this.kraj = d2;

        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        cal.setTime(pocetak);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        pocetak = cal.getTime();
        cal.setTime(kraj);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        kraj = cal.getTime();


        try (Connection con = diseminacija.getConnection()) {
//        try (Connection con = DriverManager.getConnection(conStr, connectionProps)) {
            try (CallableStatement prepCall = con.prepareCall("CALL diseminacija.nedostajuci_periodi(?,?,?,?)")) {
                for (ProgramMjerenja program : programi) {
                    PrimateljProgramKljuceviMap ppkm = ppmFac.find(primatelj, program);
                    if (pocetak.before(program.getPocetakMjerenja())) {
                        pocetak = program.getPocetakMjerenja();
                    }
                    if (program.getZavrsetakMjerenja() != null && kraj.after(program.getZavrsetakMjerenja())) {
                        kraj = program.getZavrsetakMjerenja();
                    }

                    prepCall.setString(1, ppkm.getPKljuc());
                    prepCall.setString(2, ppkm.getKKljuc());
                    prepCall.setTimestamp(3, new java.sql.Timestamp(pocetak.getTime()));
                    prepCall.setTimestamp(4, new java.sql.Timestamp(kraj.getTime()));
                    try (ResultSet rs = prepCall.executeQuery()) {
                        prepStmt = con.prepareStatement(podatakInsertSql);
                        while (rs.next()) {
                            pocetak = new Date(rs.getTimestamp("pocetak").getTime());
                            kraj = new Date(rs.getTimestamp("kraj").getTime());
                            prebaciMjerenja(ppkm);
                        }
                        prepStmt.executeBatch();
                        prepStmt.close();
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EkonergDiseminator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
