/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.web;

import dhz.skz.diseminacija.upozorenja.UpozorenjaBeanRemote;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

/**
 *
 * @author kraljevic
 */
@Named(value = "upozorenjaMB")
@ManagedBean
@ViewScoped
public class UpozorenjaMB {
    @EJB
    private UpozorenjaBeanRemote upozorenjeB;
    
    public void testDojava() {
        upozorenjeB.testirajSlanje();
    }

    /**
     * Creates a new instance of UpozorenjaMB
     */
    public UpozorenjaMB() {
    }
    
}
