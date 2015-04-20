/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rs;

import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacadeRemote;
import dhz.skz.citaci.CitacMainRemote;
import dhz.skz.rs.dto.PodatakDTO;
import dhz.skz.rs.dto.PodatakSiroviDTO;
import dhz.skz.rs.dto.StatusDTO;
import dhz.skz.rs.util.DateParam;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author kraljevic
 */
@LocalBean
@Stateless
@Path("dhz.skz.rs.sirovipodaci")
public class SiroviPodaci {

    public static final Logger log = Logger.getLogger(SiroviPodaci.class.getName());
    @EJB
    ProgramMjerenjaFacadeRemote programMjerenjaFacade;
    @EJB
    CitacMainRemote citacMainBean;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of SiroviPodaci
     */
    public SiroviPodaci() {
    }

    /**
     * Retrieves representation of an instance of dhz.skz.rs.SiroviPodaci
     *
     * @param programId
     * @param datum
     * @return an instance of test.dto.PodatakSiroviDTO
     */
    @GET
    @Path("{program}/{datum}")
    @Produces("application/json")
    public List<PodatakSiroviDTO> getPodaci(@PathParam("program") Integer programId, @PathParam("datum") DateParam datum) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        Logger.getLogger(SiroviPodaci.class.getName()).log(Level.INFO, datum.getDate().toString());
        cal.setTime(datum.getDate());
        Logger.getLogger(SiroviPodaci.class.getName()).log(Level.INFO, "XXXX::::::{0}::{1}", new Object[]{cal.getTime().toString(), cal.toString()});
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date pocetak = cal.getTime();
        cal.add(Calendar.DATE, 1);
        Date kraj = cal.getTime();
        Logger.getLogger(SiroviPodaci.class.getName()).log(Level.INFO, "{0} -- {1}", new Object[]{pocetak.toString(), kraj.toString()});
        List<PodatakSiroviDTO> lista = new ArrayList<>();
        ProgramMjerenja program = programMjerenjaFacade.find(programId);
        try {
            for (PodatakSirovi ps : citacMainBean.dohvatiSirove(program, pocetak, kraj, false, true)) {
                PodatakSiroviDTO p = new PodatakSiroviDTO();
                p.setId(ps.getId());
                p.setStatusString(ps.getStatusString());
                p.setVrijeme(ps.getVrijeme().getTime());
                p.setVrijednost(ps.getVrijednost());
                p.setValjan(Boolean.TRUE);
                lista.add(p);
            }

        } catch (NamingException ex) {
            Logger.getLogger(SiroviPodaci.class.getName()).log(Level.SEVERE, null, ex);
            throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Naming exception: " + ex.getMessage())
                    .build());
        }
        return lista;

    }
    
    enum Test{
        NULA, JEDAN, DVA
    }
    @GET
    @Path("statusi")
    @Produces("application/json")
    public List<StatusDTO> getStatusiMapiranje() {
        List<StatusDTO> lista = new ArrayList<>();
        for ( Test t : Test.values()){
            int i = t.ordinal();
            String s = t.toString();
            lista.add(new StatusDTO(i,s));
        }
        return lista;
    }
    

    /**
     * PUT method for updating or creating an instance of SiroviPodaci
     *
     * @param podaci
     */
    @PUT
    @Consumes("application/json")
    @Path("{program}")
    public void putPodaci(@PathParam("program") Integer programId, List<PodatakSiroviDTO> podaci) {
        for (PodatakSiroviDTO p : podaci) {
            Logger.getLogger(getClass().getName()).log(Level.INFO, "PODATAK STIGAO:{0}; {1}; {2}; {3}; {4}", new Object[]{programId, p.getVrijeme(), p.getVrijednost(), p.getValjan()});
        }
    }
    
    

//    @GET
//    @Path("zadnji_podatak/{izvor}/{postaja}/{vrsta}")
//    @Produces("application/json")
//    public long getZadnjiPodatak(@PathParam("izvor") String izvorS, @PathParam("izvor") String postajaS, @PathParam("izvor") String vrsta) {
//        log.log(Level.INFO, "Poceo getUnixTimeZadnjeg: {0}, {1}, {2}, {3} " , new Object[]{postajaS, vrsta });
//        IzvorPodataka izvor = izvorPodatakaFacade.findByName(izvorS);
//        Postaja postaja = postajaFacade.findByNacionalnaOznaka(postajaS);
//
//        try {
//            InitialContext ctx = new InitialContext();
//            String str = "java:module/";
//
//            String naziv = str + izvor.getBean().trim();
//            log.log(Level.FINE, "Bean: {0}", naziv);
//            CsvParser parser = (CsvParser) ctx.lookup(naziv);
//            log.log(Level.INFO, "Zavrsio getUnixTimeZadnjeg: {0}, {1}, {2}, {3} " , new Object[]{postajaS, vrsta });
//            return parser.getVrijemeZadnjegPodatka(izvor, postaja, vrsta).getTime();
//        } catch (NamingException ex) {
//            log.log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
    
//    @PUT
//    public void prihvatiOmotnicu(@WebParam(name = "omotnica") CsvOmotnica omotnica) {
//        log.log(Level.INFO, "Poceo  prihvatiOmotnicu : {0}, {1}, {2}, {3} ", new Object[]{omotnica.getIzvor(), omotnica.getPostaja(), omotnica.getDatoteka(), omotnica.getVrsta()});
//        IzvorPodataka izvor = izvorPodatakaFacade.findByName(omotnica.getIzvor());
//        try {
//            InitialContext ctx = new InitialContext();
//            String str = "java:module/";
//
//            String naziv = str + izvor.getBean().trim();
//            log.log(Level.FINE, "Bean: {0}", naziv);
//            CsvParser parser = (CsvParser) ctx.lookup(naziv);
//            parser.obradi(omotnica);
//
//        } catch (NamingException ex) {
//            log.log(Level.SEVERE, null, ex);
//        }
//
//        log.log(Level.INFO, "Zavrsio prihvatiOmotnicu: {0}, {1}, {2}, {3} ", new Object[]{omotnica.getIzvor(), omotnica.getPostaja(), omotnica.getDatoteka(), omotnica.getVrsta()});
//    }

}
