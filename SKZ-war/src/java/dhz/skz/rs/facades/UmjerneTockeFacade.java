/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rs.facades;

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
public class UmjerneTockeFacade extends AbstractFacade<UmjerneTocke> {
    @PersistenceContext(unitName = "SKZ-warPU")
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
