/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.web;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author kraljevic
 */
@Named(value = "pregledMjerenjaMB")
@RequestScoped
public class PregledMjerenjaMB {
    /**
     * Ideja je imati stranicu sa svim mjerenjima (sa svim programima) na kojoj 
     * ce se vidjeti svi programi, stanje komunikacije, zero, span, status kao semafori
     * komunikacija je vezana uz postaju, temperatura isto
     * mjerenja su grupirana po postajama
     * To bi bila nekakva tablica.
     */

    /**
     * Creates a new instance of PregledMjerenjaMB
     */
    public PregledMjerenjaMB() {
    }
    
}
