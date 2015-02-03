/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rs;

import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ZeroSpan;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacadeRemote;
import dhz.skz.aqdb.facades.ZeroSpanFacadeRemote;
import dhz.skz.rs.dto.ZeroSpanDTO;
import dhz.skz.rs.util.DateParam;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;

/**
 * REST Web Service
 *
 * @author kraljevic
 */
@Path("dhz.skz.rs.zerospan")
public class ZeroSpanResource {
    ZeroSpanFacadeRemote zeroSpanFacadeB = lookupZeroSpanFacadeBRemote();
    ProgramMjerenjaFacadeRemote programMjerenjaFacade = lookupProgramMjerenjaFacadeRemote();
    

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    public ZeroSpanResource() {
    }

    /**
     * Retrieves representation of an instance of dhz.skz.rs.GenericResource
     * @param programId
     * @param datum
     * @return an instance of java.lang.String
     */
    @GET
    @Path("{program}/{datum}")
    @Produces("application/json")
    public List<ZeroSpanDTO> getJson(@PathParam("program") Integer programId, @PathParam("datum") DateParam datum) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC+1"));
        cal.setTime(datum.getDate());
        Date kraj = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DATE, -30);
        Date pocetak = cal.getTime();

        Logger.getLogger(ZeroSpanResource.class.getName()).log(Level.SEVERE, "{0} -- {1}", new Object[]{pocetak.toString(), kraj.toString()});
        List<ZeroSpanDTO> lista = new ArrayList<>();
        ProgramMjerenja program = programMjerenjaFacade.find(programId);
        for (ZeroSpan ps : zeroSpanFacadeB.getZeroSpan(program, pocetak, kraj)) {
            ZeroSpanDTO p = new ZeroSpanDTO();
            p.setVrijeme(ps.getVrijeme().getTime());
            p.setVrijednost(ps.getVrijednost());
            p.setVrsta(ps.getVrsta().contains("Z") ? 'Z' : 'S');
            lista.add(p);
       }
        return lista;
    }

    /**
     * PUT method for updating or creating an instance of GenericResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
    }

    private ProgramMjerenjaFacadeRemote lookupProgramMjerenjaFacadeRemote() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ProgramMjerenjaFacadeRemote) c.lookup("java:global/SKZ/SKZ-ejb/ProgramMjerenjaFacade!dhz.skz.aqdb.facades.ProgramMjerenjaFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private ZeroSpanFacadeRemote lookupZeroSpanFacadeBRemote() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ZeroSpanFacadeRemote) c.lookup("java:global/SKZ/SKZ-ejb/ZeroSpanFacadeB!dhz.skz.aqdb.facades.ZeroSpanFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}
