/**
 * 
 */
package org.bgp4j.netty.service;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

/**
 * Producer qualifier for client channel socket factory
 * 
 * @author rainer
 *
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD , ElementType.PARAMETER, ElementType.METHOD})
public @interface ClientFactory {

}
