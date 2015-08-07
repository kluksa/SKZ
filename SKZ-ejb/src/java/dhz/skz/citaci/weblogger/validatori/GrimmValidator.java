/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger.validatori;

import dhz.skz.validatori.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author kraljevic
 */
public class GrimmValidator extends ValidatorImpl {

    /**
     *
     * @return
     */
    private final Map<Integer, String> MY_MAP = createMap();
    
    private Map<Integer, String> createMap() {
        HashMap<Integer, String> mapa = new HashMap<>();
        mapa.put(1,"G1");
        mapa.put(2,"G2");
        mapa.put(3,"G3");
        mapa.put(4,"G4");
        mapa.put(5,"G5");
        mapa.put(6,"G6");
        mapa.put(7,"G7");
        mapa.put(8,"G8");
        mapa.put(9,"G9");
        mapa.put(10,"G10");
        mapa.put(11,"G11");
        mapa.put(12,"G12");
        mapa.put(13,"G13");
        mapa.put(14,"G14");
        mapa.put(15,"G15");
        mapa.put(16,"G16");
        return Collections.unmodifiableMap(mapa);
    }

    public GrimmValidator() {
        a = 1.;
        b = 0.; 
        ldl = 0.;
        opseg = 1000.;
    }

    @Override
    public int provjeraStatusa(String statusStr) {
        return 0;
    }
    @Override
    public Collection<String> opisStatusa(String statusStr) {
        int parseInt = Integer.parseInt(statusStr, 16);
        List<String> statusi = new ArrayList<>();
        for (int i=0; i<16; i++ ){
            if ((parseInt & (1<<i))== (1<<i)) statusi.add(MY_MAP.get(i));
        }
        return statusi;
    }
}
