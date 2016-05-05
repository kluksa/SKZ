/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rest;

import dhz.skz.aqdb.entity.Komentar;
import dhz.skz.aqdb.facades.KomentarFacade;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacade;
import dhz.skz.rest.dto.KomentarDTO;
import dhz.skz.rest.util.DateTimeParam;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author kraljevic
 */
@Path("dhz.skz.rs.komentar")
@Stateless
public class KomentarResource {

    @Context
    private UriInfo context;

    @EJB
    private KomentarFacade komentarFacade;
    @EJB
    private ProgramMjerenjaFacade programFacade;
    /**
     * Creates a new instance of KomentarResource
     */
    public KomentarResource() {
    }

    /**
     * Retrieves representation of an instance of dhz.skz.rest.KomentarResource
     * @param id
     * @return an instance of java.lang.String
     */
    @GET
    @Produces({"application/xml", "application/json"})
    public Collection<KomentarDTO> getXml() {
        ArrayList<KomentarDTO> lista = new ArrayList<>();
        for ( Komentar k : komentarFacade.findAll()) {
            lista.add(new KomentarDTO(k));
        }
        return lista;
        
    }
    /**
     * Retrieves representation of an instance of dhz.skz.rest.KomentarResource
     * @param id
     * @return an instance of java.lang.String
     */
    @GET
    @Path("id/{id}")
    @Produces({"application/xml", "application/json"})
    public KomentarDTO getById(@PathParam("id") Integer id) {
        return new KomentarDTO(komentarFacade.find(id));
        
    }

    @GET
    @Path("{program}/{pocetak}/{kraj}")
    @Produces({"application/xml", "application/json"})
    public Collection<KomentarDTO> getXml(@PathParam("program") Integer programId, @PathParam("pocetak") DateTimeParam pocetak,
            @PathParam("kraj") DateTimeParam kraj) {
        ArrayList<KomentarDTO> lista = new ArrayList<>();
        for ( Komentar k : komentarFacade.find(programFacade.find(programId), pocetak.getDate(), kraj.getDate())) {
            lista.add(new KomentarDTO(k));
        }
        return lista;
    }

    @GET
    @Path("{program}")
    @Produces({"application/xml", "application/json"})
    public Collection<KomentarDTO> getByProgram(@PathParam("program") Integer programId) {
        ArrayList<KomentarDTO> lista = new ArrayList<>();
        for ( Komentar k : komentarFacade.find(programFacade.find(programId))) {
            lista.add(new KomentarDTO(k));
        }
        return lista;
    }

    
    /**
     * PUT method for updating or creating an instance of KomentarResource
     * @param kdto
     */
    @PUT
    @Consumes({"application/xml", "application/json"})
    public void putXml(KomentarDTO kdto) {
        Komentar k = new Komentar();
        k.setPocetak(new Date(kdto.getPocetak()));
        k.setKraj(new Date(kdto.getKraj()));
        k.setProgramMjerenjaId(programFacade.find(kdto.getProgramMjerenjaId()));
        k.setTekst(kdto.getTekst());
        komentarFacade.create(k);
    }
}
