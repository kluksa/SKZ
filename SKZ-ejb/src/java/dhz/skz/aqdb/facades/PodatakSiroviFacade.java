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

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.IzvorProgramKljuceviMap_;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.PodatakSirovi_;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ProgramMjerenja_;
import dhz.skz.aqdb.entity.ZadnjiSirovi;
import dhz.skz.aqdb.entity.ZadnjiSirovi_;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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
@LocalBean
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

    public boolean postoji(PodatakSirovi podatak) {
         return !em.createNamedQuery("PodatakSirovi.findByVrijemeProgram", PodatakSirovi.class)
                 .setParameter("vrijeme", podatak.getVrijeme()).setParameter("programMjerenja", podatak.getProgramMjerenjaId())
                .setMaxResults(1).getResultList().isEmpty();
    }

    // TODO prebaciti u named query
    public  List<PodatakSirovi> findAll(Date pocetak, Date kraj) {
        TypedQuery<PodatakSirovi> query = em.createNamedQuery("PodatakSirovi.findByPocetakKraj", PodatakSirovi.class);
        query.setParameter("pocetak", pocetak);
        query.setParameter("kraj", kraj);
        return query.getResultList();
    }

    // TODO prebaciti u named query
    public List<PodatakSirovi> getPodaci(Date pocetak, Date kraj, boolean p, boolean k) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PodatakSirovi> cq = cb.createQuery(PodatakSirovi.class);
        Root<PodatakSirovi> from = cq.from(PodatakSirovi.class);
        Expression<Date> vrijemeE = from.get(PodatakSirovi_.vrijeme);

        cq.where(
                cb.and(
                        (p ? cb.greaterThanOrEqualTo(vrijemeE, pocetak) : cb.greaterThan(vrijemeE, pocetak)),
                        (k ? cb.lessThanOrEqualTo(vrijemeE, kraj) : cb.lessThan(vrijemeE, kraj))
                )
        );
        cq.select(from).orderBy(cb.asc(vrijemeE));
        return em.createQuery(cq).getResultList();
    }

    // TODO prebaciti u named query
    public List<PodatakSirovi> getPodaci(ProgramMjerenja pm, Date pocetak, Date kraj, boolean p, boolean k) {
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
    
    public PodatakSirovi getZadnji(ProgramMjerenja program) {
        em.getEntityManagerFactory().getCache().evictAll();
        ZadnjiSirovi zadnji = getEntityManager().find(ZadnjiSirovi.class, program);
        return zadnji.getPodatakSiroviId();
    }
 
     // TODO prebaciti u named query
    public PodatakSirovi getZadnji(IzvorPodataka izvor, Postaja postaja){
        em.getEntityManagerFactory().getCache().evictAll();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ZadnjiSirovi> cq = cb.createQuery(ZadnjiSirovi.class);
        Root<ZadnjiSirovi> from = cq.from(ZadnjiSirovi.class);

        Join<ZadnjiSirovi, ProgramMjerenja> programJ = from.join(ZadnjiSirovi_.programMjerenja);
        Predicate postajaP = cb.equal(programJ.join(ProgramMjerenja_.postajaId), postaja);
        Predicate izvorP = cb.equal(programJ.join(ProgramMjerenja_.izvorPodatakaId), izvor);

        Expression<Date> vrijemeE = from.get(ZadnjiSirovi_.vrijeme);
        cq.select(from).where(cb.and(izvorP, postajaP)).orderBy(cb.desc(vrijemeE));
        
        TypedQuery<ZadnjiSirovi> createQuery = em.createQuery(cq);
        TypedQuery<ZadnjiSirovi> setMaxResults = createQuery.setMaxResults(1);
        List<ZadnjiSirovi> resultList = setMaxResults.getResultList();
        List<ZadnjiSirovi> rl = em.createQuery(cq).setMaxResults(1).getResultList();
        if (rl == null || rl.isEmpty()) {
            return null;
        }
        return rl.get(0).getPodatakSiroviId();
    }
    
        // TODO prebaciti u named query
    public PodatakSirovi getZadnji(IzvorPodataka izvor, Postaja postaja, String datoteka) {
        em.getEntityManagerFactory().getCache().evictAll();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ZadnjiSirovi> cq = cb.createQuery(ZadnjiSirovi.class);
        Root<ZadnjiSirovi> from = cq.from(ZadnjiSirovi.class);

        Join<ZadnjiSirovi, ProgramMjerenja> programJ = from.join(ZadnjiSirovi_.programMjerenja);
        Predicate postajaP = cb.equal(programJ.join(ProgramMjerenja_.postajaId), postaja);
        Predicate izvorP = cb.equal(programJ.join(ProgramMjerenja_.izvorPodatakaId), izvor);
        Predicate datotekaP = cb.equal(programJ.join(ProgramMjerenja_.izvorProgramKljuceviMap).get(IzvorProgramKljuceviMap_.nKljuc), datoteka);

        Expression<Date> vrijemeE = from.get(ZadnjiSirovi_.vrijeme);

        CriteriaQuery<ZadnjiSirovi> select = cq.select(from)
                .where(cb.and(izvorP, postajaP, datotekaP)).orderBy(cb.desc(vrijemeE));
        List<ZadnjiSirovi> rl = em.createQuery(cq).setMaxResults(1).getResultList();
        if (rl == null || rl.isEmpty()) {
            return null;
        }
        return rl.get(0).getPodatakSiroviId();
    }

   
    
//    // TODO prebaciti u named query
//    public PodatakSirovi getZadnji(IzvorPodataka izvor, Postaja postaja){
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<PodatakSirovi> cq = cb.createQuery(PodatakSirovi.class);
//        Root<PodatakSirovi> from = cq.from(PodatakSirovi.class);
//
//        Join<PodatakSirovi, ProgramMjerenja> programJ = from.join(PodatakSirovi_.programMjerenjaId);
//        Predicate postajaP = cb.equal(programJ.join(ProgramMjerenja_.postajaId), postaja);
//        Predicate izvorP = cb.equal(programJ.join(ProgramMjerenja_.izvorPodatakaId), izvor);
//
//        Expression<Date> vrijemeE = from.get(PodatakSirovi_.vrijeme);
//        cq.select(from).where(cb.and(izvorP, postajaP)).orderBy(cb.desc(vrijemeE));
//        List<PodatakSirovi> rl = em.createQuery(cq).setMaxResults(1).getResultList();
//        if (rl == null || rl.isEmpty()) {
//            return null;
//        }
//        return rl.get(0);
//    }
    
//    // TODO prebaciti u named query
//    public PodatakSirovi getZadnji(ProgramMjerenja program) {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<PodatakSirovi> cq = cb.createQuery(PodatakSirovi.class);
//        Root<PodatakSirovi> from = cq.from(PodatakSirovi.class);
//
//        Expression<Date> vrijemeE = from.get(PodatakSirovi_.vrijeme);
//        Expression<ProgramMjerenja> programE = from.get(PodatakSirovi_.programMjerenjaId);
//
//        cq.where(
//                cb.equal(programE, program)
//        );
//        cq.select(from).orderBy(cb.desc(vrijemeE));
//        List<PodatakSirovi> rl = em.createQuery(cq).setMaxResults(1).getResultList();
//        if (rl == null || rl.isEmpty()) {
//            return null;
//        }
//        return rl.get(0);
//    }

        // TODO prebaciti u named query
    public PodatakSirovi getPrvi(ProgramMjerenja program) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PodatakSirovi> cq = cb.createQuery(PodatakSirovi.class);
        Root<PodatakSirovi> from = cq.from(PodatakSirovi.class);

        Expression<Date> vrijemeE = from.get(PodatakSirovi_.vrijeme);
        Expression<ProgramMjerenja> programE = from.get(PodatakSirovi_.programMjerenjaId);

        cq.where(
                cb.equal(programE, program)
        );
        cq.select(from).orderBy(cb.asc(vrijemeE));
        List<PodatakSirovi> rl = em.createQuery(cq).setMaxResults(1).getResultList();
        if (rl == null || rl.isEmpty()) {
            return null;
        }
        return rl.get(0);
    }

    
