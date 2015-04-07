/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger.util;

import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.ZeroSpan;
import dhz.skz.citaci.weblogger.exceptions.NevaljanStatusException;
import dhz.skz.citaci.weblogger.validatori.Validator;
import java.util.Collection;
import java.util.EnumMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kraljevic
 */
public class AgregatorPodatka {

    EnumMap<Status.ModRada, AgPodatak> pod = new EnumMap<>(Status.ModRada.class);
    EnumMap<OperStatus, AgPodatak> pod2 = new EnumMap<>(OperStatus.class);
    private int mjBroj;
    private float mjIznos;
    private int mjStatus;

    private Collection<OperStatus> dodatniAgregati;
    private int dodatniAgregatiMask;
    private final int neprihvatljiviMask;

    enum Nivo {

        L1, L2
    };

    int mjerenjeMask = (1 >> OperStatus.ZERO.ordinal())
            | (1 >> OperStatus.SPAN.ordinal())
            | (1 >> OperStatus.KALIBRACIJA.ordinal())
            | (1 >> OperStatus.G5.ordinal())
            | (1 >> OperStatus.G6.ordinal());

    int invalidMask;
    int maxPrihvatljivi;

    private class AgPodatak {

        Float iznos = 0.f;
        int status = 0;
        int broj = 0;
    }

    private int minimalniBroj = 45;

    public void dodaj2(PodatakSirovi ps) {

        if ((ps.getStatus() & neprihvatljiviMask) == 0) {
            mjBroj++;
            mjIznos += ps.getVrijednost();
            if (mjBroj >= minimalniBroj) {
                mjStatus &= ~(1 >> OperStatus.OBUHVAT.ordinal());
            }
        } else if ((ps.getStatus() & dodatniAgregatiMask) != 0) {
            for (OperStatus o : dodatniAgregati) {
                if ((ps.getStatus() & (1 >> o.ordinal())) != 0) {
                    pod2.get(o).broj++;
                    pod2.get(o).iznos += ps.getVrijednost();
                    pod2.get(o).status |= ps.getStatus();
                }
            }
        }
        mjStatus |= ps.getStatus();

    }
    private Validator validator;

    public void setValidator(Validator validator) {
        this.validator = validator;
        minimalniBroj = 3 * validator.getBrojUSatu() / 4;
    }

    public AgregatorPodatka() {
        reset();
    }

    public AgregatorPodatka(Nivo n) {
    }

    public AgregatorPodatka(Collection<OperStatus> neprihvatljivi) {
    }

    public void reset() {
        mjStatus = (1 >> (OperStatus.OBUHVAT.ordinal() - 1));

        for (Status.ModRada mr : Status.ModRada.values()) {
            pod.put(mr, new AgPodatak());
        }
        pod.get(Status.ModRada.MJERENJE).status.dodajFlag(Flag.OBUHVAT);
    }

    public void dodaj(PodatakSirovi ps) {
        try {
            Status s = validator.getStatus(ps.getVrijednost(), ps.getStatusString());
            Status.ModRada vrsta = s.getModRada();

            switch (vrsta) {
                case ZERO:
                case SPAN:
                    pod.get(vrsta).broj = pod.get(vrsta).broj + 1;
                    pod.get(vrsta).iznos = pod.get(vrsta).iznos + ps.getVrijednost();
                    pod.get(vrsta).status.dodajStatus(s);
                    pod.get(Status.ModRada.MJERENJE).status.dodajStatus(s);
                    break;
                case MJERENJE:
                    if (s.isValid()) {
                        pod.get(vrsta).broj = pod.get(vrsta).broj + 1;
                        pod.get(vrsta).iznos = pod.get(vrsta).iznos + ps.getVrijednost();
                        if (pod.get(vrsta).broj >= minimalniBroj) {
                            pod.get(Status.ModRada.MJERENJE).status.iskljuciFlag(Flag.OBUHVAT);
                        }
                    }
                    pod.get(vrsta).status.dodajStatus(s);
                    break;
                default:
                    break;
            }
        } catch (NevaljanStatusException ex) {
            Logger.getLogger(AgregatorPodatka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public void dodaj(PodatakSirovi ps) {
//
//        float vrijednost = ps.getVrijednost();
//        Status s = validator.getStatus(ps.getStatusString());
//        vrste.add(s.getModRada());
//        if (!kumulativniStatus.containsKey(s.getModRada())) {
//            kumulativnaSuma.put(s.getModRada(), 0.f);
//            broj.put(s.getModRada(), 0);
//            Status st = new Status();
//            st.dodajFlag(Flag.OBUHVAT);
//            kumulativniStatus.put(s.getModRada(), st);
//        }
//        if (s.isValid()) {
//            kumulativnaSuma.put(s.getModRada(), kumulativnaSuma.get(s.getModRada()) + vrijednost);
//            int br = broj.get(s.getModRada()) + 1;
//            broj.put(s.getModRada(), br);
//            if (br >= minimalniBroj) {
//                kumulativniStatus.get(s.getModRada()).iskljuciFlag(Flag.OBUHVAT);
//            }
//        }
//        if (s.getModRada() == Status.ModRada.MJERENJE) {
//            kumulativniStatus.get(Status.ModRada.MJERENJE).dodajStatus(s);
//        } else {
//            kumulativniStatus.get(Status.ModRada.MJERENJE).dodajStatus(s);
//            kumulativniStatus.get(s.getModRada()).dodajStatus(s);
//        }
//    }
    private Float getIznos(Status.ModRada mod) {
        if (pod.get(mod).broj != 0) {
            return pod.get(mod).iznos / pod.get(mod).broj;
        } else {
            return -999.f;
        }
    }

    private short getObuhvat(Status.ModRada mod) {
        return (short) (100 * pod.get(mod).broj / validator.getBrojUSatu());
    }

    private Status getStatus(Status.ModRada mod) {
        return pod.get(mod).status;
    }

    public Podatak getPodatak() {
        Podatak p = new Podatak();
        p.setVrijednost(getIznos(Status.ModRada.MJERENJE));
        p.setStatus(getStatus(Status.ModRada.MJERENJE).toInt());
        p.setObuhvat(getObuhvat(Status.ModRada.MJERENJE));
        return p;
    }

    public ZeroSpan getZero() {
        return getZeroSpan(Status.ModRada.ZERO);
    }

    public ZeroSpan getSpan() {
        return getZeroSpan(Status.ModRada.SPAN);
    }

    private ZeroSpan getZeroSpan(Status.ModRada mod) {

        ZeroSpan p = new ZeroSpan();
        p.setVrijednost(getIznos(mod));
        if (mod == Status.ModRada.ZERO) {
            p.setVrsta("AZ");
        } else if (mod == Status.ModRada.SPAN) {
            p.setVrsta("AS");
        } else {
            //throw new Exception();
        }
        return p;

    }

    public boolean imaMjerenje() {
        return pod.get(Status.ModRada.MJERENJE).broj != 0;
    }

    public boolean imaZero() {
        return pod.get(Status.ModRada.ZERO).broj != 0;
    }

    public boolean imaSpan() {
        return pod.get(Status.ModRada.SPAN).broj != 0;
    }
}
