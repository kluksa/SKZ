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

import dhz.skz.diseminacija.upozorenja.slanje.exceptions.XmlObavijestException;
import dhz.skz.aqdb.entity.Komponenta;
import dhz.skz.aqdb.entity.Obavijesti;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.PrimateljiPodataka;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.util.eksportpodataka.PodatakEksport;
import dhz.skz.util.eksportpodataka.PodatakEksportFactory;
import dhz.skz.util.eksportpodataka.exceptions.InvalidPodatakExportException;
import dhz.skz.util.mailer.EmailSessionBean;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author kraljevic
 */
public class MailUpozorenje implements SlanjeUpozorenja {

    public void testirajSustav(PrimateljiPodataka primatelj, Podatak pod){
        String tekst = "<root><subject>Ispitivanje sustava za dojavu prekoracenja pragova upozorenja</subject>"
                     + "<body>Poštovani,\n"
                     + "Ovo je ispitivanje sustava za dojavu prekoračenja pragova upozorenja. "
                     +  "U ${VRIJEME} je na postaji ${POSTAJA} zabilježena koncentracija "
                     + "od ${KONCENTRACIJA} ug/m3 ${KOMPONENTA}.\n"
                     + " Molimo da na ovu poruku odgovorite kako bi znali da ste uspiješno primili poruku.\n\n"
                     + " Hvala!\nDržavni hidrometeorolški zavod.</body></root>";
        try {
            Komponenta komponentaId = pod.getProgramMjerenjaId().getKomponentaId();
            Postaja postajaId = pod.getProgramMjerenjaId().getPostajaId();

            XmlParser xmlp = new XmlParser();
            xmlp.parse(tekst);

            String body = xmlp.getBody();

            body = body.replaceAll("\\$\\{VRIJEME\\}", pod.getVrijeme().toString());
            body = body.replaceAll("\\$\\{POSTAJA\\}", postajaId.getNazivPostaje());
            body = body.replaceAll("\\$\\{KOMPONENTA\\}", komponentaId.getFormula());
            body = body.replaceAll("\\$\\{KONCENTRACIJA\\}", pod.getVrijednost().toString());
            String subject = xmlp.getSubject();

            EmailSessionBean esb = new EmailSessionBean();
            URL[] urlovi = getURLs(primatelj.getUrl());
 //           System.out.println(urlovi.toString());
            esb.sendEmail(urlovi, subject, body);


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

    @Override
    public void saljiUpozorenje(Obavijesti ob, ProgramMjerenja pm, Double maksimalnaVrijednost,Collection<Podatak> podaci, VrstaUpozorenja vrsta) {
        try {
            PrimateljiPodataka primatelj = ob.getPrimatelj();
            Komponenta komponentaId = pm.getKomponentaId();
            Postaja postajaId = pm.getPostajaId();

            XmlParser xmlp = new XmlParser();
            xmlp.parse(ob.getPredlozakTeksta(), vrsta);

            String bodyTemplate = xmlp.getBody();

            String body = obradiPredlozak(bodyTemplate, ob, pm, maksimalnaVrijednost);
            String subject = xmlp.getSubject();

            EmailSessionBean esb = new EmailSessionBean();
            URL[] urlovi = getURLs(primatelj.getUrl());

            // PodatkEksport treba odrediti iz xsd polja tablice primatelji_podataka, a ne ovako
            // najbolje bi bilo napraviti factory i
            if (podaci != null && !podaci.isEmpty()) {
                PodatakEksportFactory pef = new PodatakEksportFactory();
                PodatakEksport pe = pef.getPodatakEksport(ob.getPrimatelj());
                Writer wr = new StringWriter();
                pe.write(wr, podaci);

                esb.sendEmail(urlovi, subject, body, wr.toString().getBytes("UTF-8"), "text/csv");
            } else {
                esb.sendEmail(urlovi, subject, body);
            }

        } catch (IOException ex) {
            Logger.getLogger(MailUpozorenje.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XmlObavijestException ex) {
            Logger.getLogger(MailUpozorenje.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(MailUpozorenje.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(MailUpozorenje.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidPodatakExportException ex) {
            Logger.getLogger(MailUpozorenje.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private String obradiPredlozak(String bodyTemplate, Obavijesti ob, ProgramMjerenja pm, Double maksimalnaVrijednost) {
        String opis = ob.getGranica().getKategorijeGranicaId().getOpis();
        String postaja = pm.getPostajaId().getNazivPostaje();
        String komponenta = pm.getKomponentaId().getFormula();
        String body = bodyTemplate;
        body = body.replaceAll("\\$\\{OPIS\\}", opis);
        body = body.replaceAll("\\$\\{POSTAJA\\}", postaja);
        body = body.replaceAll("\\$\\{KOMPONENTA\\}", komponenta);
        body = body.replaceAll("\\$\\{MAKSIMALNA_VRIJEDNOST\\}", maksimalnaVrijednost.toString());
        return body;

    }

    private URL[] getURLs(String url) throws MalformedURLException {
        String[] split = url.split("\\|");
        URL[] urlovi = new URL[split.length];
        
        for (int i = 0; i<split.length; i++ ) {
            urlovi[i] = new URL(split[i]);
        }
        return urlovi;

    }

}
