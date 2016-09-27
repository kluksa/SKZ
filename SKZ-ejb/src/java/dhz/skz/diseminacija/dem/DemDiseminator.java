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
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.PrimateljProgramKljuceviMap;
import dhz.skz.aqdb.entity.PrimateljiPodataka;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.facades.PrimateljProgramKljuceviMapFacade;
import dhz.skz.config.Config;
import dhz.skz.diseminacija.DiseminatorPodataka;
import dhz.skz.diseminacija.datatransfer.DataTransfer;
import dhz.skz.diseminacija.datatransfer.DataTransferFactory;
import dhz.skz.diseminacija.datatransfer.exceptions.DataTransferException;
import dhz.skz.diseminacija.datatransfer.exceptions.ProtocolNotSupported;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    @Inject
    @Config
    private TimeZone tzone;
    private PrimateljiPodataka primatelj;

    @Override
    public void salji(PrimateljiPodataka primatelj) {
        log.log(Level.INFO, "Pocetak diseminacije za {0}", new Object[]{primatelj.getNaziv()});
        try {
            Collection<PrimateljProgramKljuceviMap> aktivniProgrami = ppkmf.findAktivni(primatelj);
            DataTransfer dto = DataTransferFactory.getTransferObj(primatelj);
            Map<Komponenta, Map<Postaja, PrimateljProgramKljuceviMap>> mapa = getMapaPrograma(aktivniProgrami);
            for (Komponenta k : mapa.keySet()) {
                try {
                    URL url = new URL(primatelj.getUrl());
                    URI u = url.toURI().resolve(getNazivDatoteke(primatelj, k));
                    OutputStream outputStream = dto.getOutputStream(u.toURL());
                    try (DemWriter dw = new DemWriter(new BufferedWriter(new OutputStreamWriter(outputStream)),k)) {
                        for (Postaja p : mapa.get(k).keySet()) {
                            PrimateljProgramKljuceviMap ppkm = mapa.get(k).get(p);
                            ProgramMjerenja pm = ppkm.getProgramMjerenja();
                            Date pocetak = ppkm.getZadnjiPoslani();
                            log.log(Level.INFO, "DemDiseminator:salji pm={0}, pocetak={1}", new Object[]{pm.getId(), pocetak});
                            List<Podatak> podaciOd = podatakFacade.getPodaciOd(pm, pocetak, 0);
                            if ( !podaciOd.isEmpty()){
                                dw.printPostajaPodaci(pm, podaciOd);
                                Date zadnji = dw.getZadnjiZapis().get(pm);
                                
                                if ( zadnji != null ) {
                                    ppkm.setZadnjiPoslani(zadnji);
                                    ppkmf.edit(ppkm);
                                }
                            }
                        }
                    } catch (PodatakWriterException ex) {
                        log.log(Level.SEVERE, null, ex);
                    }
                    dto.zavrsiTransfer();
                    
                } catch (IOException ex) {
                    log.log(Level.SEVERE, null, ex);
                } catch (URISyntaxException ex) {
                    Logger.getLogger(DemDiseminator.class.getName()).log(Level.SEVERE, null, ex);
                } 

            }
        } catch (ProtocolNotSupported ex) {
            log.log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            log.log(Level.SEVERE, null, ex);
        } catch (RuntimeException ex) {
            log.log(Level.SEVERE, null, ex);
        } catch (DataTransferException ex) {
            Logger.getLogger(DemDiseminator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DemDiseminator.class.getName()).log(Level.SEVERE, null, ex);
        } 
        log.log(Level.INFO, "Kraj diseminacije za {0}", new Object[]{primatelj.getNaziv()});

    }

    private Map<Komponenta, Map<Postaja, PrimateljProgramKljuceviMap>> getMapaPrograma(Collection<PrimateljProgramKljuceviMap> aktivniProgrami) {
        Map<Komponenta, Map<Postaja, PrimateljProgramKljuceviMap>> mapa = new HashMap<>();
        for (PrimateljProgramKljuceviMap ppkm : aktivniProgrami) {
            Komponenta k = ppkm.getProgramMjerenja().getKomponentaId();
            if (!mapa.containsKey(k)) {
                mapa.put(k, new HashMap<>());
            }
            Map<Postaja, PrimateljProgramKljuceviMap> komponentaMapa = mapa.get(k);
            komponentaMapa.put(ppkm.getProgramMjerenja().getPostajaId(), ppkm);
        }
        return mapa;
    }

    private String getNazivDatoteke(PrimateljiPodataka primatelj, Komponenta komponenta) {
        StringBuilder sb = new StringBuilder();
//        sb.append("TEST-");
        sb.append(new SimpleDateFormat("YYMMddHH").format(new Date()));
        sb.append("_");
        sb.append(primatelj.getMrezaId().getKratica().trim());
        sb.append("_");
        sb.append(komponenta.getIsoOznaka().trim()).append(".dem");
        return sb.toString();
    }

    @Override
    public void nadoknadi(PrimateljiPodataka primatelj, Collection<ProgramMjerenja> program, Date pocetak, Date kraj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
