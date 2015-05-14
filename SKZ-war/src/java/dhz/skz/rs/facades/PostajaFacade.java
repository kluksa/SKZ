/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rs.facades;

import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.PostajaUredjajLink;
import dhz.skz.aqdb.entity.PostajaUredjajLink_;
import dhz.skz.aqdb.entity.Postaja_;
import dhz.skz.aqdb.entity.Uredjaj;
import dhz.skz.aqdb.entity.Uredjaj_;
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
public class PostajaFacade extends AbstractFacade<Postaja> {
    @PersistenceContext(unitName = "SKZ-warPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PostajaFacade() {
        super(Postaja.class);
    }
    
    
    public Postaja findByUredjajSn(String uredjaj_id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Postaja> cq = cb.createQuery(Postaja.class);
        Root<Postaja> from = cq.from(Postaja.class);
        Join<Postaja, PostajaUredjajLink> pulJ = from.join(Postaja_.postajaUredjajLinkCollection);
        Join<PostajaUredjajLink, Uredjaj> uredjajJ = pulJ.join(PostajaUredjajLink_.uredjajId);
        
        Predicate uvjet = cb.and(cb.equal(uredjajJ.get(Uredjaj_.serijskaOznaka), uredjaj_id),
                cb.isNull(pulJ.get(PostajaUredjajLink_.vrijemeUklanjanja)));
        
        cq.select(from).where(uvjet);
        return em.createQuery(cq).getSingleResult();
    }
    
}
