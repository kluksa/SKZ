/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.facades;

import dhz.skz.aqdb.entity.UmjeravanjeKomentar;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author kraljevic
 */
@Stateless
public class UmjeravanjeKomentarFacade extends AbstractFacade<UmjeravanjeKomentar> {
    @PersistenceContext(unitName = "SKZ-warPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UmjeravanjeKomentarFacade() {
        super(UmjeravanjeKomentar.class);
    }
    
}
