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
package dhz.skz.util.mailer;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author kraljevic
 */
public class EmailSessionBeanTest {
    
    public EmailSessionBeanTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of sendEmail method, of class EmailSessionBean.
     */
    @Test
    public void testSendEmail() throws Exception {
        System.out.println("sendEmail");
        String to = "mailto:kluksa@gmail.com|mailto:kraljevic@cirus.dhz.hr";
        String subject = "Subjekt";
        String body = "Body";
        String attStr = "a;b;c;d;\n1;2;3;4\n5;6;7;8;";
        byte[] attachment = attStr.getBytes();
        String attachemntMIMEType = "text/csv";
        EmailSessionBean instance = new EmailSessionBean();

        instance.sendEmail(getURLs(to), subject, body, attachment, attachemntMIMEType);
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
