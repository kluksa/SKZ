/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rest;

import dhz.skz.aqdb.entity.Komponenta;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.facades.KomponentaFacade;
import dhz.skz.aqdb.facades.PodatakFacade;
import dhz.skz.aqdb.facades.PostajaFacade;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacade;
import dhz.skz.rest.dto.KomponentaDTO;
import dhz.skz.rest.dto.PostajaDTO;
import dhz.skz.rest.dto.SatniDTO;
import dhz.skz.rest.util.DateTimeParam;
import dhz.skz.util.OperStatus;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.ejb.EJB;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 * REST Web Service
 *
 * @author kraljevic
 */
@Path("drzavna_mreza")
@javax.enterprise.context.RequestScoped
public class DrzavnaMrezaResource {
    @EJB
    private PostajaFacade postajaFacade;
    @EJB
    private ProgramMjerenjaFacade programMjerenjaFacade;
    @EJB
    private PodatakFacade podatakFacade;
    @EJB
    private KomponentaFacade komponetaFacade;
    

    @Context
    private UriInfo context;

    /**
     * Retrieves representation of an instance of dhz.skz.rest.DrzavnaMrezaResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces({"application/xml", "application/json"})
    @Path("postaje")
    public List<PostajaDTO> getPostaje() {
        List<PostajaDTO> postaje = new ArrayList<>();
        for ( Postaja p : postajaFacade.findAll()){
            postaje.add(new PostajaDTO(p.getId(), p.getNazivPostaje(), p.getOznakaPostaje(), p.getGeogrDuzina(), p.getGeogrSirina(), p.getNadmorskaVisina()));
        }
        return postaje;
    }
    
    @GET
    @Produces({"application/xml", "application/json"})
    @Path("postaje/{id}/komponente")
    public List<KomponentaDTO> getKomponenteNaPostaji(@PathParam("id") Integer postajaId) {
        List<KomponentaDTO> komponente = new ArrayList<>();
        for ( Komponenta k : komponetaFacade.findByPostaja(postajaId)){
           komponente.add(new KomponentaDTO(k));
        }
        return komponente;
    }

    @GET
    @Produces({"application/xml", "application/json"})
    @Path("postaje/{id}")
    public PostajaDTO getPostaja(@PathParam("id") Integer postajaId) {
        return new PostajaDTO(postajaFacade.find(postajaId));
    }
    
    @GET
    @Produces({"application/xml", "application/json"})
    @Path("podaci/{postaja_id}/{komponenta_id}")
    public String getPodaci() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }
    
    
    
    @GET
    @Produces({"text/plain"})
    public String get(){
        return "ABCD";
    }
    
    @GET
    @Path("podaci/{postaja_id}/{komponenta_id}/{pocetak}/{kraj}")
    @Produces({"application/xml", "application/json"})
    public List<SatniDTO> getPodaci(@PathParam("postaja_id") Integer postajaId, @PathParam("komponenta_id") Integer komponentaId, 
            @PathParam("pocetak") DateTimeParam pocetakP, @PathParam("kraj") DateTimeParam krajP,
            @DefaultValue("true") @QueryParam("samo_valjani") Boolean samo_valjani,
            @DefaultValue("0") @QueryParam("nivo_validacije") Integer nivo) {
        
        Postaja postaja = postajaFacade.find(postajaId);
        Komponenta komponenta = komponetaFacade.find(komponentaId);
        List<SatniDTO> lista = new ArrayList<>();
        Collection <Podatak> podaci = podatakFacade.findByPostajaKomponentaUsporednoNivo(postaja, komponenta, (short) 0, nivo,  pocetakP.getDate(), krajP.getDate());
        for (Podatak p : podaci) {
            boolean valjan = OperStatus.isValid(p);
            if (!samo_valjani || valjan) {
                SatniDTO t=new SatniDTO();
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
      * @param datum
     * @param broj_dana
     * @param samo_valjani
     * @param nivo
     * @return an instance of java.lang.String
     */
    @GET
    @Path("podaci/{postaja_id}/{komponenta_id}/{datum}")
    @Produces({"application/xml", "application/json"})
    public List<SatniDTO> getPodaci(@PathParam("postaja_id") Integer postajaId, @PathParam("komponenta_id") Integer komponentaId, 
            @PathParam("datum") DateTimeParam datum,
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

        Postaja postaja = postajaFacade.find(postajaId);
        Komponenta komponenta = komponetaFacade.find(komponentaId);
        List<SatniDTO> lista = new ArrayList<>();
        Collection <Podatak> podaci = podatakFacade.findByPostajaKomponentaUsporednoNivo(postaja, komponenta, (short) 0, nivo,  pocetak, kraj);
        for (Podatak p : podaci) {
            boolean valjan = OperStatus.isValid(p);
            if (!samo_valjani || valjan) {
                SatniDTO t=new SatniDTO();
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
}
