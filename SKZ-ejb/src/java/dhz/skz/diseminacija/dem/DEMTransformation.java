/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.diseminacija.dem;

import dhz.skz.diseminacija.datatransfer.DataTransfer;
import dhz.skz.aqdb.entity.Komponenta;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.PrimateljiPodataka;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kraljevic
 */
public class DEMTransformation {

    private static final Logger log = Logger.getLogger(DEMTransformation.class.getName());
    private final SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd HH:mm");

    private final PrimateljiPodataka primatelj;
    private Komponenta komponenta;
    private Collection<ProgramMjerenja> program;
    private Map<Postaja, SortedMap<Date, Podatak>> podaciNaPostaji;
    private Date pocetak;
    private Date kraj;

    DEMTransformation(PrimateljiPodataka primatelj) {
        this.primatelj = primatelj;
        sdf.setTimeZone(LokalnaZona.getZone());

    }

    public void odradi(DataTransfer transferobj) {
        try {
            transferobj.pripremiTransfer(getNazivDatoteke());
            try (PrintStream ps = new PrintStream(transferobj.getOutputStream())) {
                ps.printf("COMPONENT %s, %s\n", komponenta.getNazivEng(), "hour");
                List<Date> sati = napraviListuSati();
                for (ProgramMjerenja pr : program) {
                    Postaja postaja = pr.getPostajaId();
                    ps.printf("STATION %s\n", postaja.getOznakaPostaje());

                    log.info(postaja.getNazivPostaje());

                    for (Date sat : sati) {
                        Integer status;
                        Float vrijednost;
                        if (podaciNaPostaji.containsKey(postaja)
                                && podaciNaPostaji.get(postaja).containsKey(sat)) {
                            Podatak pod = podaciNaPostaji.get(postaja).get(sat);
                            status = -1;
                            vrijednost = pod.getVrijednost();
                        } else {
                            status = 0;
                            vrijednost = -999.f;
                        }
                        ps.printf("%s, %8.1f, %d\n", sdf.format(sat), vrijednost, status);
                    }
                }
            }
            transferobj.zavrsiTransfer();
        } catch (IOException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

    void setKomponenta(Komponenta k) {
        this.komponenta = k;
    }

    void setProgram(Collection<ProgramMjerenja> program) {
        this.program = program;
    }

    private String getNazivDatoteke() {
        StringBuilder sb = new StringBuilder();
        sb.append(new SimpleDateFormat("YYMMddHH").format(kraj));
        sb.append("_");
        sb.append(primatelj.getMrezaId().getKratica().trim());
        sb.append("_");
        sb.append(komponenta.getIsoOznaka().trim()).append(".dem");
        return sb.toString();
    }

    void setPodaci(Collection<Podatak> podaci) {
        podaciNaPostaji = new HashMap<>();
        for (Podatak podatak : podaci) {
            Postaja postaja = podatak.getProgramMjerenjaId().getPostajaId();
            if (!podaciNaPostaji.containsKey(postaja)) {
                podaciNaPostaji.put(postaja, new TreeMap<Date, Podatak>());
            }
            podaciNaPostaji.get(postaja).put(podatak.getVrijeme(), podatak);
        }
    }

    void setPocetak(Date pocetak) {
        this.pocetak = pocetak;
    }

    void setKraj(Date kraj) {
        this.kraj = kraj;
    }

    protected List<Date> napraviListuSati() {
        List<Date> listaVremena = new ArrayList<>();

        Calendar trenutni_termin = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        trenutni_termin.setTime(pocetak);
        trenutni_termin.set(Calendar.MINUTE, 0);
        trenutni_termin.set(Calendar.SECOND, 0);
        trenutni_termin.set(Calendar.MILLISECOND, 0);

        Calendar zadnji_termin = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        zadnji_termin.setTime(kraj);
        zadnji_termin.set(Calendar.MINUTE, 0);
        zadnji_termin.set(Calendar.SECOND, 0);
        trenutni_termin.set(Calendar.MILLISECOND, 0);

        while (!trenutni_termin.after(zadnji_termin)) {
            listaVremena.add(trenutni_termin.getTime());
            trenutni_termin.add(Calendar.HOUR, 1);
        }
        return listaVremena;
    }
}
