/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.facades;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.Podatak_;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ProgramMjerenja_;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author kraljevic
 */
@Stateless
public class ProgramMjerenjaFacade extends AbstractFacade<ProgramMjerenja> {

    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProgramMjerenjaFacade() {
        super(ProgramMjerenja.class);
    }
    
    public Date getPocetakMjerenja(IzvorPodataka i, Postaja p){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Date> cq = cb.createQuery(Date.class);
        Root<ProgramMjerenja> from = cq.from(ProgramMjerenja.class);
        Predicate izvor = cb.equal(from.join(ProgramMjerenja_.izvorPodatakaId), i);
        Predicate postaja = cb.equal(from.join(ProgramMjerenja_.postajaId), p);
        Expression<Date> vrijeme = from.get(ProgramMjerenja_.pocetakMjerenja);
        cq.select(cb.least(vrijeme)).where(cb.and(postaja, izvor));
        List<Date> rl = em.createQuery(cq).getResultList();
        if ( rl.isEmpty()) {
            return null; 
        } else {
            return rl.get(0);
        }
    }
    
    public Date getKrajMjerenja(IzvorPodataka i, Postaja p){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Date> cq = cb.createQuery(Date.class);
        Root<ProgramMjerenja> from = cq.from(ProgramMjerenja.class);
        Predicate izvor = cb.equal(from.join(ProgramMjerenja_.izvorPodatakaId), i);
        Predicate postaja = cb.equal(from.join(ProgramMjerenja_.postajaId), p);
        Expression<Date> vrijeme = from.get(ProgramMjerenja_.zavrsetakMjerenja);
        cq.select(cb.greatest(vrijeme)).where(cb.and(postaja, izvor));
        List<Date> rl = em.createQuery(cq).getResultList();
        if ( rl.isEmpty()) {
            return null; 
        } else {
            return rl.get(0);
        }
    }

}
