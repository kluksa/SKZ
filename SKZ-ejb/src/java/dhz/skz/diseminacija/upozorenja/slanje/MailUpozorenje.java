/*
 * Copyright (C) 2016 kraljevic
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dhz.skz.diseminacija.upozorenja.slanje;

import dhz.skz.aqdb.entity.Komponenta;
import dhz.skz.aqdb.entity.Obavijesti;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.PrimateljiPodataka;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.util.eksportpodataka.CsvExportSiroki;
import dhz.skz.util.eksportpodataka.CsvExportUski;
import dhz.skz.util.eksportpodataka.PodatakEksport;
import dhz.skz.util.mailer.EmailSessionBean;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author kraljevic
 */
public class MailUpozorenje implements SlanjeUpozorenja{


    @Override
    public void saljiUpozorenje(Obavijesti ob, ProgramMjerenja pm, Collection<Podatak> podaci) {
        try {
            PrimateljiPodataka primatelj = ob.getPrimatelj();
            Komponenta komponentaId = pm.getKomponentaId();
            Postaja postajaId = pm.getPostajaId();
            PodatakEksport pe = new CsvExportUski();
            
            Writer wr = new StringWriter();
            pe.write(wr, podaci);
            
            XmlParser xmlp = new XmlParser();
            xmlp.parse(ob.getPredlozakTeksta());
            String bodyTemplate = xmlp.getBody();
            
            String body = obradiPredlozak(bodyTemplate, ob, pm);
            String subject = xmlp.getSubject();
            
            EmailSessionBean esb = new EmailSessionBean();
            URL url = new URL(primatelj.getUrl());
            
            
            esb.sendEmail(url, subject, body, wr.toString().getBytes("UTF-8"), "text/csv");
            
            
        } catch (IOException ex) {
            Logger.getLogger(MailUpozorenje.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XmlObavijestException ex) {
            Logger.getLogger(MailUpozorenje.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(MailUpozorenje.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(MailUpozorenje.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private String obradiPredlozak(String bodyTemplate, Obavijesti ob, ProgramMjerenja pm) {
        String opis = ob.getGranica().getKategorijeGranicaId().getOpis();
        String postaja = pm.getPostajaId().getNazivPostaje();
        String komponenta = pm.getKomponentaId().getFormula();
        String body  = bodyTemplate;
        body = body.replaceFirst("\\$\\{OPIS\\}", opis);
        body = body.replaceFirst("\\$\\{POSTAJA\\}", postaja);
        body = body.replaceFirst("\\$\\{KOMPONENTA\\}", komponenta);
        return body;
        
    }
    
}
