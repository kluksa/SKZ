/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.facades;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.Komponenta;
import dhz.skz.aqdb.entity.NivoValidacije;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.Podatak_;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ProgramMjerenja_;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
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
public class PodatakFacade extends AbstractFacade<Podatak>  {

    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;
    private static final Logger log = Logger.getLogger(PodatakFacade.class.getName());

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PodatakFacade() {
        super(Podatak.class);
    }

    public Map<ProgramMjerenja, Podatak> getZadnjiPodatakPoProgramu() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<Podatak> from = cq.from(Podatak.class);

        Expression<NivoValidacije> nivoValidacijeE = from.get(Podatak_.nivoValidacijeId);
        Expression<Date> vrijeme = from.get(Podatak_.vrijeme);
        Expression<ProgramMjerenja> program = from.get(Podatak_.programMjerenjaId);

        cq.where(cb.equal(nivoValidacijeE, new NivoValidacije(0)));
        cq.groupBy(program);
        cq.multiselect(cb.greatest(vrijeme), from);

        Map<ProgramMjerenja, Podatak> pm = new HashMap<>();
        for (Tuple t : em.createQuery(cq).getResultList()) {
            Date vr = t.get(0, Date.class);
            Podatak po = t.get(1, Podatak.class);
            pm.put(po.getProgramMjerenjaId(), po);
        }
        return pm;
    }

    public Collection<Podatak> getPodaciZaKomponentu(Date pocetak, Date kraj, Komponenta k, NivoValidacije nv, short usporedno) {
        em.refresh(k);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Podatak> cq = cb.createQuery(Podatak.class);
        Root<Podatak> from = cq.from(Podatak.class);
        Join<Podatak, ProgramMjerenja> podatakProgram = from.join(Podatak_.programMjerenjaId);

        Expression<Komponenta> komponentaE = podatakProgram.get(ProgramMjerenja_.komponentaId);
        Path<Integer> usporednoE = podatakProgram.get(ProgramMjerenja_.usporednoMjerenje);
        Expression<NivoValidacije> nivoE = from.get(Podatak_.nivoValidacijeId);
        Expression<Date> vrijemeE = from.get(Podatak_.vrijeme);

        cq.where(cb.and(
                cb.equal(komponentaE, k),
                cb.equal(nivoE, nv),
                cb.equal(usporednoE, usporedno),
                cb.greaterThanOrEqualTo(vrijemeE, pocetak),
                cb.lessThanOrEqualTo(vrijemeE, kraj)
        ));
        cq.select(from).orderBy(cb.asc(vrijemeE));
        return em.createQuery(cq).getResultList();
    }

    public List<Podatak> getPodatakOd(ProgramMjerenja pm, Date pocetak) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Podatak> cq = cb.createQuery(Podatak.class);
        Root<Podatak> from = cq.from(Podatak.class);

        Expression<NivoValidacije> nivoValidacijeE = from.get(Podatak_.nivoValidacijeId);
        Expression<Date> vrijemeE = from.get(Podatak_.vrijeme);
        Expression<ProgramMjerenja> programE = from.get(Podatak_.programMjerenjaId);

        cq.where(
                cb.and(
                        cb.equal(nivoValidacijeE, new NivoValidacije(0)),
                        cb.equal(programE, pm),
                        cb.greaterThan(vrijemeE, pocetak)
                )
        );
        cq.select(from);
        return em.createQuery(cq).getResultList();
    }

    public List<Podatak> getPodatak(ProgramMjerenja pm, Date pocetak, Date kraj, boolean p, boolean k) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Podatak> cq = cb.createQuery(Podatak.class);
        Root<Podatak> from = cq.from(Podatak.class);

        Expression<NivoValidacije> nivoValidacijeE = from.get(Podatak_.nivoValidacijeId);
        Expression<Date> vrijemeE = from.get(Podatak_.vrijeme);
        Expression<ProgramMjerenja> programE = from.get(Podatak_.programMjerenjaId);
        Predicate pocetakP, krajP;
        if ( p ) {
            pocetakP = cb.greaterThanOrEqualTo(vrijemeE, pocetak);
        } else  {
            pocetakP = cb.greaterThan(vrijemeE, pocetak);
        }
        if ( k ) {
            krajP = cb.lessThanOrEqualTo(vrijemeE, kraj);
        } else  {
            krajP = cb.lessThan(vrijemeE, kraj);
        }
       
        Predicate uvjet = cb.and(pocetakP, krajP); 
        cq.where(
                cb.and(
                        cb.equal(nivoValidacijeE, new NivoValidacije(0)),
                        cb.equal(programE, pm),
                        uvjet
                )
        );
        cq.select(from);
        return em.createQuery(cq).getResultList();
    }
    
    
    public List<Podatak> getPodatak(ProgramMjerenja pm, Date pocetak, Date kraj) {
        return getPodatak(pm, pocetak, kraj, false, true);
    }

    public Date getZadnjiPodatak(ProgramMjerenja program) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Date> cq = cb.createQuery(Date.class);
        Root<Podatak> from = cq.from(Podatak.class);

        Expression<ProgramMjerenja> programE = from.get(Podatak_.programMjerenjaId);
        Expression<NivoValidacije> nivoValidacijeE = from.get(Podatak_.nivoValidacijeId);
        Expression<Date> vrijeme = from.get(Podatak_.vrijeme);

        cq.where(
                cb.and(
                        cb.equal(nivoValidacijeE, new NivoValidacije(0)),
                        cb.equal(programE, program)
                )
        );
        cq.select(cb.greatest(vrijeme));

        List<Date> rl = em.createQuery(cq).getResultList();
        if (rl == null || rl.isEmpty() || rl.get(0) == null) {
            return program.getPocetakMjerenja();
        }
        return rl.get(0);
    }
    
    public Podatak getZadnji(IzvorPodataka i, Postaja p) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Podatak> cq = cb.createQuery(Podatak.class);
        Root<Podatak> from = cq.from(Podatak.class);
        Join<Podatak, ProgramMjerenja> pmJ = from.join(Podatak_.programMjerenjaId);
        Predicate postaja = cb.equal(pmJ.join(ProgramMjerenja_.postajaId),p);
        Predicate izvor = cb.equal(pmJ.join(ProgramMjerenja_.izvorPodatakaId), i);
        Predicate nivo = cb.equal(from.get(Podatak_.nivoValidacijeId), new NivoValidacije(0));
        Expression<Date> vrijeme = from.get(Podatak_.vrijeme);
        cq.select(from).where(cb.and(postaja, izvor, nivo)).orderBy(cb.desc(vrijeme));
        List<Podatak> rl = em.createQuery(cq).setMaxResults(1).getResultList();
        if ( rl.isEmpty()) {
            return null;
        } else {
            return rl.get(0);
        }
    }

