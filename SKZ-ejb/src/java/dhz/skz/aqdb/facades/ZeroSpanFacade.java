/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.facades;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ZeroSpan;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author kraljevic
 */
@Local
public interface ZeroSpanFacade extends AbstractFacadeInterface<ZeroSpan> {

    Collection<ProgramMjerenja> getProgramNaPostajiZaIzvor(Postaja p, IzvorPodataka i, Date zadnji);

    List<ProgramMjerenja> getProgramSaZeroSpanomNaPostaji(IzvorPodataka izvor, Postaja p);

    Date getVrijemeZadnjeg(IzvorPodataka izvor, Postaja p);

    Date getVrijemeZadnjeg(IzvorPodataka izvor, Postaja postaja, String datoteka);

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
//    List<ZeroSpan> getZeroSpan(ProgramMjerenja programMjerenja, Date pocetak, Date kraj);

    List<ZeroSpan> getZeroSpanOd(ProgramMjerenja programMjerenja, Date pocetak);

    void spremi(Collection<ZeroSpan> podaci);

    void spremi(ZeroSpan podaci);
    
}
