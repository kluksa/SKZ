/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.diseminacija.datatransfer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 *
 * @author kraljevic
 */
public class FtpTransfer implements DataTransfer {
    private static final Logger log = Logger.getLogger(FtpTransfer.class.getName());
    private FTPClient ftp;
    private final URI url;
    private boolean spojen;
    private String filename;
    private OutputStream stream;

    FtpTransfer(URI url) {
        this.url = url;
    }
    
    

    @Override
    public void pripremiTransfer(String filename) throws IOException {
        this.filename = filename;
        String host = url.getHost();
        String userpass = url.getUserInfo();
        String username = null;
        String password = null;
        int i = userpass.indexOf(":");
        if ( i > 0 ){
            username = userpass.substring(0, i);
            password = userpass.substring(i+1);
        } else  {
            username = userpass;
        }
        
        ftp = new FTPClient();
//        //ftp.addProtocolCommandListener(new PrintCommandListener(
        try {
            int reply;
            ftp.connect(host);
            log.log(Level.INFO, "Connected to {0}.", host);
            reply = ftp.getReplyCode();

            if (FTPReply.isPositiveCompletion(reply)) {
                if (!ftp.login(username, password)) {
                    ftp.logout();
                    log.log(Level.SEVERE, "FTP error user password.");
                    disconnect();
                } else {
                   ftp.enterLocalPassiveMode();
//                   ftp.setFileType(FTP.BINARY_FILE_TYPE);
                   spojen = true;
                }
            }  else {
                disconnect();
                log.log(Level.SEVERE, "FTP server refused connection.");
            }
            
        } catch (IOException e) {
            disconnect();
            log.log(Level.SEVERE, "Could not connect to server.");
        }
        this.stream = ftp.storeFileStream(filename);
        
    }

    @Override
    public OutputStream getOutputStream() {
        return stream;
    }

    @Override
    public void zavrsiTransfer() throws IOException {
        disconnect();
        log.log(Level.INFO, "Zatvorio konekciju {0}", url.getAuthority());
    }
    
    private void disconnect() {
        spojen = false;
        if (ftp.isConnected()) {
            try {
                ftp.disconnect();
            } catch (IOException f) {
            }
        }
    }
    
}
