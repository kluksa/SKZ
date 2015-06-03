/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.web;

import dhz.skz.aqdb.facades.AgregacijeFacade;
import dhz.skz.aqdb.facades.GraniceFacade;
import dhz.skz.aqdb.facades.KategorijeGranicaFacade;
import dhz.skz.aqdb.facades.KomponentaFacade;
import dhz.skz.aqdb.facades.MjerneJediniceFacade;
import dhz.skz.aqdb.entity.Agregacije;
import dhz.skz.aqdb.entity.Granice;
import dhz.skz.aqdb.entity.KategorijeGranica;
import dhz.skz.aqdb.entity.Komponenta;
import dhz.skz.aqdb.entity.MjerneJedinice;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.MenuActionEvent;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuItem;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author kraljevic
 */
@Named(value = "komponentaMB")
@ViewScoped
public class KomponentaMB implements Serializable {
    @Inject
    private  transient Logger log;
    @EJB
    private AgregacijeFacade agregacijeFacade;
    @EJB
    private MjerneJediniceFacade mjerneJediniceFacade;
    @EJB
    private KategorijeGranicaFacade kategorijeGranicaFacade;
    @EJB
    private GraniceFacade graniceFacade;
    @EJB
    private KomponentaFacade komponentaFacade;
    
    private Granice granica, odabranaGranica;

    private Komponenta komponenta = new Komponenta();
    
//    private NavigableMap<Integer, Komponenta> komponente;
    private MenuModel model;

    @PostConstruct
    public void init() {
        model = new DefaultMenuModel();
        for (Komponenta k : komponentaFacade.findAll()) {
            DefaultMenuItem item = new DefaultMenuItem(k.getNaziv());
            String command = String.format("#{komponentaMB.update('%S')}", k.getNaziv());
            item.setCommand("#{komponentaMB.displayList}");
            item.setParam("id", k.getId());
            item.setUpdate(":sadrzaj");
            model.addElement(item);
        }
    }

    public Komponenta getKomponenta() {
        return komponenta;
    }

    public void setKomponenta(Komponenta komponenta) {
        this.komponenta = komponenta;
    }

    public KomponentaMB() {
    }

    public MenuModel getModel() {
        return model;
    }

    public void displayList(ActionEvent event) {
        MenuItem menuItem = ((MenuActionEvent) event).getMenuItem();
        Map<String, List<String>> params = menuItem.getParams();
        Integer id = Integer.parseInt(menuItem.getParams().get("id").get(0));
        komponenta = komponentaFacade.find(id);
    }
    public void ponistiIzmjene() {
        komponenta = komponentaFacade.find(komponenta.getId());
    }
    
     public void spremiIzmjene() {
         if ( komponenta.getId()!= null) {
            komponentaFacade.edit(komponenta);
            init();
         }
    }
     
     public Collection<MjerneJedinice> getMjerneJedinice(){
         return mjerneJediniceFacade.findAll();
     }
     
     public Collection<Agregacije> getVrsteAgregacije(){
         return agregacijeFacade.findAll();
     }
     
    public Granice getGranica() {
        return granica;
    }
        
    public void setGranica(Granice granica) {
        this.granica = granica;
    }

    public Collection<Granice> getGranice(){
        if (komponenta != null)
            return graniceFacade.findBy(komponenta);
        return null;
    }
    
    public Collection<KategorijeGranica> getKategorijeGranica(){
        return kategorijeGranicaFacade.findAll();
    }

   

     
     public void dodajGranicu(){
         granica = new Granice();
     }
     
     public void spremiGranicu(){
         if ( granica.getId()!= null) {
            graniceFacade.edit(granica);
         } else {
             granica.setKomponentaId(komponenta);
             graniceFacade.create(granica);
         }
    }

     public void izbrisiGranicu(){
         graniceFacade.remove(granica);
     }

    public Granice getOdabranaGranica() {
        return odabranaGranica;
    }

    public void setOdabranaGranica(Granice odabranaGranica) {
        this.odabranaGranica = odabranaGranica;
    }
     
}
