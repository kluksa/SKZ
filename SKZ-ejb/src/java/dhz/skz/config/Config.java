/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

/**
 *
 * @author kraljevic
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface Config {
//    /**
//     * Key that will be searched when injecting the value.
//     */
//    @Nonbinding
//    String value() default "";
//
//    /**
//     * Defines if value for the given key must be defined.
//     */
//    @Nonbinding
//    boolean required() default true; 
    
    @Nonbinding
    String  vrijednost() default "";

}