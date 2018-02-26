/*
 * Copyright (C) 2017 kraljevic
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

import dhz.skz.aqdb.entity.PrimateljiPodataka;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kraljevic
 */
public class UpozorenjaBeanTest {
    
    public UpozorenjaBeanTest() {
    }

    @Test
    public void testPopuniPredlozak() throws IOException {
        
        String contents = new String(Files.readAllBytes(Paths.get("test/dhz/skz/diseminacija/upozorenja/upozorenje_test.xml")));
        System.out.println("popuniPredlozak");
        String template = "<poruka><email>ovo ono $mailto jos $maksimalna_vrijednost.</email></poruka>";
        UpozorenjaBean instance = new UpozorenjaBean();
        String expResult = "<poruka><email>ovo ono World jos mv.</email></poruka>";
//        String result = instance.popuniPredlozak(template);
//        assertEquals(expResult, result);
    }
    
}
