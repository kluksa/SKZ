/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dhz.skz.aqdb.facades;

import dhz.skz.aqdb.entity.PrimateljProgramKljuceviMap_;
import dhz.skz.aqdb.entity.PrimateljiPodataka;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ProgramMjerenja_;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

/**
 *
 * @author kraljevic
 */
@Stateless
public class PrimateljiPodatakaFacade extends AbstractFacade<PrimateljiPodataka> {
    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PrimateljiPodatakaFacade() {
        super(PrimateljiPodataka.class);
    }
    
    public Collection<ProgramMjerenja> getProgramZaPrimatelje(PrimateljiPodataka primatelj) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ProgramMjerenja> cq = cb.createQuery(ProgramMjerenja.class);
        
        Root<ProgramMjerenja> pmT = cq.from(ProgramMjerenja.class);
        Expression<PrimateljiPodataka> primateljE = pmT.join(ProgramMjerenja_.primateljProgramKljuceviMapCollection)
                .get(PrimateljProgramKljuceviMap_.primateljiPodataka);
        cq.where(
                        cb.equal(primateljE, primatelj)
        );
        cq.select(pmT);
        return em.createQuery(cq).getResultList();
    }
    
}
