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
import dhz.skz.aqdb.entity.IzvorProgramKljuceviMap;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ZeroSpan;
import dhz.skz.aqdb.facades.PodatakSiroviFacade;
import dhz.skz.aqdb.facades.ZeroSpanFacade;
import dhz.skz.citaci.CitacIzvora;
import dhz.skz.util.OperStatus;
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
import javax.ejb.EJBContext;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.sql.DataSource;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * @author kraljevic
 */
@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.BEAN)
public class DcsCitacBean implements CitacIzvora {
    @EJB
    private ProgramMjerenjaFacade programMjerenjaFacade;

    private static final Logger log = Logger.getLogger(DcsCitacBean.class.getName());

    @Resource(name = "dcs")
    private DataSource dcsDS;
    
    @Resource
    private EJBContext context;

    @EJB
    private PodatakFacade podatakFacade;
    
    @EJB
    private PodatakSiroviFacade podatakSiroviFacade;

    @EJB
    private IzvorPodatakaFacade izvorPodatakaFacade;
    
    @EJB
    private ZeroSpanFacade zeroSpanFacade;
    


    @Override
    public void napraviSatne(IzvorPodataka izvor) {
        UserTransaction utx = context.getUserTransaction();

        for (ProgramMjerenja pm : programMjerenjaFacade.find(izvor)) {
            PodatakSirovi ps = podatakSiroviFacade.getZadnji(pm);
            Date vrijemeZadnjeg;
            if ( ps != null ) {
                vrijemeZadnjeg = podatakSiroviFacade.getZadnji(pm).getVrijeme();
            } else {
                vrijemeZadnjeg = pm.getPocetakMjerenja();
            }
            Date vrijemeZadnjegZS = zeroSpanFacade.getVrijemeZadnjeg(pm);
            Collection<ZeroSpan> zs=null;
            Collection<PodatakSirovi> podaci=null;

            try (Connection con = dcsDS.getConnection()) {
                podaci = getPodaci(con, pm, vrijemeZadnjeg);
                zs=getZeroSpan(con, pm, vrijemeZadnjegZS);
            } catch (SQLException ex) {
                log.log(Level.SEVERE, null, ex);
            }
            try {
                utx.begin();
                zeroSpanFacade.spremi(zs);
                podatakSiroviFacade.spremi(podaci);
                utx.commit();
//            } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
            } catch (Exception ex) {
                log.log(Level.SEVERE, "GRESKA PROGRAM={0}, ZADNJI={1}, ZADNJIZS={2}",new Object[]{pm.getId(), vrijemeZadnjeg, vrijemeZadnjegZS});
                log.log(Level.SEVERE, null, ex);
            }
        }
    }

    private Collection<PodatakSirovi> getPodaci(Connection con, ProgramMjerenja pm, Date zadnji) throws SQLException {
        List<PodatakSirovi> podaci = new ArrayList<>();
        
//        log.log(Level.INFO, "PM= {0}, IPKM={1}", new Object[]{pm.getId(), pm.getIzvorProgramKljuceviMap()} );
        IzvorProgramKljuceviMap ipkm = pm.getIzvorProgramKljuceviMap();
//        log.log(Level.INFO, "IPKM= {0}, P= {1}, K={2}, N={3}, U={4}", new Object[]{ipkm.getProgramMjerenjaId(), ipkm.getPKljuc(), ipkm.getKKljuc(), ipkm.getNKljuc(), ipkm.getUKljuc()} );
   
        if ( zadnji == null) {
            zadnji = pm.getPocetakMjerenja();
        }
        Integer station = Integer.parseInt(pm.getIzvorProgramKljuceviMap().getPKljuc());
        Integer component = Integer.parseInt(pm.getIzvorProgramKljuceviMap().getKKljuc());
//        NivoValidacije nv = nivoValidacijeFacade.find(0);
        String selektSql = "SELECT endtime, value, validity, opestatus_b, errstatus_b, intstatus_b FROM mjerenja WHERE station=? AND component=? AND endtime > ?";

        try (PreparedStatement prepStmt = con.prepareStatement(selektSql)) {
            prepStmt.setInt(1, station);
            prepStmt.setInt(2, component);
            prepStmt.setTimestamp(3, new Timestamp(zadnji.getTime()));
            try (ResultSet rs = prepStmt.executeQuery()) {
                while (rs.next()) {
                    String strTime = rs.getString(1);
                    Timestamp timestamp = rs.getTimestamp(1);
                    log.log(Level.FINER, "VRIJEME: str={0}, ts={1}, long={2}",new Object[]{strTime, timestamp, timestamp.getTime()});
                    double aFloat = rs.getDouble(2);
                    Integer obuhvat = rs.getInt(3);
                    Integer statusB = rs.getInt(4);
                    Integer greskaB = rs.getInt(5);
                    Integer intB = rs.getInt(6);
                    if ( aFloat == -999 ) continue;
                    String statusStr = obuhvat.toString() + ";" + statusB.toString() + ";"
                            + greskaB.toString() + ";" + intB.toString();
                    Integer status = getStatus(statusB, greskaB, intB, obuhvat);
                    PodatakSirovi ps = new PodatakSirovi();
//                    Podatak p = new Podatak();

                    ps.setProgramMjerenjaId(pm);
//                    p.setProgramMjerenjaId(pm);
                    ps.setNivoValidacijeId(0);
//                    p.setNivoValidacijeId(0);

//                    p.setStatus(status);
                    ps.setStatus(status);
                    ps.setStatusString(statusStr);
                    
                    ps.setVrijeme(new Date(timestamp.getTime()));
//                    p.setVrijeme(new Date(timestamp.getTime()));

                    ps.setVrijednost(aFloat);
//                    p.setVrijednost(aFloat);

//                    p.setObuhvat(obuhvat);
                    podaci.add(ps);
//                    podatakFacade.create(p);
//                    podatakSiroviFacade.create(ps);
//                    log.log(Level.INFO,"Podatak: t={0}, pm={1}, st={2}, ststr={3}, ob={4}, val={5} ", 
//                            new Object[]{timestamp, pm.getId(),status,statusStr,obuhvat,aFloat});

                }
            }
            return podaci;
        }
    }
    private Integer getStatus(Integer statusB, Integer greskaB, Integer intB, Integer obuhvat) {
        Integer st = 0;
        if (obuhvat < 75 || intB == -1) {
            st |= 1 << OperStatus.OBUHVAT.ordinal();
        }
        
        if ( greskaB != 0 ) {
            st |= 1 << OperStatus.FAULT.ordinal();
        }
//        if ( (statusB & 8) == 8) {
//            st |= 1 << OperStatus.SPAN.ordinal();
//        }
//        if ( (statusB & 4) == 4) {
//            st |= 1 << OperStatus.ZERO.ordinal();
//        }
        if ( (statusB & 2) == 2) {
            st |= 1 << OperStatus.ODRZAVANJE.ordinal();
        }
        return st;
    }

