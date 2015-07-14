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
import dhz.skz.aqdb.entity.Komponenta_;
import dhz.skz.aqdb.entity.ModelUredjaja;
import dhz.skz.aqdb.entity.ModelUredjaja_;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.Uredjaj;
import dhz.skz.aqdb.entity.Uredjaj_;
import java.util.Collection;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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
public class KomponentaFacade extends AbstractFacade<Komponenta> {
    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KomponentaFacade() {
        super(Komponenta.class);
    }
    
    // TODO prebaciti u named query
    public Collection<Komponenta> findByUredjajSn(String sernum) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Komponenta> cq = cb.createQuery(Komponenta.class);
        Root<Komponenta> from = cq.from(Komponenta.class);
        Join<Komponenta, ModelUredjaja> modelJ = from.join(Komponenta_.modelUredjajaCollection);
        Join<ModelUredjaja, Uredjaj> uredjajJ =  modelJ.join(ModelUredjaja_.uredjajCollection);
        
        Predicate uvjet = cb.equal(uredjajJ.get(Uredjaj_.serijskaOznaka), sernum);
        
        cq.select(from).where(uvjet);
        return em.createQuery(cq).getResultList();
    }
    
    public Collection<Komponenta> findByPostaja(Integer pId) {
        TypedQuery<Komponenta> query = em.createNamedQuery("Komponenta.findByPostajaDistinct", Komponenta.class);
        query.setParameter("postaja_id", pId);
        return query.getResultList();
    }
    
}
