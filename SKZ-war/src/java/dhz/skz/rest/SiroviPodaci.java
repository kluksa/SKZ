/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rest;

import dhz.skz.CitaciGlavniBeanRemote;
import dhz.skz.aqdb.facades.KorisnikFacade;
import dhz.skz.aqdb.facades.PodatakSiroviFacade;
import dhz.skz.aqdb.entity.Korisnik;
import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacade;
import dhz.skz.aqdb.facades.UredjajFacade;
import dhz.skz.rest.dto.PodatakSiroviDTO;
import dhz.skz.rest.dto.StatusDTO;
import dhz.skz.rest.util.DateParam;
import dhz.skz.util.OperStatus;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.SecurityContext;

/**
 * REST Web Service
 *
 * @author kraljevic
 */
//@LocalBean
@Stateless
//@javax.enterprise.context.RequestScoped
@Path("dhz.skz.rs.sirovipodaci")
public class SiroviPodaci {
    @EJB
    private CitaciGlavniBeanRemote citaciGlavniBean;
    @EJB
    private UredjajFacade uredjajFacade;
    private static final Logger log = Logger.getLogger(SiroviPodaci.class.getName());
    @EJB
    private PodatakSiroviFacade podatakSiroviFacade;
    @EJB
    private KorisnikFacade korisnikFacade;

    @EJB
    private ProgramMjerenjaFacade programMjerenjaFacade;
    
    

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of SiroviPodaci
     */
    public SiroviPodaci() {
    }

//    
//    @GET
//    @Path("{program}/{pocetak}/{kraj}")
//    @Produces("application/json")
//    public List<PodatakSiroviDTO> getPodaci(@PathParam("program") Integer programId, @PathParam("pocetak") DateParam pocetak, @PathParam("kraj") DateParam kraj, 
//            @DefaultValue("1") @QueryParam("nivo_validacije") Integer nivoValidacije) {
//        
//        Logger.getLogger(SiroviPodaci.class.getName()).log(Level.INFO, "{0} -- {1}", new Object[]{pocetak.toString(), kraj.toString()});
//        List<PodatakSiroviDTO> lista = new ArrayList<>();
//        ProgramMjerenja program = programMjerenjaFacade.find(programId);
//        for (PodatakSirovi ps : podatakSiroviFacade.getPodaci(program, pocetak.getDate(), kraj.getDate(), 1, true, true)) {
//            PodatakSiroviDTO p = new PodatakSiroviDTO();
//            p.setId(ps.getId());
//            p.setStatusString(ps.getStatusString());
//            p.setVrijeme(ps.getVrijeme().getTime());
//            p.setVrijednost(ps.getVrijednost());
//            p.setStatusInt(ps.getStatus());
//            p.setValjan(OperStatus.isValid(ps));
//            p.setNivoValidacije(ps.getNivoValidacijeId());
//            lista.add(p);
//        }
//        return lista;
//    }
    /**
     * Retrieves representation of an instance of dhz.skz.rs.SiroviPodaci
     *
     * @param programId
     * @param datum
     * @param brojDana
     * @return an instance of test.dto.PodatakSiroviDTO
     */
    @GET
    @Path("{program}/{datum}")
    @Produces("application/json")
    public List<PodatakSiroviDTO> getPodaci(@PathParam("program") Integer programId, @PathParam("datum") DateParam datum, 
            @DefaultValue("4") @QueryParam("broj_dana") Integer brojDana) {
        
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        Logger.getLogger(SiroviPodaci.class.getName()).log(Level.INFO, datum.getDate().toString());
        cal.setTime(datum.getDate());
        Logger.getLogger(SiroviPodaci.class.getName()).log(Level.INFO, "XXXX::::::{0}::{1}", new Object[]{cal.getTime().toString(), cal.toString()});
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DATE, 1);
        Date kraj = cal.getTime();
        cal.add(Calendar.DATE, -brojDana);
        Date pocetak = cal.getTime();
        
        Logger.getLogger(SiroviPodaci.class.getName()).log(Level.INFO, "{0} -- {1}", new Object[]{pocetak.toString(), kraj.toString()});
        List<PodatakSiroviDTO> lista = new ArrayList<>();
        ProgramMjerenja program = programMjerenjaFacade.find(programId);
        for (PodatakSirovi ps : podatakSiroviFacade.getPodaci(program, pocetak, kraj, false, true)) {
            PodatakSiroviDTO p = new PodatakSiroviDTO();
            p.setId(ps.getId());
            p.setStatusString(ps.getStatusString());
            p.setVrijeme(ps.getVrijeme().getTime());
            p.setVrijednost(ps.getVrijednost());
            p.setStatusInt(ps.getStatus());
            p.setValjan(OperStatus.isValid(ps));
            p.setNivoValidacije(ps.getNivoValidacijeId());
            lista.add(p);
        }
        return lista;
    }

    @GET
    @Path("statusi")
    @Produces("application/json")
    public List<StatusDTO> getStatusiMapiranje() {
        List<StatusDTO> lista = new ArrayList<>();
        for (OperStatus t : OperStatus.values()) {
            int i = t.ordinal();
            String s = t.toString();
            lista.add(new StatusDTO(i, s));
        }
        return lista;
    }
    
    @GET
    @Path("opis_statusa/{podatak_id}/{n_status_string}")
    @Produces("application/json")
    public Map<String,String> getStatusiMapiranje(@PathParam("podatak_id") Integer podatakId, @PathParam("n_status_string") String nStatus) throws NamingException {
        Map<String,String> mapa = new HashMap<>();
        OperStatus valueOf = OperStatus.valueOf(nStatus.toUpperCase().trim());
        PodatakSirovi ps = podatakSiroviFacade.find(podatakId);
        switch ( valueOf){
            case FAULT:
                mapa.putAll(citaciGlavniBean.opisiStatus(ps));
                break;
            case KONTROLA:
                if ( ps != null && ps.getKorisnikId() != null ) {
                    Korisnik korisnik = ps.getKorisnikId();
                    mapa.put("mjeritelj:", korisnik.getKorisnickoIme());
                    mapa.put("ime:", korisnik.getIme());
                    mapa.put("prezime:", korisnik.getPrezime());
                }
                break;
            default:
        }
        return mapa;
    }
    
    /**
     * PUT method for updating or creating an instance of SiroviPodaci
     *
     * @param programId
     * @param datum
     * @param sc
     * @param podaci
     */
    @PUT
    @Consumes("application/json")
    @Path("{program}/{datum}")
    public void putPodaci(@PathParam("program") Integer programId, @PathParam("datum") DateParam datum, @Context SecurityContext sc, List<PodatakSiroviDTO> podaci) {
        Korisnik user = korisnikFacade.findByIme(sc.getUserPrincipal().getName());
        log.log(Level.INFO, "Stizu podaci. program= {0}, dan={1}, korisnik={2}", new Object[]{programId, datum.getDate(), user.getKorisnickoIme()});
        for (PodatakSiroviDTO p : podaci) {
            Boolean valjan = p.isValjan();
            PodatakSirovi ps = podatakSiroviFacade.find(p.getId());
            int st = ps.getStatus();
            st &= ~ (1 << OperStatus.KONTROLA.ordinal());                       // prvo reset bita
            st |= ((p.isValjan() ? 0 : 1) << OperStatus.KONTROLA.ordinal());    // 
            ps.setStatus(st);
            ps.setKorisnikId(user);
            ps.setVrijemeUpisa(new Date());
            ps.setNivoValidacijeId(1);
            podatakSiroviFacade.edit(ps);
        }
    }
}
