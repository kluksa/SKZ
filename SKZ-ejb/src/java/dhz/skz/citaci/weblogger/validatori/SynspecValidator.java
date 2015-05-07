/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger.validatori;

import dhz.skz.validatori.*;
import javax.ejb.Stateless;


/**
 *
 * @author kraljevic
 */
@Stateless
public class SynspecValidator extends ValidatorImpl {

    @Override
    public int provjeraStatusa(String statusStr) {
        return 0;
    }
}
