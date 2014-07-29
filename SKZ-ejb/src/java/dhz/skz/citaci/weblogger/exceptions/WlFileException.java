/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger.exceptions;

import java.util.logging.Logger;

/**
 *
 * @author kraljevic
 */
public class WlFileException extends Exception {

    public WlFileException() {
    }

    public WlFileException(String message) {
        super(message);
    }

    public WlFileException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public WlFileException(Throwable throwable) {
        super(throwable);
    }

}
