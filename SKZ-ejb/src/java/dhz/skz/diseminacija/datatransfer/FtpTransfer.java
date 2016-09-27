/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.diseminacija.datatransfer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
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
    private final FTPClient ftp;
    private URL url;

    public FtpTransfer(){
        ftp = new FTPClient();
    }
    
    private void connect(URL url) throws IOException{
        String userpass = url.getUserInfo();
        String username;
        String password = null;
        int i = userpass.indexOf(":");
        if (i > 0) {
            username = userpass.substring(0, i);
            password = userpass.substring(i + 1);
        } else {
            username = userpass;
        }
        ftp.connect(url.getHost());
        
        log.log(Level.INFO, "Connected to {0}.", url.getHost());
        if (FTPReply.isPositiveCompletion(ftp.getReplyCode()) && ftp.login(username, password)) {
            ftp.enterLocalPassiveMode();
        } else {
            log.log(Level.SEVERE, "FTP server {0} refused connection with reply {1}.", new Object[]{url.getHost(), ftp.getReplyCode()});
            disconnect();
            throw new IOException();
        }
        
    }

    @Override
    public OutputStream getOutputStream(URL url) throws IOException {
        this.url = url;
        if ( ! ftp.isConnected()) {
            connect(url);
        }
        return ftp.storeFileStream(url.getPath());
    }

    @Override
    public void zavrsiTransfer() throws IOException {
        ftp.completePendingCommand();
        disconnect();
        log.log(Level.INFO, "Zatvorio konekciju {0}", url.getAuthority());
    }

    private void disconnect() {
        if (ftp.isConnected()) {
            try {
                ftp.logout();
                ftp.disconnect();
            } catch (IOException f) {
            }
        }
    }

}
