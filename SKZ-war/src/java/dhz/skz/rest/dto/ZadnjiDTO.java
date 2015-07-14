/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rest.dto;

import java.util.Date;

/**
 *
 * @author kraljevic
 */
public class ZadnjiDTO {
    private final Integer id;
    private final Date zadnjiSatni;
    private final Date zadnjiSirovi;

    public ZadnjiDTO(Integer id, Date zadnjiSatni, Date zadnjiSirovi) {
        this.id = id;
        this.zadnjiSatni = zadnjiSatni;
        this.zadnjiSirovi = zadnjiSirovi;
    }
    
}
