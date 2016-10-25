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
public class Koncentracija {
    String komponenta;
    double c;
    double Urc;
    String jedinica;
    
    public Koncentracija(){
        
    }

    public Koncentracija(String komponenta, double c, double Urc, String jedinica) {
        this.komponenta = komponenta;
        this.c = c;
        this.Urc = Urc;
        this.jedinica = jedinica;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getUrc() {
        return Urc;
    }

    public void setUrc(double Urc) {
        this.Urc = Urc;
    }

    public String getJedinica() {
        return jedinica;
    }

    public void setJedinica(String jedinica) {
        this.jedinica = jedinica;
    }
    
    
}
