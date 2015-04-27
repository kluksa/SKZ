/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.facades;

import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import java.util.Collection;
import java.util.Date;
import javax.ejb.Remote;

/**
 *
 * @author kraljevic
 */
@Remote
public interface PodatakSiroviFacadeRemote {
    public Collection<PodatakSirovi> getPodaciZaREST(ProgramMjerenja pm, Date pocetak, Date kraj);
    public void update(Collection<PodatakSirovi> pod);
}
