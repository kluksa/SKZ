/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.PodatakSirovi;
import java.util.Map;
import java.util.concurrent.Future;
import javax.ejb.Asynchronous;

/**
 *
 * @author kraljevic
 */
public interface CitacIzvora {
//    public Future<Boolean> napraviSatne(IzvorPodataka izvor);
    public Boolean napraviSatne(IzvorPodataka izvor);
    public Map<String,String> opisiStatus(PodatakSirovi ps);
}