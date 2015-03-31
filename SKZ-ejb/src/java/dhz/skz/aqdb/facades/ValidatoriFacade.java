/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.facades;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.ModelUredjaja;
import dhz.skz.aqdb.entity.ModelUredjaja_;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ProgramUredjajLink;
import dhz.skz.aqdb.entity.ProgramUredjajLink_;
import dhz.skz.aqdb.entity.Uredjaj;
import dhz.skz.aqdb.entity.Uredjaj_;
import dhz.skz.aqdb.entity.ValidatorModelIzvor;
import dhz.skz.aqdb.entity.ValidatorModelIzvor_;
import dhz.skz.aqdb.entity.Validatori;
import dhz.skz.aqdb.entity.Validatori_;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CollectionJoin;
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
public class ValidatoriFacade extends AbstractFacade<Validatori> {
    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ValidatoriFacade() {
        super(Validatori.class);
    }
    
    public Validatori find(ModelUredjaja model, IzvorPodataka izvor){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ValidatorModelIzvor> cq = cb.createQuery(ValidatorModelIzvor.class);
        Root<ValidatorModelIzvor> from = cq.from(ValidatorModelIzvor.class);
        Predicate izvorP = cb.equal(from.get(ValidatorModelIzvor_.izvorPodataka), izvor);
        Predicate modelP = cb.equal(from.get(ValidatorModelIzvor_.modelUredjaja), model);
        cq.select(from).where(cb.and(izvorP,modelP));
        
        return em.createQuery(cq).getSingleResult().getValidatoriId();
    }
    
    public Collection<Validatori> findAll(IzvorPodataka izvor) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Validatori> cq = cb.createQuery(Validatori.class);
        Root<Validatori> from = cq.from(Validatori.class);
        CollectionJoin<Validatori, ValidatorModelIzvor> vmiJ = from.join(Validatori_.validatorModelIzvorCollection);
        Join<ValidatorModelIzvor, IzvorPodataka> ipJ = vmiJ.join(ValidatorModelIzvor_.izvorPodataka);
        Join<ValidatorModelIzvor, ModelUredjaja> muJ = vmiJ.join(ValidatorModelIzvor_.modelUredjaja);
        CollectionJoin<ModelUredjaja, Uredjaj> uJ = muJ.join(ModelUredjaja_.uredjajCollection);
        CollectionJoin<Uredjaj, ProgramUredjajLink> pulJ = uJ.join(Uredjaj_.programUredjajLinkCollection);
        Join<ProgramUredjajLink, ProgramMjerenja> pmJ = pulJ.join(ProgramUredjajLink_.programMjerenjaId);
        
        cq.select(from).where(cb.equal(uJ, izvor));
        return em.createQuery(cq).getResultList();
    }
}
