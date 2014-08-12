/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dhz.skz.aqdb.facades;

import dhz.skz.aqdb.entity.PrimateljProgramKljuceviMap;
import dhz.skz.aqdb.entity.PrimateljiPodataka;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import java.util.Collection;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author kraljevic
 */
@Local
public interface PrimateljProgramKljuceviMapFacadeLocal {

    void create(PrimateljProgramKljuceviMap primateljProgramKljuceviMap);

    void edit(PrimateljProgramKljuceviMap primateljProgramKljuceviMap);

    void remove(PrimateljProgramKljuceviMap primateljProgramKljuceviMap);

    PrimateljProgramKljuceviMap find(Object id);

    List<PrimateljProgramKljuceviMap> findAll();

    List<PrimateljProgramKljuceviMap> findRange(int[] range);

    int count();

    public PrimateljProgramKljuceviMap find(final PrimateljiPodataka primatelj, final ProgramMjerenja program);
    public Collection<PrimateljProgramKljuceviMap> find(final PrimateljiPodataka primatelj);
    
}
