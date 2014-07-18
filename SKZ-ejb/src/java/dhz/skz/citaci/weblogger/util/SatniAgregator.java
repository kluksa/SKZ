/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger.util;

//import dhz.utils.LokalnaZona;
import dhz.skz.citaci.weblogger.util.Flag;
import dhz.skz.citaci.weblogger.util.PodatakWl;
import dhz.skz.citaci.weblogger.validatori.Validator;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kraljevic
 */
public class SatniAgregator  {
    public static final Logger log = Logger.getLogger(SatniAgregator.class.getName());
    
    
    protected static final short MIN_OBUHVAT = 75;
    protected NizPodataka agregiraniNiz;
    protected NizPodataka minutniNiz;
    protected int korak = 1;
    protected List<Date> listaVremena;
    protected int nivo_validacije = 0;
    protected double minT = 15.0;
    protected double maxT = 25.0;
    protected float akreditacijskoPodrucje = (float) 1000.0;
    protected double ldl = 0.0;
//    private NavigableMap<Date, KalKoef> kalibracije = null;
    private String komponenta;

    public SatniAgregator() {
    }

    public double getMaxT() {
        return maxT;
    }

    public void setMaxT(double maxT) {
        this.maxT = maxT;
    }

    public double getMinT() {
        return minT;
    }

    public void setMinT(double minT) {
        this.minT = minT;
    }

    public int getKorak() {
        return korak;
    }

    public void setKorak(int korak) {
        this.korak = korak;
    }

    public void setNeagregiraniNiz(NizPodataka niz) {
        minutniNiz = niz;
    }

    public NizPodataka getAgregiraniNiz() {
        return agregiraniNiz;
    }

    protected void napraviListuVremena() {
        listaVremena = new ArrayList<>();

        Calendar trenutni_termin = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        trenutni_termin.setTime(minutniNiz.getPodaci().firstKey());
        trenutni_termin.set(Calendar.MINUTE, 0);
        trenutni_termin.set(Calendar.SECOND, 0);
        trenutni_termin.set(Calendar.MILLISECOND, 0);

        Calendar zadnji_termin = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        zadnji_termin.setTime(minutniNiz.getPodaci().lastKey());
        zadnji_termin.set(Calendar.MINUTE, 0);
        zadnji_termin.set(Calendar.SECOND, 0);
        trenutni_termin.set(Calendar.MILLISECOND, 0);

        // svakom satnom terminu dodajemo nekoliko sekundi offseta tako da pri odsijecanju
        // dobijemo niz <t-1h,t]
        // da bi ovo moglo raditi minutne vrijednosti moraju biti zapisane prije OFFSER sekunde

        while (!trenutni_termin.after(zadnji_termin)) {
            listaVremena.add(trenutni_termin.getTime());
            trenutni_termin.add(Calendar.HOUR, korak);
        }
    }

    public int getNivo_validacije() {
        return nivo_validacije;
    }

    public void setNivo_validacije(int nivo_validacije) {
        this.nivo_validacije = nivo_validacije;
    }

    public void agregiraj() {

        napraviListuVremena();
        agregiraniNiz = new NizPodataka();
        agregiraniNiz.setKljuc(minutniNiz.getKljuc());
        for (int i = 1; i < listaVremena.size(); i++) {
            Date po = listaVremena.get(i - 1);
            Date kr = listaVremena.get(i);

            NavigableMap<Date, PodatakWl> podmapa = minutniNiz.getPodaci()
                                                .subMap(po, false, kr, true);
            if (!podmapa.isEmpty()) {
                PodatakWl ps = agregiraj_podmapu(kr, podmapa);
                ps.setProgramMjerenjaId(minutniNiz.getKljuc());
                agregiraniNiz.dodajPodatak(kr, ps);
            }
        }
    }

    protected PodatakWl agregiraj_podmapu(Date vrijeme,
            NavigableMap<Date, PodatakWl> podmapa) {
        
        float kum_sum = 0;
        int count = 0;
        
        if (podmapa.isEmpty()) {
            return null;
        }
        
        Validator v = minutniNiz.getValidatori().floorEntry(vrijeme).getValue();
        
        PodatakWl agregirani = new PodatakWl();
        agregirani.setVrijeme(vrijeme);
        for (Date t : podmapa.keySet()){
            PodatakWl trenutniPodatak = podmapa.get(t);
            Float iznos = trenutniPodatak.getVrijednost();
            agregirani.dodajStatus(trenutniPodatak.getStatus());

            if (trenutniPodatak.isValid()) {
                kum_sum += iznos;
                count++;
            }
            log.log(Level.FINEST, "podmapa:: {0}:{1}:{2}:{3}:{4}", new Object[]{this.komponenta, t, iznos, kum_sum, count});
        }

        int obuhvat = 100 * count / v.getBrojMjerenjaUSatu();
        if ( obuhvat < MIN_OBUHVAT) {
            agregirani.dodajStatus(Flag.OBUHVAT);
        }

        agregirani.setObuhvat((short)obuhvat);
        if (count > 0) {
            float iznos = kum_sum / count;
            agregirani.setVrijednost(iznos);
            
        } else {
            agregirani.setVrijednost(-999.f);
        }
        log.log(Level.FINEST, "PS:: {0}:{1}::{2}::{3}", new Object[]{agregirani.getVrijeme(), agregirani.getVrijednost(), count, kum_sum});
        return agregirani;
    }
}
