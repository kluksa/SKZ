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

import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ZeroSpanReferentneVrijednosti;
import dhz.skz.aqdb.entity.ZeroSpanReferentneVrijednosti_;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

/**
 *
 * @author kraljevic
 */
@Stateless
@LocalBean
public class ZeroSpanReferentneVrijednostiFacade extends AbstractFacade<ZeroSpanReferentneVrijednosti> {

    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ZeroSpanReferentneVrijednostiFacade() {
        super(ZeroSpanReferentneVrijednosti.class);
    }
    
    public List<ZeroSpanReferentneVrijednosti> findByProgramVrsta(final ProgramMjerenja program, final String vrsta) {
        return em.createNamedQuery("ZeroSpanReferentneVrijednosti.findByProgramVrsta", ZeroSpanReferentneVrijednosti.class)
                .setParameter("program", program).setParameter("vrsta", vrsta)
                .getResultList();
    }

    // TODO prebaciti u named query
    public List<ZeroSpanReferentneVrijednosti> findZadnjiPrije(final ProgramMjerenja program, final Date uPrimjeniPrije) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ZeroSpanReferentneVrijednosti> cq = cb.createQuery(ZeroSpanReferentneVrijednosti.class);
        Root<ZeroSpanReferentneVrijednosti> zsT = cq.from(ZeroSpanReferentneVrijednosti.class);

        Expression<Date> vrijemeT = zsT.get(ZeroSpanReferentneVrijednosti_.pocetakPrimjene);
        Expression<String> vrstaT = zsT.get(ZeroSpanReferentneVrijednosti_.vrsta);
        Expression<ProgramMjerenja> programT = zsT.get(ZeroSpanReferentneVrijednosti_.programMjerenjaId);
        cq.where(
                cb.and(
                        cb.lessThanOrEqualTo(vrijemeT, uPrimjeniPrije),
                        cb.equal(programT, program)
                )
        );
        cq.select(zsT).orderBy(cb.desc(vrijemeT));
        return em.createQuery(cq).getResultList();
    }

}
