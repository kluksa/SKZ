/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.web.converters;

import dhz.skz.aqdb.facades.AgregacijeFacade;
import dhz.skz.aqdb.entity.Agregacije;
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
@Named
@RequestScoped
public class AgregacijeKonverter implements Converter {
    @EJB
    private AgregacijeFacade agregacijeFacade;

    /**
     * Creates a new instance of PostajaKonverter
     */
    public AgregacijeKonverter() {
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String oznaka) {
        if (oznaka == null || oznaka.isEmpty()) {
            return null;
        }
        return agregacijeFacade.findByOznaka(oznaka);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }

        if (value instanceof Agregacije) {
            return ((Agregacije) value).getOznaka();
//            return String.valueOf(((Postaja) value).getId());
        } else {
            return "";
//            throw new ConverterException(new FacesMessage(String.format("%s is not a valid User", value)));
        }
    }

    
}
