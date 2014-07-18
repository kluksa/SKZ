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
public class FtpKlijentException extends Exception {

    public FtpKlijentException() {
    }

    public FtpKlijentException(String message) {
        super(message);
    }

    public FtpKlijentException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public FtpKlijentException(Throwable throwable) {
        super(throwable);
    }
}
