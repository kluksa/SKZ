/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.diseminacija.ekonerg;

import dhz.skz.aqdb.facades.PodatakFacade;
import dhz.skz.aqdb.facades.PrimateljProgramKljuceviMapFacade;
import dhz.skz.aqdb.facades.ZeroSpanFacade;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.PrimateljProgramKljuceviMap;
import dhz.skz.aqdb.entity.PrimateljiPodataka;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ZeroSpan;
import dhz.skz.diseminacija.DiseminatorPodataka;
import dhz.skz.util.OperStatus;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
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
public class EkonergDiseminator implements DiseminatorPodataka {
    @Resource(name = "diseminacija")
    private DataSource diseminacija;
    
    private static final Logger log = Logger.getLogger(EkonergDiseminator.class.getName());

    private PrimateljiPodataka primatelj;
    @EJB
    private PrimateljProgramKljuceviMapFacade ppmFac;
    @EJB
    private PodatakFacade podatakFacade;
    @EJB
    private ZeroSpanFacade zeroSpanFacade;

//    private Date pocetak, kraj;
//    private PreparedStatement prepStmt, zsStmt;
//    private final Properties connectionProps = new Properties();
    private final String podatakInsertSql = "INSERT INTO mjerenja (postaja_oznaka_azo, komponenta_iso, vrijeme, vrijednost, status, obuhvat) VALUES (?,?,?,?,?,?)";
    private final String zsInsertSql = "INSERT INTO zero_span (postaja_azo, komponenta_iso, vrijeme, vrijednost, vrsta) VALUES (?,?,?,?,?)";
//    private PreparedStatement zadnjiMStmt;
//    private PreparedStatement zadnjiZSStmt;

    @Override
    public void salji(PrimateljiPodataka primatelj) {
        log.log(Level.INFO,"EkonergDiseminator:salji(primatelj_id = {0})", new Object[]{primatelj.getId()});

        this.primatelj = primatelj;
        String conStr = "jdbc:" + primatelj.getUrl();
        log.log(Level.INFO, conStr);
        Date sada = new Date();
        try (Connection con = diseminacija.getConnection()) {
            Collection<PrimateljProgramKljuceviMap> kljuceviZaPrimatelja = ppmFac.findAll(primatelj);
            for (PrimateljProgramKljuceviMap pm : kljuceviZaPrimatelja) {
                if (pm.getAktivan()>0) {
                    log.log(Level.INFO, "EkonergDiseminator:salji  pm={0}, postaja={1}, komponenta={2}", new Object[]{pm.getProgramMjerenja().getId(), pm.getProgramMjerenja().getPostajaId().getNazivPostaje(), pm.getProgramMjerenja().getKomponentaId().getFormula()});
                    prebaciMjerenja(con, pm, getZadnjeMjerenje(con, pm), sada);
                    prebaciZS(con, pm, getZadnjiZS(con, pm), sada);
                }
            }
            log.log(Level.INFO,"EkonergDiseminator:salji:: Prije zatvaranja konekcije");
        } catch (Exception ex) {
            log.log(Level.SEVERE, "", ex);
        }
    }

    @Override
    public void nadoknadi(PrimateljiPodataka primatelj, Collection<ProgramMjerenja> program, Date pocetak, Date kraj) {
        for ( ProgramMjerenja pm : program){
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void prebaciMjerenja(Connection con, PrimateljProgramKljuceviMap pm, Date pocetak, Date kraj) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(podatakInsertSql)) {
            for (Podatak p : podatakFacade.getPodaciOd(pm.getProgramMjerenja(), pocetak, 0)) {
                log.log(Level.FINE, "Spremam {0}:::{1}::{2}", new Object[]{p.getVrijeme(),
                    p.getProgramMjerenjaId().getId(), p.getVrijednost()});
                stmt.setString(1, pm.getProgramMjerenja().getPostajaId().getOznakaPostaje());
                stmt.setString(2, pm.getProgramMjerenja().getKomponentaId().getIsoOznaka());
                stmt.setTimestamp(3, new java.sql.Timestamp(p.getVrijeme().getTime()));
                if ( OperStatus.isValid(p)){
                    stmt.setDouble(4, p.getVrijednost());
                    stmt.setInt(5, 0);
                } else {
                    stmt.setDouble(4, -999.);
                     stmt.setInt(5, p.getStatus());
                }
                stmt.setInt(6, p.getObuhvat());
                stmt.execute();
//                stmt.addBatch();
            }
//            stmt.executeBatch();
        }
    }

    private void prebaciZS(Connection con, PrimateljProgramKljuceviMap pm, Date pocetak, Date kraj) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(zsInsertSql)) {
            for (ZeroSpan zs : zeroSpanFacade.getZeroSpanOd(pm.getProgramMjerenja(), pocetak)) {
                log.log(Level.FINE, "Spremam zs {0}:::{1}::{2}::{3}::{4}", new Object[]{zs.getVrijeme(),
                    pm.getPKljuc(), pm.getKKljuc(), zs.getVrijednost(), zs.getVrsta()});
                stmt.setString(1, pm.getProgramMjerenja().getPostajaId().getOznakaPostaje());
                stmt.setString(2, pm.getProgramMjerenja().getKomponentaId().getIsoOznaka());
                stmt.setTimestamp(3, new Timestamp(zs.getVrijeme().getTime()));
                stmt.setDouble(4, zs.getVrijednost());
                stmt.setString(5, zs.getVrsta());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private Date getZadnjiZS(Connection con, PrimateljProgramKljuceviMap pm) throws SQLException {
        final String zadnjiZSSql = "SELECT MAX(vrijeme) from zero_span WHERE postaja_azo=? AND komponenta_iso=?";
        return getZadnji(con, pm, zadnjiZSSql);
    }

    private Date getZadnjeMjerenje(Connection con, PrimateljProgramKljuceviMap pm) throws SQLException {
        final String zadnjiPodatakSql = "SELECT MAX(vrijeme) from mjerenja WHERE postaja_oznaka_azo=? AND komponenta_iso=?";
        return getZadnji(con, pm, zadnjiPodatakSql);
    }

    private Date getZadnji(Connection con, PrimateljProgramKljuceviMap pm, String sql) throws SQLException {
        Date zadnji = new Date(1388534400000L);
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            String nacionalnaOznaka = pm.getProgramMjerenja().getPostajaId().getOznakaPostaje();
            String isoOznaka = pm.getProgramMjerenja().getKomponentaId().getIsoOznaka();
            stmt.setString(1, nacionalnaOznaka);
            stmt.setString(2, isoOznaka);
            stmt.execute();
            try (ResultSet rs = stmt.getResultSet()) {
                if (rs.next() && rs.getTimestamp(1) != null) {
                    zadnji = rs.getTimestamp(1);
                }
            }
        }
        return zadnji;
    }
}
