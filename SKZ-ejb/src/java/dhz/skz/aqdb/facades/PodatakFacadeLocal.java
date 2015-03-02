/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.facades;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.Komponenta;
import dhz.skz.aqdb.entity.NivoValidacije;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author kraljevic
 */
public interface PodatakFacadeLocal {

    void flush();

    Collection<Podatak> getPodaciZaKomponentu(Date pocetak, Date kraj, Komponenta k, NivoValidacije nv, short usporedno);

    List<Podatak> getPodatak(ProgramMjerenja pm, Date pocetak, Date kraj, boolean p, boolean k);

    List<Podatak> getPodatak(ProgramMjerenja pm, Date pocetak, Date kraj);

    List<Podatak> getPodatakOd(ProgramMjerenja pm, Date pocetak);

    Date getVrijemeZadnjegNaPostajiZaIzvor(Postaja p, IzvorPodataka i);

    Podatak getZadnji(IzvorPodataka i, Postaja p);

    Date getZadnjiPodatak(ProgramMjerenja program);

    Map<ProgramMjerenja, Podatak> getZadnjiPodatakPoProgramu();

    void spremi(Podatak ps);

    void spremi(Collection<Podatak> ps);
    
}
