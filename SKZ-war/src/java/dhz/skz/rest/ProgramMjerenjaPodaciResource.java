/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rest;

import dhz.skz.aqdb.entity.IzvorProgramKljuceviMap;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.facades.IzvorProgramKljuceviMapFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * REST Web Service
 *
 * @author kraljevic
 */
@Stateless
@LocalBean
@Path("dhz.skz.rs.programmjerenja")
public class ProgramMjerenjaPodaciResource {

    @EJB
    private IzvorProgramKljuceviMapFacade izvorProgramKljuceviMapFacade;
    
    @Context
    private UriInfo context;
    
    

    /**
     * Creates a new instance of ProgramMjerenjaPodaciResource
     */
    public ProgramMjerenjaPodaciResource() {
    }

    /**
     * Retrieves representation of an instance of dhz.skz.rest.ProgramMjerenjaPodaciResource
     * @return an instance of java.lang.String
     */
    @GET
    @Path("podaci/{program}")
    @Produces({"application/xml", "application/json"})
    public IzvorProgramKljuceviMap getProgramPodaci(@PathParam("program") Integer programId) {
        return izvorProgramKljuceviMapFacade.find(programId);
    }
}
