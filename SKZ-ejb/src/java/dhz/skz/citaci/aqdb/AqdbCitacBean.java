/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.aqdb;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ZeroSpan;
import dhz.skz.aqdb.facades.PodatakFacade;
import dhz.skz.aqdb.facades.PodatakSiroviFacadeLocal;
import dhz.skz.citaci.CitacIzvora;
import dhz.skz.validatori.Validator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author kraljevic
 */
@Stateless
@LocalBean
public class AqdbCitacBean implements CitacIzvora {

    private static final Logger log = Logger.getLogger(AqdbCitacBean.class.getName());
    protected static final short MIN_OBUHVAT = 75;

    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;
    @EJB
    private PodatakSiroviFacadeLocal podatakSiroviFacade;
    @EJB
    private PodatakFacade podatakFacade;

    private NavigableMap<Date, Validator> validatori;
    private List<ZeroSpan> zslista = new ArrayList<>();

    @Override
    public void procitaj(IzvorPodataka izvor, Map<ProgramMjerenja, Podatak> zadnjiPodatak) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void napraviSatne(IzvorPodataka izvor) {
        log.log(Level.INFO, "POCETAK CITANJA");
        em.refresh(izvor);
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        for (ProgramMjerenja program : izvor.getProgramMjerenjaCollection()) {
            Date zadnjiSatni = podatakFacade.getZadnjiPodatak(program);
            Date zadnjiSirovi = podatakSiroviFacade.getZadnjiPodatak(program);
            Calendar kraj = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
            kraj.setTime(zadnjiSirovi);
            kraj.set(Calendar.MINUTE, 0);
            kraj.set(Calendar.SECOND, 0);
            kraj.set(Calendar.MILLISECOND, 0);

            cal.setTime(zadnjiSatni);
            cal.add(Calendar.HOUR, 1);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            while (cal.compareTo(kraj) <= 0) {
//                Collection<PodatakSirovi> sirovi = podatakSiroviFacade.getPodatkeZaSat(program, cal);
//                obradiSat(program, sirovi, cal);
                cal.add(Calendar.HOUR, 1);
            }
        }
        log.log(Level.INFO, "KRAJ CITANJA");
    }

