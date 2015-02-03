/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.newpackage;

import dhz.skz.aqdb.entity.Agregacije;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author kraljevic
 */
@Remote
public interface AgregacijeFacadeRemote {

    void create(Agregacije agregacije);

    void edit(Agregacije agregacije);

    void remove(Agregacije agregacije);

    Agregacije find(Object id);

    List<Agregacije> findAll();

    List<Agregacije> findRange(int[] range);

    int count();
    
}
