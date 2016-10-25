/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.umjeravanje;

import dhz.skz.umjeravanje.dto.Crm;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;

/**
 * REST Web Service
 *
 * @author kraljevic
 */
public class CrmResource {

    private Integer id;

    /**
     * Creates a new instance of CrmResource
     */
    private CrmResource(Integer id) {
        this.id = id;
    }

    /**
     * Get instance of the CrmResource
     */
    public static CrmResource getInstance(Integer id) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of CrmResource class.
        return new CrmResource(id);
    }

    /**
     * Retrieves representation of an instance of dhz.skz.umjeravanje.CrmResource
     * @return an instance of dhz.skz.umjeravanje.dto.Crm
     */
    @GET
    @Produces("application/xml")
    public Crm getXml() {
        return Crm.getInstance(id);
    }

    /**
     * PUT method for updating or creating an instance of CrmResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(Crm content) {
    }

    /**
     * DELETE method for resource CrmResource
     */
    @DELETE
    public void delete() {
    }
}
