/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dhz.skz.sirovi;

import dhz.skz.webservis.omotnica.CsvOmotnica;
import dhz.skz.wsbackend.PrihvatSirovihPodatakaRemote;
import javax.ejb.embeddable.EJBContainer;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author kraljevic
 */
public class PrihvatPodatakaTest {
    
    public PrihvatPodatakaTest() {
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
     * Test of prihvatiOmotnicu method, of class PrihvatPodataka.
     */
    @Test
    public void testPrihvatiOmotnicu() throws Exception {
        System.out.println("prihvatiOmotnicu");
        CsvOmotnica omotnica = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        PrihvatSirovihPodatakaRemote instance = (PrihvatSirovihPodatakaRemote)container.getContext().lookup("java:global/classes/PrihvatPodataka");
        instance.prihvatiOmotnicu(omotnica);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUnixTimeZadnjeg method, of class PrihvatPodataka.
     */
    @Test
    public void testGetUnixTimeZadnjeg() throws Exception {
        System.out.println("getUnixTimeZadnjeg");
        String izvorS = "";
        String postajaS = "";
        String datotekaS = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        PrihvatSirovihPodatakaRemote instance = (PrihvatSirovihPodatakaRemote)container.getContext().lookup("java:global/classes/PrihvatPodataka");
        Long expResult = null;
        Long result = instance.getUnixTimeZadnjeg(izvorS, postajaS, datotekaS);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of test method, of class PrihvatPodataka.
     */
    @Test
    public void testTest() throws Exception {
        System.out.println("test");
        String inStr = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        PrihvatSirovihPodatakaRemote instance = (PrihvatSirovihPodatakaRemote)container.getContext().lookup("java:global/classes/PrihvatPodataka");
        String expResult = "";
        String result = instance.test(inStr);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
