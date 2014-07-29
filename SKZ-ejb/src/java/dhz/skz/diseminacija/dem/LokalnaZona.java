/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.diseminacija.dem;

import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 *
 * @author kraljevic
 */
public class LokalnaZona {

    private static SimpleTimeZone tz = null;

    public static TimeZone getZone() {
        if (tz == null) {
            tz = new SimpleTimeZone(3600000, "LT");
        }
        return tz;
    }
}
