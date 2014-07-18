/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dhz.skz.citaci.weblogger.validatori;

/**
 *
 * @author kraljevic
 */
public abstract class ValidatorImpl implements Validator {
    protected Float tempMin = 15.f;
    protected Float tempMax = 25.f;
    protected Integer brojMjerenjaUSatu = 60;

    public Float getTempMin() {
        return tempMin;
    }

    public void setTempMin(Float tempMin) {
        this.tempMin = tempMin;
    }

    public Float getTempMax() {
        return tempMax;
    }

    public void setTempMax(Float tempMax) {
        this.tempMax = tempMax;
    }

    @Override
    public Integer getBrojMjerenjaUSatu() {
        return brojMjerenjaUSatu;
    }

    @Override
    public void setBrojMjerenjaUSatu(Integer brojMjerenjaUSatu) {
        this.brojMjerenjaUSatu = brojMjerenjaUSatu;
    }

}
