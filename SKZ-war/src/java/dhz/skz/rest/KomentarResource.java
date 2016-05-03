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
    @Path("{program}/{datum}")
    @Produces({"application/xml", "application/json"})
    public KomentarDTO getXml(@PathParam("id") Integer id) {
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

    /**
     * PUT method for updating or creating an instance of KomentarResource
     * @param programId
     * @param pocetak
     * @param kraj
     * @param content representation for the resource
     */
    @PUT
    @Path("{program}/{pocetak}/{kraj}")
    @Consumes({"application/xml", "application/json"})
    public void putXml(@PathParam("program") Integer programId, @PathParam("pocetak") DateTimeParam pocetak,
            @PathParam("kraj") DateTimeParam kraj, String content) {
        Komentar k = new Komentar();
        k.setPocetak(pocetak.getDate());
        k.setKraj(kraj.getDate());
        k.setProgramMjerenjaId(programFacade.find(programId));
        k.setTekst(content);
        komentarFacade.create(k);
    }
}
