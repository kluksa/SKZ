/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger;

import dhz.skz.citaci.weblogger.exceptions.WlFileException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import javax.transaction.UserTransaction;

/**
 *
 * @author kraljevic
 */
interface WlFileParser {

    void parse(InputStream fileStream) throws WlFileException, IOException;

    void setZadnjiPodatak(Date zadnjiPodatak);

    boolean isDobarTermin();
    
    void setTerminDatoteke(Date terminDatoteke);
    
}
