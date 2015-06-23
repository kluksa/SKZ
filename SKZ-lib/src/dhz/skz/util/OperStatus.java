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

    W1, // spare bit za upozorenje
    W2, // spare bit za upozorenje
    OKOLISNI_UVJETI,
    ISKLJUCENO_MJERENJE,
    FAULT,
    OPSEG,
    ODRZAVANJE,
    ERR1, // UMJERENOST
    ERR2,
    ERR3,// spare bit za nevaljani sirovi nizi od kontrole
    KONTROLA,
    LDL,
    ZERO,
    SPAN,
    KALIBRACIJA,
    NEDOSTAJE,
    SATNI_ERR1, // spare bit za nevaljani sirovi
    SATNI_ERR2, // spare bit za nevaljani satni nizeg prioritera 
    KONTROLA_SATNI,
    OBUHVAT;

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
