/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.umjeravanje;

import dhz.skz.umjeravanje.dto.Dilucija;
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
public class DilucijskaJedinicaResource {

    private String id;

    /**
     * Creates a new instance of DilucijskaJedinicaResource
     */
    private DilucijskaJedinicaResource(String id) {
        this.id = id;
    }

    /**
     * Get instance of the DilucijskaJedinicaResource
     */
    public static DilucijskaJedinicaResource getInstance(String id) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of DilucijskaJedinicaResource class.
        return new DilucijskaJedinicaResource(id);
    }

    /**
     * Retrieves representation of an instance of dhz.skz.umjeravanje.DilucijskaJedinicaResource
     * @return an instance of dhz.skz.umjeravanje.dto.Dilucija
     */
    @GET
    @Produces("application/xml")
    public Dilucija getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of DilucijskaJedinicaResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(Dilucija content) {
    }

    /**
     * DELETE method for resource DilucijskaJedinicaResource
     */
    @DELETE
    public void delete() {
    }
}
