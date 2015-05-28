/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dhz.skz.web;

import dhz.skz.aqdb.facades.OdgovornoTijeloFacade;
import dhz.skz.aqdb.facades.PodrucjeFacade;
import dhz.skz.aqdb.facades.PostajaFacade;
import dhz.skz.aqdb.entity.OdgovornoTijelo;
import dhz.skz.aqdb.entity.Podrucje;
import dhz.skz.aqdb.entity.Postaja;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author kraljevic
 */
@Named(value = "postajaMB")
@ViewScoped
public class PostajaMB implements Serializable{
    @Inject
    private  transient Logger log;
    @EJB
    private PostajaFacade postajaFacade;
    @EJB
    private OdgovornoTijeloFacade tijeloFacade;
    @EJB
    private PodrucjeFacade podrucjeFacade;
    private Map<String, Postaja> postaje;
    private Postaja postaja = new Postaja();
    private Postaja postajaOld;
    private MenuModel model;
    private MapModel gmapModel;
    final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    
    /**
     * Creates a new instance of PostajaMB
     */
    public PostajaMB() {
        postaja.setGeogrDuzina(0.);
        postaja.setGeogrSirina(0.);
    }
    @PostConstruct
    public void init() {
        gmapModel = new DefaultMapModel();
        model = new DefaultMenuModel();
        postaje = new TreeMap<>();
        for ( Postaja p : postajaFacade.findAll()) {
            DefaultMenuItem item = new DefaultMenuItem(p.getNazivPostaje());
            gmapModel.addOverlay(new Marker(new LatLng(p.getGeogrSirina(), p.getGeogrDuzina()), p.getNazivPostaje()));

            String command = String.format("#{postajaMB.update('%S')}",p.getNazivPostaje());
            item.setCommand(command);
            item.setUpdate(":sadrzaj");
            model.addElement(item);
            postaje.put(p.getNazivPostaje().toLowerCase(), p);
        }
    }
    
    public MapModel getGmapModel() {
        return gmapModel;
    }

    public MenuModel getModel() {
        return model;
    }

    public Postaja getPostaja() {
        return postaja;
    }

    public void setPostaja(Postaja postaja) {
        this.postaja = postaja;
    }
    
    public void update(String p) {
        postaja = postaje.get(p.toLowerCase());
    }
    
    public void ponistiIzmjene() {
        postaja = postajaFacade.find(postaja.getId());
        postaje.put(postaja.getNazivPostaje().toLowerCase(), postaja);
    }
    
     public void spremiIzmjene() {
         if ( postaja.getId()!= null) {
            postajaFacade.edit(postaja);
         }
    }
    
    public List<OdgovornoTijelo> getOdgovornaTijela(){
        return tijeloFacade.findAll();
    }
    public List<Podrucje> getPodrucja(){
        return podrucjeFacade.findAll();
    }
    
    public String getOpisPodrucja(Podrucje p) {
        if ( p != null ) {
            return p.getOznaka() + "-" + p.getOpis();
        }
        return null;
    }
            
}


  