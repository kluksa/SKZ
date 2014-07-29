/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.diseminacija.datatransfer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
    public void pripremiTransfer(String ime) throws IOException {
        String fname = path + "/" + ime.toLowerCase();
        os = new FileOutputStream(new File(fname));
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
