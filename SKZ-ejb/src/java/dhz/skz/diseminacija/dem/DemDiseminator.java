/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.diseminacija.dem;

import dhz.skz.aqdb.facades.PodatakFacade;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacade;
import dhz.skz.aqdb.entity.Komponenta;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.PrimateljProgramKljuceviMap;
import dhz.skz.aqdb.entity.PrimateljiPodataka;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.facades.PrimateljProgramKljuceviMapFacade;
import dhz.skz.config.Config;
import dhz.skz.diseminacija.DiseminatorPodataka;
import dhz.skz.diseminacija.datatransfer.DataTransfer;
import dhz.skz.diseminacija.datatransfer.DataTransferFactory;
import dhz.skz.diseminacija.datatransfer.exceptions.ProtocolNotSupported;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author kraljevic
 */
@Stateless
@LocalBean
public class DemDiseminator implements DiseminatorPodataka {
    private static final Logger log = Logger.getLogger(DemDiseminator.class.getName());

    @EJB
    private ProgramMjerenjaFacade ppf;
    @EJB
    private PodatakFacade podatakFacade;
    @EJB
    private PrimateljProgramKljuceviMapFacade ppkmf;
    
    @Inject @Config private TimeZone tzone;
    private PrimateljiPodataka primatelj;
    
    @Override
    public void salji(PrimateljiPodataka primatelj) {
        this.primatelj = primatelj;
//        Map<Komponenta, Collection<ProgramMjerenja>> programPoKomponentama = 
//                getProgramPoKomponentama(primatelj.getProgramMjerenjaCollection());
        
        
        Map<Komponenta, Collection<ProgramMjerenja>> programPoKomponentama
                = getProgramPoKomponentama(ppkmf.findAktivni(primatelj));

        DEMTransformation demT = new DEMTransformation(primatelj, tzone);
        Integer nv = 0;

        Date zadnji = getZadnji();
        Date prvi = getPrvi();

        for (Komponenta k : programPoKomponentama.keySet()) {
            log.log(Level.INFO, "KOMPONENTA= {0}", k.getFormula());
            try {
                DataTransfer dto = DataTransferFactory.getTransferObj(primatelj);
//                Collection<Podatak> podaci = podatakFacade.find(prvi, zadnji, k, nv, (short) 0);
                demT.setKomponenta(k);
                demT.setProgram(programPoKomponentama.get(k));
                demT.setPocetak(prvi);
                demT.setKraj(zadnji);
                demT.setPodaciFasada(podatakFacade);
//                demT.setPodaci(podaci);
                demT.odradi(dto);

            } catch (ProtocolNotSupported ex) {
                Logger.getLogger(DemDiseminator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Map<Komponenta, Collection<ProgramMjerenja>> getProgramPoKomponentama(Collection<PrimateljProgramKljuceviMap> program) {
        Map<Komponenta, Collection<ProgramMjerenja>> kpm = new HashMap<>();
        log.log(Level.INFO, "PROGRAM:::::");
        for (PrimateljProgramKljuceviMap ppkm : program) {
            ProgramMjerenja pm = ppkm.getProgramMjerenja();
            Komponenta k = pm.getKomponentaId();
            if (!kpm.containsKey(k)) {
                kpm.put(k, new HashSet<ProgramMjerenja>());
            }
            kpm.get(k).add(pm);
            log.log(Level.INFO, "PROGRAM:::::{0}::{1}", new Object[]{pm.getPostajaId().getNazivPostaje(), pm.getKomponentaId().getFormula()});
        }
        return kpm;
    }

    private Date getZadnji() {
        Calendar trenutni_termin = new GregorianCalendar(tzone);
        trenutni_termin.setTime(new Date());
        trenutni_termin.set(Calendar.MINUTE, 0);
        trenutni_termin.set(Calendar.SECOND, 0);
        trenutni_termin.set(Calendar.MILLISECOND, 0);
        trenutni_termin.getTime();
        return trenutni_termin.getTime();
    }

    private Date getPrvi() {
        Calendar trenutni_termin = new GregorianCalendar(tzone);
        trenutni_termin.setTime(new Date());
        trenutni_termin.set(Calendar.MINUTE, 0);
        trenutni_termin.set(Calendar.SECOND, 0);
        trenutni_termin.set(Calendar.MILLISECOND, 0);
        trenutni_termin.add(Calendar.HOUR, -2);
        trenutni_termin.getTime();
        return trenutni_termin.getTime();
    }

    @Override
    public void nadoknadi(PrimateljiPodataka primatelj, Collection<ProgramMjerenja> program, Date pocetak, Date kraj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
