/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.web;

import dhz.skz.aqdb.facades.UmjeravanjeFacade;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 *
 * @author kraljevic
 */
@Named(value = "umjeravanjeMB")
@Dependent
public class UmjeravanjeMB {
    @Inject
    private  transient Logger log;
    @EJB
    private UmjeravanjeFacade umjeravanjeFacade;

    /**
     * Creates a new instance of UmjeravanjeMB
     */
    public UmjeravanjeMB() {
    }
    
}
