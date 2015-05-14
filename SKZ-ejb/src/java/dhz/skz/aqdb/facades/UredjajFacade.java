/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.facades;

import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.Uredjaj;
import java.util.Date;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author kraljevic
 */
@Stateless
public class UredjajFacade extends AbstractFacade<Uredjaj> {

    private static final Logger log = Logger.getLogger(UredjajFacade.class.getName());

    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UredjajFacade() {
        super(Uredjaj.class);
    }

    @TransactionAttribute(REQUIRES_NEW)
    public void premjesti(final Uredjaj uredjaj, final Postaja novaPostaja, final short usporednoMjerenje, final Date vrijeme){
        // update postaja_uredjaj_link sa datumom uklanjanja
        if (novaPostaja.getId() != 0) {
            // provjeri postoji li program sa komponentama koje ovaj uredjaj mjeri
            // ako ne baci iznimku
        }
        // insert postaja_uredjaj_link sa datumom postavljanja
        
    }
}
