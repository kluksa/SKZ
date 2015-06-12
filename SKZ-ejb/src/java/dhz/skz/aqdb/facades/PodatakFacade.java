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
import javax.ejb.LocalBean;
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
@LocalBean
public class PodatakFacade extends AbstractFacade<Podatak> {

    private static final Logger log = Logger.getLogger(PodatakFacade.class.getName());
    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

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
        if (p) {
            pocetakP = cb.greaterThanOrEqualTo(vrijemeE, pocetak);
        } else {
            pocetakP = cb.greaterThan(vrijemeE, pocetak);
        }
        if (k) {
            krajP = cb.lessThanOrEqualTo(vrijemeE, kraj);
        } else {
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
        return getZadnjiPodatak(program, new NivoValidacije(0));
    }
    
    public Date getZadnjiPodatak(ProgramMjerenja program, NivoValidacije nv) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Date> cq = cb.createQuery(Date.class);
        Root<Podatak> from = cq.from(Podatak.class);

        Expression<ProgramMjerenja> programE = from.get(Podatak_.programMjerenjaId);
        Expression<NivoValidacije> nivoValidacijeE = from.get(Podatak_.nivoValidacijeId);
        Expression<Date> vrijeme = from.get(Podatak_.vrijeme);

        cq.where(
                cb.and(
                        cb.equal(nivoValidacijeE, nv),
                        cb.equal(programE, program)
                )
        );
        cq.select(cb.greatest(vrijeme));

        List<Date> rl = em.createQuery(cq).getResultList();
        if (rl == null || rl.isEmpty() || rl.get(0) == null) {
//            return program.getPocetakMjerenja();
            return new Date(1388534400000L);
        }
        return rl.get(0);
    }

    public Podatak getZadnji(IzvorPodataka i, Postaja p) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Podatak> cq = cb.createQuery(Podatak.class);
        Root<Podatak> from = cq.from(Podatak.class);
        Join<Podatak, ProgramMjerenja> pmJ = from.join(Podatak_.programMjerenjaId);
        Predicate postaja = cb.equal(pmJ.join(ProgramMjerenja_.postajaId), p);
        Predicate izvor = cb.equal(pmJ.join(ProgramMjerenja_.izvorPodatakaId), i);
        Predicate nivo = cb.equal(from.get(Podatak_.nivoValidacijeId), new NivoValidacije(0));
        Expression<Date> vrijeme = from.get(Podatak_.vrijeme);
        cq.select(from).where(cb.and(postaja, izvor, nivo)).orderBy(cb.desc(vrijeme));
        List<Podatak> rl = em.createQuery(cq).setMaxResults(1).getResultList();
        if (rl.isEmpty()) {
            return null;
        } else {
            return rl.get(0);
        }
    }

    public Date getVrijemeZadnjeg(IzvorPodataka i, Postaja p) {
        Podatak pod = getZadnji(i, p);
        if (pod != null) {
            return pod.getVrijeme();
        } else {
            return null;
        }
    }

    public Podatak find(Podatak pod) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Podatak> cq = cb.createQuery(Podatak.class);
        Root<Podatak> from = cq.from(Podatak.class);
        cq.select(from)
                .where(
                        cb.and(
                                cb.equal(from.get(Podatak_.vrijeme), pod.getVrijeme()),
                                cb.equal(from.get(Podatak_.programMjerenjaId), pod.getProgramMjerenjaId()),
                                cb.equal(from.get(Podatak_.nivoValidacijeId), pod.getNivoValidacijeId())));
        List<Podatak> resultList = em.createQuery(cq).getResultList();
        if (resultList.isEmpty()) {
            return null;
        } else {
            return resultList.get(0);
        }
    }

    public void spremi(Podatak ps) {
        log.log(Level.FINEST, "SPREMAM: {0}:{1}:{2}:{3}", new Object[]{ps.getVrijeme(), ps.getProgramMjerenjaId(), ps.getStatus(), ps.getVrijednost()});
        Podatak pod = find(ps);
        if (pod == null) {
            em.persist(ps);
        } else {
            ps.setPodatakId(pod.getPodatakId());
            em.merge(ps);
        }
    }

    public void spremi(Collection<Podatak> ps) {
        for (Podatak p : ps) {
            spremi(ps);
        }
    }

    public void flush() {
        em.flush();
    }
}
