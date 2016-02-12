/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.web;

import dhz.skz.CitaciGlavniBeanRemote;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.facades.PostajaFacade;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacade;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

/**
 *
 * @author kraljevic
 */
@Named(value = "citanjeMB")
@ManagedBean
@ViewScoped
public class CitanjeMB extends NewClass implements Serializable{
    @EJB
    private CitaciGlavniBeanRemote citaciGlavniBean;

    @EJB
    private ProgramMjerenjaFacade programMjerenjaFacade;
    
    @EJB
    private PostajaFacade postajaFacade;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private Postaja postaja;
    private Integer postajaId;
    private List<Postaja> postaje;
//    private TreeMap<String, Postaja> postaje;

    private ProgramMjerenja selektiraniProgram;
    private Collection<ProgramMjerenja> programiSvi;
    private Date d1, d2;
    private boolean novi = false;


    


    public CitanjeMB() {
        super();
    }
    
    @PostConstruct
    public void init(){
        postaje = postajaFacade.findAll();
        programiSvi = programMjerenjaFacade.find(postaja);
    }
    
    @Override
    protected CitaciGlavniBeanRemote getBean(){
        return citaciGlavniBean;
    }
    
    public void agregirajPonovno(){
        citaciGlavniBean.dodajOdPocetka();
    }
    public Date getD1() {
        return d1;
    }

    public void setD1(Date d1) {
        this.d1 = d1;
    }

    public Date getD2() {
        return d2;
    }

    public void setD2(Date d2) {
        this.d2 = d2;
    }
    
        public Integer getPostajaId() {
        return postajaId;
    }

    public void setPostajaId(Integer postajaId) {
        this.postajaId = postajaId;
    }

    public Postaja getPostaja() {
        return postaja;
    }

    public void setPostaja(Postaja postaja) {
        this.postaja = postaja;
    }

    public Collection<Postaja> getPostaje() {
        return postaje;
    }

    public ProgramMjerenja getSelektiraniProgram() {
        return selektiraniProgram;
    }

    public void setSelektiraniProgram(ProgramMjerenja selektiraniProgram) {
        this.selektiraniProgram = selektiraniProgram;
    }

    public Collection<ProgramMjerenja> getProgramiSvi() {
        return programiSvi;
    }

    public void setProgramiSvi(List<ProgramMjerenja> programiSvi) {
        this.programiSvi = programiSvi;
    }

    public void onPostajaChange() {
        novi = false;
        if (postaja != null) {
            programiSvi = programMjerenjaFacade.find(postaja);
            System.out.println(programiSvi.size());
        } else {
            programiSvi = new ArrayList<>();

        }
    }

    public void onProgramChange() {
        novi = false;
    }


}
