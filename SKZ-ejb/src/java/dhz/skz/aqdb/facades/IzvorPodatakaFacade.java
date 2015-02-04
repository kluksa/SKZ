/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.facades;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.IzvorPodataka_;
import dhz.skz.aqdb.entity.IzvorProgramKljuceviMap;
import dhz.skz.aqdb.entity.IzvorProgramKljuceviMap_;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ProgramMjerenja_;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author kraljevic
 */
@Stateless
public class IzvorPodatakaFacade extends AbstractFacade<IzvorPodataka> {

    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public IzvorPodatakaFacade() {
        super(IzvorPodataka.class);
    }

    public IzvorPodataka findByName(final String naziv) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<IzvorPodataka> cq = cb.createQuery(IzvorPodataka.class);

        Root<IzvorPodataka> from = cq.from(IzvorPodataka.class);

        cq.select(from).where(cb.equal(from.get(IzvorPodataka_.naziv), naziv));
        return em.createQuery(cq).getSingleResult();
    }

    public Iterable<IzvorPodataka> getAktivniIzvori() {
        em.flush();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<IzvorPodataka> cq = cb.createQuery(IzvorPodataka.class);
        Root<IzvorPodataka> from = cq.from(IzvorPodataka.class);
        cq.where(cb.equal(from.get(IzvorPodataka_.aktivan), true));
        cq.select(from);
        return em.createQuery(cq).getResultList();
    }
}
