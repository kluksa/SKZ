/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.diseminacija.datatransfer;

import dhz.skz.diseminacija.datatransfer.exceptions.ProtocolNotSupported;
import dhz.skz.aqdb.entity.PrimateljiPodataka;
import dhz.skz.diseminacija.datatransfer.exceptions.DataTransferException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kraljevic
 */
public class DataTransferFactory {

    private static final Logger log = Logger.getLogger(DataTransferFactory.class.getName());

    public static DataTransfer getTransferObj(PrimateljiPodataka pp) throws ProtocolNotSupported, MalformedURLException, DataTransferException, IOException {
        DataTransfer dto = null;
        URL url = new URL(pp.getUrl());
        String protokol = url.getProtocol();

        switch (protokol) {
            case "file":
                log.log(Level.INFO, "Protokol file: {0}", url.getPath());
                dto = new LocalSaveFile();
                break;
            case "ftp":
                log.log(Level.INFO, "Protokol ftp  {0}", url.getPath());
                dto = new FtpTransfer();
                break;
            default:
                throw new ProtocolNotSupported(protokol);
        }
        return dto;

    }
}
