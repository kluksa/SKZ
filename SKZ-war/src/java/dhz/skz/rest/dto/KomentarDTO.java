/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rest.dto;

import dhz.skz.aqdb.entity.Komentar;

/**
 *
 * @author kraljevic
 */
public class KomentarDTO {
    private Integer id;
    private int programMjerenjaId;
    private long pocetak;
    private long kraj;
    private String tekst;

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    public KomentarDTO() {
    }

    public KomentarDTO(Komentar k) {
        this.id = k.getId();
        this.programMjerenjaId = k.getProgramMjerenjaId().getId();
        this.pocetak = k.getPocetak().getTime();
        this.kraj = k.getKraj().getTime();
        this.tekst = k.getTekst();
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getProgramMjerenjaId() {
        return programMjerenjaId;
    }

    public void setProgramMjerenjaId(int programMjerenjaId) {
        this.programMjerenjaId = programMjerenjaId;
    }

    public long getPocetak() {
        return pocetak;
    }

    public void setPocetak(long pocetak) {
        this.pocetak = pocetak;
    }

    public long getKraj() {
        return kraj;
    }

    public void setKraj(long kraj) {
        this.kraj = kraj;
    }
    
}
