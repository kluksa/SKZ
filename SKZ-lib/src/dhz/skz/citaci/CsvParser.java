/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dhz.skz.citaci;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.webservis.omotnica.CsvOmotnica;
import java.util.Date;

/**
 *
 * @author kraljevic
 */
public interface CsvParser {
    void obradi(CsvOmotnica omotnica);
    public Date getVrijemeZadnjegPodatka(IzvorPodataka izvor, Postaja postaja, String datoteka);
}
