/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dhz.skz.wsbackend;

import dhz.skz.sirovi.exceptions.CsvPrihvatException;
import dhz.skz.webservis.omotnica.CsvOmotnica;
import javax.ejb.Remote;

/**
 *
 * @author kraljevic
 */
@Remote
public interface PrihvatSirovihPodatakaRemote {

    void prihvatiOmotnicu(final CsvOmotnica  omotnica);

    Long getUnixTimeZadnjeg(String izvorS, String postajaS, String datotekaS);

    String test(String inStr) throws CsvPrihvatException;
    
}
