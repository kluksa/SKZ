/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz;

import dhz.skz.aqdb.entity.PrimateljiPodataka;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import java.util.Collection;
import java.util.Date;
import javax.ejb.Remote;

/**
 *
 * @author kraljevic
 */
@Remote
public interface GlavnaFasadaRemote {

    void pokreniDiseminaciju();

    void pokreniCitanje();

    void nadoknadiPodatke(PrimateljiPodataka primatelji, Collection<ProgramMjerenja> programi, Date pocetak, Date kraj);

}
