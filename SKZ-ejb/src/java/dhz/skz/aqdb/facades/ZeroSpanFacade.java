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
import dhz.skz.aqdb.entity.IzvorProgramKljuceviMap;
import dhz.skz.aqdb.entity.IzvorProgramKljuceviMap_;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ProgramMjerenja_;
import dhz.skz.aqdb.entity.ZeroSpan;
import dhz.skz.aqdb.entity.ZeroSpan_;
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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author kraljevic
 */
@Stateless
@LocalBean
public class ZeroSpanFacade extends AbstractFacade<ZeroSpan> {
    private static final Logger log = Logger.getLogger(ZeroSpanFacade.class.getName());
    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ZeroSpanFacade() {
        super(ZeroSpan.class);
    }

    // TODO prebaciti u named query
    public Collection<ProgramMjerenja> getProgram(Postaja postaja) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ProgramMjerenja> cq = cb.createQuery(ProgramMjerenja.class);

        Root<ZeroSpan> from = cq.from(ZeroSpan.class);

        Join<ZeroSpan, ProgramMjerenja> programJ = from.join(ZeroSpan_.programMjerenjaId);
        Join<ProgramMjerenja, Postaja> postajaJ = programJ.join(ProgramMjerenja_.postajaId);

        cq.select(programJ).where(cb.equal(postajaJ, postaja)).groupBy(programJ).distinct(true);
        return em.createQuery(cq).getResultList();

    }

    // TODO prebaciti u named query
    public Iterable<ZeroSpan> getPodaci(ProgramMjerenja selektiraniProgram, Date pocetak, Date kraj) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ZeroSpan> cq = cb.createQuery(ZeroSpan.class);
        Root<ZeroSpan> zsT = cq.from(ZeroSpan.class);

        Expression<Date> vrijemeT = zsT.get(ZeroSpan_.vrijeme);
        Expression<ProgramMjerenja> programT = zsT.get(ZeroSpan_.programMjerenjaId);
        cq.where(
                cb.and(
                        cb.greaterThan(vrijemeT, pocetak),
                        cb.lessThanOrEqualTo(vrijemeT, kraj),
                        cb.equal(programT, selektiraniProgram)
                )
        );
        cq.select(zsT);
        return em.createQuery(cq).getResultList();
    }
    
    // TODO prebaciti u named query
    public Date getVrijemeZadnjeg(IzvorPodataka izvor, Postaja p) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Date> cq = cb.createQuery(Date.class);
        Root<ZeroSpan> zsT = cq.from(ZeroSpan.class);
        Join<ZeroSpan, ProgramMjerenja> programT = zsT.join(ZeroSpan_.programMjerenjaId);

//        Join<ZeroSpan, Uredjaj>  uredjajT= zsT.join(ZeroSpan_.uredjajId);
//        Join<Uredjaj, ProgramUredjajLink> uredjajProgramT = uredjajT.join(Uredjaj_.programUredjajLinkCollection);
//        Join<ProgramUredjajLink, ProgramMjerenja> programT = uredjajProgramT.join(ProgramUredjajLink_.programMjerenjaId);
//        Join<ProgramMjerenja, IzvorPodataka> izvorT = programT.join(ProgramMjerenja_.izvorPodatakaId);
//        Join<ProgramMjerenja, Postaja> postajaT = programT.join(ProgramMjerenja_.postajaId);
        Expression<Date> vrijemeE = zsT.get(ZeroSpan_.vrijeme);
        Expression<Postaja> postajaE = programT.get(ProgramMjerenja_.postajaId);
        Expression<IzvorPodataka> izvorE = programT.get(ProgramMjerenja_.izvorPodatakaId);
        cq.where(
                cb.and(
                        cb.equal(postajaE, p),
                        cb.equal(izvorE, izvor)
                )
        );
        cq.select(cb.greatest(vrijemeE));
        List<Date> rl = em.createQuery(cq).setMaxResults(1).getResultList();
        if (rl == null || rl.isEmpty() || rl.get(0) == null) {
            return new Date(0L);
        }
        return rl.get(0);
    }

    // TODO prebaciti u named query
    public Collection<ProgramMjerenja> getProgramNaPostajiZaIzvor(Postaja p, IzvorPodataka i, Date zadnji) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ProgramMjerenja> cq = cb.createQuery(ProgramMjerenja.class);
        Root<ProgramMjerenja> program = cq.from(ProgramMjerenja.class);
        Expression<Postaja> postaja = program.get(ProgramMjerenja_.postajaId);
        Expression<IzvorPodataka> izvor = program.get(ProgramMjerenja_.izvorPodatakaId);
        Expression<Date> kraj = program.get(ProgramMjerenja_.zavrsetakMjerenja);

        cq.select(program).where(
                cb.and(
                        cb.equal(postaja, p),
                        cb.equal(izvor, i),
                        cb.or(
                                cb.isNull(kraj),
                                cb.greaterThan(kraj, zadnji)
                        )
                )
        );
        return em.createQuery(cq).getResultList();
    }

    // TODO prebaciti u named query
    public List<ZeroSpan> getZeroSpan(ProgramMjerenja programMjerenja, Date pocetak, Date kraj) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ZeroSpan> cq = cb.createQuery(ZeroSpan.class);
        Root<ZeroSpan> zsT = cq.from(ZeroSpan.class);

        Expression<Date> vrijemeT = zsT.get(ZeroSpan_.vrijeme);
        Expression<ProgramMjerenja> programT = zsT.get(ZeroSpan_.programMjerenjaId);
        cq.where(
                cb.and(
                        cb.greaterThan(vrijemeT, pocetak),
                        cb.lessThanOrEqualTo(vrijemeT, kraj),
                        cb.equal(programT, programMjerenja)
                )
        );
        cq.select(zsT).orderBy(cb.asc(vrijemeT));
        log.log(Level.INFO, "ZERO SPAN PROGRAM: {1}, SADA: {3} POCETAK: {0}, KRAJ: {2}", new Object[]{pocetak.getTime(), programMjerenja.getId(), kraj.getTime(), new Date().getTime()});
        return em.createQuery(cq).getResultList();
    }

    // TODO prebaciti u named query
    public List<ProgramMjerenja> getProgramSaZeroSpanomNaPostaji(IzvorPodataka izvor, Postaja p) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ProgramMjerenja> cq = cb.createQuery(ProgramMjerenja.class);

