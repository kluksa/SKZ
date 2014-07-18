/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateful;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;
import dhz.skz.citaci.weblogger.exceptions.FtpKlijentException;

/**
 *
 * @author kraljevic
 */
@Stateful
public class FtpKlijent {

    private static final Logger log = Logger.getLogger(FtpKlijent.class.getName());

    private String host;
    private String user;
    private String passwd;
    private boolean spojen = false;

    private FTPClient ftp;
    private InputStream istream;

    private void setUri(URI uri) {
        host = uri.getHost();
        String userpass = uri.getUserInfo();
        int i = userpass.indexOf(":");
        user = userpass.substring(0, i);
        passwd = userpass.substring(i + 1);
    }

    public FTPFile[] getFileList(FTPFileFilter filter) throws FtpKlijentException  {
        try {
            return ftp.listFiles(null, filter);
        } catch (IOException ex) {
            log.log(Level.SEVERE, "Could not get File list", ex);
            throw new FtpKlijentException("Could not get File list.", ex); 
        }
    }

    public InputStream getFileStream(FTPFile file) throws FtpKlijentException {
        String filename = file.getName();
        try {
            istream = ftp.retrieveFileStream(filename);
        } catch (IOException ex) {
            log.log(Level.SEVERE, "Could not get file stream", ex);
            throw new FtpKlijentException("Could not get file stream.", ex); 
        }
        return istream;
    }

    public boolean zavrsi() throws FtpKlijentException {
        try {
            return ftp.completePendingCommand();
        } catch (IOException ex) {
            log.log(Level.SEVERE, "Could not complete.", ex);
            throw new FtpKlijentException("Could not complete.", ex); 
        }
    }

    public void zatvoriStream() throws FtpKlijentException {
        try {
            istream.close();
        } catch (IOException ex) {
            log.log(Level.SEVERE, "Could not close stream.", ex);
            throw new FtpKlijentException("Could not close stream.", ex); 
        }
    }
    
    public void connect(URI uri) throws FtpKlijentException {
        this.setUri(uri);
        ftp = new FTPClient();
        try {
            ftp.connect(host);
            log.log(Level.INFO, "FTP Connected to {0} : {1} : {2}.", new Object[]{host, user, passwd});

            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                throw new FtpKlijentException("FTP server refused connection.");
            }
            if (!ftp.login(user, passwd)) {
                throw new FtpKlijentException("FTP error user password.");
            }
            ftp.enterLocalPassiveMode();
//                   ftp.setFileType(FTP.BINARY_FILE_TYPE);
        } catch (IOException e) {
            disconnect();
            throw new FtpKlijentException("Could not connect to server.", e);
        } 
    }

    public void disconnect() {
        if (ftp.isConnected()) {
            try {
                ftp.disconnect();
            } catch (IOException f) {
            }
        }
    }

    public FTPFile[] getFileList() throws FtpKlijentException {
        try {
            return ftp.listFiles();
        } catch (IOException ex) {
            log.log(Level.SEVERE, "Could not get File list", ex);
            throw new FtpKlijentException("Could not get File list.", ex); 
        }
    }
}
