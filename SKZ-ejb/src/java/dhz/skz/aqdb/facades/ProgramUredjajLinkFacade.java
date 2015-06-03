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

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ProgramMjerenja_;
import dhz.skz.aqdb.entity.ProgramUredjajLink;
import dhz.skz.aqdb.entity.ProgramUredjajLink_;
import java.util.Collection;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author kraljevic
 */
@Stateless
@LocalBean
public class ProgramUredjajLinkFacade extends AbstractFacade<ProgramUredjajLink> {

    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProgramUredjajLinkFacade() {
        super(ProgramUredjajLink.class);
    }

    public Collection<ProgramUredjajLink> findAll(IzvorPodataka ip) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ProgramUredjajLink> cq = cb.createQuery(ProgramUredjajLink.class);
        Root<ProgramUredjajLink> from = cq.from(ProgramUredjajLink.class);
        Join<ProgramUredjajLink, ProgramMjerenja> pmJ = from.join(ProgramUredjajLink_.programMjerenjaId);
        Predicate equal = cb.equal(pmJ.get(ProgramMjerenja_.izvorPodatakaId), ip);
        cq.select(from).where(equal);
        return em.createQuery(cq).getResultList();
    }

}
