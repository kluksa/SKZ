/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger.validatori;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kraljevic
 */
public class API200EValidator extends APIValidator {
    
    @Override
    protected Map<Integer, String> createMap() {
        HashMap<Integer, String> mapa = new HashMap<>();
        mapa.put(0,"SAMPLE FLOW WARNING");
        mapa.put(1,"OZONE FLOW WARNING");
        mapa.put(2,"RCEL PRESS WARN");
        mapa.put(3,"BOX TEMP WARNING");
        mapa.put(4,"RCELL TEMP WARNING");
        mapa.put(5,"IZS TEMP WARNING");
        mapa.put(6,"PMT TEMP WARNING");
        mapa.put(7,"CONV TEMP WARNING");
        mapa.put(8,"SPARE");
        mapa.put(9,"In MANUAL Calibration Mode");
        mapa.put(10,"In Zero Calibration Mode");
        mapa.put(11,"In Span Calibration Mode");
        mapa.put(12,"In WARMUP Mode");
        mapa.put(13,"MGM");
        mapa.put(14,"PPB");
        mapa.put(15,"Invalid conc");
        return Collections.unmodifiableMap(mapa);
    }
}
