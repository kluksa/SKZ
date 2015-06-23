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

import dhz.skz.aqdb.entity.PrimateljiPodataka;
import dhz.skz.aqdb.entity.PrimateljiPodataka_;
import javax.ejb.LocalBean;
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
@LocalBean
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

    //    @Override
//    public Collection<ProgramMjerenja> getProgramZaPrimatelje(PrimateljiPodataka primatelj) {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<ProgramMjerenja> cq = cb.createQuery(ProgramMjerenja.class);
//
//        Root<ProgramMjerenja> pmT = cq.from(ProgramMjerenja.class);
//        Expression<PrimateljiPodataka> primateljE = pmT.join(ProgramMjerenja_.primateljProgramKljuceviMapCollection)
//                .get(PrimateljProgramKljuceviMap_.primateljiPodataka);
//        cq.where(
//                cb.equal(primateljE, primatelj)
//        );
//        cq.select(pmT);
//        return em.createQuery(cq).getResultList();
//    }
//
//    @Override
//    public Collection<ProgramMjerenja> getProgram(PrimateljiPodataka primatelj) {
//        return getProgramZaPrimatelje(primatelj);
//    }
    // TODO prebaciti u named query
    public Iterable<PrimateljiPodataka> getAktivniPrimatelji() {
        em.flush();
        em.clear();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PrimateljiPodataka> cq = cb.createQuery(PrimateljiPodataka.class);
        Root<PrimateljiPodataka> from = cq.from(PrimateljiPodataka.class);
        cq.where(cb.equal(from.get(PrimateljiPodataka_.aktivan), true));
        cq.select(from);
        return em.createQuery(cq).getResultList();
    }

//    @Override
//    public PrimateljiPodataka findByNaziv(String naziv) {
//        em.flush();
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<PrimateljiPodataka> cq = cb.createQuery(PrimateljiPodataka.class);
//        Root<PrimateljiPodataka> from = cq.from(PrimateljiPodataka.class);
//        cq.where(cb.equal(from.get(PrimateljiPodataka_.naziv), naziv));
//        cq.select(from);
//        return em.createQuery(cq).getSingleResult();
//    }
}
