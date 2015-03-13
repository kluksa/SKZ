/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.facades;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.ModelUredjaja;
import dhz.skz.aqdb.entity.ValidatorModelIzvor;
import dhz.skz.aqdb.entity.ValidatorModelIzvor_;
import dhz.skz.aqdb.entity.Validatori;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author kraljevic
 */
@Stateless
public class ValidatoriFacade extends AbstractFacade<Validatori> {
    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ValidatoriFacade() {
        super(Validatori.class);
    }
    
    public Validatori find(ModelUredjaja model, IzvorPodataka izvor){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ValidatorModelIzvor> cq = cb.createQuery(ValidatorModelIzvor.class);
        Root<ValidatorModelIzvor> from = cq.from(ValidatorModelIzvor.class);
        Predicate izvorP = cb.equal(from.get(ValidatorModelIzvor_.izvorPodataka), izvor);
        Predicate modelP = cb.equal(from.get(ValidatorModelIzvor_.modelUredjaja), model);
        cq.select(from).where(cb.and(izvorP,modelP));
        return em.createQuery(cq).getSingleResult().getValidatoriId();
    }
}
