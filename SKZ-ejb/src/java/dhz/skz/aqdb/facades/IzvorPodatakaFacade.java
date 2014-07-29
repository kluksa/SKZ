/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.facades;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.IzvorPodataka_;
import dhz.skz.aqdb.entity.IzvorProgramKljuceviMap;
import dhz.skz.aqdb.entity.IzvorProgramKljuceviMap_;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ProgramMjerenja_;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author kraljevic
 */
@Stateless
public class IzvorPodatakaFacade extends AbstractFacade<IzvorPodataka> {

    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public IzvorPodatakaFacade() {
        super(IzvorPodataka.class);
    }

    public IzvorPodataka findByName(final String naziv) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<IzvorPodataka> cq = cb.createQuery(IzvorPodataka.class);

        Root<IzvorPodataka> from = cq.from(IzvorPodataka.class);

        cq.select(from).where(cb.equal(from.get(IzvorPodataka_.naziv), naziv));
        return em.createQuery(cq).getSingleResult();
    }

    public ProgramMjerenja getProgram(Postaja p, IzvorPodataka i, String kKljuc, String nKljuc) {
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

    public Iterable<IzvorPodataka> getAktivniIzvori() {
        em.flush();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<IzvorPodataka> cq = cb.createQuery(IzvorPodataka.class);
        Root<IzvorPodataka> from = cq.from(IzvorPodataka.class);
        cq.where(cb.equal(from.get(IzvorPodataka_.aktivan), true));
        cq.select(from);
        return em.createQuery(cq).getResultList();
    }
}
