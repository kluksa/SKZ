/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.web;

import dhz.skz.aqdb.entity.Komponenta;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.facades.PostajaFacade;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacade;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.primefaces.model.DualListModel;

/**
 *
 * @author kraljevic
 */
@Named(value = "dohvatPodatakaMB")
@RequestScoped
public class DohvatPodatakaMB implements Serializable {

    @Inject
    private transient Logger log;

    /**
     * Creates a new instance of DohvatPodatakaMB
     */
    public DohvatPodatakaMB() {
    }

    @EJB
    private PostajaFacade postajaFacade;

    @EJB
    private ProgramMjerenjaFacade programMjerenjaFacade;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private DualListModel<Postaja> postaje;
    private DualListModel<Komponenta> komponente;
    private List<ProgramMjerenja> programiSvi;


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
    private Date d1, d2;

    @PostConstruct
    public void init() {
        HashSet<Komponenta> kl = new HashSet<>();
        for (ProgramMjerenja pm : programMjerenjaFacade.findAll()) {
            kl.add(pm.getKomponentaId());
        }

        List<Komponenta> kk = new ArrayList<>(Arrays.asList(kl.toArray(new Komponenta[0])));
        postaje = new DualListModel<>(postajaFacade.findAll(), new ArrayList<Postaja>());
        komponente = new DualListModel<>(kk, new ArrayList<Komponenta>());
        programiSvi = programMjerenjaFacade.findAll();
    }

    public DualListModel<Postaja> getPostaje() {
        return postaje;
    }

    public void setPostaje(DualListModel<Postaja> postaje) {
        this.postaje = postaje;
    }

    public DualListModel<Komponenta> getKomponente() {
        return komponente;
    }

    public void setKomponente(DualListModel<Komponenta> komponente) {
        this.komponente = komponente;
    }

    public void submitPostaje() {

        List<Postaja> target = postaje.getTarget();
        List<ProgramMjerenja> programi = programiSvi;
        if (!target.isEmpty()) {
            programi = new ArrayList<>();
            for (Postaja p : target) {
                programi.addAll(p.getProgramMjerenjaCollection());
            }
        }
        HashSet<Komponenta> kl = new HashSet<>();
        for (ProgramMjerenja pm : programi) {
            kl.add(pm.getKomponentaId());
        }

        List<Komponenta> kk = new ArrayList<>(Arrays.asList(kl.toArray(new Komponenta[0])));
        komponente.setSource(kk);
    }
    public void submitKomponente() {
        List<Komponenta> kl = komponente.getTarget();
        List<ProgramMjerenja> programi = programiSvi;
        System.out.println("OOOOOOOOOOOO:" + kl.size());
        if ( !kl.isEmpty()) {
            programi = new ArrayList<>();
            for (Komponenta k : kl) {
                System.out.println("KKK: " + k.getFormula());
                programi.addAll(k.getProgramMjerenjaCollection());
            }
        }
        HashSet<Postaja> pl = new HashSet<>();
        for (ProgramMjerenja pm : programi) {
            pl.add(pm.getPostajaId());
//            System.out.println("PPPPP:" + pm.getId());
        }

        postaje.setSource(new ArrayList<>(Arrays.asList(pl.toArray(new Postaja[0]))));

    }
}
