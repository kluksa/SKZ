/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.util;

import dhz.skz.aqdb.entity.NivoValidacije;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ZeroSpan;
import dhz.skz.citaci.weblogger.util.MijesaniProgramiException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 *
 * @author kraljevic
 */
public class AgregatorPodatka {

    private AgPodatak mjerenje, zero, span;
    private final int falseMask; 
    private final int trueMask;
    private final int ocekivaniBroj;
    private final NivoValidacije nivo;
    private Date vrijeme;
    private ProgramMjerenja pm;

    public boolean hasZero() {
        return zero.broj > 0;
    }

    public boolean hasSpan() {
        return span.broj > 0;
    }

    public boolean hasPodatak() {
        return mjerenje.broj > 0;
    }

    private class AgPodatak {

        Float iznos = 0.f;
        int status = 0;
        int broj = 0;
    }

    public AgregatorPodatka(NivoValidacije n, int ocekivaniBroj) {
        this.nivo = n;
        trueMask = 0;
        this.ocekivaniBroj = ocekivaniBroj;
        switch (n.getId()) {
            case 0:
                falseMask = ~0;
                break;
            case 1:
                falseMask = ~((1 << OperStatus.KONTROLA_PROVEDENA.ordinal()) - 1);
                break;
            default:
                falseMask = ~0;
        }
    }
    
    private void reset() {
        mjerenje = new AgPodatak();
        zero = new AgPodatak();
        span = new AgPodatak();
    }

    public Podatak agregiraj(Collection<PodatakSirovi> podaci, Date vrijeme) throws MijesaniProgramiException {
        reset();
        Iterator<PodatakSirovi> it = podaci.iterator();
        Podatak agregirani = null;
        this.vrijeme = vrijeme;
        if (it.hasNext()) {
            PodatakSirovi p = it.next();
            pm = p.getProgramMjerenjaId();
            dodaj(p);
            while (it.hasNext()) {
                p = it.next();
                if (!p.getProgramMjerenjaId().equals(pm)) {
                    throw (new MijesaniProgramiException());
                }
                dodaj(p);
            }
        }
        return agregirani;
    }

    private void dodaj(PodatakSirovi ps) {
        if (((ps.getStatus() & falseMask) == 0) && ((ps.getStatus() & trueMask) == trueMask)) {
            mjerenje.broj++;
            mjerenje.iznos += ps.getVrijednost();
        } else if ((ps.getStatus() & (1 << OperStatus.ZERO.ordinal())) != 0) {
            zero.broj++;
            zero.iznos += ps.getVrijednost();
            zero.status |= ps.getStatus();
        } else if ((ps.getStatus() & (1 << OperStatus.SPAN.ordinal())) != 0) {
            span.broj++;
            span.iznos += ps.getVrijednost();
            span.status |= ps.getStatus();
        }
        mjerenje.status |= ps.getStatus();
    }

    public Podatak getPodatak() {
        if (mjerenje.broj > 0) {
            if (mjerenje.broj < 3 * this.ocekivaniBroj / 4) {
                mjerenje.status |= (1 << OperStatus.OBUHVAT.ordinal());
            }
            Podatak agregirani = new Podatak();
            agregirani.setProgramMjerenjaId(pm);
            agregirani.setVrijeme(vrijeme);
            agregirani.setNivoValidacijeId(nivo);
            agregirani.setStatus(mjerenje.status);
            agregirani.setObuhvat((short) (100 * mjerenje.broj / ocekivaniBroj));
            return agregirani;
        } else {
            return null;
        }
    }

    public ZeroSpan getZero() {
        if (zero.broj == 0) {
            return null;
        }
        ZeroSpan z = new ZeroSpan();
        z.setVrijednost(zero.iznos / zero.broj);
        z.setVrsta("AZ");
        return z;
    }

    public ZeroSpan getSpan() {
        if (zero.broj == 0) {
            return null;
        }
        ZeroSpan s = new ZeroSpan();
        s.setVrijednost(zero.iznos / zero.broj);
        s.setVrsta("AS");
        return s;
    }
}
