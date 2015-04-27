/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rs;

import dhz.skz.aqdb.entity.Komponenta;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.Uredjaj;
import dhz.skz.facades.KomponentaFacade;
import dhz.skz.facades.PostajaFacade;
import dhz.skz.facades.UredjajFacade;
import java.util.Collection;
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
@Path("uredjaj")
@LocalBean
@Stateless
public class UredjajResource {

    @EJB
    private KomponentaFacade komponentaFacade;
    @EJB
    private PostajaFacade postajaFacade;
    @EJB
    private UredjajFacade uredjajFacade;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of UredjajResource
     */
    public UredjajResource() {
    }

    @GET
    @Path("{sernum}")
    @Produces({"application/xml", "application/json"})
    public Uredjaj getUredjaj(@PathParam("sernum") String sernum) {
        return uredjajFacade.findBySn(sernum);
    }
    
    @GET
    @Path("{sernum}/lokacija/")
    @Produces({"application/xml", "application/json"})
    public Postaja getPostaja(@PathParam("sernum") String sernum) {
        return postajaFacade.findByUredjajSn(sernum);
    }

    @GET
    @Path("{sernum}/komponente/")
    @Produces({"application/xml", "application/json"})
    public Collection<Komponenta> getKomponente(@PathParam("sernum") String sernum) {
        return komponentaFacade.findByUredjajSn(sernum);
    }

}
