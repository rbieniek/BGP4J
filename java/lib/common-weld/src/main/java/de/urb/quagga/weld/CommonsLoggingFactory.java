/**
 * 
 */
package de.urb.quagga.weld;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author rainer
 *
 */
public class CommonsLoggingFactory {
	@Produces Log produceLog(InjectionPoint ip) {
		return LogFactory.getLog(ip.getMember().getDeclaringClass());
	}
}
