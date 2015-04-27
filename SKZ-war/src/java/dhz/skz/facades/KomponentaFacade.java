/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.facades;

import dhz.skz.aqdb.entity.Komponenta;
import dhz.skz.aqdb.entity.Komponenta_;
import dhz.skz.aqdb.entity.ModelUredjaja;
import dhz.skz.aqdb.entity.ModelUredjaja_;
import dhz.skz.aqdb.entity.Uredjaj;
import dhz.skz.aqdb.entity.Uredjaj_;
import java.util.Collection;
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
public class KomponentaFacade extends AbstractFacade<Komponenta> {
    @PersistenceContext(unitName = "SKZ-warPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KomponentaFacade() {
        super(Komponenta.class);
    }

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
    
}
