/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rs.dto;

/**
 *
 * @author kraljevic
 */
public class UlazniSiroviDTO {
    
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private Boolean valjan;

    /**
     * Get the value of valjan
     *
     * @return the value of valjan
     */
    public Boolean isValjan() {
        return valjan;
    }

    /**
     * Set the value of valjan
     *
     * @param valjan new value of valjan
     */
    public void setValjan(Boolean valjan) {
        this.valjan = valjan;
    }

    
}
