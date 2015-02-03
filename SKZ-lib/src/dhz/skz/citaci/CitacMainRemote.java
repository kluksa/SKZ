/* To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci;

import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import java.util.Collection;
import java.util.Date;
import javax.ejb.Remote;
import javax.naming.NamingException;

/**
 *
 * @author kraljevic
 */
@Remote
public interface CitacMainRemote {

    Collection<PodatakSirovi> dohvatiSirove(final ProgramMjerenja program, final Date pocetak, final Date kraj, final boolean p, final boolean k) throws NamingException;
}
