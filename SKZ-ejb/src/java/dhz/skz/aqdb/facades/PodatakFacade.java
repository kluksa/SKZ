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

import dhz.skz.aqdb.entity.Komponenta;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.Podatak_;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ProgramMjerenja_;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
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

    public Collection<Podatak> getPodaciZaKomponentu(Date pocetak, Date kraj, Komponenta k, Integer nv, short usporedno) {
        em.refresh(k);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Podatak> cq = cb.createQuery(Podatak.class);
        Root<Podatak> from = cq.from(Podatak.class);
        Join<Podatak, ProgramMjerenja> podatakProgram = from.join(Podatak_.programMjerenjaId);

        Expression<Komponenta> komponentaE = podatakProgram.get(ProgramMjerenja_.komponentaId);
        Path<Integer> usporednoE = podatakProgram.get(ProgramMjerenja_.usporednoMjerenje);
        Expression<Integer> nivoE = from.get(Podatak_.nivoValidacijeId);
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

    public List<Podatak> getPodaciOd(ProgramMjerenja pm, Date pocetak, Integer nv) {
        CriteriaQuery<Podatak> cq = getQueryPodaci(pm, pocetak, null, nv, false, false, null);
        return em.createQuery(cq).getResultList();
    }

    public List<Podatak> getPodatak(ProgramMjerenja pm, Date pocetak, Date kraj, Integer nv, boolean p, boolean k) {
        CriteriaQuery<Podatak> cq = getQueryPodaci(pm, pocetak, kraj, nv, p, k, null);
        return em.createQuery(cq).getResultList();
    }

    private CriteriaQuery<Podatak> getQueryPodaci(ProgramMjerenja pm, Date pocetak, Date kraj, Integer nv, boolean p, boolean k, Order orderBy) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Podatak> cq = cb.createQuery(Podatak.class);
        Root<Podatak> from = cq.from(Podatak.class);

        Expression<Integer> nivoValidacijeE = from.get(Podatak_.nivoValidacijeId);
        Expression<Date> vrijemeE = from.get(Podatak_.vrijeme);
        Expression<ProgramMjerenja> programE = from.get(Podatak_.programMjerenjaId);
        List<Predicate> uvjeti = new ArrayList<>();
        uvjeti.add(cb.equal(nivoValidacijeE, nv));
        uvjeti.add(cb.equal(programE, pm));

        if (pocetak != null) {
            if (p) {
                uvjeti.add(cb.greaterThanOrEqualTo(vrijemeE, pocetak));
            } else {
                uvjeti.add(cb.greaterThan(vrijemeE, pocetak));
            }
        }
        if (kraj != null) {
            if (k) {
                uvjeti.add(cb.lessThanOrEqualTo(vrijemeE, kraj));
            } else {
                uvjeti.add(cb.lessThan(vrijemeE, kraj));
            }
        }
        cq.where(cb.and(uvjeti.toArray(new Predicate[]{})));
        cq.select(from);
        if (orderBy != null) {
            cq.orderBy(orderBy);
        }
        return cq;
    }

    public Date getVrijemeZadnjeg(ProgramMjerenja program, Integer nv) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Date> cq = cb.createQuery(Date.class);
        Root<Podatak> from = cq.from(Podatak.class);

        Expression<ProgramMjerenja> programE = from.get(Podatak_.programMjerenjaId);
        Expression<Integer> nivoValidacijeE = from.get(Podatak_.nivoValidacijeId);
        Expression<Date> vrijeme = from.get(Podatak_.vrijeme);

        cq.where(
                cb.and(
                        cb.equal(nivoValidacijeE, nv),
                        cb.equal(programE, program)
                )
        );
        cq.select(cb.greatest(vrijeme));

        List<Date> rl = em.createQuery(cq).getResultList();
        return (rl == null || rl.isEmpty() || rl.get(0) == null) ? new Date(0L) : rl.get(0);
    }

    public Date getVrijemeZadnjeg(Postaja p) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Podatak> cq = cb.createQuery(Podatak.class);
        Root<Podatak> from = cq.from(Podatak.class);

        Order vrijemeO = cb.desc(from.get(Podatak_.vrijeme));
        Predicate validP = cb.equal(from.get(Podatak_.nivoValidacijeId), 0);
        Predicate postajaP = cb.equal(from.join(Podatak_.programMjerenjaId).join(ProgramMjerenja_.postajaId), p);
        Predicate and = cb.and(validP, postajaP);

        cq.select(from).where(and).orderBy(vrijemeO);
        List<Podatak> rl = em.createQuery(cq).setMaxResults(1).getResultList();
        return (rl.isEmpty() || rl.get(0) == null) ? new Date(0L) : rl.get(0).getVrijeme();
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
        return (!resultList.isEmpty())? resultList.get(0) : null;
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
}
