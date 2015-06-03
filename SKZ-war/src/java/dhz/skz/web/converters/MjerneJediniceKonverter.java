/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.web.converters;

import dhz.skz.aqdb.facades.MjerneJediniceFacade;
import dhz.skz.aqdb.entity.MjerneJedinice;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

/**
 *
 * @author kraljevic
 */
@Named(value = "mjerneJediniceKonverter")
@RequestScoped
public class MjerneJediniceKonverter implements Converter {

    @EJB
    private MjerneJediniceFacade jedinicaFacade;

    /**
     * Creates a new instance of PostajaKonverter
     */
    public MjerneJediniceKonverter() {
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String oznaka) {
        if (oznaka == null || oznaka.isEmpty()) {
            return null;
        }
        return jedinicaFacade.findByOznaka(oznaka);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }

        if (value instanceof MjerneJedinice) {
            return ((MjerneJedinice) value).getOznaka();
//            return String.valueOf(((Postaja) value).getId());
        } else {
            return "";
//            throw new ConverterException(new FacesMessage(String.format("%s is not a valid User", value)));
        }
    }

}
