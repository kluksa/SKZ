/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.validatori;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.ModelUredjaja;
import dhz.skz.citaci.weblogger.validatori.*;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ProgramUredjajLink;
import dhz.skz.aqdb.entity.ValidatorModelIzvor;
import dhz.skz.aqdb.entity.Validatori;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacadeLocal;
import dhz.skz.aqdb.facades.ValidatorModelIzvorFacade;
import dhz.skz.aqdb.facades.ValidatoriFacade;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author kraljevic
 */
@Stateful
public class ValidatorFactoryNovi {
    @EJB
    private ProgramMjerenjaFacadeLocal programMjerenjaFacade;

    @EJB
    private ValidatorModelIzvorFacade validatorModelIzvorFacade;
    
    private Map<ProgramMjerenja,NavigableMap<Date,Validator>> programVrijemeValidatorMapa;
    
    private static final Logger log = Logger.getLogger(ValidatorFactoryNovi.class.getName());

    public Validator getValidator(ProgramMjerenja pm, Date vrijeme) {
        return programVrijemeValidatorMapa.get(pm).floorEntry(vrijeme).getValue();
    }

    public Validator lookupValidatorBean(Validatori model) throws NamingException {
        String str = "java:module/" + model.getNaziv().trim();
        Validator v = (Validator) new InitialContext().lookup(str);
        return v;
    }

    public void init(IzvorPodataka ip) throws NamingException {
        Map<ModelUredjaja, Validator> validatorModel = new HashMap<>();
        
        for( ValidatorModelIzvor v : validatorModelIzvorFacade.findAll(ip)){
            validatorModel.put(v.getModelUredjaja(), 
                                lookupValidatorBean(v.getValidatoriId()));
        }
        
        programVrijemeValidatorMapa = new HashMap<>();
        for(ProgramMjerenja pm : programMjerenjaFacade.find(ip)){
            NavigableMap<Date,Validator> dm = new TreeMap<>();
            programVrijemeValidatorMapa.put(pm, dm);
            for(ProgramUredjajLink pul : pm.getProgramUredjajLinkCollection()){
                dm.put(pul.getVrijemePostavljanja(), 
                        validatorModel.get(
                                pul.getUredjajId().getModelUredjajaId()));
            }
        }
    }
}