    private void obradiSat(ProgramMjerenja program, Collection<PodatakSirovi> pod, Calendar cal) {
        if (pod == null || pod.isEmpty()) {
            return;
        }

        Validator v = validatori.floorEntry(cal.getTime()).getValue();
        Podatak podatak = new Podatak();

        float kum_sum = 0, zskum_sum = 0;
        int count = 0, zscount = 0;

        podatak.setProgramMjerenjaId(program);

        boolean zero = false;
        boolean span = false;
        zslista.clear();
        String zsvrsta = "";

//        for (PodatakSirovi p : pod) {
//            try {
//                Status s = v.toInt(p.getVrijednost(), p.getStatusString());
//                if (s.isAktivan(Flag.ZERO)) {
//                    if (!s.isAktivan(Flag.FAULT)) {
//                        zscount++;
//                        zskum_sum += p.getVrijednost();
//                    }
//                    zero = true;
//                    span = false;
//                    zsvrsta = "AZ";
//                } else if (s.isAktivan(Flag.SPAN)) {
//                    if (!s.isAktivan(Flag.FAULT)) {
//                        zscount++;
//                        zskum_sum += p.getVrijednost();
//                    }
//                    zsvrsta = "AS";
//                    span = true;
//                    zero = false;
//                } else {
//                    if (s.isValid()) {
//                        count++;
//                        kum_sum += p.getVrijednost();
//                    }
//                    if (zero || span) {
//                        if (zscount > 0) {
//                            ZeroSpan zspod = new ZeroSpan();
////                        zspod.setKomponentaId(program.getKomponentaId());
////                        zspod.setUredjajId(program.);
//                            zspod.setProgramMjerenjaId(program);
//                            zspod.setVrsta(zsvrsta);
//                            zspod.setVrijednost(zskum_sum / zscount);
//                            zslista.add(zspod);
//                        }
//                        zero = false;
//                        span = false;
//                        zscount = 0;
//                        zskum_sum = 0;
//                        zsvrsta = "";
//                    }
//                }
//                status.dodajStatus(s);
//            } catch (NevaljanStatusException ex) {
//                Logger.getLogger(AqdbCitacBean.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
////        int obuhvat = 100 * count / v.getBrojMjerenjaUSatu();
////        if (obuhvat < MIN_OBUHVAT) {
////            status.dodajFlag(Flag.OBUHVAT);
////        }
//
////        podatak.setObuhvat((short) obuhvat);
//        podatak.setStatus(status.toInt());
//
//        if (count > 0) {
//            float iznos = kum_sum / count;
//            podatak.setVrijednost(iznos);
//
//        } else {
//            podatak.setVrijednost(-999.f);
//        }
//        podatak.setVrijeme(cal.getTime());
    }

//    @Override
//    public Map<ProgramMjerenja, NizOcitanja> napraviSatne(IzvorPodataka izvor) {
//        log.log(Level.INFO, "POCETAK CITANJA");
//        em.refresh(izvor);
//        for ( ProgramMjerenja program : izvor.getProgramMjerenjaCollection()) {
//            Date zadnjiSat = podatakFacade.getZadnjiPodatak(program);
//            List<Date> lista = getListaVremena(zadnjiSat);
//            Iterator<Date> it1 = lista.iterator();
//            Iterator<Date> it2 = lista.iterator();
//            if ( it2.hasNext()) {
//                it2.next();
//                while (it2.hasNext()) {
//                    try {
//                        Date d1 = it1.next();
//                        Date d2 = it2.next();
//                        List<PodatakSirovi> pod = podatakSiroviFacade.getPodaci(program, d1, d2);
//                        obradiSat(program, pod, d1, d2);
//                    } catch (NevaljanStatusException ex) {
//                        log.log(Level.SEVERE, null, ex);
//                    }
//                    
//                }
//            }
//            
//        }
//        log.log(Level.INFO, "KRAJ CITANJA");
//        return null;   
//    }
//    private void pokupiMjerenjaSaPostaje(IzvorPodataka izvor, Postaja p, Date zadnji) {
//        Map<ProgramMjerenja, NizOcitanja> tmp = getMapaNizova(p, izvor, zadnji);
//        obradiISpremiNizove(tmp);
//    }
//    
//    private void obradiISpremiNizove(Map<ProgramMjerenja, NizOcitanja> ulaz) {
//        for (ProgramMjerenja p : ulaz.keySet()) {
//            NizOcitanja niz = ulaz.get(p);
//            if (!niz.getPodaci().isEmpty()) {
//                try {
//                    SatniAgregator a = new SatniAgregator();
//                    a.setNeagregiraniNiz(niz);
//                    a.agregiraj();
//                    NizOcitanja agr = a.getAgregiraniNiz();
//                    log.log(Level.INFO, "Pospremam Postaja {0}, komponenta {1}", new Object[]{p.getPostajaId().getNazivPostaje(), p.getKomponentaId().getFormula()});
//                    if ( ! agr.getPodaci().isEmpty() ) {
//                        podatakFacade.pospremiNiz(agr);
//                        niz.getPodaci().clear();
//                    }
//                    niz.getValidatori().clear();
//                } catch (Exception ex) {
//                    log.log(Level.SEVERE, "Postaja {0}, komponenta {1}", new Object[]{p.getPostajaId().getNazivPostaje(), p.getKomponentaId().getFormula()});
//                    throw ex;
//                }
//            }
//        }
//    }
//    
//    private Map<ProgramMjerenja, NizOcitanja> getMapaNizova(Postaja p, IzvorPodataka izvor, Date zadnji ) {
//
//        Map<ProgramMjerenja, NizOcitanja> tmp = new HashMap<>();
//        Collection<ProgramMjerenja> programNaPostajiZaIzvor = podatakFacade.getProgramNaPostajiZaIzvor(p, izvor, zadnji);
//        for ( ProgramMjerenja pm : programNaPostajiZaIzvor) {
//            log.log(Level.INFO,"Program: {0}: {1}", new Object[]{pm.getPostajaId().getNazivPostaje(), pm.getKomponentaId().getFormula()});
//            NizOcitanja np = new NizOcitanja();
//            np.setKljuc(pm);
//            np.setValidatori(validatorFac.getValidatori(pm));
//            tmp.put(pm, np);
//        }
//        return tmp;
//    }
//
//
//    private void obradiSat(ProgramMjerenja program, List<PodatakSirovi> pod, Date d1, Date d2) throws NevaljanStatusException {
//        Validator v = validatori.floorEntry(d1).getValue();
//        Podatak podatak = new Podatak();
//        
//        float kum_sum = 0, zskum_sum=0;
//        int count = 0, zscount=0;
//        Status status = new Status();
//
//        if ( pod.isEmpty() ) return;
//        
//        podatak.setProgramMjerenjaId(program);
//        
//        boolean zero = false;
//        boolean span = false;
//        zslista.clear();
//        String zsvrsta = "";
//        
//        for ( PodatakSirovi p : pod ) {
//            Status s = v.toInt(p.getVrijednost(), p.getStatusString(), 0.f);
//            if ( s.isAktivan(Flag.ZERO)) {
//                if ( !s.isAktivan(Flag.FAULT)) {
//                    zscount++;
//                    zskum_sum += p.getVrijednost();
//                }
//                zero = true;
//                span = false;
//                zsvrsta="AZ";
//            } else if ( s.isAktivan(Flag.SPAN)) {
//                if ( !s.isAktivan(Flag.FAULT)) {
//                    zscount++;
//                    zskum_sum += p.getVrijednost();
//                }
//                zsvrsta="AS";
//                span = true;
//                zero = false;
//            } else {
//                if ( s.isValid()) {
//                    count++;
//                    kum_sum += p.getVrijednost();
//                }
//                if ( zero || span ) {
//                    if ( zscount > 0) {
//                        ZeroSpan zspod = new ZeroSpan();
////                        zspod.setKomponentaId(program.getKomponentaId());
////                        zspod.setUredjajId(program.);
//                        zspod.setProgramMjerenjaId(program);
//                        zspod.setVrsta(zsvrsta);
//                        zspod.setVrijednost(zskum_sum/zscount);
//                        zslista.add(zspod);
//                    }
//                    zero = false;
//                    span = false;
//                    zscount = 0;
//                    zskum_sum = 0;
//                    zsvrsta = "";
//                }
//            }
//            
//                
//            status.dodajStatus(s);
//        }
//        
//        int obuhvat = 100 * count / v.getBrojMjerenjaUSatu();
//        if ( obuhvat < MIN_OBUHVAT) {
//            status.dodajFlag(Flag.OBUHVAT);
//        }
//
//        podatak.setObuhvat((short)obuhvat);
//        podatak.setStatus(status.toInt());
//
//        if (count > 0) {
//            float iznos = kum_sum / count;
//            podatak.setVrijednost(iznos);
//            
//        } else {
//            podatak.setVrijednost(-999.f);
//        }
//        podatak.setVrijeme(d2);
//        
//        
//    }
//
//    private List<Date> getListaVremena(Date zadnjiSat) {
//        List<Date> listaVremena = new ArrayList<>();
//
//        Calendar trenutni_termin = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
//        trenutni_termin.setTime(zadnjiSat);
//        trenutni_termin.set(Calendar.MINUTE, 0);
//        trenutni_termin.set(Calendar.SECOND, 0);
//        trenutni_termin.set(Calendar.MILLISECOND, 0);
//
//        Calendar zadnji_termin = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
//        zadnji_termin.setTime(new Date());
//        zadnji_termin.set(Calendar.MINUTE, 0);
//        zadnji_termin.set(Calendar.SECOND, 0);
//        trenutni_termin.set(Calendar.MILLISECOND, 0);
//
//        // svakom satnom terminu dodajemo nekoliko sekundi offseta tako da pri odsijecanju
//        // dobijemo niz <t-1h,t]
//        // da bi ovo moglo raditi minutne vrijednosti moraju biti zapisane prije OFFSER sekunde
//
//        while (!trenutni_termin.after(zadnji_termin)) {
//            listaVremena.add(trenutni_termin.getTime());
//            trenutni_termin.add(Calendar.HOUR, 1);
//        }
//        return listaVremena;
//    }    

    @Override
    public Collection<PodatakSirovi> dohvatiSirove(ProgramMjerenja program, Date pocetak, Date kraj, boolean p, boolean k) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
