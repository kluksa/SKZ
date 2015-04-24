/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger;

import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.citaci.weblogger.exceptions.WlFileException;
import dhz.skz.citaci.weblogger.util.NizProcitanihWl;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author kraljevic
 */
interface WlFileParser {

    void parse(InputStream fileStream) throws WlFileException, IOException;

    void setZadnjiPodatak(Date zadnjiPodatak);

    void setNizKanala(Map<ProgramMjerenja, NizProcitanihWl> mapaMjernihNizova, Collection<ProgramMjerenja> aktivniProgram);
    
    boolean isDobarTermin();
    
    void setTerminDatoteke(Date terminDatoteke);
    
}
