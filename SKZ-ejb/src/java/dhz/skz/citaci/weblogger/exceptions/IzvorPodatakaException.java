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
public class IzvorPodatakaException extends Exception {

    public IzvorPodatakaException() {
    }

    public IzvorPodatakaException(String message) {
        super(message);
    }

    public IzvorPodatakaException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public IzvorPodatakaException(Throwable throwable) {
        super(throwable);
    }
}