//    // TODO prebaciti u named query
//    public PodatakSirovi getZadnji(IzvorPodataka izvor, Postaja postaja, String datoteka) {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<PodatakSirovi> cq = cb.createQuery(PodatakSirovi.class);
//        Root<PodatakSirovi> from = cq.from(PodatakSirovi.class);
//
//        Join<PodatakSirovi, ProgramMjerenja> programJ = from.join(PodatakSirovi_.programMjerenjaId);
//        Predicate postajaP = cb.equal(programJ.join(ProgramMjerenja_.postajaId), postaja);
//        Predicate izvorP = cb.equal(programJ.join(ProgramMjerenja_.izvorPodatakaId), izvor);
//        Predicate datotekaP = cb.equal(programJ.join(ProgramMjerenja_.izvorProgramKljuceviMap).get(IzvorProgramKljuceviMap_.nKljuc), datoteka);
//
//        Expression<Date> vrijemeE = from.get(PodatakSirovi_.vrijeme);
//
//        CriteriaQuery<PodatakSirovi> select = cq.select(from)
//                .where(cb.and(izvorP, postajaP, datotekaP)).orderBy(cb.desc(vrijemeE));
//        List<PodatakSirovi> rl = em.createQuery(cq).setMaxResults(1).getResultList();
//        if (rl == null || rl.isEmpty()) {
//            return null;
//        }
//        return rl.get(0);
//    }
    
//    public Date getVrijemeZadnjeg(IzvorPodataka izvor, Postaja postaja){
//        Date zadnji = new Date(0L);
//        PodatakSirovi ps = getZadnji(izvor, postaja);
//        if ( ps != null) {
//            zadnji = ps.getVrijeme();
//        }
//        return zadnji;
//    }
//
//    public Date getVrijemeZadnjeg(IzvorPodataka izvor, Postaja postaja, String datoteka){
//        Date zadnji = new Date(0L);
//        PodatakSirovi ps = getZadnji(izvor, postaja, datoteka);
//        if ( ps != null) {
//            zadnji = ps.getVrijeme();
//        }
//        return zadnji;
//    }
    
    public void edit(PodatakSirovi entity) {
        try {
            getEntityManager().merge(entity);
        } catch (Throwable ex) {
            log.log(Level.SEVERE,"", ex);
            Throwable cause = ex.getCause();
            while ( cause != null) {
                log.log(Level.SEVERE,"", cause);
                cause = ex.getCause();
            }
            throw(ex);
        }
    }
    
    public PodatakSirovi find(Integer id) {
        PodatakSirovi find = getEntityManager().find(PodatakSirovi.class, id);
        return find;
    }
    
    public void spremi(Collection<PodatakSirovi> podaci) {
        for (PodatakSirovi ps : podaci) {
            try {
                create(ps);
            } catch (Exception ex) {
                log.log(Level.SEVERE, "", ex);
            }
        }
        em.flush();
    }

    public PodatakSirovi findPrvi(ProgramMjerenja pm) {
         List<PodatakSirovi> rl = em.createNamedQuery("PodatakSirovi.findByProgramAsc", PodatakSirovi.class).setParameter("program", pm).setFirstResult(0).setMaxResults(1).getResultList();
        if (!rl.isEmpty() )
            return rl.get(0);
        else 
            return null;
    }
}
