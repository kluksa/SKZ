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

import dhz.skz.aqdb.entity.Obavijesti;
import dhz.skz.aqdb.entity.PrekoracenjaUpozorenjaResult;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author kraljevic
 */
@Stateless
public class PrekoracenjaUpozorenjaResultFacade  {
    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }
    
    public List<PrekoracenjaUpozorenjaResult> findAll(Obavijesti o, OffsetDateTime vrijeme, Integer broj_sati){
        
        StoredProcedureQuery q = em.createNamedStoredProcedureQuery("PrekoracenjaUpozorenja");
        q.setParameter("obavijest_id", o.getId());
        q.setParameter("vrijeme", new Date(1000*vrijeme.toEpochSecond()));
        q.setParameter("broj_sati", broj_sati);
        q.execute();
        List resultList = q.getResultList();
        return resultList;
    }
    
}
