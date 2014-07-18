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
    public static DataTransfer getTransferObj(PrimateljiPodataka pp) throws ProtocolNotSupported {
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
            
        } catch (URISyntaxException ex) {
            log.log(Level.SEVERE, null, ex);
            throw new ProtocolNotSupported("Los URL");
        }
        return dto;
    }
}
