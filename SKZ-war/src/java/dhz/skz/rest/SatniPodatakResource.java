/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rest;

import dhz.skz.aqdb.facades.PodatakFacade;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacade;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.rest.dto.SatniDTO;
import dhz.skz.rest.util.DateTimeParam;
import dhz.skz.util.OperStatus;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

/**
 * REST Web Service
 *
 * @author kraljevic
 */
@Path("dhz.skz.rs.satnipodatak")
@LocalBean
@Stateless
//@javax.enterprise.context.RequestScoped
public class SatniPodatakResource {
    private static final Logger log = Logger.getLogger(SatniPodatakResource.class.getName());
    @EJB
    private PodatakFacade podatakFacade;
    @EJB
    private ProgramMjerenjaFacade programMjerenjaFacade;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of SatniPodatakResource
     */
    public SatniPodatakResource() {
    }
    
    
    
    @GET
    @Path("{program}/{pocetak}/{kraj}")
    @Produces("application/json")
    public List<SatniDTO> getPodaci(@PathParam("program") Integer programId, @PathParam("pocetak") DateTimeParam pocetakP, @PathParam("kraj") DateTimeParam krajP,
            @DefaultValue("true") @QueryParam("samo_valjani") Boolean samo_valjani,
            @DefaultValue("0") @QueryParam("nivo_validacije") Integer nivo) {
        ProgramMjerenja program = programMjerenjaFacade.find(programId);
        List<SatniDTO> lista = new ArrayList<>();
        List<Podatak> podaci = podatakFacade.getPodatak(program, pocetakP.getDate(), krajP.getDate(), nivo, true, true);
        for (Podatak p : podaci) {
            boolean valjan = OperStatus.isValid(p);
            if (!samo_valjani || valjan) {
                SatniDTO t=new SatniDTO();
                t.setProgramMjerenjaId(programId);
                t.setVrijeme(p.getVrijeme().getTime() / 1000);
                t.setVrijednost(p.getVrijednost());
                t.setObuhvat((int) p.getObuhvat());
                t.setStatus(p.getStatus());
                t.setValjan(valjan);
                lista.add(t);
            }
        }
        return lista;
    }


    /**
     * Retrieves representation of an instance of
     * dhz.skz.rs.SatniPodatakResource
     *
     * @param programId
     * @param datum
     * @param broj_dana
     * @param samo_valjani
     * @param nivo
     * @return an instance of java.lang.String
     */
    @GET
    @Path("{program}/{datum}")
    @Produces("application/json")
    public List<SatniDTO> getPodaci(@PathParam("program") Integer programId, @PathParam("datum") DateTimeParam datum,
            @DefaultValue("1") @QueryParam("broj_dana") Integer broj_dana, 
            @DefaultValue("true") @QueryParam("samo_valjani") Boolean samo_valjani,
            @DefaultValue("0") @QueryParam("nivo_validacije") Integer nivo) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC+1"));
        cal.setTime(datum.getDate());
//        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date kraj = cal.getTime();
        cal.add(Calendar.DATE, -broj_dana);
        Date pocetak = cal.getTime();
        log.log(Level.INFO, "Od {0} do {1}", new Object[]{pocetak, kraj});

        ProgramMjerenja program = programMjerenjaFacade.find(programId);
        List<SatniDTO> lista = new ArrayList<>();
        for (Podatak p : podatakFacade.getPodatak(program, pocetak, kraj, nivo, true, true)) {

            boolean valjan = OperStatus.isValid(p);
            log.log(Level.INFO, "P: {0},{1}", new Object[]{p.getVrijeme(), valjan});
            
            if (!samo_valjani || valjan) {
                SatniDTO po = new SatniDTO();
                po.setProgramMjerenjaId(programId);
                po.setVrijeme(p.getVrijeme().getTime() / 1000);
                po.setVrijednost(p.getVrijednost());
                po.setObuhvat((int) p.getObuhvat());
                po.setStatus(p.getStatus());
                po.setValjan(valjan);
                lista.add(po);
                log.log(Level.INFO, "PP: {0},{1}", new Object[]{p.getVrijeme(), valjan});
            }
        }
        log.log(Level.INFO, "SVE OK");
        return lista;
    }
    
}
