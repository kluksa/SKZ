/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.umjeravanje;

import dhz.skz.umjeravanje.dto.Crm;
import java.util.ArrayList;
import java.util.List;
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
@Path("/cs")
public class CrmsResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CrmsResource
     */
    public CrmsResource() {
    }

    /**
     * Retrieves representation of an instance of dhz.skz.umjeravanje.CrmsResource
     * @return an instance of java.util.List<CrmDto>
     */
    @GET
    @Produces("application/xml")
    public java.util.List<Crm> getXml() {
        List<Crm> lista = new ArrayList<>();
        lista.add(Crm.getInstance(1));
        lista.add(Crm.getInstance(2));
        lista.add(Crm.getInstance(3));
        return lista;
    }

    /**
     * POST method for creating an instance of CrmResource
     * @param content representation for the new resource
     * @return an HTTP response with content of the created resource
     */
    @POST
    @Consumes("application/xml")
    @Produces("application/xml")
    public Response postXml(Crm content) {
        //TODO
        return Response.created(context.getAbsolutePath()).build();
    }

    /**
     * Sub-resource locator method for {id}
     */
    @Path("{id}")
    public CrmResource getCrmResource(@PathParam("id") Integer id) {
        return CrmResource.getInstance(id);
    }
}
