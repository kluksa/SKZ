/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.util;

import dhz.skz.aqdb.entity.NivoValidacije;

/**
 *
 * @author kraljevic
 */
public enum OperStatus {

    W1, // spare bit za upozorenje
    W2, // spare bit za upozorenje
    ISKLJUCENO_MJERENJE,
    FAULT,
    OPSEG,
    OKOLISNI_UVJETI,
    ODRZAVANJE,
    ERR1, // UMJERENOST
    ERR2, // spare bit za nevaljani sirovi nizi od kontrole
    KONTROLA_PROVEDENA,
    KONTROLA_ODBACENO,
    LDL,
    ZERO,
    SPAN,
    KALIBRACIJA,
    NEDOSTAJE,
    ERR3, // spare bit za nevaljani sirovi
    SATNI_ERR1, // spare bit za nevaljani satni nizeg prioritera 
    KONTROLA_SATNI,
    OBUHVAT;

    public static boolean isValidSirovi(int i, NivoValidacije nivo) {
        boolean valid = false;
        int falseMaska;
        int trueMaska;
        
        switch (nivo.getId()) {
            case 0:
                falseMaska = 0b00000111110111011100;
                trueMaska =  0;
                break;
            case 1:
                falseMaska = 0b00000111110000000000;
                trueMaska  = 0b00000000001000000000;
                break;
            default:
                return false;
        }
        return ((i & falseMaska) == 0) && ((i & trueMaska) == trueMaska);
    }

    public static boolean isValidSatni(int i, NivoValidacije nivo) {
        boolean valid = false;
        int falseMaska;
        int trueMaska;
        switch (nivo.getId()) {
            case 0:
                falseMaska = 0b10000000000000000000;
                trueMaska = 0;
                break;
            case 1:
                falseMaska = 0b11000000000000000000;
                trueMaska = 0;
                break;
            default:
                return false;
        }
        return ((i & falseMaska) == 0) && ((i & trueMaska) == trueMaska);
        
    }
}
