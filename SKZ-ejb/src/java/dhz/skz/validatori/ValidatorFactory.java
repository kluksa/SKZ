/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.validatori;

import dhz.skz.aqdb.entity.IspitneVelicine;
import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.ModelUredjaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ProgramUredjajLink;
import dhz.skz.aqdb.entity.UmjeravanjeHasIspitneVelicine;
import dhz.skz.aqdb.entity.ValidatorModelIzvor;
import dhz.skz.aqdb.entity.Validatori;
import dhz.skz.aqdb.facades.IspitneVelicineFacade;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacadeLocal;
import dhz.skz.aqdb.facades.UmjeravanjeHasIspitneVelicineFacade;
import dhz.skz.aqdb.facades.ValidatorModelIzvorFacade;
import java.util.Collection;
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
public class ValidatorFactory {

    @EJB
    private UmjeravanjeHasIspitneVelicineFacade umjeravanjeHasIspitneVelicineFacade;
    @EJB
    private IspitneVelicineFacade ispitneVelicineFacade;
    private static final Logger log = Logger.getLogger(ValidatorFactory.class.getName());

    @EJB
    private ProgramMjerenjaFacadeLocal programMjerenjaFacade;

    @EJB
    private ValidatorModelIzvorFacade validatorModelIzvorFacade;

    private Map<ProgramMjerenja, NavigableMap<Date, Validator>> programVrijemeValidatorMapa;

    private Map<ProgramMjerenja, NavigableMap<Date, Float>> koefAMapa;
    private Map<ProgramMjerenja, NavigableMap<Date, Float>> koefBMapa;
    private Map<ProgramMjerenja, NavigableMap<Date, Float>> srzMapa;
    private Map<ProgramMjerenja, NavigableMap<Date, Float>> opsegMapa;

    public Validator getValidator(ProgramMjerenja pm, Date vrijeme) {
        Validator v = programVrijemeValidatorMapa.get(pm).floorEntry(vrijeme).getValue();
        v.setPodaciUmjeravanja(koefAMapa.get(pm).floorEntry(vrijeme).getValue(),
                               koefBMapa.get(pm).floorEntry(vrijeme).getValue(),
                               3.33f*srzMapa.get(pm).floorEntry(vrijeme).getValue()/koefAMapa.get(pm).floorEntry(vrijeme).getValue(),
                               opsegMapa.get(pm).floorEntry(vrijeme).getValue()
                );
                
        return programVrijemeValidatorMapa.get(pm).floorEntry(vrijeme).getValue();
    }

    public Validator lookupValidatorBean(Validatori model) throws NamingException {
        String str = "java:module/" + model.getNaziv().trim();
        Validator v = (Validator) new InitialContext().lookup(str);
        return v;
    }

    public void init(IzvorPodataka ip) throws NamingException {
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

    private NavigableMap<Date, Float> getAKoefMapa(ProgramMjerenja pm) {
        return getIspitneVelicineMapa(pm, ispitneVelicineFacade.findByOznaka("A"));
    }

    private NavigableMap<Date, Float> getBKoefMapa(ProgramMjerenja pm) {
        return getIspitneVelicineMapa(pm, ispitneVelicineFacade.findByOznaka("B"));
    }

    private NavigableMap<Date, Float> getOpsegMapa(ProgramMjerenja pm) {
        return getIspitneVelicineMapa(pm, ispitneVelicineFacade.findByOznaka("o"));
    }

    private NavigableMap<Date, Float> getSrzMapa(ProgramMjerenja pm) {
        return getIspitneVelicineMapa(pm, ispitneVelicineFacade.findByOznaka("Srz"));
    }

    private NavigableMap<Date, Float> getIspitneVelicineMapa(ProgramMjerenja pm, IspitneVelicine iv) {
        NavigableMap<Date, Float> mapa = new TreeMap<>();
        for (UmjeravanjeHasIspitneVelicine uiv : umjeravanjeHasIspitneVelicineFacade.find(pm, iv)) {
            mapa.put(uiv.getUmjeravanje().getDatum(), uiv.getIznos());
        }
        return mapa;
    }

}
