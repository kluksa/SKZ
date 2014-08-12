/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger;

import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.citaci.FtpKlijent;
import dhz.skz.citaci.weblogger.exceptions.FtpKlijentException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author kraljevic
 */
@Stateless
@LocalBean
public class WlPostajaCitac {

    private Postaja postaja;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private Date pocetak;
    private Date kraj;

    public void procitaj(Postaja p, Date pocetak, Date kraj) {
        this.postaja = p;
        this.pocetak = pocetak;
        this.kraj = kraj;
        FtpKlijent ftp = new FtpKlijent();

        try {
            ftp.connect(null);
            Map<Date, Collection<FTPFile>> datoteke = getListaDatoteka(ftp, p);

            Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));

            cal.setTime(pocetak);
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            while (cal.getTime().before(kraj)) {
                for (FTPFile file : datoteke.get(cal.getTime())) {

                }
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }
        } catch (FtpKlijentException ex) {
            Logger.getLogger(WlPostajaCitac.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private FTPFile[] getFile(Date time) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    protected final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

    private Map<Date, Collection<FTPFile>> getListaDatoteka(FtpKlijent ftp, Postaja p) throws FtpKlijentException {
        Map<Date, Collection<FTPFile>> mapa = new HashMap<>();
        FTPFile[] fileList = ftp.getFileList();
        String ptStr = "^(" + p.getNazivPostaje() + ")-(\\d{8})(.?)\\.csv";
        for (FTPFile f : fileList) {
            try {
                String strL = f.getName().toLowerCase();
                Pattern pt = Pattern.compile(ptStr);
                Matcher m = pt.matcher(strL);
                if (m.matches()) {
                    Date sada = formatter.parse(m.group(2));
                    if (!mapa.containsKey(sada)) {
                        mapa.put(sada, new ArrayList<FTPFile>());
                    }
                    mapa.get(sada).add(f);

                }
            } catch (ParseException ex) {
            }
        }
        return mapa;
    }
}
