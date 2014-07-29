/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dhz.skz.diseminacija;

import dhz.skz.aqdb.entity.PrimateljiPodataka;
import java.util.Date;

/**
 *
 * @author kraljevic
 */
public interface DiseminatorPodataka {
    public void salji(PrimateljiPodataka primatelj);
    public void nadoknadi(PrimateljiPodataka primatelj, Date pocetak, Date kraj);
}
