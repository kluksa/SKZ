/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.umjeravanje;

import dhz.skz.umjeravanje.dto.Dilucija;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author kraljevic
 */
@Path("/dilucijska_jedinica")
public class DilucijskaJedinicasResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of DilucijskaJedinicasResource
     */
    public DilucijskaJedinicasResource() {
    }

    /**
     * Retrieves representation of an instance of dhz.skz.umjeravanje.DilucijskaJedinicasResource
     * @return an instance of java.util.List<dhz.skz.umjeravanje.dto.Dilucija>
     */
    @GET
    @Produces("application/xml")
    public java.util.List<dhz.skz.umjeravanje.dto.Dilucija> getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * POST method for creating an instance of DilucijskaJedinicaResource
     * @param content representation for the new resource
     * @return an HTTP response with content of the created resource
     */
    @POST
    @Consumes("application/xml")
    @Produces("application/xml")
    public Response postXml(Dilucija content) {
        //TODO
        return Response.created(context.getAbsolutePath()).build();
    }

    /**
     * Sub-resource locator method for {id}
     */
    @Path("{id}")
    public DilucijskaJedinicaResource getDilucijskaJedinicaResource(@PathParam("id") String id) {
        return DilucijskaJedinicaResource.getInstance(id);
    }
}
