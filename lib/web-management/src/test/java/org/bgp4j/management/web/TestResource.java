/**
 * 
 */
package org.bgp4j.management.web;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author rainer
 *
 */
@Path("/test")
@ApplicationScoped
public class TestResource {

	private @Inject TestBean testBean;
	
	@GET
	@Path("/bean")
	@Produces("application/text")
	public String isTestBeanInjected() {
		return (testBean != null) ? "found" : "null";
	}
}
