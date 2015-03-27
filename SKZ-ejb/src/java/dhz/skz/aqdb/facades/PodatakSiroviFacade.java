/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.facades;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.IzvorProgramKljuceviMap_;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.PodatakSirovi_;
import dhz.skz.aqdb.entity.Podatak_;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ProgramMjerenja_;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
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
public class PodatakSiroviFacade extends AbstractFacade<PodatakSirovi> implements PodatakSiroviFacadeLocal, PodatakSiroviFacadeRemote  {
    @EJB
    private PostajaFacade postajaFacade;
    @EJB
    private IzvorPodatakaFacade izvorPodatakaFacade;
    

    private static final Logger log = Logger.getLogger(PodatakSiroviFacade.class.getName());

    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;
    

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PodatakSiroviFacade() {
        super(PodatakSirovi.class);
    }

    @Override
    public void spremi(Collection<PodatakSirovi> podaci) {
        for (PodatakSirovi ps : podaci) {
            if (!postoji(ps)) {
                em.persist(ps);
            }
        }
        em.flush();
    }

//    public void spremi(PodatakSirovi podatak) {
//        if (!postoji(podatak)) {
//            create(podatak);
//        } else {
//            log.log(Level.INFO, "Podatak: {0}: {1} vec postoji", new Object[]{
//                podatak.getProgramMjerenjaId().getId(), podatak.getVrijeme()
//            });
//        }
//    }
    @Override
    public boolean postoji(PodatakSirovi podatak) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PodatakSirovi> cq = cb.createQuery(PodatakSirovi.class);
        Root<PodatakSirovi> from = cq.from(PodatakSirovi.class);

        Expression<Date> vrijemeE = from.get(PodatakSirovi_.vrijeme);
        Expression<ProgramMjerenja> programE = from.get(PodatakSirovi_.programMjerenjaId);

