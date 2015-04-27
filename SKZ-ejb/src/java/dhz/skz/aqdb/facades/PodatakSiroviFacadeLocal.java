/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.facades;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import java.util.Collection;
import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author kraljevic
 */
@Local
public interface PodatakSiroviFacadeLocal {
    
    Collection<PodatakSirovi> getPodaci(ProgramMjerenja pm, Date pocetak, Date kraj, boolean p, boolean k);

    Date getVrijemeZadnjeg(IzvorPodataka izvor, Postaja postaja, String datoteka);

    Date getVrijemeZadnjeg(IzvorPodataka izvor, Postaja postaja);

    Date getZadnjiPodatak(ProgramMjerenja program);

    //    public void spremi(PodatakSirovi podatak) {
    //        if (!postoji(podatak)) {
    //            create(podatak);
    //        } else {
    //            log.log(Level.INFO, "Podatak: {0}: {1} vec postoji", new Object[]{
    //                podatak.getProgramMjerenjaId().getId(), podatak.getVrijeme()
    //            });
    //        }
    //    }
    boolean postoji(PodatakSirovi podatak);

    void spremi(Collection<PodatakSirovi> podaci);
 
    void spremi(PodatakSirovi ps);
}
