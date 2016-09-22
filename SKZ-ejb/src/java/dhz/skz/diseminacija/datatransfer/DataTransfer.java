/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.diseminacija.datatransfer;

import dhz.skz.diseminacija.datatransfer.exceptions.DataTransferException;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author kraljevic
 */
public interface DataTransfer {

    public void pripremiTransfer(String url) throws DataTransferException;

    public OutputStream getOutputStream();

    public void zavrsiTransfer() throws IOException;

}
