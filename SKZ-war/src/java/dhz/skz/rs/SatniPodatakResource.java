/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rs;

import dhz.skz.aqdb.entity.NivoValidacije;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacadeRemote;
import dhz.skz.rs.facades.PodatakFacade;
import dhz.skz.rs.dto.PodatakDTO;
import dhz.skz.rs.util.DateTimeParam;
import dhz.skz.util.OperStatus;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
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
//@LocalBean
@Stateless
//@javax.enterprise.context.RequestScoped
public class SatniPodatakResource {
    private static final Logger log = Logger.getLogger(SatniPodatakResource.class.getName());
    @EJB
    PodatakFacade podatakFacade;
    @EJB
    ProgramMjerenjaFacadeRemote programMjerenjaFacade;

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
    public List<PodatakDTO> getPodaci(@PathParam("program") Integer programId, @PathParam("pocetak") DateTimeParam pocetakP, @PathParam("kraj") DateTimeParam krajP,
            @DefaultValue("true") @QueryParam("samo_valjani") Boolean samo_valjani,
            @DefaultValue("0") @QueryParam("nivo_validacije") Integer nivo) {
        //TODO return proper representation object

        ProgramMjerenja program = programMjerenjaFacade.find(programId);
        List<PodatakDTO> lista = new ArrayList<>();
        List<Podatak> podaci = podatakFacade.getPodatak(program, pocetakP.getDate(), krajP.getDate());
        for (Podatak p : podaci) {
            boolean valjan = OperStatus.isValidSatni(p.getStatus(), new NivoValidacije(nivo));
            if (!samo_valjani || valjan) {
                PodatakDTO po = new PodatakDTO();
                po.setProgramMjerenjaId(programId);
                po.setVrijeme(p.getVrijeme().getTime() / 1000);
                po.setVrijednost(p.getVrijednost());
                po.setObuhvat((int) p.getObuhvat());
                po.setStatus(p.getStatus());
                po.setValjan(valjan);
                lista.add(po);
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
    public List<PodatakDTO> getPodaci(@PathParam("program") Integer programId, @PathParam("datum") DateTimeParam datum,
            @DefaultValue("1") @QueryParam("broj_dana") Integer broj_dana, 
            @DefaultValue("true") @QueryParam("samo_valjani") Boolean samo_valjani,
            @DefaultValue("0") @QueryParam("nivo_validacije") Integer nivo) {
        //TODO return proper representation object
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC+1"));
        cal.setTime(datum.getDate());
//        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date kraj = cal.getTime();
        cal.add(Calendar.DATE, -broj_dana);
        Date pocetak = cal.getTime();

        ProgramMjerenja program = programMjerenjaFacade.find(programId);
        List<PodatakDTO> lista = new ArrayList<>();
        for (Podatak p : podatakFacade.getPodatak(program, pocetak, kraj)) {
            boolean valjan = OperStatus.isValidSatni(p.getStatus(), new NivoValidacije(nivo));
            if (!samo_valjani || valjan) {
                PodatakDTO po = new PodatakDTO();
                po.setProgramMjerenjaId(programId);
                po.setVrijeme(p.getVrijeme().getTime() / 1000);
                po.setVrijednost(p.getVrijednost());
                po.setObuhvat((int) p.getObuhvat());
                po.setStatus(p.getStatus());
                po.setValjan(valjan);
                lista.add(po);
            }
        }
        return lista;
    }
}
