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
package dhz.skz.diseminacija.upozorenja;

import dhz.skz.aqdb.entity.Obavijesti;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.PrekoracenjaUpozorenjaResult;
import dhz.skz.aqdb.entity.PrimateljiPodataka;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.facades.ObavijestiFacade;
import dhz.skz.aqdb.facades.PodatakFacade;
import dhz.skz.aqdb.facades.PrekoracenjaUpozorenjaResultFacade;
import dhz.skz.aqdb.facades.PrimateljiPodatakaFacade;
import dhz.skz.diseminacija.DiseminatorPodataka;
import dhz.skz.diseminacija.upozorenja.poruka.SlanjePoruka;
import dhz.skz.diseminacija.upozorenja.slanje.MailUpozorenje;
import dhz.skz.diseminacija.upozorenja.slanje.UpozorenjeSlanjeFactory;
import dhz.skz.diseminacija.upozorenja.slanje.VrstaUpozorenja;
import dhz.skz.util.OperStatus;
import java.io.StringWriter;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 *
 * @author kraljevic
 */
@Stateless
//@LocalBean
public class UpozorenjaBean implements DiseminatorPodataka, UpozorenjaBeanRemote {

    private static final Logger log = Logger.getLogger(UpozorenjaBean.class.getName());

    @EJB
    private PodatakFacade pf;

    @EJB
    private ObavijestiFacade obf;

    @EJB
    private PrekoracenjaUpozorenjaResultFacade prFac;

    @EJB
    private PrimateljiPodatakaFacade primF;

    @Override
    public void salji(PrimateljiPodataka primatelj) {
        return;
//        OffsetDateTime odt_sada = OffsetDateTime.now();
//        SlanjePoruka suf = new SlanjePoruka();
//
//        for (Obavijesti obavijest : obf.findAll(primatelj)) {
//            Integer granicniBroj = obavijest.getGranica().getDozvoljeniBrojPrekoracenja();
//            for (PrekoracenjaUpozorenjaResult pur : prFac.findAll(obavijest, odt_sada, granicniBroj)) {
//                Collection<Podatak> podaci = pokupiPodatke(primatelj, odt_sada);
//                Podatak pod = pf.getPodatakZaSat(pur.getProgramMjerenja(), odt_sada);
//                if (Objects.equals(pur.getBrojPojavljivanja(), granicniBroj)) {
//                    suf.salji(obavijest, pur, pod, podaci, VrstaUpozorenja.UPOZORENJE);
//                } else if (pod != null && (pod.getVrijednost() > obavijest.getGranica().getVrijednost()) && OperStatus.isValid(pod)) {
//                    suf.salji(obavijest, pur, pod, podaci, VrstaUpozorenja.OBAVIJEST);
//                }
//            }
//        }
    }

    public String popuniPredlozak(String template) {
        VelocityContext context = new VelocityContext();
        context.put("mailto", "World");
        context.put("opis", "op");
        context.put("komponenta", "ko");
        context.put("postaja", "po");
        context.put("maksimalna_vrijednost", "mv");

        /* now render the template into a StringWriter */
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context, writer, "VEL:", template);
        return writer.toString();
    }

    @Override
    public void nadoknadi(PrimateljiPodataka primatelj, Collection<ProgramMjerenja> program, Date pocetak, Date kraj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Collection<Podatak> pokupiPodatke(PrimateljiPodataka primatelj, OffsetDateTime sada) {
        OffsetDateTime pocetak = sada.minusHours(6);
        return pf.getVrijemePrimatelj(primatelj, pocetak, sada);
    }

    @Override
    public void testirajSlanje() {
        OffsetDateTime odt_sada = OffsetDateTime.now();
        MailUpozorenje suf = new MailUpozorenje();
        for (PrimateljiPodataka pp : primF.getAktivniPrimateljiUpozorenja()) {
            System.out.println(pp.getNaziv());
        }
        
        for (PrimateljiPodataka pp : primF.getAktivniPrimateljiUpozorenja()) {
            for (ProgramMjerenja pm : pp.getProgramMjerenjaCollection()) {
                Podatak pod = pf.getPodatakZaSat(pm, odt_sada);
                if ( pod != null) {
                    suf.testirajSustav(pp, pod);
                } else {
                    System.out.println("NULL:" + pm.getKomponentaId().getFormula() + ":" + pm.getPostajaId().getNazivPostaje() );
                }
            }
        }
    }
}
