/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dhz.skz.aqdb.facades;

import dhz.skz.aqdb.entity.PrimateljProgramKljuceviMap;
import dhz.skz.aqdb.entity.PrimateljProgramKljuceviMap_;
import dhz.skz.aqdb.entity.PrimateljiPodataka;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import java.util.Collection;
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
public class PrimateljProgramKljuceviMapFacade extends AbstractFacade<PrimateljProgramKljuceviMap> implements PrimateljProgramKljuceviMapFacadeLocal {
    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PrimateljProgramKljuceviMapFacade() {
        super(PrimateljProgramKljuceviMap.class);
    }

    @Override
    public PrimateljProgramKljuceviMap find(final PrimateljiPodataka primatelj, final ProgramMjerenja program) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PrimateljProgramKljuceviMap> cq = cb.createQuery(PrimateljProgramKljuceviMap.class);

        Root<PrimateljProgramKljuceviMap> from = cq.from(PrimateljProgramKljuceviMap.class);
        Predicate prim = cb.equal(from.get(PrimateljProgramKljuceviMap_.primateljiPodataka), primatelj);
        Predicate prog = cb.equal(from.get(PrimateljProgramKljuceviMap_.programMjerenja), program);
        cq.select(from).where(cb.and(prim, prog));
        return em.createQuery(cq).getResultList().get(0);
    }
    
    
     public Collection<PrimateljProgramKljuceviMap> find(final PrimateljiPodataka primatelj) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PrimateljProgramKljuceviMap> cq = cb.createQuery(PrimateljProgramKljuceviMap.class);

        Root<PrimateljProgramKljuceviMap> from = cq.from(PrimateljProgramKljuceviMap.class);
        Predicate prim = cb.equal(from.get(PrimateljProgramKljuceviMap_.primateljiPodataka), primatelj);
        cq.select(from).where(prim);
        return em.createQuery(cq).getResultList();
    }
    
    
}
