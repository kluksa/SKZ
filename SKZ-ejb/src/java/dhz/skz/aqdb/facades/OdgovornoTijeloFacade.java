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

import dhz.skz.aqdb.entity.OdgovornoTijelo;
import dhz.skz.aqdb.entity.OdgovornoTijelo_;
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
public class OdgovornoTijeloFacade extends AbstractFacade<OdgovornoTijelo> {
    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OdgovornoTijeloFacade() {
        super(OdgovornoTijelo.class);
    }
    
    // TODO prebaciti u named query
    public OdgovornoTijelo findByNaziv(String naziv) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<OdgovornoTijelo> cq = cb.createQuery(OdgovornoTijelo.class);

        Root<OdgovornoTijelo> from = cq.from(OdgovornoTijelo.class);

        cq.select(from).where(cb.equal(from.get(OdgovornoTijelo_.naziv), naziv));
        return em.createQuery(cq).getSingleResult();
    }
    
}
