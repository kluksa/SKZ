/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger.validatori;

import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ProgramUredjajLink;
import dhz.skz.aqdb.entity.Validatori;
import dhz.skz.aqdb.facades.ValidatoriFacade;
import java.util.Date;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.logging.Level;
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
    private ValidatoriFacade validatoriFacade;
    private NavigableMap<Date, Validator> validatori;
    
    
    private static final Logger log = Logger.getLogger(ValidatorFactory.class.getName());

    public Validator getValidator(ProgramMjerenja pm, Date vrijeme) {
        Map.Entry<Date, Validator> floorEntry = validatori.floorEntry(vrijeme);
        if ( floorEntry == null ) {
            log.log(Level.SEVERE, "NEMA VALIDATORA : {0}, {1}, {2},{3}", new Object[]{pm.getId(), 
                pm.getKomponentaId().getFormula(), pm.getPostajaId().getNazivPostaje(), vrijeme});
        }
        return validatori.floorEntry(vrijeme).getValue();
    }
    
    public Validator lookupValidatorBean(Validatori model) throws NamingException {
        String str = "java:module/" + model.getNaziv().trim();
        Validator v = (Validator) new InitialContext().lookup(str);
        return v;
    }

    public void init(ProgramMjerenja pm) throws NamingException {
        validatori = new TreeMap<>();
        for (ProgramUredjajLink pul : pm.getProgramUredjajLinkCollection()) {
            Validatori val = validatoriFacade.find(pul.getUredjajId().getModelUredjajaId(), pm.getIzvorPodatakaId());
            Validator lookupValidatorBean = lookupValidatorBean(val);
            validatori.put(pul.getVrijemePostavljanja(),lookupValidatorBean);
        }
    }
}
