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
package dhz.skz;

import dhz.skz.aqdb.entity.NivoValidacije;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.facades.PodatakFacade;
import dhz.skz.aqdb.facades.PodatakSiroviFacade;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacade;
import dhz.skz.config.Config;
import dhz.skz.util.MinutniUSatne;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.inject.Inject;

/**
 *
 * @author kraljevic
 */
@Stateless
public class DnevnaAgrebacijaBean extends Scheduler implements DnevnaAgrebacijaBeanRemote {
    private static final Logger log = Logger.getLogger(DnevnaAgrebacijaBean.class.getName());

    @EJB
    private PodatakFacade podatakFacade;
    @EJB
    private PodatakSiroviFacade podatakSiroviFacade;
    @EJB
    private ProgramMjerenjaFacade programMjerenjaFacade;
    @EJB
    private MinutniUSatne siroviUSatneBean;

    

    
    private boolean aktivan = false;

    @Inject
    @Config(vrijednost="23")
    private Integer sat;
    
    public DnevnaAgrebacijaBean() {
        super("DnevnaAgrebacijaTimer");
    }
    
    @PostConstruct
    public void init(){
        schedule(sat);
    }

    @Override
    @Timeout
    public void pokreni() {
        log.log(Level.INFO, "Pokrecem DnevnaAgrebacijaBean");
        if (!aktivan) {
            aktivan = true;
            for (ProgramMjerenja pm : programMjerenjaFacade.findAll()){
                siroviUSatneBean.spremiSatneIzSirovih(pm, new NivoValidacije(1));
            }
            aktivan = false;
            log.log(Level.INFO, "Kraj pokretanja DnevnaAgrebacijaBean");
        } else {
            log.log(Level.INFO, "Prethodna DnevnaAgrebacijaBean jos nije zavrsila");
        }
    }
}
