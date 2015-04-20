/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.validatori;

import javax.ejb.Stateless;

/**
 *
 * @author kraljevic
 */
@Stateless
public class GrimmValidator extends ValidatorImpl {

    public GrimmValidator() {
        a = 1;
        b = 0; 
        ldl = 0;
        opseg = 1000;
    }

    @Override
    public int provjeraStatusa(String statusStr) {
        return 0;
    }
}
