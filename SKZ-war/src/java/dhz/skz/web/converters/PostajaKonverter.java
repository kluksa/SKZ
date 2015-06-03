/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dhz.skz.web.converters;

import dhz.skz.aqdb.facades.PostajaFacade;
import dhz.skz.aqdb.entity.Postaja;
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
@Named(value = "postajaKonverter")
@RequestScoped
public class PostajaKonverter implements Converter {
    @EJB
    private PostajaFacade postajaFacade;

    /**
     * Creates a new instance of PostajaKonverter
     */
    public PostajaKonverter() {
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
    if (value == null || value.isEmpty()) {
            return null;
        }
        try {
//            return postajaFacade.find(value);
            return postajaFacade.findByNaziv(value);
        } catch (NumberFormatException e) {
            throw new ConverterException(new FacesMessage(String.format("%s is not a valid User ID", value)), e);
        }    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }

        if (value instanceof Postaja) {
            return String.valueOf(((Postaja) value).getNazivPostaje());
//            return String.valueOf(((Postaja) value).getId());
        } else {
            return "";
//            throw new ConverterException(new FacesMessage(String.format("%s is not a valid User", value)));
        }
    }
    
}
