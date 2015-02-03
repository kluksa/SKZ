/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.facades;

import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ZeroSpan;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author kraljevic
 */
@Remote
public interface ZeroSpanFacadeRemote {

  
    List<ZeroSpan> getZeroSpan(ProgramMjerenja programMjerenja, Date pocetak, Date kraj);
    
}
