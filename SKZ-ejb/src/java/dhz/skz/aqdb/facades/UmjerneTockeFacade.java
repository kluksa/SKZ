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

import dhz.skz.aqdb.entity.Umjeravanje;
import dhz.skz.aqdb.entity.UmjerneTocke;
import dhz.skz.aqdb.entity.UmjerneTocke_;
import java.util.Collection;
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
public class UmjerneTockeFacade extends AbstractFacade<UmjerneTocke> {
    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UmjerneTockeFacade() {
        super(UmjerneTocke.class);
    }
    
    public Collection<UmjerneTocke> findBy(Umjeravanje u) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UmjerneTocke> cq = cb.createQuery(UmjerneTocke.class);
        Root<UmjerneTocke> from = cq.from(UmjerneTocke.class);
        cq.select(from).where(cb.equal(from.get(UmjerneTocke_.umjeravanjeId), u));
        return em.createQuery(cq).getResultList();
    }
}
