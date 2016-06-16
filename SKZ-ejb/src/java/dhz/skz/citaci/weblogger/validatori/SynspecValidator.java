/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger.validatori;

import dhz.skz.aqdb.entity.ZeroSpan;
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
public class SynspecValidator extends ValidatorImpl {

    @Override
    public int provjeraStatusa(String statusStr) {
        return 0;
    }

    private final Map<Integer, String> MY_MAP = createMap();
    
    private Map<Integer, String> createMap() {
        HashMap<Integer, String> mapa = new HashMap<>();
        mapa.put(1,"S1");
        mapa.put(2,"S2");
        mapa.put(3,"S3");
        mapa.put(4,"S4");
        mapa.put(5,"S5");
        mapa.put(6,"S6");
        mapa.put(7,"S7");
        mapa.put(8,"S8");
        mapa.put(9,"S9");
        mapa.put(10,"S10");
        mapa.put(11,"S11");
        mapa.put(12,"S12");
        mapa.put(13,"S13");
        mapa.put(14,"S14");
        mapa.put(15,"S15");
        mapa.put(16,"S16");
        return Collections.unmodifiableMap(mapa);
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
