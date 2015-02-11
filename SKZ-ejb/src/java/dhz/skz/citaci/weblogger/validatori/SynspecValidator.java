/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger.validatori;

import javax.ejb.Stateless;
import dhz.skz.citaci.weblogger.util.Flag;
import dhz.skz.citaci.weblogger.exceptions.NevaljanStatusException;
import dhz.skz.citaci.weblogger.util.Status;

/**
 *
 * @author kraljevic
 */
@Stateless
public class SynspecValidator implements Validator {

    @Override
    public Status getStatus(Float iznos, String statusStr) throws NevaljanStatusException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Status getStatus(String status) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getBrojUSatu() {
       return 4;
    }

}
