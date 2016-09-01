/*
 * Copyright (C) 2015 kraljevic
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dhz.skz.aqdb.facades;

import dhz.skz.aqdb.entity.PrimateljProgramKljuceviMap;
import dhz.skz.aqdb.entity.PrimateljProgramKljuceviMap_;
import dhz.skz.aqdb.entity.PrimateljiPodataka;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import java.util.Collection;
import javax.ejb.LocalBean;
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
@LocalBean
public class PrimateljProgramKljuceviMapFacade extends AbstractFacade<PrimateljProgramKljuceviMap> {

    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PrimateljProgramKljuceviMapFacade() {
        super(PrimateljProgramKljuceviMap.class);
    }

    // TODO prebaciti u named query
    public PrimateljProgramKljuceviMap find(final PrimateljiPodataka primatelj, final ProgramMjerenja program) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PrimateljProgramKljuceviMap> cq = cb.createQuery(PrimateljProgramKljuceviMap.class);

        Root<PrimateljProgramKljuceviMap> from = cq.from(PrimateljProgramKljuceviMap.class);
        Predicate prim = cb.equal(from.get(PrimateljProgramKljuceviMap_.primateljiPodataka), primatelj);
        Predicate prog = cb.equal(from.get(PrimateljProgramKljuceviMap_.programMjerenja), program);
        cq.select(from).where(cb.and(prim, prog));
        if ( ! (em.createQuery(cq).getResultList() == null) && !em.createQuery(cq).getResultList().isEmpty()){
            return em.createQuery(cq).getResultList().get(0);
        } else {
            return null;
        }
    }

    // TODO prebaciti u named query
    public Collection<PrimateljProgramKljuceviMap> find(final PrimateljiPodataka primatelj) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PrimateljProgramKljuceviMap> cq = cb.createQuery(PrimateljProgramKljuceviMap.class);

        Root<PrimateljProgramKljuceviMap> from = cq.from(PrimateljProgramKljuceviMap.class);
        Predicate prim = cb.equal(from.get(PrimateljProgramKljuceviMap_.primateljiPodataka), primatelj);
        cq.select(from).where(prim);
        return em.createQuery(cq).getResultList();
    }

    // TODO prebaciti u named query
    public Collection<PrimateljProgramKljuceviMap> findAktivni(final PrimateljiPodataka primatelj) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PrimateljProgramKljuceviMap> cq = cb.createQuery(PrimateljProgramKljuceviMap.class);

        Root<PrimateljProgramKljuceviMap> from = cq.from(PrimateljProgramKljuceviMap.class);
        Predicate prim = cb.equal(from.get(PrimateljProgramKljuceviMap_.primateljiPodataka), primatelj);
        Predicate aktivan = cb.equal(from.get(PrimateljProgramKljuceviMap_.aktivan), 1);
        cq.select(from).where(cb.and(prim,aktivan));
        return em.createQuery(cq).getResultList();
    }
    
}
