/*
 * Copyright (C) 2015 kraljevic
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dhz.skz.citaci;

import dhz.skz.aqdb.entity.NivoValidacije;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ZeroSpan;
import dhz.skz.aqdb.facades.PodatakFacade;
import dhz.skz.aqdb.facades.PodatakSiroviFacadeLocal;
import dhz.skz.aqdb.facades.ZeroSpanFacade;
import dhz.skz.config.Config;
import dhz.skz.util.MijesaniProgramiException;
import dhz.skz.util.OperStatus;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * @author kraljevic
 */
@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.BEAN)
public class MinutniUSatne {

    private static final Logger log = Logger.getLogger(MinutniUSatne.class.getName());
    @Resource
    private EJBContext context;
    @EJB
    private ZeroSpanFacade zeroSpanFacade;
    @EJB
    private PodatakFacade podatakFacade;
    @EJB
    private PodatakSiroviFacadeLocal podatakSiroviFacade;
    @Inject
    @Config
    private TimeZone tzone;

    private AgPodatak mjerenje, zero, span;
    private int ocekivaniBroj;
    private NivoValidacije nivo;
    private Date zavrsnoVrijeme;
    private ProgramMjerenja program;

    private class AgPodatak {

        Double iznos = 0.;
        int status = 0;
        int broj = 0;
    }

    private void reset() {
        mjerenje = new AgPodatak();
        zero = new AgPodatak();
        span = new AgPodatak();
    }

    private void agregiraj(Collection<PodatakSirovi> podaci) throws MijesaniProgramiException {
        for (PodatakSirovi ps : podaci) {
            if (ps.getProgramMjerenjaId().equals(program)) {
                if (OperStatus.isValidSirovi(ps.getStatus(), nivo)) {
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
            } else {
                throw (new MijesaniProgramiException());
            }
        }

    }

    private Podatak izracunajPodatak() {
        int obuhvat = 100 * mjerenje.broj / ocekivaniBroj;
        if (obuhvat < 75) {
            mjerenje.status |= (1 << OperStatus.OBUHVAT.ordinal());
        }
        Podatak agregirani = new Podatak();
        agregirani.setProgramMjerenjaId(program);
        agregirani.setVrijeme(zavrsnoVrijeme);
        agregirani.setNivoValidacijeId(nivo);
        agregirani.setStatus(mjerenje.status);

        agregirani.setObuhvat(obuhvat);
        if (mjerenje.broj > 0) {
            agregirani.setVrijednost(mjerenje.iznos / mjerenje.broj);
        } else {
            agregirani.setVrijednost(-999.);
        }
        return agregirani;
    }

    private HashSet<ZeroSpan> izracunajZeroSpan() {
        HashSet<ZeroSpan> zs = new HashSet<>();
        if (zero.broj != 0) {
            ZeroSpan z = new ZeroSpan();
            z.setProgramMjerenjaId(program);
            z.setVrijeme(zavrsnoVrijeme);
            z.setVrijednost(zero.iznos / zero.broj);
            z.setVrsta("AZ");
            zs.add(z);
        }
        if (span.broj != 0) {
            ZeroSpan s = new ZeroSpan();
            s.setProgramMjerenjaId(program);
            s.setVrijeme(zavrsnoVrijeme);
            s.setVrijednost(zero.iznos / zero.broj);
            s.setVrsta("AS");
            zs.add(s);
        }
        return zs;
    }

    public void spremiSatneIzSirovih(ProgramMjerenja program, NivoValidacije nv) throws Throwable {
        this.nivo = nv;
        this.ocekivaniBroj = 60;
        this.program = program;
        Date zadnjiSatni = podatakFacade.getZadnjiPodatak(program);
        Date zadnjiSirovi = podatakSiroviFacade.getZadnjiPodatak(program);
        log.log(Level.INFO, "ZADNJI SATNI: {0}; SIROVI: {1}", new Object[]{zadnjiSatni, zadnjiSirovi});
        UserTransaction utx = context.getUserTransaction();
        
        SatniIterator sat = new SatniIterator(zadnjiSatni, zadnjiSirovi, tzone);
        Date pocetnoVrijeme = sat.getVrijeme();
        try {
            utx.begin();
            int n = 1;
            while (sat.next()) {
                zavrsnoVrijeme = sat.getVrijeme();
                reset();
                Collection<PodatakSirovi> sirovi = podatakSiroviFacade.getPodaci(program, pocetnoVrijeme, zavrsnoVrijeme, false, true);
                if (!sirovi.isEmpty()) {
                    try {
                        agregiraj(sirovi);
                        podatakFacade.spremi(izracunajPodatak());
                        zeroSpanFacade.spremi(izracunajZeroSpan());
                    } catch (MijesaniProgramiException ex) {
                        log.log(Level.SEVERE, null, ex);
                    }
                }
                if ((n % 24) == 0) {
                    log.log(Level.INFO, "SAT: {0}", zavrsnoVrijeme);
                    utx.commit();
                    utx.begin();
                }
                n++;
                pocetnoVrijeme = zavrsnoVrijeme;
            }
            utx.commit();
        } catch (Throwable ex) {
            log.log(Level.SEVERE, null, ex);
            utx.rollback();
            throw ex;
        }
    }
}
