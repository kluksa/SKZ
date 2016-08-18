/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.web;

import dhz.skz.aqdb.entity.PrimateljProgramKljuceviMap;
import dhz.skz.aqdb.entity.PrimateljiPodataka;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.facades.PrimateljProgramKljuceviMapFacade;
import dhz.skz.aqdb.facades.PrimateljiPodatakaFacade;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author kraljevic
 */
@Named(value = "primateljiMB")
@ViewScoped
public class PrimateljiMB implements Serializable{
    @EJB
    private ProgramMjerenjaFacade programMjerenjaFacade;
    @EJB
    private PrimateljiPodatakaFacade primateljiPodatakaFacade;
    @EJB
    private PrimateljProgramKljuceviMapFacade primateljProgramKljuceviMapFacade;

    private PrimateljiPodataka primatelj;

    private MenuModel model;
    
    private Tmp programSelektirani;
    
    
    @PostConstruct
    public void init() {
        model = new DefaultMenuModel();
        for ( PrimateljiPodataka p : getPrimatelji()) {

            DefaultMenuItem item = new DefaultMenuItem(p.getNaziv());
            String command = String.format("#{primateljiMB.update('%d')}", p.getId());
            item.setCommand(command);
            item.setUpdate(":primatelj-panel");
            model.addElement(item);
        }
    }
    
    public MenuModel getModel() {
        return model;
    }
    
    public PrimateljiMB() {
    }
    
    public List<PrimateljiPodataka> getPrimatelji(){
        return primateljiPodatakaFacade.findAll();
    }

    public PrimateljiPodataka getPrimatelj() {
        return primatelj;
    }

    public void update(String primatelj) {
        this.primatelj = primateljiPodatakaFacade.find(Integer.parseInt(primatelj));
        
    }
    
    public Collection<Tmp> getProgramUkupni(){
        if ( primatelj == null ) return null;
        Collection<Tmp> tmp = new ArrayList<>();
        for ( ProgramMjerenja pm : programMjerenjaFacade.findAll()){
            Tmp tete = new Tmp();
            tete.pm = pm;
            tete.ppkm = primateljProgramKljuceviMapFacade.find(primatelj, pm);
            tmp.add(tete);
        }
        return tmp;
    }

    public Tmp getProgramSelektirani() {
        return programSelektirani;
    }

    public void setProgramSelektirani(Tmp programSelektirani) {
        this.programSelektirani = programSelektirani;
    }
    
    public class Tmp{
        ProgramMjerenja pm;
        PrimateljProgramKljuceviMap ppkm;

        public ProgramMjerenja getPm() {
            return pm;
        }

        public void setPm(ProgramMjerenja pm) {
            this.pm = pm;
        }

        public PrimateljProgramKljuceviMap getPpkm() {
            return ppkm;
        }

        public void setPpkm(PrimateljProgramKljuceviMap ppkm) {
            this.ppkm = ppkm;
        }
        
    }
    
}
