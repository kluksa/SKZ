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
package dhz.skz.citaci.weblogger;

import dhz.skz.aqdb.entity.Postaja;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.regex.Matcher;
import javax.annotation.PostConstruct;

/**
 *
 * @author kraljevic
 */
public class WlPostajaDatoteke {
    
    private final Postaja postaja;
    private Date vrjmemeZadnjegMjerenja;
    private Date vrijemeZadnjegZS;
    private Date danZadnjegMjerenja;
    private Date danZadnjegZS;
    private final SortedSet<WlFileName> mjerenjaFname = new TreeSet<>();
    private final SortedSet<WlFileName> zsFname =  new TreeSet<>();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private final TimeZone timeZone;

    WlPostajaDatoteke(Postaja p, TimeZone timeZone) {
        this.postaja = p;
        this.timeZone = timeZone;
    }
        
    @PostConstruct
    public void init() {
        sdf.setTimeZone(timeZone);
    }

    public void setVrjmemeZadnjegMjerenja(Date vrjmemeZadnjegMjerenja) {
        this.vrjmemeZadnjegMjerenja = vrjmemeZadnjegMjerenja;
        this.danZadnjegMjerenja = odreziNaDan(vrjmemeZadnjegMjerenja);
    }

    public void setVrijemeZadnjegZS(Date vrijemeZadnjegZS) {
        this.vrijemeZadnjegZS = vrijemeZadnjegZS;
        this.danZadnjegZS = odreziNaDan(vrijemeZadnjegZS);
    }

    public SortedSet<WlFileName> getMjerenjaFname() {
        return mjerenjaFname;
    }

    public SortedSet<WlFileName> getZsFname() {
        return zsFname;
    }

    private Date odreziNaDan(Date vrijeme) {
        long s = 24*3600*1000;
        long t = s * (vrijeme.getTime()/s);
        return new Date(t);
    }
    
    public void dodajFajl(String fname, Matcher m) throws ParseException {
        //"^(.*)(_c)?-(\\d{8})([A-Z]?)(\\.csv)$"
        WlFileName wfn = new WlFileName(m, timeZone);
        wfn.setFname(fname);
        Date termin = wfn.getTermin();
        if ( m.group(2) == null || m.group(2).isEmpty()) {
            if ( termin.after(new Date(1466596800000L))) {
                System.out.println("JE");
            }
            if ( !termin.before(danZadnjegMjerenja)){
                mjerenjaFname.add(wfn);
            }
        } else {
            if ( !termin.before(danZadnjegZS)){
                zsFname.add(wfn);
            }
        }
    }

    public Postaja getPostaja() {
        return postaja;
    }
}
