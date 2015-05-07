/*
 * Copyright (C) 2015 kraljevic
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
package dhz.skz.validatori;

import dhz.skz.aqdb.entity.Komponenta;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ProgramUredjajLink;
import dhz.skz.aqdb.entity.Umjeravanje;
import dhz.skz.aqdb.entity.UmjeravanjeHasIspitneVelicine;
import dhz.skz.aqdb.entity.Uredjaj;
import dhz.skz.validatori.Validator;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 *
 * @author kraljevic
 */
public abstract class ValidatorFactoryFF {

    private final Map<ProgramMjerenja, NavigableMap<Date, Uredjaj>> programUredjaji;
    private final Map<Uredjaj, NavigableMap<Date, Umjeravanje>> uredjajUmjeravanja;
    private final Map<Umjeravanje, Map<Komponenta, RezultatiUmjeravanja>> umjeravanjaKomponente;

    private final RezultatiUmjeravanja defaultRU;

    public ValidatorFactoryFF(Collection<ProgramMjerenja> programi) {
        programUredjaji = new HashMap<>(4 * programi.size() / 3 + 2);
        uredjajUmjeravanja = new HashMap<>();
        umjeravanjaKomponente = new HashMap<>();
        defaultRU = new RezultatiUmjeravanja();
        for (ProgramMjerenja p : programi) {
            NavigableMap<Date, Uredjaj> vrijemeUredjaj = new TreeMap<>();
            for (ProgramUredjajLink pul : p.getProgramUredjajLinkCollection()) {
                int id = pul.getUredjajId().getId();
                vrijemeUredjaj.put(pul.getVrijemePostavljanja(), pul.getUredjajId());
                if (!uredjajUmjeravanja.containsKey(pul.getUredjajId())) {
                    NavigableMap<Date, Umjeravanje> vrijemeUmjeravanje = new TreeMap<>();
                    for (Umjeravanje u : pul.getUredjajId().getUmjeravanjeCollection()) {
                        vrijemeUmjeravanje.put(u.getDatum(), u);
                        Map<Komponenta, RezultatiUmjeravanja> komponentarezultati = new HashMap<>();
                        for (UmjeravanjeHasIspitneVelicine uiv : u.getUmjeravanjeHasIspitneVelicineCollection()) {
                            if (!komponentarezultati.containsKey(uiv.getKomponenta())) {
                                komponentarezultati.put(uiv.getKomponenta(), new RezultatiUmjeravanja());
                            }
                            RezultatiUmjeravanja rezultati = komponentarezultati.get(uiv.getKomponenta());
                            switch (uiv.getIspitneVelicine().getOznaka()) {
                                case "A":
                                    rezultati.A = uiv.getIznos();
                                    break;
                                case "B":
                                    rezultati.B = uiv.getIznos();
                                    break;
                                case "Srz":
                                    rezultati.Srz = uiv.getIznos();
                                    break;
                                case "o":
                                    rezultati.o = uiv.getIznos();
                                    break;
                            }

                        }
                        umjeravanjaKomponente.put(u, komponentarezultati);
                    }
                    uredjajUmjeravanja.put(pul.getUredjajId(), vrijemeUmjeravanje);
                }
            }
            programUredjaji.put(p, vrijemeUredjaj);
        }
    }

    private RezultatiUmjeravanja getRezultati(ProgramMjerenja pm, Date vrijeme) {
        try {
            Uredjaj uredjaj = programUredjaji.get(pm).floorEntry(vrijeme).getValue();
            NavigableMap<Date, Umjeravanje> vrijemeUmjeravanje = uredjajUmjeravanja.get(uredjaj);
            Umjeravanje umjeravanje = vrijemeUmjeravanje.floorEntry(vrijeme).getValue();
            RezultatiUmjeravanja ru = umjeravanjaKomponente.get(umjeravanje).get(pm.getKomponentaId());
            return (ru == null) ? defaultRU : ru;
        } catch (NullPointerException ex) {
            return defaultRU;
        }
    }

    public double getA(ProgramMjerenja pm, Date vrijeme) {
        return getRezultati(pm, vrijeme).A;
    }

    public double getB(ProgramMjerenja pm, Date vrijeme) {
        return getRezultati(pm, vrijeme).B;
    }

    public double getOpseg(ProgramMjerenja pm, Date vrijeme) {
        return getRezultati(pm, vrijeme).o;
    }

    public double getDL(ProgramMjerenja pm, Date vrijeme) {
        return 3.33 * getRezultati(pm, vrijeme).Srz / getRezultati(pm, vrijeme).A;
    }

    public Uredjaj getUredjaj(ProgramMjerenja pm, Date vrijeme) {
        return programUredjaji.get(pm).floorEntry(vrijeme).getValue();
    }

    public abstract Validator getValidator(ProgramMjerenja pm, Date vrijeme);

    private static class RezultatiUmjeravanja {

        private double A = 1.0, B = 0.0, o = 1000., Srz = 1.0;
    }

}
