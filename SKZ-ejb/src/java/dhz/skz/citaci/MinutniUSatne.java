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

import dhz.skz.aqdb.entity.Granice;
import dhz.skz.aqdb.entity.IzvorProgramKljuceviMap;
import dhz.skz.aqdb.facades.PodatakFacade;
import dhz.skz.aqdb.facades.PodatakSiroviFacade;
import dhz.skz.aqdb.facades.ZeroSpanFacade;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ZeroSpan;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacade;
import dhz.skz.config.Config;
import dhz.skz.util.MijesaniProgramiException;
import dhz.skz.util.OperStatus;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
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
    private PodatakSiroviFacade podatakSiroviFacade;
    @EJB
    private ProgramMjerenjaFacade programMjerenjaFacade;

    @Inject
    @Config
    private TimeZone tzone;

    private AgPodatak mjerenje, zero, span;
    private int ocekivaniBroj;
    private Integer nivo = 0;
    private Date zavrsnoVrijeme;
    private ProgramMjerenja program;

    private void spremiVjetarIzSirovih(Vjetar get, Integer nv) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void napraviViseProsjeke(ProgramMjerenja pm, Integer nv) {
        for (Granice granica : pm.getKomponentaId().getGraniceCollection()){
            String agregacija = granica.getAgregacijeId().getOznaka();
            switch(agregacija){
                case "1day":
                    break;
                case "8hour":
                    break;
                default:
                    break;
            }
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

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
                try {
                    Integer status = ps.getStatus();
                    if (OperStatus.isValid(ps)) {
                        mjerenje.broj++;
                        mjerenje.iznos += ps.getVrijednost();
                    } else if ((status & (1 << OperStatus.ZERO.ordinal())) != 0) {
                        zero.broj++;
                        zero.iznos += ps.getVrijednost();
                        zero.status |= status;
                    } else if ((status & (1 << OperStatus.SPAN.ordinal())) != 0) {
                        span.broj++;
                        span.iznos += ps.getVrijednost();
                        span.status |= status;
                    }
                    mjerenje.status |= status;
                } catch (NullPointerException ex) {
                    log.log(Level.SEVERE, "{0},{1},{2}", new Object[]{ps.getProgramMjerenjaId(), ps.getStatus(), nivo});
                    throw ex;
                }
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
            s.setVrijednost(span.iznos / span.broj);
            s.setVrsta("AS");
            zs.add(s);
        }
        return zs;
    }

    class Vjetar {

        ProgramMjerenja programWS;
        ProgramMjerenja programWD;
    }

    class VjetarKljuc {

        Postaja po;
        int usporedno;
    }

    public void napraviSatne(Integer nv) {
        Map<VjetarKljuc, Vjetar> vj = new HashMap<>();
        for (ProgramMjerenja pm : programMjerenjaFacade.findAll()) {
//            if ( !(pm.getKomponentaId().getFormula().equals("WS") || pm.getKomponentaId().getFormula().equals("WD"))) {
            spremiSatneIzSirovih(pm, nv);
//            napraviViseProsjeke(pm, nv);
//            } else {
//                VjetarKljuc kljuc = new VjetarKljuc();
//                kljuc.po = pm.getPostajaId();
//                kljuc.usporedno = pm.getUsporednoMjerenje();
//                if ( !vj.containsKey(kljuc)) {
//                    vj.put(kljuc, new Vjetar());
//                }
//                Vjetar get = vj.get(kljuc);
//                if ( pm.getKomponentaId().getFormula().equals("WS")) {
//                    get.programWS = pm;
//                } else if ( pm.getKomponentaId().getFormula().equals("WD")) {
//                    get.programWD = pm;
//                } else {
//                    log.log(Level.SEVERE, "POGRESKA");
//                }
//                spremiVjetarIzSirovih(get,nv);
//            }
        }
    }

    public void spremiSatneIzSirovih(ProgramMjerenja program, Integer nv, Date pocetak, Date kraj) {
        this.program = program;
        if (program.getIzvorProgramKljuceviMap() == null || program.getIzvorProgramKljuceviMap().getBrojUSatu() == null) {
            this.ocekivaniBroj = 60;
        } else {
            this.ocekivaniBroj = program.getIzvorProgramKljuceviMap().getBrojUSatu();
        }

        this.nivo = nv;

        UserTransaction utx = context.getUserTransaction();

        SatniIterator sat = new SatniIterator(pocetak, kraj, tzone);
        Date pocetnoVrijeme = sat.getVrijeme();
        try {
            utx.begin();
            int n = 1;
            while (sat.next()) {
                zavrsnoVrijeme = sat.getVrijeme();
                log.log(Level.INFO, "Vrijeme: {0}", new Object[]{zavrsnoVrijeme});
                reset();
                Collection<PodatakSirovi> sirovi = podatakSiroviFacade.getPodaci(program, pocetnoVrijeme, zavrsnoVrijeme, false, true);
                if (!sirovi.isEmpty()) {
                    try {
                        agregiraj(sirovi);
                        podatakFacade.spremi(izracunajPodatak());
                        IzvorProgramKljuceviMap ipkm = program.getIzvorProgramKljuceviMap();
                        if (ipkm != null) {
                            if ((ipkm.getZasebniZS() != null) && !ipkm.getZasebniZS()) {
                                zeroSpanFacade.spremi(izracunajZeroSpan());
                            }
                        }
                    } catch (MijesaniProgramiException ex) {
                        log.log(Level.SEVERE, null, ex);
                    }
                }
                if ((n % 24) == 0) {
                    log.log(Level.FINE, "SAT: {0}", zavrsnoVrijeme);
                    utx.commit();
                    utx.begin();
                }
                n++;
                pocetnoVrijeme = zavrsnoVrijeme;
            }
            utx.commit();
        } catch (Throwable ex) {
            log.log(Level.SEVERE, null, ex);
            try {
                utx.rollback();
            } catch (IllegalStateException | SecurityException | SystemException ex1) {
                log.log(Level.SEVERE, null, ex1);
            }
        }
    }

    public void spremiSatneIzSirovih(ProgramMjerenja program, Integer nv) {

        PodatakSirovi zadnjiSirovi = podatakSiroviFacade.getZadnji(program);

        if (zadnjiSirovi == null) {
            log.log(Level.INFO, "NEMA PODATAKA ZA AGREGACIJU ZA PROGRAM {0}", program.getId());
            return;
        }

        Date zadnjiSatni = podatakFacade.getVrijemeZadnjeg(program, nv);
        if (zadnjiSatni == null) {
            PodatakSirovi ps = podatakSiroviFacade.getPrvi(program);
            zadnjiSatni = ps.getVrijeme();
            log.log(Level.INFO, "NIJE BILO UPISANIH SATNIH PODATAKA ZA PROGRAM {0}", program.getId());
        }

        log.log(Level.INFO, "ZADNJI SATNI: {0}; SIROVI: {1}", new Object[]{zadnjiSatni, zadnjiSirovi});
        spremiSatneIzSirovih(program, nv, zadnjiSatni, zadnjiSirovi.getVrijeme());
    }
}
