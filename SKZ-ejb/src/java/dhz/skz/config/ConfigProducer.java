/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.config;

import java.util.TimeZone;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 *
 * @author kraljevic
 */
public class ConfigProducer {
    @Produces
    @Config
    public TimeZone injectTimeZone(InjectionPoint ip) {
        return TimeZone.getTimeZone("UTC");
    }
    
    @Produces
    @Config
    public String injectString(InjectionPoint ip) {
        return ip.getMember().getName();
    }
}
