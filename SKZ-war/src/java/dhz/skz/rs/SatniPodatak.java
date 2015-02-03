/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rs;

import dhz.skz.rs.dto.PodatakDTO;
import dhz.skz.rs.util.DateTimeParam;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

/**
 * REST Web Service
 *
 * @author kraljevic
 */
@Path("dhz.skz.rs.satnipodaci")
public class SatniPodatak {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of SatniPodatak
     */
    public SatniPodatak() {
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<PodatakDTO> findAll() {
        return new ArrayList<>();
    }
    /**
     * Retrieves representation of an instance of dhz.skz.rs.SatniPodatak
     * @param programId
     * @param pocetak
     * @param kraj
     * @return an instance of dhz.skz.rs.dto.PodatakDTO
     */
    @GET
    @Path("{program}/{pocetak}/{kraj}")
    @Produces({"application/xml", "application/json"})
    public List<PodatakDTO> getSatniValjani(@PathParam("program") Integer programId,  
            @PathParam("pocetak") DateTimeParam pocetak,
            @PathParam("kraj") DateTimeParam kraj) {
        //TODO return proper representation object
        List<PodatakDTO> lista = new ArrayList<>();
        return lista;
//        throw new UnsupportedOperationException();
    }

}
