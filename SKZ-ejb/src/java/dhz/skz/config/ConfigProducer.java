/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 *
 * @author kraljevic
 */
public class ConfigProducer {
    private static final Logger log = Logger.getLogger(ConfigProducer.class.getName());
    private volatile static Properties configProperties;
   
    
    public @Produces @Config TimeZone injectTimeZone(InjectionPoint ip) {
        return TimeZone.getTimeZone("GMT");
    }
    
    public @Produces @Config Logger getLogger(InjectionPoint ip) {
        return Logger.getLogger(ip.getMember().getDeclaringClass().getName());
        
    }
    
    public synchronized static Properties getProperties() {
        InputStream resourceAsStream = ConfigProducer.class.getClassLoader().getResourceAsStream("/dhz/skz/config/skz.properties");
        if(configProperties==null) {
            configProperties=new Properties();
            try {
                configProperties.load(resourceAsStream);
            } catch (IOException ex) {
                log.log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex);
            }
        }
        return configProperties;
    }


    public @Produces @Config String getConfiguration(InjectionPoint p) {
        Properties config=getProperties();
        String configKey=p.getMember().getDeclaringClass().getName()+"."+p.getMember().getName();
        if(config.getProperty(configKey)==null) {
            configKey=p.getMember().getDeclaringClass().getSimpleName()+"."+p.getMember().getName();
            if(config.getProperty(configKey)==null)
                configKey=p.getMember().getName();
        }
        log.log(Level.INFO, "Config key= {0} value = {1}", new Object[]{configKey,config.getProperty(configKey)});

        return config.getProperty(configKey);
    }

     public @Produces @Config Integer getConfigurationInteger(InjectionPoint p) {
         String val=getConfiguration(p);
         return Integer.parseInt(val);
     }
}
