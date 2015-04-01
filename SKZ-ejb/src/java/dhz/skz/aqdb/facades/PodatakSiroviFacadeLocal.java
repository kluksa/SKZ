/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.facades;

import dhz.skz.aqdb.entity.IzvorPodataka;
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
public interface PodatakSiroviFacadeLocal extends AbstractFacadeInterface<PodatakSirovi>  {
    Collection<PodatakSirovi> getPodaci(ProgramMjerenja pm, Date pocetak, Date kraj);

    Collection<PodatakSirovi> getPodaci(ProgramMjerenja pm, Date pocetak, Date kraj, boolean p, boolean k);

    Collection<PodatakSirovi> getPodatkeZaSat(ProgramMjerenja pm, Date kraj);

    Date getVrijemeZadnjeg(IzvorPodataka izvor, Postaja postaja, String datoteka);

    Date getVrijemeZadnjegOptimizirano(IzvorPodataka izvor, Postaja postaja, String datoteka);

    Date getVrijemeZadnjegS(IzvorPodataka izvor, Postaja postaja, String datoteka);

    void getVrijemeZadnjegTest();

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
 
}
