/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.diseminacija.datatransfer;

import dhz.skz.diseminacija.datatransfer.exceptions.DataTransferException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kraljevic
 */
public class LocalSaveFile implements DataTransfer {

    private OutputStream os = null;
    private String path = "/home/kraljevic/";

    LocalSaveFile(String path) {
        this.path = path;
    }

    @Override
    public void pripremiTransfer(String ime) throws DataTransferException {
        try {
            String fname = path + "/" + ime.toLowerCase();
            os = new FileOutputStream(new File(fname));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LocalSaveFile.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataTransferException();
        }
    }

    @Override
    public OutputStream getOutputStream() {
        return os;
    }

    @Override
    public void zavrsiTransfer() throws IOException {
        os.close();
    }
}
