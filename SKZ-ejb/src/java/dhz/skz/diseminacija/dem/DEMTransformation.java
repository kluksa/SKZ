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
import dhz.skz.citaci.SatniIterator;
import dhz.skz.diseminacija.datatransfer.exceptions.DataTransferException;
import dhz.skz.util.OperStatus;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
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
    private final TimeZone tzone;

    DEMTransformation(PrimateljiPodataka primatelj, TimeZone tzone) {
        this.primatelj = primatelj;
        this.tzone = tzone;
        sdf.setTimeZone(LokalnaZona.getZone());

    }

    public void odradi(DataTransfer transferobj) {
        if ( transferobj == null) {
            log.log(Level.SEVERE, null, "transferobj == null");
            throw new IllegalArgumentException("transferobj == null");
        }
        try {
            transferobj.pripremiTransfer(getNazivDatoteke());
            try (PrintStream ps = new PrintStream(transferobj.getOutputStream())) {
                
                ps.printf("COMPONENT %s, %s\n", komponenta.getNazivEng(), "hour");
                for (ProgramMjerenja pr : program) {
                    Postaja postaja = pr.getPostajaId();
                    ps.printf("STATION %s\n", postaja.getOznakaPostaje());

                    log.info(postaja.getNazivPostaje());

                    SatniIterator sat = new SatniIterator(pocetak, kraj, tzone);
                    while (sat.next()) {
                        Integer status;
                        Date vr = sat.getVrijeme();
                        Double vrijednost;
                        if (podaciNaPostaji.containsKey(postaja)
                                && podaciNaPostaji.get(postaja).containsKey(vr)) {
                            Podatak pod = podaciNaPostaji.get(postaja).get(vr);
                            if (OperStatus.isValid(pod)) {
                                status = -1;
                                vrijednost = pod.getVrijednost();
                            } else {
                                status = 0;
                                vrijednost = -999.;
                            }

                        } else {
                            status = 0;
                            vrijednost = -999.;
                        }
                        ps.printf("%s, %8.1f, %d\n", sdf.format(vr), vrijednost, status);
                    }
                }
            }
            transferobj.zavrsiTransfer();
        } catch (IOException | DataTransferException ex) {
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
}
