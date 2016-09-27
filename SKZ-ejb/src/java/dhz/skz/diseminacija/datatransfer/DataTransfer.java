/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.diseminacija.datatransfer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

/**
 *
 * @author kraljevic
 */
public interface DataTransfer {

    public OutputStream getOutputStream(URL url) throws IOException ;

    public void zavrsiTransfer() throws IOException;

}