//    public Date getVrijemeZadnjegNaPostajiZaIzvor(Postaja p, IzvorPodataka i) {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Date> cq = cb.createQuery(Date.class);
//        Root<Podatak> from = cq.from(Podatak.class);
//        Join<Podatak, ProgramMjerenja> podatakProgram = from.join(Podatak_.programMjerenjaId);
//
//        Expression<NivoValidacije> nivoValidacijeE = from.get(Podatak_.nivoValidacijeId);
//        Expression<Postaja> postaja = podatakProgram.get(ProgramMjerenja_.postajaId);
//        Expression<IzvorPodataka> izvor = podatakProgram.get(ProgramMjerenja_.izvorPodatakaId);
//        Expression<Date> vrijeme = from.get(Podatak_.vrijeme);
//
//        cq.where(
//                cb.and(
//                        cb.equal(nivoValidacijeE, new NivoValidacije((short) 0)),
//                        cb.equal(postaja, p),
//                        cb.equal(izvor, i)
//                )
//        );
//        cq.select(cb.greatest(vrijeme));
//        return em.createQuery(cq).getSingleResult();
//    }


    public void spremi(Podatak ps) {
        log.log(Level.FINEST, "SPREMAM: {0}:{1}:{2}:{3}", new Object[]{ps.getVrijeme(), ps.getProgramMjerenjaId(), ps.getStatus(), ps.getVrijednost()});
        em.persist(ps);
    }
    
    public void spremi(Collection<Podatak> ps) {
        for ( Podatak p : ps ) {
            em.persist(p);
        }
    }
    
    public void flush(){
        em.flush();
    }
}
