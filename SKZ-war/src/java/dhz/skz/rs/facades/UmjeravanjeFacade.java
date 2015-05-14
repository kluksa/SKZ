/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rs.facades;

import dhz.skz.aqdb.entity.Umjeravanje;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author kraljevic
 */
@Stateless(name="war/UmjeravanjeFacade")
public class UmjeravanjeFacade extends AbstractFacade<Umjeravanje> {
    @PersistenceContext(unitName = "SKZ-warPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UmjeravanjeFacade() {
        super(Umjeravanje.class);
    }
    
}
