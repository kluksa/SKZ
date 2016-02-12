/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz;

import dhz.skz.aqdb.entity.NivoValidacije;
import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import java.util.Date;
import java.util.Map;
import javax.ejb.Remote;
import javax.naming.NamingException;

/**
 *
 * @author kraljevic
 */
@Remote
public interface CitaciGlavniBeanRemote extends GlavniBeanInterace {
    public Map<String, String> opisiStatus(PodatakSirovi podatakSirovi) throws NamingException;

    void agregirajProgramOdDo(final ProgramMjerenja program, final Integer nivo, final Date pocetak, final Date kraj);

    void dodajOdPocetka();
}
