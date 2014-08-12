/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dhz.skz.citaci.weblogger;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;

/**
 *
 * @author kraljevic
 */
public class PostajaFileFilter implements FTPFileFilter {
    protected final String regex;

    public PostajaFileFilter(String nazivPostaje) {
        regex = "^(" + nazivPostaje.toLowerCase() + ")-(\\d{8})(.?)\\.csv";
    }

    @Override
    public boolean accept(FTPFile file) {
        return file.getName().toLowerCase().matches(regex);
    }
    
}
