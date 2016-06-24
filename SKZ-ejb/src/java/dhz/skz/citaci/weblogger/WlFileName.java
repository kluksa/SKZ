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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;
import java.util.regex.Matcher;

/**
 *
 * @author kraljevic
 */
public class WlFileName implements Comparable {

    
    public enum Vrsta { MJERENJE, ZEROSPAN };
    private String fname;
    private Date termin;

    
    private Matcher m;
    private final TimeZone timeZone;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    
    @Override
    public int compareTo(Object o) {
        return this.fname.compareTo(((WlFileName)o).getFname());
    }

    public WlFileName(Matcher m, TimeZone timeZone) {
        this.timeZone = timeZone;
        this.m=m; //To change body of generated methods, choose Tools | Templates.
    }
    
    public String getFname() {
        return fname;
    }
    
    public void setFname(String fname) throws ParseException {
        this.fname = fname;
        sdf.setTimeZone(timeZone);
        termin = sdf.parse(m.group(3));
    }
    
    public Date getTermin() {
        return termin;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.fname);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WlFileName other = (WlFileName) obj;
        if (!Objects.equals(this.fname, other.fname)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return fname;
    }
    
}
