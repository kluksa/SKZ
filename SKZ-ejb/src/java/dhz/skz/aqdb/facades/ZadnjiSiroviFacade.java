/*
 * Copyright (C) 2016 kraljevic
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
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ProgramMjerenja_;
import dhz.skz.aqdb.entity.ZadnjiSirovi;
import dhz.skz.aqdb.entity.ZadnjiSirovi_;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
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
public class ZadnjiSiroviFacade extends AbstractFacade<ZadnjiSirovi> {

    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @EJB
    private ProgramMjerenjaFacade pmf;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ZadnjiSiroviFacade() {
        super(ZadnjiSirovi.class);
    }

    public Date getVrijeme(IzvorPodataka izvor, Postaja postaja) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ZadnjiSirovi> cq = cb.createQuery(ZadnjiSirovi.class);
        Root<ZadnjiSirovi> from = cq.from(ZadnjiSirovi.class);

        Join<ZadnjiSirovi, ProgramMjerenja> programJ = from.join(ZadnjiSirovi_.programMjerenja);
        Predicate postajaP = cb.equal(programJ.join(ProgramMjerenja_.postajaId), postaja);
        Predicate izvorP = cb.equal(programJ.join(ProgramMjerenja_.izvorPodatakaId), izvor);

        Expression<Date> vrijemeE = from.get(ZadnjiSirovi_.vrijeme);
        cq.select(from).where(cb.and(izvorP, postajaP)).orderBy(cb.desc(vrijemeE));

        List<ZadnjiSirovi> rl = em.createQuery(cq).setMaxResults(1).getResultList();

        if (rl == null || rl.isEmpty()) {
            return pmf.getPocetakMjerenja(izvor, postaja);
        }
        em.refresh(rl.get(0));
        return rl.get(0).getVrijeme();
    }

    public Date getVrijeme(IzvorPodataka izvor, Postaja postaja, String datoteka) {
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
            return pmf.getPocetakMjerenja(izvor, postaja);
        }
        em.refresh(rl.get(0));
        return rl.get(0).getVrijeme();
    }
}
