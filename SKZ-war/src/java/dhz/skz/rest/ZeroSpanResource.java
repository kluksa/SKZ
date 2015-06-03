/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rest;

import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ZeroSpan;
import dhz.skz.aqdb.entity.ZeroSpanReferentneVrijednosti;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacade;
import dhz.skz.aqdb.facades.ZeroSpanFacade;
import dhz.skz.aqdb.facades.ZeroSpanReferentneVrijednostiFacade;
import dhz.skz.rest.dto.ZeroSpanDTO;
import dhz.skz.rest.util.DateParam;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
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
    private ZeroSpanReferentneVrijednostiFacade zeroSpanReferentneVrijednostiFacade;
    @EJB
    private ProgramMjerenjaFacade programMjerenjaFacade;
    @EJB
    private ZeroSpanFacade zeroSpanFacade;

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
        List<ZeroSpanDTO> zeroSpan = getZeroSpan(program, pocetak, kraj);
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
    
    private List<ZeroSpanDTO> getZeroSpan(ProgramMjerenja pm, Date vrijemeOd, Date vrijemeDo) {
            
        List<ZeroSpan> zeroSpan = zeroSpanFacade.getZeroSpan(pm, vrijemeOd, vrijemeDo);
        List<ZeroSpanReferentneVrijednosti> refZS = zeroSpanReferentneVrijednostiFacade.findZadnjiPrije(pm, vrijemeDo);
        
        NavigableMap<Date, ZeroSpanReferentneVrijednosti> refZ = new TreeMap<>();
        NavigableMap<Date, ZeroSpanReferentneVrijednosti> refS = new TreeMap<>();
        NavigableMap<Date, ZeroSpan> zero = new TreeMap<>();
        
        Double deltaz = pm.getMetodaId().getZeroDriftAbsolut();
        Double deltas = pm.getMetodaId().getSpanDriftRelativ();
        
        for (ZeroSpanReferentneVrijednosti rzs : refZS){
            if (rzs.getVrsta().contains("Z")) {
                refZ.put(rzs.getPocetakPrimjene(), rzs);
            } else if ( rzs.getVrsta().contains("S")) {
                refS.put(rzs.getPocetakPrimjene(), rzs);
            } 
        }
        
        for (ZeroSpan zs : zeroSpan) {
            if ( zs.getVrsta().contains("Z")) {
                zero.put(zs.getVrijeme(), zs);
            } 
        }
        
        List<ZeroSpanDTO> zsl = new ArrayList<>();
        
        for (ZeroSpan zs : zeroSpan) {
            ZeroSpanDTO  zz = new ZeroSpanDTO();
            zz.setVrijeme(zs.getVrijeme().getTime());
            zz.setVrijednost(zs.getVrijednost());
            
            if ( zs.getVrsta().contains("Z")) {
                zz.setVrsta('Z');
                if ( refZ.floorEntry(zs.getVrijeme()) != null ) {
                    zz.setMinDozvoljeno(refZ.floorEntry(zs.getVrijeme()).getValue().getVrijednost()-deltaz);
                    zz.setMaxDozvoljeno(refZ.floorEntry(zs.getVrijeme()).getValue().getVrijednost()+deltaz);
                }
            } else if ( zs.getVrsta().contains("S")){
                zz.setVrsta('S');
                
                Map.Entry<Date, ZeroSpan> najbliziZero = zero.floorEntry(new Date(zs.getVrijeme().getTime()+3600*1000));
                
                if ( najbliziZero!=null && refZ.floorEntry(najbliziZero.getKey())!= null && refS.floorEntry(zs.getVrijeme())!=null){
                    Double dz =  najbliziZero.getValue().getVrijednost() - refZ.floorEntry(najbliziZero.getKey()).getValue().getVrijednost();
                    zz.setMinDozvoljeno((1-deltas)*refS.floorEntry(zs.getVrijeme()).getValue().getVrijednost()+dz);
                    zz.setMaxDozvoljeno((1+deltas)*refS.floorEntry(zs.getVrijeme()).getValue().getVrijednost()+dz);
                }
            }
            
            zsl.add(zz);
        }
        return zsl;
    }
}
