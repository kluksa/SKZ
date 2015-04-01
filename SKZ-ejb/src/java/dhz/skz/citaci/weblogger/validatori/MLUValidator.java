/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger.validatori;

import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.citaci.weblogger.exceptions.NevaljanStatusException;
import dhz.skz.citaci.weblogger.util.Flag;
import dhz.skz.citaci.weblogger.util.OperStatus;
import dhz.skz.citaci.weblogger.util.Status;

/**
 *
 * @author kraljevic
 */
public class MLUValidator implements Validator {

    @Override
    public Status getStatus(Float iznos, String statusStr) throws NevaljanStatusException {
        Status s = new Status();
        String[] st = statusStr.split(";");
        int ss = Integer.parseInt(st[0]);
        int bs = Integer.parseInt(st[1]);
        int fs = Integer.parseInt(st[2]);
        int nc = Integer.parseInt(st[3]);

        if (iznos == -9999.f) {
            s.dodajFlag(Flag.NEDOSTAJE);
        }
        if (fs != 0) {
            s.dodajFlag(Flag.FAULT);
        }
        if (ss != 0) {
            s.dodajFlag(Flag.FAULT);
        }
        if ((bs & 1) == 1) {
            s.dodajFlag(Flag.MAINTENENCE);
        }
        if ((bs & 2) == 2) {
            s.dodajFlag(Flag.ZERO);
        }
        if ((bs & 4) == 4) {
            s.dodajFlag(Flag.SPAN);
        }
        if ((bs & 8) == 8) {
            s.dodajFlag(Flag.MAINTENENCE);
        }
        if ((bs & 16) == 16) {
            s.dodajFlag(Flag.MAINTENENCE);
        }
        if ((bs & 32) == 32) {
        }
        if ((bs & 64) == 64) {
        }
        if ((bs & 128) == 128) {
        }
        return s;
    }

    @Override
    public Status getStatus(String status) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getBrojUSatu() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void validiraj(PodatakSirovi ps) {

        String[] st = ps.getStatusString().split(";");
        int ss = Integer.parseInt(st[0]);
        int bs = Integer.parseInt(st[1]);
        int fs = Integer.parseInt(st[2]);
        int nc = Integer.parseInt(st[3]);

        if (ps.getVrijednost() == -9999.f) {
            ps.setGreska(Flag.NEDOSTAJE.ordinal());
        } else {

            if (fs != 0) {
                ps.setGreska(Flag.FAULT.ordinal());
            }

            if (ss != 0) {
                ps.setGreska(Flag.FAULT.ordinal());
            }

            if (bs == 0) {
                ps.setStatus(OperStatus.MJERENJE.ordinal());
            } else {
                if ((bs & 1) == 1) {
                    ps.setStatus(OperStatus.KALIBRACIJA.ordinal());
                } else if ((bs & 2) == 2) {
                    ps.setStatus(OperStatus.ZERO.ordinal());
                } else if ((bs & 4) == 4) {
                    ps.setStatus(OperStatus.SPAN.ordinal());
                } else if ((bs & 8) == 8) {
                    ps.setStatus(OperStatus.ODRZAVANJE.ordinal());
                } else {
                    ps.setStatus(OperStatus.NEDEFINIRANO.ordinal());
                    ps.setGreska(Flag.FAULT.ordinal());
                }
            }
        }
    }
}
