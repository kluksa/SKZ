/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.umjeravanje.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@XmlRootElement
public class MFC {
    String sljedivost;
    double ur;

    public MFC(String sljedivost, double ur) {
        this.sljedivost = sljedivost;
        this.ur = ur;
    }

    public MFC() {
    }
    

    public String getSljedivost() {
        return sljedivost;
    }

    public void setSljedivost(String sljedivost) {
        this.sljedivost = sljedivost;
    }

    public double getUr() {
        return ur;
    }

    public void setUr(double ur) {
        this.ur = ur;
    }
    
    
}
