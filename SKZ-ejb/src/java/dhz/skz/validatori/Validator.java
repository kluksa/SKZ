/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.validatori;

import dhz.skz.aqdb.entity.PodatakSirovi;
import java.util.Collection;

/**
 *
 * @author kraljevic
 */
public interface Validator {

    public void validiraj(PodatakSirovi ps);
    
    public int provjeraStatusa(String statusStr);

    public void setPodaciUmjeravanja(Double a, Double b, Double ldl, Double opseg);
    
    public void setTemperatura(Double temperatura);
    
    public abstract Collection<String> opisStatusa(String statusStr);

}
