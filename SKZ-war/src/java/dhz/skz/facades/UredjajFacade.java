/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.facades;

import dhz.skz.aqdb.entity.Uredjaj;
import dhz.skz.aqdb.entity.Uredjaj_;
import dhz.skz.aqdb.entity.ValidatorModelIzvor;
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
public class UredjajFacade extends AbstractFacade<Uredjaj> {
    @PersistenceContext(unitName = "SKZ-warPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UredjajFacade() {
        super(Uredjaj.class);
    }

    public Uredjaj findBySn(String sernum) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Uredjaj> cq = cb.createQuery(Uredjaj.class);
        Root<Uredjaj> from = cq.from(Uredjaj.class);
        cq.select(from).where(cb.equal(from.get(Uredjaj_.serijskaOznaka), sernum.replaceAll("^0+", "")));
        return em.createQuery(cq).getSingleResult();
    }
    
}
