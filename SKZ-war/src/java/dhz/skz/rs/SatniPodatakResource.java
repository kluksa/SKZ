/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rs;

import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacadeRemote;
import dhz.skz.beans.PodatakFacade;
import dhz.skz.rs.dto.PodatakDTO;
import dhz.skz.rs.util.DateTimeParam;
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
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
public class SatniPodatakResource {

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

    /**
     * Retrieves representation of an instance of
     * dhz.skz.rs.SatniPodatakResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Path("{program}/{datum}")
    @Produces("application/json")
    public List<PodatakDTO> getPodaci(@PathParam("program") Integer programId, @PathParam("datum") DateTimeParam datum,
            @DefaultValue("1") @QueryParam("broj_dana") Integer broj_dana, @DefaultValue("true") @QueryParam("samo_valjani") Boolean samo_valjani) {
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
            if (!samo_valjani || p.getObuhvat() > 75) {
                PodatakDTO po = new PodatakDTO();
                po.setProgramMjerenjaId(programId);
                po.setVrijeme(p.getVrijeme().getTime() / 1000);
                po.setVrijednost(p.getVrijednost());
                po.setObuhvat((int) p.getObuhvat());
                po.setStatus(p.getStatus());
                lista.add(po);
            }
        }
        return lista;
    }

    /**
     * PUT method for updating or creating an instance of SatniPodatakResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }

    private PodatakFacade lookupPodatakFacadeBean() {
        try {
            javax.naming.Context c = new InitialContext();
            return (PodatakFacade) c.lookup("java:global/SKZ/SKZ-war/PodatakFacade!dhz.skz.beans.PodatakFacade");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
