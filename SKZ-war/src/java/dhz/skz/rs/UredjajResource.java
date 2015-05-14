/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rs;

import dhz.skz.aqdb.entity.Komponenta;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.Uredjaj;
import dhz.skz.rs.facades.KomponentaFacade;
import dhz.skz.rs.facades.PostajaFacade;
import dhz.skz.rs.facades.UredjajFacade;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
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
//@LocalBean
@Stateless
//@javax.enterprise.context.RequestScoped
public class UredjajResource {

    private static final Logger log = Logger.getLogger(UredjajResource.class.getName());
    @EJB(name="war/KomponentaFacade")
    private KomponentaFacade komponentaFacade;
    @EJB(name="war/PostajaFacade")
    private PostajaFacade postajaFacade;
    @EJB(name="war/UredjajFacade")
    private UredjajFacade uredjajFacade;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of UredjajResource
     */
    public UredjajResource() {
    }

    /**
     *
     * @param sernum
     * @return
     */
    @GET
    @Path("{sernum}")
    @Produces({"application/xml", "application/json"})
    public Uredjaj getUredjaj(@PathParam("sernum") String sernum) {
        try {
            return uredjajFacade.findBySn(sernum);
        } catch (NoResultException ex) {
            log.log(Level.SEVERE, "Nema uredjaja sa sn: {0}", sernum);
            throw ex;
        }
    }
    
    /**
     *
     * @param sernum
     * @return
     */
    @GET
    @Path("{sernum}/lokacija/")
    @Produces({"application/xml", "application/json"})
    public Postaja getPostaja(@PathParam("sernum") String sernum) {
        return postajaFacade.findByUredjajSn(sernum);
    }

    /**
     *
     * @param sernum
     * @return
     */
    @GET
    @Path("{sernum}/komponente/")
    @Produces({"application/xml", "application/json"})
    public Collection<Komponenta> getKomponente(@PathParam("sernum") String sernum) {
        return komponentaFacade.findByUredjajSn(sernum);
    }

}
