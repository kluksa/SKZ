/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.diseminacija.datatransfer.exceptions;

/**
 *
 * @author kraljevic
 */
public class ProtocolNotSupported extends Exception {

    public ProtocolNotSupported(String protokol) {
        super(protokol);
    }

}
