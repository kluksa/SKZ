/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.facades;

import dhz.skz.aqdb.entity.IzvorPodataka;
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
public interface ProgramMjerenjaFacadeLocal {

    Date getKrajMjerenja(IzvorPodataka i, Postaja p);

    Date getPocetakMjerenja(IzvorPodataka i, Postaja p);
    
    ProgramMjerenja find(Integer id);

    ProgramMjerenja find(Postaja p, IzvorPodataka i, String kKljuc, String nKljuc);

    Collection<ProgramMjerenja> find(Postaja p, IzvorPodataka i);

    Collection<ProgramMjerenja> find(IzvorPodataka i);
    
//    Collection<ProgramMjerenja> find(Postaja p, IzvorPodataka i, Date zadnji);
    
    Collection<ProgramMjerenja> findZaTermin(Postaja p, IzvorPodataka i, Date termin);

    
}
