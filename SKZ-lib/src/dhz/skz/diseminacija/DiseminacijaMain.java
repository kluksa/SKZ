/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.diseminacija;

import dhz.skz.aqdb.entity.PrimateljiPodataka;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import java.util.Collection;
import java.util.Date;
import javax.ejb.Remote;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author kraljevic
 */
@Remote
public interface DiseminacijaMain {

    void nadoknadiPodatke(PrimateljiPodataka primatelj, Collection<ProgramMjerenja> program, Date pocetak, Date kraj);

    @TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
    void pokreniDiseminaciju();

}
