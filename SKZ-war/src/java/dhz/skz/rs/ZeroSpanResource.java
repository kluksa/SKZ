/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rs;

import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ZeroSpanReferentneVrijednosti;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacadeRemote;
import dhz.skz.aqdb.facades.ZeroSpanReferentneVrijednostiFacadeRemote;
import dhz.skz.rs.facades.ZeroSpanBean;
import dhz.skz.rs.dto.ZeroSpanDTO;
import dhz.skz.rs.util.DateParam;
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
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author kraljevic
 */
//@LocalBean
@Stateless
@Path("dhz.skz.rs.zerospan")
//@javax.enterprise.context.RequestScoped
public class ZeroSpanResource {

    @EJB
    private ZeroSpanReferentneVrijednostiFacadeRemote zeroSpanReferentneVrijednostiFacade;
    @EJB
    private ZeroSpanBean zsb;
    @EJB
    private ProgramMjerenjaFacadeRemote programMjerenjaFacade;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    public ZeroSpanResource() {
    }

    /**
     * Retrieves representation of an instance of dhz.skz.rs.GenericResource
     *
     * @param programId
     * @param datum
     * @param broj_dana
     * @return an instance of java.lang.String
     */
    @GET
    @Path("{program}/{datum}")
    @Compress
    @Produces(MediaType.APPLICATION_JSON)
    public List<ZeroSpanDTO> getZeroSpanLista(@PathParam("program") Integer programId, @PathParam("datum") DateParam datum,
            @DefaultValue("30") @QueryParam("broj_dana") Integer broj_dana) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC+1"));
        cal.setTime(datum.getDate());
        Date kraj = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DATE, -broj_dana);
        Date pocetak = cal.getTime();

        Logger.getLogger(ZeroSpanResource.class.getName()).log(Level.SEVERE, "{0} -- {1}", new Object[]{pocetak.toString(), kraj.toString()});
        ProgramMjerenja program = programMjerenjaFacade.find(programId);
        List<ZeroSpanDTO> zeroSpan = zsb.getZeroSpan(program, pocetak, kraj);
        return zeroSpan;
    }

    /**
     * PUT method for updating or creating an instance of GenericResource
     *
     * @param programId
     * @param zs
     */
    @PUT
    @Path("{program}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void putZeroSpanReferentnuVrijednost(@PathParam("program") Integer programId, ZeroSpanDTO zs) {
        ProgramMjerenja program = programMjerenjaFacade.find(programId);
        if (program != null) {
            ZeroSpanReferentneVrijednosti zsr = new ZeroSpanReferentneVrijednosti();
            zsr.setProgramMjerenjaId(program);
            zsr.setPocetakPrimjene(new Date(1000*zs.getVrijeme()));
            zsr.setVrsta(zs.getVrsta().toString());
            zsr.setVrijednost(zs.getVrijednost());
            zeroSpanReferentneVrijednostiFacade.create(zsr);
        }
        
    }
}
