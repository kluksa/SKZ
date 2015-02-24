/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.facades;

import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ZeroSpanReferentneVrijednosti;
import dhz.skz.aqdb.entity.ZeroSpanReferentneVrijednosti_;
import java.util.Collection;
import java.util.Date;
import java.util.List;
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
public class ZeroSpanReferentneVrijednostiFacade extends AbstractFacade<ZeroSpanReferentneVrijednosti> implements ZeroSpanReferentneVrijednostiFacadeLocal, dhz.skz.aqdb.facades.ZeroSpanReferentneVrijednostiFacadeRemote {
    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ZeroSpanReferentneVrijednostiFacade() {
        super(ZeroSpanReferentneVrijednosti.class);
    }

    @Override
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
        return  em.createQuery(cq).getResultList();
    }
}