        cq.where(cb.and(cb.equal(vrijemeE, podatak.getVrijeme()),
                cb.equal(programE, podatak.getProgramMjerenjaId())));
        cq.select(from);
        List<PodatakSirovi> resultList = em.createQuery(cq).getResultList();
        return (resultList != null) && (!resultList.isEmpty());
    }

    @Override
    public Date getVrijemeZadnjegOptimizirano(IzvorPodataka izvor, Postaja postaja, String datoteka) {

        Date zadnjiSatni = getVrijemeZadnjegS(izvor,postaja, datoteka);
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Date> cq = cb.createQuery(Date.class);
        Root<PodatakSirovi> from = cq.from(PodatakSirovi.class);

        Join<PodatakSirovi, ProgramMjerenja> programJ = from.join(PodatakSirovi_.programMjerenjaId);
        
        Predicate datotekaP = cb.equal(programJ.join(ProgramMjerenja_.izvorProgramKljuceviMap).get(IzvorProgramKljuceviMap_.nKljuc), datoteka);
        Predicate postajaP = cb.equal(programJ.join(ProgramMjerenja_.postajaId), postaja);
        Predicate izvorP = cb.equal(programJ.join(ProgramMjerenja_.izvorPodatakaId), izvor);
        Predicate zadnjiSatniP = cb.greaterThanOrEqualTo(from.get(PodatakSirovi_.vrijeme), zadnjiSatni);

        Expression<Date> vrijeme = from.get(PodatakSirovi_.vrijeme);


        CriteriaQuery<Date> select = cq.select(cb.greatest(vrijeme))
                                        .where(cb.and(izvorP, postajaP, datotekaP, zadnjiSatniP));
        List<Date> rl = em.createQuery(cq).getResultList();
        if (rl == null || rl.isEmpty() || rl.get(0) == null) {
            return new Date(0L);
        }

        return rl.get(0);
    }

    @Override
    public Date getVrijemeZadnjegS(IzvorPodataka izvor, Postaja postaja, String datoteka) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Date> cq = cb.createQuery(Date.class);
        Root<Podatak> from = cq.from(Podatak.class);

        Join<Podatak, ProgramMjerenja> programJ = from.join(Podatak_.programMjerenjaId);
        
        Predicate datotekaP = cb.equal(programJ.join(ProgramMjerenja_.izvorProgramKljuceviMap).get(IzvorProgramKljuceviMap_.nKljuc), datoteka);
        Predicate postajaP = cb.equal(programJ.join(ProgramMjerenja_.postajaId), postaja);
        Predicate izvorP = cb.equal(programJ.join(ProgramMjerenja_.izvorPodatakaId), izvor);

        Expression<Date> vrijeme = from.get(Podatak_.vrijeme);


        CriteriaQuery<Date> select = cq.select(cb.greatest(vrijeme))
                                        .where(cb.and(izvorP, postajaP, datotekaP));
        List<Date> rl = em.createQuery(cq).setMaxResults(1).getResultList();
        if (rl == null || rl.isEmpty() || rl.get(0) == null) {
            return new Date(0L);
        }

        return rl.get(0);
    }

    
    @Override
    public Collection<PodatakSirovi> getPodaci(ProgramMjerenja pm, Date pocetak, Date kraj) {
        return getPodaci(pm, pocetak, kraj, false, true); 
    }

    @Override
    public Collection<PodatakSirovi> getPodaci(ProgramMjerenja pm, Date pocetak, Date kraj, boolean p, boolean k) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PodatakSirovi> cq = cb.createQuery(PodatakSirovi.class);
        Root<PodatakSirovi> from = cq.from(PodatakSirovi.class);
        Expression<Date> vrijemeE = from.get(PodatakSirovi_.vrijeme);
        Expression<ProgramMjerenja> programE = from.get(PodatakSirovi_.programMjerenjaId);

        cq.where(
                cb.and(
                        cb.equal(programE, pm),
                        (p ? cb.greaterThanOrEqualTo(vrijemeE, pocetak) : cb.greaterThan(vrijemeE, pocetak)),
                        (k ? cb.lessThanOrEqualTo(vrijemeE, kraj) : cb.lessThan(vrijemeE, kraj))
                )
        );

        cq.select(from).orderBy(cb.asc(vrijemeE));
        return em.createQuery(cq).getResultList();
    }

    
    @Override
    public Collection<PodatakSirovi> getPodatkeZaSat(ProgramMjerenja pm, Date kraj) {

//        Date kraj = sat.getTime();
//        sat.add(Calendar.HOUR, -1);
        Date pocetak = new Date(kraj.getTime() - 3600 * 1000);
        return getPodaci(pm, pocetak, kraj);
    }

    @Override
    public Date getZadnjiPodatak(ProgramMjerenja program) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Date> cq = cb.createQuery(Date.class);
        Root<PodatakSirovi> from = cq.from(PodatakSirovi.class);

        Expression<Date> vrijeme = from.get(PodatakSirovi_.vrijeme);
        Expression<ProgramMjerenja> programE = from.get(PodatakSirovi_.programMjerenjaId);

        cq.where(
                cb.equal(programE, program)
        );
        CriteriaQuery<Date> select = cq.select(cb.greatest(vrijeme));
        List<Date> rl = em.createQuery(cq).getResultList();
        if (rl == null || rl.isEmpty() || rl.get(0) == null) {
            return program.getPocetakMjerenja();
        }
        return rl.get(0);
    }

    @Override
    public void getVrijemeZadnjegTest() {
        IzvorPodataka izvor = izvorPodatakaFacade.find(5);
        Postaja postaja = postajaFacade.find(32);
        String s = "A";
        Date pocetak = new Date();
        Date sada;
        getVrijemeZadnjeg(izvor, postaja, s);
        sada = new Date();
        log.log(Level.INFO, "1. vrijeme {0}s", (sada.getTime()-pocetak.getTime())/1000.);
        pocetak = sada;
        getVrijemeZadnjegOptimizirano(izvor,postaja,s);
        sada = new Date();
        log.log(Level.INFO, "2. vrijeme {0}s", (sada.getTime()-pocetak.getTime())/1000.);

    }
    
    
    @Override
    public Date getVrijemeZadnjeg(IzvorPodataka izvor, Postaja postaja, String datoteka) {

        StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("get_vrijeme_zadnjeg_sirovog_za_postaju_izvor_nkljuc");
        storedProcedure.registerStoredProcedureParameter("_postaja", Integer.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("_izvor", Integer.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("_nkljuc", String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("_vrijeme", java.sql.Timestamp.class, ParameterMode.OUT);

        log.log(Level.INFO, "{0}, {1}, {2}", new Object[]{postaja.getId(), izvor.getId(), datoteka});
        storedProcedure.setParameter("_postaja", postaja.getId());
        storedProcedure.setParameter("_izvor", izvor.getId());
        storedProcedure.setParameter("_nkljuc", datoteka);
        storedProcedure.execute();
        java.sql.Timestamp vrijeme = (java.sql.Timestamp) storedProcedure.getOutputParameterValue("_vrijeme");

        return ( vrijeme == null ) ? null : new Date(vrijeme.getTime());
    }

}
