/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rs.facades;

import dhz.skz.aqdb.entity.Korisnik;
import dhz.skz.aqdb.entity.Korisnik_;
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
public class KorisnikFacade extends AbstractFacade<Korisnik> {
    @PersistenceContext(unitName = "SKZ-warPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KorisnikFacade() {
        super(Korisnik.class);
    }
    
    public Korisnik findByIme(String name){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Korisnik> cq = cb.createQuery(Korisnik.class);
        Root<Korisnik> from = cq.from(Korisnik.class);
        cq.select(from).where(cb.equal(from.get(Korisnik_.korisnickoIme), name));
        return em.createQuery(cq).getSingleResult();
    }
    
}
