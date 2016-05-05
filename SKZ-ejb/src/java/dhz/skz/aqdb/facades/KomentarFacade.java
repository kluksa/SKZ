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

import dhz.skz.aqdb.entity.Komentar;
import dhz.skz.aqdb.entity.Komponenta;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author kraljevic
 */
@Stateless
public class KomentarFacade extends AbstractFacade<Komentar> {
    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KomentarFacade() {
        super(Komentar.class);
    }

    public List<Komentar> find(ProgramMjerenja program, Date pocetak, Date kraj) {
        TypedQuery<Komentar> query = em.createNamedQuery("Komentar.findByProgramPocetakKraj", Komentar.class);
        query.setParameter("programMjerenjaId", program);
        query.setParameter("pocetak", pocetak);
        query.setParameter("kraj", kraj);
        return query.getResultList();
    }
    
    public List<Komentar> find(ProgramMjerenja program) {
        TypedQuery<Komentar> query = em.createNamedQuery("Komentar.findByProgram", Komentar.class);
        query.setParameter("programMjerenjaId", program);
        return query.getResultList();
    }

    
}
