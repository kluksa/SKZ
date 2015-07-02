/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rest.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@XmlRootElement
public class PodatakDTO {
    private Integer programMjerenjaId;
    private long vrijeme;
    private Double vrijednost;
    private Integer status;
    private Integer obuhvat;
    private Boolean valjan;

    public Integer getProgramMjerenjaId() {
        return programMjerenjaId;
    }

    public void setProgramMjerenjaId(Integer programMjerenjaId) {
        this.programMjerenjaId = programMjerenjaId;
    }

    public long getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(long vrijeme) {
        this.vrijeme = vrijeme;
    }

    public Double getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(Double vrijednost) {
        this.vrijednost = vrijednost;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getObuhvat() {
        return obuhvat;
    }

    public void setObuhvat(Integer obuhvat) {
        this.obuhvat = obuhvat;
    }

    public Boolean isValjan() {
        return valjan;
    }

    public void setValjan(boolean valjan) {
        this.valjan = valjan;
    }
}
