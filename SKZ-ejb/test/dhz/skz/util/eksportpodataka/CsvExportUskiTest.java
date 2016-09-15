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
package dhz.skz.util.eksportpodataka;

import dhz.skz.aqdb.entity.Komponenta;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kraljevic
 */
public class CsvExportUskiTest {
    Collection<Podatak> podaci; 
    
    public CsvExportUskiTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        podaci = new ArrayList<>();
        Date sada = new Date();
        int npostaja = 3;
        Postaja[] postaje = new Postaja[npostaja];
        for (int i = 0; i<npostaja; i++) {
            Postaja p = new Postaja();
            p.setNazivPostaje("Postaja -" + i);
            postaje[i] = p;
        }
        int nkomponenta = 5;
        Komponenta[] komponente = new Komponenta[nkomponenta];
        for ( int i = 0; i<nkomponenta; i++) {
            Komponenta k = new Komponenta();
            k.setFormula("K"+i);
            komponente[i] = k;
        }
        int nvremena = 10;
        Date[] vremena = new Date[nvremena];
        for ( int i = 0; i<nvremena; i++) {
            Date vijeme = new Date(sada.getTime() + 1000 + 1000*i);
            vremena[i] = vijeme;
        }
        for ( int p = 0; p<npostaja; p++){
            for (int k = 0; k<nkomponenta; k++){
                ProgramMjerenja pm = new ProgramMjerenja();
                pm.setPostajaId(postaje[p]);
                pm.setKomponentaId(komponente[k]);
                for ( int v = 0; v<nvremena; v++) {
                    Podatak pod = new Podatak();
                    pod.setVrijeme(vremena[v]);
                    pod.setProgramMjerenjaId(pm);
                    pod.setVrijednost((100*p+10*k+v)/3.);
                    pod.setStatus(0);
                    pod.setNivoValidacijeId(0);
                    podaci.add(pod);
                }
            }
        }
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of write method, of class CsvExportUski.
     */
    @Test
    public void testWrite() throws Exception {
        System.out.println("write");
        Writer writer = new StringWriter();
        CsvExportUski instance = new CsvExportUski();
        instance.write(writer, podaci);
        System.out.print(writer.toString());
    }
    
}
