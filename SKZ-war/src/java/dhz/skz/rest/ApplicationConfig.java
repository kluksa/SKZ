/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rest;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author kraljevic
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        try {
            Class jsonProvider = Class.forName("org.glassfish.jersey.jackson.JacksonFeature");
            // Class jsonProvider = Class.forName("org.glassfish.jersey.moxy.json.MoxyJsonFeature");
            // Class jsonProvider = Class.forName("org.glassfish.jersey.jettison.JettisonFeature");
            resources.add(jsonProvider);
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(dhz.skz.rest.DrzavnaMrezaResource.class);
        resources.add(dhz.skz.rest.GZIPWriterInterceptor.class);
        resources.add(dhz.skz.rest.ProgramMjerenjaFacadeREST.class);
        resources.add(dhz.skz.rest.SatniPodatakResource.class);
        resources.add(dhz.skz.rest.SiroviPodaci.class);
        resources.add(dhz.skz.rest.UmjeravanjeResource.class);
        resources.add(dhz.skz.rest.UredjajResource.class);
        resources.add(dhz.skz.rest.ZeroSpanResource.class);
    }
    
}
