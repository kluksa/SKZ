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
public class API100EValidator extends APIValidator {
    
    @Override
    protected Map<Integer, String> createMap() {
        HashMap<Integer, String> mapa = new HashMap<>();
        mapa.put(0,"SAMPLE FLOW WARNING"); // 0x1
        mapa.put(1,"PMT DET WARNING"); // 0x2
        mapa.put(2,"HVPS WARNING"); // 0x4
        mapa.put(3,"DARK CAL WARNING"); // 0x8
        mapa.put(4,"RCELL TEMP WARNING"); // 0x10
        mapa.put(5,"IZS TEMP WARNING"); // 0x20
        mapa.put(6,"PMT TEMP WARNING"); // 0x40
        mapa.put(7,"INVALID CONC"); // 0x80
        mapa.put(8,"SPARE"); // 0x100
        mapa.put(9,"In Manual Calibration Mode"); // 0x200
        mapa.put(10,"In Zero Calibration Mode"); // 0x400
        mapa.put(11,"In Span Calibration Mode"); // 0x800
        mapa.put(12,"SPARE"); // 0x1000
        mapa.put(13,"PPB"); // 0x2000
        mapa.put(14,"PPM"); // 0x4000
        mapa.put(15,"SPARE"); // 0x8000
        return Collections.unmodifiableMap(mapa);
    }
}
