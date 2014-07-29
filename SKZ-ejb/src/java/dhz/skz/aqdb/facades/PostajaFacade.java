/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dhz.skz.aqdb.facades;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.IzvorPodataka_;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.Postaja_;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ProgramMjerenja_;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CollectionJoin;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

/**
 *
 * @author kraljevic
 */
@Stateless
public class PostajaFacade extends AbstractFacade<Postaja> {
    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PostajaFacade() {
        super(Postaja.class);
    }
    public Postaja findByNacionalnaOznaka(final String oznaka) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Postaja> cq = cb.createQuery(Postaja.class);
        
        Root<Postaja> from = cq.from(Postaja.class);
        
        cq.select(from).where(cb.equal(from.get(Postaja_.nacionalnaOznaka), oznaka));
        return em.createQuery(cq).getSingleResult();
    }
    
    public Collection<Postaja> getPostajeZaIzvor(IzvorPodataka i) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Postaja> cq = cb.createQuery(Postaja.class);
        Root<IzvorPodataka> izvor = cq.from(IzvorPodataka.class);
        CollectionJoin<IzvorPodataka, ProgramMjerenja> izvorProgram = izvor.join(IzvorPodataka_.programMjerenjaCollection);
        Join<ProgramMjerenja, Postaja> programPostaja = izvorProgram.join(ProgramMjerenja_.postajaId);
        Expression<Postaja> postaja = izvorProgram.get(ProgramMjerenja_.postajaId);
        Expression<String> naziv = programPostaja.get(Postaja_.nazivPostaje);
        cq.where(cb.equal(izvor, i)).select(postaja).distinct(true).orderBy(cb.asc(naziv));
        return em.createQuery(cq).getResultList();
    }

    
}
