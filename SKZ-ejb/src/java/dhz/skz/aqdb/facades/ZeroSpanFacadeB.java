/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
public class ZeroSpanFacadeB extends AbstractFacade<ZeroSpan> implements ZeroSpanFacade, ZeroSpanFacadeRemote {

    private static final Logger log = Logger.getLogger(ZeroSpanFacadeB.class.getName());

    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ZeroSpanFacadeB() {
        super(ZeroSpan.class);
    }

    @Override
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
        return em.createQuery(cq).getSingleResult();
    }

    @Override
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

//    public List<ZeroSpan> getZeroSpanNakonVremena(ProgramMjerenja programMjerenja, Date vrijeme) {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<ZeroSpan> cq = cb.createQuery(ZeroSpan.class);
//        Root<ZeroSpan> zsT = cq.from(ZeroSpan.class);
//        
//        Join<ZeroSpan, Uredjaj>  uredjajT= zsT.join(ZeroSpan_.uredjajId);
//        Join<Uredjaj, ProgramUredjajLink> uredjajProgramT = uredjajT.join(Uredjaj_.programUredjajLinkCollection);
//        Join<ProgramUredjajLink, ProgramMjerenja> programT = uredjajProgramT.join(ProgramUredjajLink_.programMjerenjaId);
////        Join<ZeroSpan, Komponenta> komponentaT = zsT.join(ZeroSpan_.komponentaId);
//        Expression<Date> vrijemeT = zsT.get(ZeroSpan_.vrijeme);
//        
//        cq.where(
//                cb.and(
//                        cb.greaterThan(vrijemeT, vrijeme),
//                        cb.equal(programT, programMjerenja)
//                )
//        );
//        cq.select(zsT);
//        Date sada = new Date();
//        TypedQuery<ZeroSpan> q = em.createQuery(cq);
//        
//        log.log(Level.INFO, "ZERO SPAN PROGRAM: {1}, SADA: {0}, POCETAK: {2}, DULJINA: {3}", new Object[]{sada.getTime(), programMjerenja.getId(), vrijeme.getTime(), q.getResultList().size()});
//        return q.getResultList();
//    }
//    public List<ZeroSpan> getZeroSpan(ProgramMjerenja programMjerenja, Date pocetak, Date kraj) {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<ZeroSpan> cq = cb.createQuery(ZeroSpan.class);
//        Root<ZeroSpan> zsT = cq.from(ZeroSpan.class);
//        
//        Join<ZeroSpan, Uredjaj>  uredjajT= zsT.join(ZeroSpan_.uredjajId);
//        Join<Uredjaj, ProgramUredjajLink> uredjajProgramT = uredjajT.join(Uredjaj_.programUredjajLinkCollection);
//        Join<ProgramUredjajLink, ProgramMjerenja> programT = uredjajProgramT.join(ProgramUredjajLink_.programMjerenjaId);
////        Join<ZeroSpan, Komponenta> komponentaT = zsT.join(ZeroSpan_.komponentaId);
//        Expression<Date> vrijemeT = zsT.get(ZeroSpan_.vrijeme);
//        
//        cq.where(
//                cb.and(
//                        cb.greaterThan(vrijemeT, pocetak),
//                        cb.lessThanOrEqualTo(vrijemeT, kraj),
//                        cb.equal(programT, programMjerenja)
//                )
//        );
//        cq.select(zsT);
//        log.log(Level.INFO, "ZERO SPAN PROGRAM: {1}, SADA: {3} POCETAK: {0}, KRAJ: {2}", new Object[]{pocetak.getTime(), programMjerenja.getId(), kraj.getTime(), new Date().getTime()});
//        return em.createQuery(cq).getResultList();
//    }
    @Override
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

    @Override
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

    @Override
    public void spremi(Collection<ZeroSpan> podaci) {
        for (ZeroSpan ps : podaci) {
            try {
                log.log(Level.INFO, "PODATAK: {0}: {1} : {2} : {3}", new Object[]{ps.getProgramMjerenjaId().getKomponentaId().getFormula(),
                    ps.getVrijeme(), ps.getVrijednost(), ps.getVrsta()});
                create(ps);
            } catch (Exception ex) {
                log.log(Level.SEVERE, "", ex);
            }
        }
        em.flush();
    }

    @Override
    public void spremi(ZeroSpan zs) {
        create(zs);
    }

    @Override
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

    @Override
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

}
