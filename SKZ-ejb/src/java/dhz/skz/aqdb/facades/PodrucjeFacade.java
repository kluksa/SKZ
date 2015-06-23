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

import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.Podatak_;
import dhz.skz.aqdb.entity.Podrucje;
import dhz.skz.aqdb.entity.Podrucje_;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja_;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author kraljevic
 */
@Stateless
@LocalBean
public class PodrucjeFacade extends AbstractFacade<Podrucje> {

    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PodrucjeFacade() {
        super(Podrucje.class);
    }

    // TODO prebaciti u named query
    public Podrucje findByOznaka(String oznaka) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Podrucje> cq = cb.createQuery(Podrucje.class);

        Root<Podrucje> from = cq.from(Podrucje.class);

        cq.select(from).where(cb.equal(from.get(Podrucje_.oznaka), oznaka));
        return em.createQuery(cq).getSingleResult();
    }

    // TODO prebaciti u named query
    public Date getZadnjeVrijeme(Postaja p) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Podatak> cq = cb.createQuery(Podatak.class);
        Root<Podatak> from = cq.from(Podatak.class);

        Order vrijemeO = cb.desc(from.get(Podatak_.vrijeme));
        Predicate validP = cb.equal(from.get(Podatak_.nivoValidacijeId), 0);
        Predicate postajaP = cb.equal(from.join(Podatak_.programMjerenjaId).join(ProgramMjerenja_.postajaId), p);
        Predicate and = cb.and(validP, postajaP);

        cq.select(from).where(and).orderBy(vrijemeO);
        List<Podatak> rl = em.createQuery(cq).setMaxResults(1).getResultList();
        if (rl.isEmpty() || rl.get(0) == null) {
            return null;
        } else {
            return rl.get(0).getVrijeme();
        }
    }

}
