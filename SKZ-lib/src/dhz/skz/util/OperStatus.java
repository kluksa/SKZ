/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.util;

import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.PodatakSirovi;

/**
 *
 * @author kraljevic
 */
public enum OperStatus {

    W1, // spare bit za upozorenje //1
    W2, // spare bit za upozorenje //2
    OKOLISNI_UVJETI, //4
    ISKLJUCENO_MJERENJE, //8
    FAULT, //16
    OPSEG, //32
    ODRZAVANJE, //64
    ERR1, // UMJERENOST //128
    ERR2, //256
    ERR3,// spare bit za nevaljani sirovi nizi od kontrole //512
    KONTROLA, //1024
    LDL, //2048
    ZERO, // 4096
    SPAN, // 8192
    KALIBRACIJA, // 16384
    NEDOSTAJE, // 32768
    SATNI_ERR1, // spare bit za nevaljani sirovi // 65536
    SATNI_ERR2, // spare bit za nevaljani satni nizeg prioritera  // 131072
    KONTROLA_SATNI, // 262144
    OBUHVAT; // 524288

    public static boolean isValid(PodatakSirovi ps) {
        switch ( ps.getNivoValidacijeId() ) {
            case 1:
                return ps.getStatus() < (1<< OperStatus.KONTROLA.ordinal());
            case 0:
            default:
                return ps.getStatus() < (1<< OperStatus.OKOLISNI_UVJETI.ordinal()+1);
        }
    }

    public static boolean isValid(Podatak p) {
        switch ( p.getNivoValidacijeId() ) {
            case 1:
                return p.getStatus() < (1<< OperStatus.KONTROLA_SATNI.ordinal());
            case 0:
            default:
                return p.getStatus() < (1<< OperStatus.SATNI_ERR1.ordinal()+1);
        }
        
    }
}
