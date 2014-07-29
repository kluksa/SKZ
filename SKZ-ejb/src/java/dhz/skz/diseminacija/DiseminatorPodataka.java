/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dhz.skz.diseminacija;

import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.PrimateljiPodataka;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import java.util.Map;

/**
 *
 * @author kraljevic
 */
public interface DiseminatorPodataka {
    public void salji(PrimateljiPodataka primatelj);
    public void salji(PrimateljiPodataka primatelj, Map<ProgramMjerenja, Podatak> zadnjiPodatak);
}
