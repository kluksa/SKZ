/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.facades;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.IzvorProgramKljuceviMap;
import dhz.skz.aqdb.entity.IzvorProgramKljuceviMap_;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ProgramMjerenja_;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author kraljevic
 */
@Stateless
public class ProgramMjerenjaFacade extends AbstractFacade<ProgramMjerenja> implements ProgramMjerenjaFacadeLocal, ProgramMjerenjaFacadeRemote {

    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProgramMjerenjaFacade() {
        super(ProgramMjerenja.class);
    }
    
    @Override
    public Date getPocetakMjerenja(IzvorPodataka i, Postaja p){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Date> cq = cb.createQuery(Date.class);
        Root<ProgramMjerenja> from = cq.from(ProgramMjerenja.class);
        Predicate izvor = cb.equal(from.join(ProgramMjerenja_.izvorPodatakaId), i);
        Predicate postaja = cb.equal(from.join(ProgramMjerenja_.postajaId), p);
        Expression<Date> vrijeme = from.get(ProgramMjerenja_.pocetakMjerenja);
        cq.select(cb.least(vrijeme)).where(cb.and(postaja, izvor));
        List<Date> rl = em.createQuery(cq).getResultList();
        if ( rl.isEmpty()) {
            return null; 
        } else {
            return rl.get(0);
        }
    }
    
    @Override
    public Date getKrajMjerenja(IzvorPodataka i, Postaja p){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Date> cq = cb.createQuery(Date.class);
        Root<ProgramMjerenja> from = cq.from(ProgramMjerenja.class);
        Predicate izvor = cb.equal(from.join(ProgramMjerenja_.izvorPodatakaId), i);
        Predicate postaja = cb.equal(from.join(ProgramMjerenja_.postajaId), p);
        Expression<Date> vrijeme = from.get(ProgramMjerenja_.zavrsetakMjerenja);
        cq.select(cb.greatest(vrijeme)).where(cb.and(postaja, izvor));
        List<Date> rl = em.createQuery(cq).getResultList();
        if ( rl.isEmpty()) {
            return null; 
        } else {
            return rl.get(0);
        }
    }

    @Override
    public ProgramMjerenja find(Integer id) {
        return super.find(id);
    }
    
    @Override
    public ProgramMjerenja find(Postaja p, IzvorPodataka i, String kKljuc, String nKljuc) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ProgramMjerenja> cq = cb.createQuery(ProgramMjerenja.class);
        Root<ProgramMjerenja> from = cq.from(ProgramMjerenja.class);

        Join<ProgramMjerenja, IzvorPodataka> izvorJ = from.join(ProgramMjerenja_.izvorPodatakaId);
        Join<ProgramMjerenja, IzvorProgramKljuceviMap> kljuceviJ = from.join(ProgramMjerenja_.izvorProgramKljuceviMap);

        Expression<String> kKljucE = kljuceviJ.get(IzvorProgramKljuceviMap_.kKljuc);
        Expression<String> nKljucE = kljuceviJ.get(IzvorProgramKljuceviMap_.nKljuc);
        Expression<Postaja> postajaE = from.get(ProgramMjerenja_.postajaId);

        Predicate and = cb.and(
                cb.equal(postajaE, p),
                cb.equal(izvorJ, i),
                cb.equal(kKljucE, kKljuc),
                cb.equal(nKljucE, nKljuc)
        );
        cq.select(from).where(and);
        List<ProgramMjerenja> resultList = em.createQuery(cq).getResultList();
        if (resultList == null || resultList.isEmpty()) {
            return null;
        }
        return resultList.get(0);
    }

    @Override
    public Collection<ProgramMjerenja> find(Postaja p, IzvorPodataka i) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ProgramMjerenja> cq = cb.createQuery(ProgramMjerenja.class);
        Root<ProgramMjerenja> from = cq.from(ProgramMjerenja.class);

        Join<ProgramMjerenja, IzvorPodataka> izvorJ = from.join(ProgramMjerenja_.izvorPodatakaId);
        Expression<Postaja> postajaE = from.get(ProgramMjerenja_.postajaId);

        Predicate and = cb.and(
                cb.equal(postajaE, p),
                cb.equal(izvorJ, i)
        );
        cq.select(from).where(and);
        return em.createQuery(cq).getResultList();
    }
    
    @Override
    public Collection<ProgramMjerenja> find(IzvorPodataka i) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ProgramMjerenja> cq = cb.createQuery(ProgramMjerenja.class);
        Root<ProgramMjerenja> from = cq.from(ProgramMjerenja.class);


        cq.select(from).where(cb.equal(from.join(ProgramMjerenja_.izvorPodatakaId), i));
        return em.createQuery(cq).getResultList();
    }

//    @Override
//    public Collection<ProgramMjerenja> find(Postaja p, IzvorPodataka i, Date zadnji) {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<ProgramMjerenja> cq = cb.createQuery(ProgramMjerenja.class);
//        Root<ProgramMjerenja> program = cq.from(ProgramMjerenja.class);
//        Expression<Postaja> postaja = program.get(ProgramMjerenja_.postajaId);
//        Expression<IzvorPodataka> izvor = program.get(ProgramMjerenja_.izvorPodatakaId);
//        Expression<Date> kraj = program.get(ProgramMjerenja_.zavrsetakMjerenja);
//
//        cq.select(program).where(
//                cb.and(
//                        cb.equal(postaja, p),
//                        cb.equal(izvor, i),
//                        cb.or(
//                                cb.isNull(kraj),
//                                cb.greaterThan(kraj, zadnji)
//                        )
//                )
//        );
//        return em.createQuery(cq).getResultList();
//    }

    @Override
    public Collection<ProgramMjerenja> findZaTermin(Postaja p, IzvorPodataka i, Date termin) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ProgramMjerenja> cq = cb.createQuery(ProgramMjerenja.class);
        Root<ProgramMjerenja> program = cq.from(ProgramMjerenja.class);
        Expression<Postaja> postaja = program.get(ProgramMjerenja_.postajaId);
        Expression<IzvorPodataka> izvor = program.get(ProgramMjerenja_.izvorPodatakaId);
        Expression<Date> pocetak = program.get(ProgramMjerenja_.pocetakMjerenja);
        Expression<Date> kraj = program.get(ProgramMjerenja_.zavrsetakMjerenja);

        cq.select(program).where(
                cb.and(
                        cb.equal(postaja, p),
                        cb.equal(izvor, i),
                        cb.lessThanOrEqualTo(pocetak, termin),
                        cb.or(
                                cb.isNull(kraj),
                                cb.greaterThan(kraj, termin)
                        )
                )
        );
        return em.createQuery(cq).getResultList();
    }

}
