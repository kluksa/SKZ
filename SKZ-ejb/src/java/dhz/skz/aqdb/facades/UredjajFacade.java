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

import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.Uredjaj;
import dhz.skz.aqdb.entity.Uredjaj_;
import java.util.Date;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author kraljevic
 */
@Stateless
@LocalBean
public class UredjajFacade extends AbstractFacade<Uredjaj> {

    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UredjajFacade() {
        super(Uredjaj.class);
    }

    // TODO prebaciti u named query
    public Uredjaj findBySn(String sernum) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Uredjaj> cq = cb.createQuery(Uredjaj.class);
        Root<Uredjaj> from = cq.from(Uredjaj.class);
        cq.select(from).where(cb.equal(from.get(Uredjaj_.serijskaOznaka), sernum.replaceAll("^0+", "")));
        return em.createQuery(cq).getSingleResult();
    }

    @TransactionAttribute(REQUIRES_NEW)
    public void premjesti(final Uredjaj uredjaj, final Postaja novaPostaja, final short usporednoMjerenje, final Date vrijeme) {
        // update postaja_uredjaj_link sa datumom uklanjanja
        if (novaPostaja.getId() != 0) {
            // provjeri postoji li program sa komponentama koje ovaj uredjaj mjeri
            // ako ne baci iznimku
        }
        // insert postaja_uredjaj_link sa datumom postavljanja

    }
}
