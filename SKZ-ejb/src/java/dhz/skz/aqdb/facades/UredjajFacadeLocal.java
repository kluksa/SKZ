/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.facades;

import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.Uredjaj;
import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author kraljevic
 */
@Local
public interface UredjajFacadeLocal extends AbstractFacadeInterface<Uredjaj>{

    public void premjesti(Uredjaj uredjaj, Postaja novaPostaja, short usporednoMjerenje, Date vrijeme);
    
}
