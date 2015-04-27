/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rs;

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
        resources.add(dhz.skz.rs.GZIPWriterInterceptor.class);
        resources.add(dhz.skz.rs.SatniPodatakResource.class);
        resources.add(dhz.skz.rs.SiroviPodaci.class);
        resources.add(dhz.skz.rs.UmjeravanjeResource.class);
        resources.add(dhz.skz.rs.UredjajResource.class);
        resources.add(dhz.skz.rs.ZeroSpanResource.class);
        resources.add(dhz.skz.rs.service.ProgramMjerenjaFacadeREST.class);
        resources.add(dhz.skz.rs.service.service.AnalitickeMetodeFacadeREST.class);
        resources.add(dhz.skz.rs.service.service.IndustrijskePostajeSvojstvaFacadeREST.class);
        resources.add(dhz.skz.rs.service.service.ModelUredjajaFacadeREST.class);
        resources.add(dhz.skz.rs.service.service.OdgovornoTijeloFacadeREST.class);
        resources.add(dhz.skz.rs.service.service.PodrucjeFacadeREST.class);
        resources.add(dhz.skz.rs.service.service.PostajaFacadeREST.class);
        resources.add(dhz.skz.rs.service.service.PostajaUredjajLinkFacadeREST.class);
        resources.add(dhz.skz.rs.service.service.ProizvodjacFacadeREST.class);
        resources.add(dhz.skz.rs.service.service.PrometnePostajeSvojstvaFacadeREST.class);
        resources.add(dhz.skz.rs.service.service.ReprezentativnostFacadeREST.class);
        resources.add(dhz.skz.rs.service.service.UredjajFacadeREST.class);
        resources.add(dhz.skz.rs.service.service.VrstaOpremeFacadeREST.class);
        resources.add(dhz.skz.rs.service.service.VrstaPostajeIzvorFacadeREST.class);
    }
    
}
