/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger.validatori;

import dhz.skz.aqdb.entity.ModelUredjaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ProgramUredjajLink;
import java.util.Date;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author kraljevic
 */
@Singleton
public class ValidatorFactory {

    private static final Logger log = Logger.getLogger(ValidatorFactory.class.getName());

    public Validator getValidator(ProgramMjerenja pm, Date vrijeme) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public NavigableMap<Date, Validator> getValidatori(ProgramMjerenja pm) {
        NavigableMap<Date, Validator> validatori = new TreeMap<>();
        for (ProgramUredjajLink pul : pm.getProgramUredjajLinkCollection()) {
            try {
                Validator val = lookupValidator(pul.getUredjajId().getModelUredjajaId());
                validatori.put(pul.getVrijemePostavljanja(), val);
            } catch (NamingException ex) {
                log.log(Level.SEVERE, null, ex);
            }
        }
        return validatori;
    }

    public Validator lookupValidator(ModelUredjaja model) throws NamingException {
        String str = "java:module/" + model.getValidatorId().getNaziv().trim();
        Validator v = (Validator) new InitialContext().lookup(str);
        return v;
    }
}
