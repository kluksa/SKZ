/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger.validatori;

import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.citaci.weblogger.exceptions.NevaljanStatusException;
import dhz.skz.citaci.weblogger.util.Status;

/**
 *
 * @author kraljevic
 */
public interface Validator {

    Status getStatus(Float iznos, String statusStr) throws NevaljanStatusException;

    public Status getStatus(String status);
    
    int getBrojUSatu();

    public void validiraj(PodatakSirovi ps);

}
