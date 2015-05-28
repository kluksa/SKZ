/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dhz.skz.web.converters;

import dhz.skz.aqdb.facades.OdgovornoTijeloFacade;
import dhz.skz.aqdb.entity.OdgovornoTijelo;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Named;

/**
 *
 * @author kraljevic
 */
@Named(value = "odgovornoTijeloKonverter")
@RequestScoped
public class OdgovornoTijeloKonverter implements Converter {
    @EJB
    private OdgovornoTijeloFacade tijeloFacade;

    /**
     * Creates a new instance of PostajaKonverter
     */
    public OdgovornoTijeloKonverter() {
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
    if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return tijeloFacade.findByNaziv(value);
        } catch (NumberFormatException e) {
            throw new ConverterException(new FacesMessage(String.format("%s is not a valid User ID", value)), e);
        }    
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }

        if (value instanceof OdgovornoTijelo) {
            return String.valueOf(((OdgovornoTijelo) value).getNaziv());
//            return String.valueOf(((Postaja) value).getId());
        } else {
            return "";
//            throw new ConverterException(new FacesMessage(String.format("%s is not a valid User", value)));
        }
    }
    
}
