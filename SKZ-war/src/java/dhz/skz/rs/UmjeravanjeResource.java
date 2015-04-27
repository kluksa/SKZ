/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rs;

import dhz.skz.aqdb.entity.IspitneVelicine;
import dhz.skz.aqdb.entity.Komponenta;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.Umjeravanje;
import dhz.skz.aqdb.entity.UmjerneTocke;
import dhz.skz.aqdb.entity.Uredjaj;
import dhz.skz.facades.IspitneVelicineFacade;
import dhz.skz.facades.KomponentaFacade;
import dhz.skz.facades.PostajaFacade;
import dhz.skz.facades.UmjeravanjeFacade;
import dhz.skz.facades.UmjerneTockeFacade;
import dhz.skz.facades.UredjajFacade;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.PathParam;

/**
 * REST Web Service
 *
 * @author kraljevic
 */
@Path("umjeravanje")
@LocalBean
@Stateless
public class UmjeravanjeResource {
    @EJB
    private UmjerneTockeFacade umjerneTockeFacade;
    @EJB
    private UmjeravanjeFacade umjeravanjeFacade;
    @EJB
    IspitneVelicineFacade ispitneVelicineFacade;

    /**
     * Creates a new instance of UmjeravanjeResource
     */
    public UmjeravanjeResource() {
    }
    
    @GET
    @Produces({"application/xml", "application/json"})
    public Collection<Umjeravanje> getUmjeravanja(){
        return umjeravanjeFacade.findAll();
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Umjeravanje getUmjeravanje(@PathParam("id") Integer id){
        return umjeravanjeFacade.find(id);
    }

    /**
     * PUT method for updating or creating an instance of UmjeravanjeResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
    
//    @PATH("{id}/tocke/{komponenta_id}/")
//    @PATH("{id}/ispitne_velicine/{komponenta_id}/{velicina_id}/
//    @PATH("{id}/komentar")
    @GET
    @Produces({"application/xml", "application/json"})
    @Path("ispitne_velicine")
    public Collection<IspitneVelicine> getIspitneVelicine(){
        return ispitneVelicineFacade.findAll();
    }
    
    @GET
    @Path("{id}/tocke")
    @Produces({"application/xml", "application/json"})
    public Collection<UmjerneTocke> getUmjerneTocke(@PathParam("id") Integer id){
        return umjerneTockeFacade.findBy(umjeravanjeFacade.find(id));
    }
    

}
