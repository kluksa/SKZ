/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.facades;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.ValidatorModelIzvor;
import dhz.skz.aqdb.entity.ValidatorModelIzvor_;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

/**
 *
 * @author kraljevic
 */
@Stateless
public class ValidatorModelIzvorFacade extends AbstractFacade<ValidatorModelIzvor> {

    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ValidatorModelIzvorFacade() {
        super(ValidatorModelIzvor.class);
    }

    public Collection<ValidatorModelIzvor> findAll(IzvorPodataka ip) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ValidatorModelIzvor> cq = cb.createQuery(ValidatorModelIzvor.class);
        Root<ValidatorModelIzvor> from = cq.from(ValidatorModelIzvor.class);
        Join<ValidatorModelIzvor, IzvorPodataka> join = from.join(ValidatorModelIzvor_.izvorPodataka);

        cq.select(from).where(cb.equal(join, ip));
        return em.createQuery(cq).getResultList();
    }
}
