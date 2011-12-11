/**
 * 
 */
package de.urb.quagga.weld;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Singleton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author rainer
 *
 */
@ApplicationScoped
@Singleton
public class CommonsLoggingFactory {
	@Produces Log produceLog(InjectionPoint ip) {
		return LogFactory.getLog(ip.getMember().getDeclaringClass());
	}
}
