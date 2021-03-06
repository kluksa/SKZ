/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.webservis.omotnica;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author kraljevic
 */
public class CsvOmotnica implements Serializable {

    private String izvor;

    private String postaja;

    private String[] headeri;

    private List<String[]> linije;

    private String datoteka;

    private String vrsta;

    public String getVrsta() {
        return vrsta;
    }

    public void setVrsta(String vrsta) {
        this.vrsta = vrsta;
    }

    public String getDatoteka() {
        return datoteka;
    }

    public void setDatoteka(String datoteka) {
        this.datoteka = datoteka;
    }

    public String getIzvor() {
        return izvor;
    }

    public void setIzvor(String izvor) {
        this.izvor = izvor;
    }

    public List<String[]> getLinije() {
        return linije;
    }

    public void setLinije(List<String[]> linije) {
        this.linije = linije;
    }

    public String[] getHeaderi() {
        return headeri;
    }

    public void setHeaderi(String[] headeri) {
        this.headeri = headeri;
    }

    public String getPostaja() {
        return postaja;
    }

    public void setPostaja(String postaja) {
        this.postaja = postaja;
    }
}
