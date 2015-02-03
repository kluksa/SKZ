/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.newpackage;

import dhz.skz.aqdb.entity.Agregacije;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author kraljevic
 */
@Stateless
public class AgregacijeFacade extends AbstractFacade<Agregacije> implements AgregacijeFacadeLocal, dhz.skz.newpackage.AgregacijeFacadeRemote {
    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AgregacijeFacade() {
        super(Agregacije.class);
    }
    
}
