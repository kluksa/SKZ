/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.web.converters;

import dhz.skz.aqdb.facades.KategorijeGranicaFacade;
import dhz.skz.aqdb.entity.KategorijeGranica;
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
@Named
@RequestScoped
public class KategorijeGranicaKonverter implements Converter {
    @EJB
    private KategorijeGranicaFacade fasada;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return fasada.findByOpis(value);    
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }

        if (value instanceof KategorijeGranica) {
            return ((KategorijeGranica) value).getOpis();
        } else {
            throw new ConverterException(new FacesMessage(String.format("%s is not a valid KategorijeGranica", value)));
        }
    }
}
