/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.facades;

import dhz.skz.aqdb.entity.ProgramMjerenja;
import javax.ejb.Remote;

/**
 *
 * @author kraljevic
 */
@Remote
public interface ProgramMjerenjaFacadeRemote {
     public ProgramMjerenja find(Integer id);
}
