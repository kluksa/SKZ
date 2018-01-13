/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.diseminacija.datatransfer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 *
 * @author kraljevic
 */
public class LocalSaveFile implements DataTransfer {

    private OutputStream os = null;

    @Override
    public OutputStream getOutputStream(URL url) throws FileNotFoundException {
        return  new FileOutputStream(new File(url.getPath()));
    }

    @Override
    public void zavrsiTransfer(){
    }
}
