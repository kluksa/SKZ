/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dhz.skz.citaci.weblogger.exceptions;

/**
 *
 * @author kraljevic
 */
public class NevaljanStatusException extends Exception {

    public NevaljanStatusException() {
    }
    
    public NevaljanStatusException(String message) {
        super(message);
    }

    public NevaljanStatusException(String message, Throwable throwable) {
        super(message, throwable);
    }
    
    public NevaljanStatusException(Throwable throwable) {
        super(throwable);
    }
}
