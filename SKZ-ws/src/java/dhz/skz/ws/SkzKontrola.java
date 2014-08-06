/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dhz.skz.ws;

import dhz.skz.GlavnaFasadaRemote;
import dhz.skz.aqdb.entity.PrimateljiPodataka;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import java.util.Collection;
import java.util.Date;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author kraljevic
 */
@WebService(serviceName = "SkzKontrola")
@Stateless()
public class SkzKontrola {
    @EJB
    private GlavnaFasadaRemote ejbRef;// Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Web Service Operation")

    @WebMethod(operationName = "pokreniDiseminaciju")
    @Oneway
    public void pokreniDiseminaciju() {
        ejbRef.pokreniDiseminaciju();
    }

    @WebMethod(operationName = "pokreniCitanje")
    @Oneway
    public void pokreniCitanje() {
        ejbRef.pokreniCitanje();
    }

    @WebMethod(operationName = "nadoknadiPodatke")
    @Oneway
    public void nadoknadiPodatke(@WebParam(name = "primatelji") PrimateljiPodataka primatelji, @WebParam(name = "programi") Collection<ProgramMjerenja> programi, @WebParam(name = "pocetak") Date pocetak, @WebParam(name = "kraj") Date kraj) {
        ejbRef.nadoknadiPodatke(primatelji, programi, pocetak, kraj);
    }
    
}