//        Root<IzvorProgramKljuceviMap> ipkmT = cq.from(IzvorProgramKljuceviMap.class);
        Root<ProgramMjerenja> pmT = cq.from(ProgramMjerenja.class);

        Join<ProgramMjerenja, IzvorProgramKljuceviMap> ipkmT = pmT.join(ProgramMjerenja_.izvorProgramKljuceviMap);
//        Join<IzvorProgramKljuceviMap, ProgramMjerenja> pmT = ipkmT.join(IzvorProgramKljuceviMap_.programMjerenja);
//        Join<ProgramMjerenja, Postaja> pT = pmT.join(ProgramMjerenja_.postajaId);
        Expression<IzvorPodataka> izvorE = pmT.get(ProgramMjerenja_.izvorPodatakaId);
        Expression<String> ukljucE = ipkmT.get(IzvorProgramKljuceviMap_.uKljuc);
        Expression<Postaja> postajaE = pmT.get(ProgramMjerenja_.postajaId);
        Expression<Date> zadnjiE = pmT.get(ProgramMjerenja_.zavrsetakMjerenja);

        cq.where(
                cb.and(
                        cb.isNotNull(ukljucE),
                        cb.equal(postajaE, p),
                        cb.equal(izvorE, izvor)
                )
        );
        cq.select(pmT);
        return em.createQuery(cq).getResultList();
    }

    public void spremi(Collection<ZeroSpan> podaci) {
        for (ZeroSpan ps : podaci) {
            try {
                spremi(ps);
            } catch (Exception ex) {
                log.log(Level.SEVERE, "", ex);
            }
        }
        em.flush();
    }

    public void spremi(ZeroSpan zs) {
        create(zs);
    }

    // TODO prebaciti u named query
    public Date getVrijemeZadnjeg(IzvorPodataka izvor, Postaja postaja, String datoteka) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Date> cq = cb.createQuery(Date.class);
        Root<ZeroSpan> zsT = cq.from(ZeroSpan.class);
        Join<ZeroSpan, ProgramMjerenja> programT = zsT.join(ZeroSpan_.programMjerenjaId);
        Predicate datotekaP = cb.equal(programT.join(ProgramMjerenja_.izvorProgramKljuceviMap).get(IzvorProgramKljuceviMap_.nKljuc), datoteka);
//        Join<ZeroSpan, Uredjaj>  uredjajT= zsT.join(ZeroSpan_.uredjajId);
//        Join<Uredjaj, ProgramUredjajLink> uredjajProgramT = uredjajT.join(Uredjaj_.programUredjajLinkCollection);
//        Join<ProgramUredjajLink, ProgramMjerenja> programT = uredjajProgramT.join(ProgramUredjajLink_.programMjerenjaId);
//        Join<ProgramMjerenja, IzvorPodataka> izvorT = programT.join(ProgramMjerenja_.izvorPodatakaId);
//        Join<ProgramMjerenja, Postaja> postajaT = programT.join(ProgramMjerenja_.postajaId);
        Expression<Date> vrijemeE = zsT.get(ZeroSpan_.vrijeme);
        Predicate postajaP = cb.equal(programT.get(ProgramMjerenja_.postajaId), postaja);
        Predicate izvorP = cb.equal(programT.get(ProgramMjerenja_.izvorPodatakaId), izvor);
        cq.select(cb.greatest(vrijemeE)).where(cb.and(postajaP, izvorP));
        List<Date> rl = em.createQuery(cq).getResultList();
        if (rl == null || rl.isEmpty() || rl.get(0) == null) {
            return new Date(0L);
        } else {
            return rl.get(0);
        }
    }

    // TODO prebaciti u named query
    public List<ZeroSpan> getZeroSpanOd(ProgramMjerenja programMjerenja, Date pocetak) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ZeroSpan> cq = cb.createQuery(ZeroSpan.class);
        Root<ZeroSpan> zsT = cq.from(ZeroSpan.class);

        Expression<Date> vrijemeT = zsT.get(ZeroSpan_.vrijeme);
        Expression<ProgramMjerenja> programT = zsT.get(ZeroSpan_.programMjerenjaId);
        cq.where(
                cb.and(
                        cb.greaterThan(vrijemeT, pocetak),
                        cb.equal(programT, programMjerenja)
                )
        );
        cq.select(zsT).orderBy(cb.asc(vrijemeT));
        return em.createQuery(cq).getResultList();
    }

    public boolean postoji(ZeroSpan ps) {
         return !em.createNamedQuery("ZeroSpan.findByVrijemeProgramVrsta", ZeroSpan.class)
                 .setParameter("vrijeme", ps.getVrijeme())
                 .setParameter("programMjerenja", ps.getProgramMjerenjaId())
                 .setParameter("vrsta", ps.getVrsta())
                .setMaxResults(1).getResultList().isEmpty();
    }

}
