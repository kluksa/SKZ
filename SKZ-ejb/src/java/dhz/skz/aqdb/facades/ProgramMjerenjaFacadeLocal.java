/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.facades;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.Postaja;
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
    
}
