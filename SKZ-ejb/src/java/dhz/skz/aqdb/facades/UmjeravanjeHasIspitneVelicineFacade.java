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

import dhz.skz.aqdb.entity.IspitneVelicine;
import dhz.skz.aqdb.entity.IspitneVelicine_;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ProgramMjerenja_;
import dhz.skz.aqdb.entity.ProgramUredjajLink;
import dhz.skz.aqdb.entity.ProgramUredjajLink_;
import dhz.skz.aqdb.entity.Umjeravanje;
import dhz.skz.aqdb.entity.UmjeravanjeHasIspitneVelicine;
import dhz.skz.aqdb.entity.UmjeravanjeHasIspitneVelicine_;
import dhz.skz.aqdb.entity.Umjeravanje_;
import dhz.skz.aqdb.entity.Uredjaj;
import dhz.skz.aqdb.entity.Uredjaj_;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author kraljevic
 */
@Stateless
public class UmjeravanjeHasIspitneVelicineFacade extends AbstractFacade<UmjeravanjeHasIspitneVelicine> {
    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UmjeravanjeHasIspitneVelicineFacade() {
        super(UmjeravanjeHasIspitneVelicine.class);
    }
    
    public Collection<UmjeravanjeHasIspitneVelicine> find(ProgramMjerenja pm, IspitneVelicine iv) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UmjeravanjeHasIspitneVelicine> cq = cb.createQuery(UmjeravanjeHasIspitneVelicine.class);
        
        Root<UmjeravanjeHasIspitneVelicine> from = cq.from(UmjeravanjeHasIspitneVelicine.class);
        Join<UmjeravanjeHasIspitneVelicine, Umjeravanje> umjJ =  from.join(UmjeravanjeHasIspitneVelicine_.umjeravanje);
        Join<Umjeravanje, Uredjaj> uredjajJ = umjJ.join(Umjeravanje_.uredjajId);
        Join<Uredjaj, ProgramUredjajLink> pulJ = uredjajJ.join(Uredjaj_.programUredjajLinkCollection);
        Join<ProgramUredjajLink, ProgramMjerenja> pmJ = pulJ.join(ProgramUredjajLink_.programMjerenjaId);
        Join<UmjeravanjeHasIspitneVelicine, IspitneVelicine> ispJ = from.join(UmjeravanjeHasIspitneVelicine_.ispitneVelicine);
        
        Predicate programP = cb.equal(pmJ.get(ProgramMjerenja_.id), pm);
        Predicate vrijemeOdP = cb.lessThanOrEqualTo(pulJ.get(ProgramUredjajLink_.vrijemePostavljanja), umjJ.get(Umjeravanje_.datum));
        Predicate vrijemeDoP = cb.greaterThanOrEqualTo(pulJ.get(ProgramUredjajLink_.vrijemeUklanjanja), umjJ.get(Umjeravanje_.datum));
        Predicate uklanjanjeNulP = cb.isNull(pulJ.get(ProgramUredjajLink_.vrijemeUklanjanja));
        Predicate ispitneVelicineP = cb.equal(ispJ.get(IspitneVelicine_.id), iv);
        
        cq.select(from).where(cb.and(programP, ispitneVelicineP, cb.or(uklanjanjeNulP, vrijemeDoP)));
        return em.createQuery(cq).getResultList();
        /*select * from `aqdb_likz`.`umjeravanje_has_ispitne_velicine` uiv
join `umjeravanje` umj on umj.id = uiv.umjeravanje_id
join `uredjaj` u on u.id = umj.uredjaj_id
join `program_uredjaj_link` pul on u.id = pul.uredjaj_id
join `program_mjerenja` pm on pm.id = pul.program_mjerenja_id
join `analiticke_metode` am on am.id = pm.metoda_id
join `komponenta` k on k.id = pm.komponenta_id
join `ispitne_velicine` iv on iv.id = uiv.ispitne_velicine_id

where pm.id = 45
and umj.datum >= pul.vrijeme_postavljanja and ( pul.vrijeme_uklanjanja is null or umj.datum <= pul.vrijeme_uklanjanja)
and pm.komponenta_id = uiv.komponenta_id
and iv.oznaka = 'Srz'/*
        
        */
    }
}
