/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dhz.skz.diseminacija.ekonerg;

import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.PrimateljiPodataka;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import java.util.Map;
import javax.ejb.embeddable.EJBContainer;
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
public class EkonergDiseminatorTest {
    
    public EkonergDiseminatorTest() {
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
     * Test of salji method, of class EkonergDiseminator.
     */
    @Test
    public void testSalji_PrimateljiPodataka() throws Exception {
        System.out.println("salji");
        PrimateljiPodataka primatelj = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        EkonergDiseminator instance = (EkonergDiseminator)container.getContext().lookup("java:global/classes/EkonergDiseminator");
        instance.salji(primatelj);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    
}
