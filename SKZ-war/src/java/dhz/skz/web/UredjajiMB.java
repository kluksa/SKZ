/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.web;

import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author kraljevic
 */
@Named(value = "uredjajiMB")
@ViewScoped
public class UredjajiMB {
    @Inject
    private transient Logger log;

    /**
     * Creates a new instance of UredjajiMB
     */
    public UredjajiMB() {
    }
    
}
