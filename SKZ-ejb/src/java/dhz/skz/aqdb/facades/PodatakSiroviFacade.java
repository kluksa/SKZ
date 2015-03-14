/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.facades;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.IzvorProgramKljuceviMap_;
import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.PodatakSirovi_;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ProgramMjerenja_;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
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
public class PodatakSiroviFacade extends AbstractFacade<PodatakSirovi> {

    private static final Logger log = Logger.getLogger(PodatakSiroviFacade.class.getName());

    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PodatakSiroviFacade() {
        super(PodatakSirovi.class);
    }

    public void spremi(Collection<PodatakSirovi> podaci) {
        for (PodatakSirovi ps : podaci) {
            if (!postoji(ps)) {
                em.persist(ps);
            }
        }
        em.flush();
    }

//    public void spremi(PodatakSirovi podatak) {
//        if (!postoji(podatak)) {
//            create(podatak);
//        } else {
//            log.log(Level.INFO, "Podatak: {0}: {1} vec postoji", new Object[]{
//                podatak.getProgramMjerenjaId().getId(), podatak.getVrijeme()
//            });
//        }
//    }
    public boolean postoji(PodatakSirovi podatak) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PodatakSirovi> cq = cb.createQuery(PodatakSirovi.class);
        Root<PodatakSirovi> from = cq.from(PodatakSirovi.class);

        Expression<Date> vrijemeE = from.get(PodatakSirovi_.vrijeme);
        Expression<ProgramMjerenja> programE = from.get(PodatakSirovi_.programMjerenjaId);

        cq.where(cb.and(cb.equal(vrijemeE, podatak.getVrijeme()),
                cb.equal(programE, podatak.getProgramMjerenjaId())));
        cq.select(from);
        List<PodatakSirovi> resultList = em.createQuery(cq).getResultList();
        return (resultList != null) && (!resultList.isEmpty());
    }

    public Date getVrijemeZadnjeg(IzvorPodataka izvor, Postaja postaja, String datoteka) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Date> cq = cb.createQuery(Date.class);
        Root<PodatakSirovi> from = cq.from(PodatakSirovi.class);

        Join<PodatakSirovi, ProgramMjerenja> programJ = from.join(PodatakSirovi_.programMjerenjaId);
        
        Predicate datotekaP = cb.equal(programJ.join(ProgramMjerenja_.izvorProgramKljuceviMap).get(IzvorProgramKljuceviMap_.nKljuc), datoteka);
        Predicate postajaP = cb.equal(programJ.join(ProgramMjerenja_.postajaId), postaja);
        Predicate izvorP = cb.equal(programJ.join(ProgramMjerenja_.izvorPodatakaId), izvor);

        Expression<Date> vrijeme = from.get(PodatakSirovi_.vrijeme);


        CriteriaQuery<Date> select = cq.select(cb.greatest(vrijeme))
                                        .where(cb.and(izvorP, postajaP, datotekaP));
        List<Date> rl = em.createQuery(cq).getResultList();
        if (rl == null || rl.isEmpty() || rl.get(0) == null) {
            return new Date(0L);
        }

        return rl.get(0);
    }

    public Collection<PodatakSirovi> getPodaci(ProgramMjerenja pm, Date pocetak, Date kraj) {
        return getPodaci(pm, pocetak, kraj, false, true); 
    }

    public Collection<PodatakSirovi> getPodaci(ProgramMjerenja pm, Date pocetak, Date kraj, boolean p, boolean k) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PodatakSirovi> cq = cb.createQuery(PodatakSirovi.class);
        Root<PodatakSirovi> from = cq.from(PodatakSirovi.class);
        Expression<Date> vrijemeE = from.get(PodatakSirovi_.vrijeme);
        Expression<ProgramMjerenja> programE = from.get(PodatakSirovi_.programMjerenjaId);

        cq.where(
                cb.and(
                        cb.equal(programE, pm),
                        (p ? cb.greaterThanOrEqualTo(vrijemeE, pocetak) : cb.greaterThan(vrijemeE, pocetak)),
                        (k ? cb.lessThanOrEqualTo(vrijemeE, kraj) : cb.lessThan(vrijemeE, kraj))
                )
        );

        cq.select(from).orderBy(cb.asc(vrijemeE));
        return em.createQuery(cq).getResultList();
    }

    
    public Collection<PodatakSirovi> getPodatkeZaSat(ProgramMjerenja pm, Date kraj) {

//        Date kraj = sat.getTime();
//        sat.add(Calendar.HOUR, -1);
        Date pocetak = new Date(kraj.getTime() - 3600 * 1000);
        return getPodaci(pm, pocetak, kraj);
    }

    public Date getZadnjiPodatak(ProgramMjerenja program) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Date> cq = cb.createQuery(Date.class);
        Root<PodatakSirovi> from = cq.from(PodatakSirovi.class);

        Expression<Date> vrijeme = from.get(PodatakSirovi_.vrijeme);
        Expression<ProgramMjerenja> programE = from.get(PodatakSirovi_.programMjerenjaId);

        cq.where(
                cb.equal(programE, program)
        );
        CriteriaQuery<Date> select = cq.select(cb.greatest(vrijeme));
        List<Date> rl = em.createQuery(cq).getResultList();
        if (rl == null || rl.isEmpty() || rl.get(0) == null) {
            return program.getPocetakMjerenja();
        }
        return rl.get(0);
    }
}
