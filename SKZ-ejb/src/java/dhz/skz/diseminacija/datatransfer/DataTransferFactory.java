/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.diseminacija.datatransfer;

import dhz.skz.diseminacija.datatransfer.exceptions.ProtocolNotSupported;
import dhz.skz.aqdb.entity.PrimateljiPodataka;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kraljevic
 */
public class DataTransferFactory {

    private static final Logger log = Logger.getLogger(DataTransferFactory.class.getName());

    public static DataTransfer getTransferObj(PrimateljiPodataka pp) throws ProtocolNotSupported, URISyntaxException {
        DataTransfer dto = null;
        try {
            URI url = new URI(pp.getUrl());
            String protokol = url.getScheme();

            switch (protokol) {
                case "file":
                    log.log(Level.INFO, "Protokol file: {0}", url.getPath());
                    dto = new LocalSaveFile(url.getPath());
                    break;
                case "ftp":
                    log.log(Level.INFO, "Protokol ftp  {0}", url.getPath());
                    dto = new FtpTransfer(url);
                    break;
                default:
                    throw new ProtocolNotSupported(protokol);
            }
            return dto;
        } catch (URISyntaxException ex) {
            log.log(Level.SEVERE, "Los URL {0}", new Object[]{pp.getUrl()} );
            throw ex;
        }
        
    }
}
