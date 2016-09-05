/*
 * Copyright (C) 2016 kraljevic
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dhz.skz.citaci.iox;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kraljevic
 */
public class IoxKonekcija {
    private static final Logger log = Logger.getLogger(IoxKonekcija.class.getName());
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd%20HH:mm");
    private String urlPredlozak;
    
    public IoxKonekcija(String urlPredlozak, String hostname, String username, String password){
        urlPredlozak = urlPredlozak.replaceFirst("\\$\\{USERNAME\\}", username);
        urlPredlozak = urlPredlozak.replaceFirst("\\$\\{PASSWORD\\}", password);
        this.urlPredlozak = urlPredlozak.replaceFirst("\\$\\{HOSTNAME\\}", hostname);
    }
    
    public IoxKonekcija(String urlPredlozak, String hostname){
        this(urlPredlozak, hostname, "horiba", "password");
    }
    
    public InputStream getInputStream(String period, String dbstr, Date vrijeme) throws IOException {
        URL url = napraviURL(period, dbstr, sdf.format(vrijeme));
        log.log(Level.INFO, "IOCON URL: {0}", new Object[]{url});
        String userInfo = url.getUserInfo();
        byte[] authEncBytes = Base64.getEncoder().encode(userInfo.getBytes());
        String authStringEnc = new String(authEncBytes);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        con.setRequestProperty("Authorization", "Basic " + authStringEnc);
        con.setConnectTimeout(20000);
        con.setReadTimeout(30000);

        int responseCode = con.getResponseCode();
        log.log(Level.FINE, "Sending 'GET' request to URL : {0}", url.toString());
        log.log(Level.FINE, "Response Code : {0}", responseCode);
        return con.getInputStream();
    }
    
    private URL napraviURL(String period, String dbstr, String vrijemeStr) throws MalformedURLException {
        String urlString = urlPredlozak.replaceFirst("\\$\\{PERIOD\\}", period);
        urlString = urlString.replaceFirst("\\$\\{DB\\}", dbstr);
        urlString = urlString.replaceFirst("\\$\\{POCETAK\\}", vrijemeStr);
        return new URL(urlString);
    }

}
