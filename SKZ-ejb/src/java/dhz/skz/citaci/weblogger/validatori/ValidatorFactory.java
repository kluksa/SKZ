/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger.validatori;

import dhz.skz.aqdb.entity.ProgramMjerenja;
import java.util.Date;
import java.util.logging.Logger;
import javax.ejb.Singleton;

/**
 *
 * @author kraljevic
 */
@Singleton
public class ValidatorFactory {

    private static final Logger log = Logger.getLogger(ValidatorFactory.class.getName());

    public Validator getValidator(ProgramMjerenja pm, Date vrijeme) {
        return new API100EValidator();
    }
    
//    public NavigableMap<Date, Validator> getValidatori(ProgramMjerenja pm) {
//        NavigableMap<Date, Validator> validatori = new TreeMap<>();
//        for (ProgramUredjajLink pul : pm.getProgramUredjajLinkCollection()) {
//            try {
//                Validator val = lookupValidator(pul.getUredjajId().getModelUredjajaId());
//                validatori.put(pul.getVrijemePostavljanja(), val);
//            } catch (NamingException ex) {
//                log.log(Level.SEVERE, null, ex);
//            }
//        }
//        return validatori;
//    }
//
//    public Validator lookupValidator(ModelUredjaja model) throws NamingException {
//        String str = "java:module/" + model.getValidatorId().getNaziv().trim();
//        Validator v = (Validator) new InitialContext().lookup(str);
//        return v;
//    }
}
