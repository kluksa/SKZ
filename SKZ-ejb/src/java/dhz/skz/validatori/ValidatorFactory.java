/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.validatori;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.ModelUredjaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ProgramUredjajLink;
import dhz.skz.aqdb.entity.ValidatorModelIzvor;
import dhz.skz.aqdb.entity.Validatori;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacadeLocal;
import dhz.skz.aqdb.facades.UmjeravanjeHasIspitneVelicineFacade;
import dhz.skz.aqdb.facades.ValidatorModelIzvorFacade;
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
public class ValidatorFactory extends AbstractValidatorFactory {
    private static final Logger log = Logger.getLogger(ValidatorFactory.class.getName());

    @EJB
    private UmjeravanjeHasIspitneVelicineFacade umjeravanjeHasIspitneVelicineFacade;
    @EJB
    private ProgramMjerenjaFacadeLocal programMjerenjaFacade;
    @EJB
    private ValidatorModelIzvorFacade validatorModelIzvorFacade;

    

    private Validator lookupValidatorBean(Validatori model) throws NamingException {
        String str = "java:module/" + model.getNaziv().trim();
        Validator v = (Validator) new InitialContext().lookup(str);
        return v;
    }
    
    public void init(IzvorPodataka ip) throws NamingException {
        setUmjeravanjeHasIspitneVelicineFacade(umjeravanjeHasIspitneVelicineFacade);
        Map<ModelUredjaja, Validator> validatorModel = new HashMap<>();

        for (ValidatorModelIzvor v : validatorModelIzvorFacade.findAll(ip)) {
            validatorModel.put(v.getModelUredjaja(),
                    lookupValidatorBean(v.getValidatoriId()));
        }
        programVrijemeValidatorMapa = new HashMap<>();
        koefAMapa = new HashMap<>();
        koefBMapa = new HashMap<>();
        srzMapa = new HashMap<>();
        opsegMapa = new HashMap<>();

        for (ProgramMjerenja pm : programMjerenjaFacade.find(ip)) {
            koefAMapa.put(pm, getAKoefMapa(pm));
            koefBMapa.put(pm, getBKoefMapa(pm));
            srzMapa.put(pm, getSrzMapa(pm));
            opsegMapa.put(pm, getOpsegMapa(pm));
            programVrijemeValidatorMapa.put(pm, getValidatorMapa(pm, validatorModel));
        }
    }

    private NavigableMap<Date, Validator> getValidatorMapa(ProgramMjerenja pm, Map<ModelUredjaja, Validator> validatorModel) {
        NavigableMap<Date, Validator> dm = new TreeMap<>();
        for (ProgramUredjajLink pul : pm.getProgramUredjajLinkCollection()) {
            dm.put(pul.getVrijemePostavljanja(),
                    validatorModel.get(
                            pul.getUredjajId().getModelUredjajaId()));
        }
        return dm;
    }
}
