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
import dhz.skz.aqdb.entity.IzvorPodataka_;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.PostajaUredjajLink;
import dhz.skz.aqdb.entity.PostajaUredjajLink_;
import dhz.skz.aqdb.entity.Postaja_;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ProgramMjerenja_;
import dhz.skz.aqdb.entity.Uredjaj;
import dhz.skz.aqdb.entity.Uredjaj_;
import java.util.Collection;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CollectionJoin;
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
public class PostajaFacade extends AbstractFacade<Postaja> {

    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PostajaFacade() {
        super(Postaja.class);
    }

    // TODO prebaciti u named query
    public Postaja findByNacionalnaOznaka(final String oznaka) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Postaja> cq = cb.createQuery(Postaja.class);
        Root<Postaja> from = cq.from(Postaja.class);
        cq.select(from).where(cb.equal(from.get(Postaja_.nacionalnaOznaka), oznaka));
        return em.createQuery(cq).getSingleResult();
    }


    // TODO prebaciti u named query
    public Collection<Postaja> getPostajeZaIzvor(IzvorPodataka i) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Postaja> cq = cb.createQuery(Postaja.class);
        Root<IzvorPodataka> izvor = cq.from(IzvorPodataka.class);
        CollectionJoin<IzvorPodataka, ProgramMjerenja> izvorProgram = izvor.join(IzvorPodataka_.programMjerenjaCollection);
        Join<ProgramMjerenja, Postaja> programPostaja = izvorProgram.join(ProgramMjerenja_.postajaId);
        Expression<Postaja> postaja = izvorProgram.get(ProgramMjerenja_.postajaId);
        Expression<String> naziv = programPostaja.get(Postaja_.nazivPostaje);
        cq.where(cb.equal(izvor, i)).select(postaja).distinct(true).orderBy(cb.asc(naziv));
        return em.createQuery(cq).getResultList();
    }

    // TODO prebaciti u named query
    public Postaja findByNaziv(String naziv) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Postaja> cq = cb.createQuery(Postaja.class);

        Root<Postaja> from = cq.from(Postaja.class);

        cq.select(from).where(cb.equal(from.get(Postaja_.nazivPostaje), naziv));
        return em.createQuery(cq).getSingleResult();
    }

    // TODO prebaciti u named query
    public Postaja findByUredjajSn(String uredjaj_id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Postaja> cq = cb.createQuery(Postaja.class);
        Root<Postaja> from = cq.from(Postaja.class);
        Join<Postaja, PostajaUredjajLink> pulJ = from.join(Postaja_.postajaUredjajLinkCollection);
        Join<PostajaUredjajLink, Uredjaj> uredjajJ = pulJ.join(PostajaUredjajLink_.uredjajId);

        Predicate uvjet = cb.and(cb.equal(uredjajJ.get(Uredjaj_.serijskaOznaka), uredjaj_id),
                cb.isNull(pulJ.get(PostajaUredjajLink_.vrijemeUklanjanja)));

        cq.select(from).where(uvjet);
        return em.createQuery(cq).getSingleResult();
    }
}