    @Override
    public Map<String, String> opisiStatus(PodatakSirovi ps) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Collection<ZeroSpan> getZeroSpan(Connection con, ProgramMjerenja pm, Date zadnji)  throws SQLException {
        log.log(Level.FINER, "ZS: pm={0}, zadnji={1}", new Object[]{pm.getId(), zadnji});
        List<ZeroSpan> podaci = new ArrayList<>();
        Integer station = Integer.parseInt(pm.getIzvorProgramKljuceviMap().getPKljuc());
        Integer component = Integer.parseInt(pm.getIzvorProgramKljuceviMap().getKKljuc());
        if ( zadnji == null ) {
            zadnji = pm.getPocetakMjerenja();
        }
        String selektSql = "SELECT endtime, zero_value, span_value, opestatus_b, errstatus_b, intstatus_b FROM zero_span WHERE station=? AND component=? AND endtime > ?";

        try (PreparedStatement prepStmt = con.prepareStatement(selektSql)) {
            prepStmt.setInt(1, station);
            prepStmt.setInt(2, component);
            prepStmt.setTimestamp(3, new Timestamp(zadnji.getTime()));
            try (ResultSet rs = prepStmt.executeQuery()) {
                while (rs.next()) {
                    Timestamp timestamp = rs.getTimestamp(1);
                    double zero = rs.getDouble(2);
                    double span = rs.getDouble(3);
                    int status = rs.getInt(4);
                    int error = rs.getInt(5);
                    int intstatus = rs.getInt(6);
                    log.log(Level.FINER,"RS: t={0}, zero={1}, span={2}, st={3}, er={4}, in={5} ", new Object[]{timestamp, pm.getId(), zero, span, status, error, intstatus});

                    if ( status > 0 && error == 0){
                        if ( (status & 8) != 0 && span != -999){
                            ZeroSpan spanE = new ZeroSpan();
                            spanE.setVrsta("AS");
                            spanE.setProgramMjerenjaId(pm);
                            spanE.setVrijeme(new Date(timestamp.getTime()));
                            spanE.setVrijednost(span);                    
//                            log.log(Level.INFO,"Span: t={0}, pm={1}, val={2} ", new Object[]{timestamp, pm.getId(), span});
                            podaci.add(spanE);
//                            zeroSpanFacade.create(spanE);
                            
                        }
                        if ( (status & 4) != 0 && zero != -999) {
                            ZeroSpan zeroE = new ZeroSpan();
                            zeroE.setVrsta("AZ");
                            zeroE.setProgramMjerenjaId(pm);
                            zeroE.setVrijeme(new Date(timestamp.getTime()));
                            zeroE.setVrijednost(zero);
//                            zeroSpanFacade.create(zeroE);
//                            log.log(Level.INFO,"Zero: t={0}, pm={1}, val={2} ", new Object[]{timestamp, pm.getId(), zero});
                            podaci.add(zeroE);

                        }
                    }
                }
            }
            return podaci;
        } 
    }

}
