/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dhz.skz.sirovi.exceptions;

/**
 *
 * @author kraljevic
 */
public class CsvPrihvatException extends Exception {

    /**
     * Creates a new instance of <code>CsvPrihvatException</code> without detail
     * message.
     */
    public CsvPrihvatException() {
    }

    /**
     * Constructs an instance of <code>CsvPrihvatException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CsvPrihvatException(String msg) {
        super(msg);
    }
}
