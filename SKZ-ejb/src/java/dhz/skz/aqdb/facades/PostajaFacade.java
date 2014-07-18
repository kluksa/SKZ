/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dhz.skz.aqdb.facades;

import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.Postaja_;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author kraljevic
 */
@Stateless
public class PostajaFacade extends AbstractFacade<Postaja> {
    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PostajaFacade() {
        super(Postaja.class);
    }
    public Postaja findByNacionalnaOznaka(final String oznaka) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Postaja> cq = cb.createQuery(Postaja.class);
        
        Root<Postaja> from = cq.from(Postaja.class);
        
        cq.select(from).where(cb.equal(from.get(Postaja_.nacionalnaOznaka), oznaka));
        return em.createQuery(cq).getSingleResult();
    }
    
}
