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

import dhz.skz.aqdb.entity.Komponenta;
import dhz.skz.aqdb.entity.Obavijesti;
import dhz.skz.aqdb.entity.OdgovornoTijelo;
import dhz.skz.aqdb.entity.OdgovornoTijelo_;
import dhz.skz.aqdb.entity.PrimateljiPodataka;
import java.util.Collection;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author kraljevic
 */
@Stateless
@LocalBean
public class ObavijestiFacade extends AbstractFacade<Obavijesti> {
    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ObavijestiFacade() {
        super(Obavijesti.class);
    }
    
    public Collection<Obavijesti> findAll(PrimateljiPodataka primatelj) {
        TypedQuery<Obavijesti> query = em.createNamedQuery("Obavijesti.findByPrimatelj", Obavijesti.class);
        query.setParameter("primatelj", primatelj);
        return query.getResultList();
    }
}
