/**
 * 
 */
package org.bgp4j.rib.processor;

import java.util.List;

import javax.inject.Inject;

import org.bgp4j.config.global.ApplicationConfiguration;

/**
 * @author rainer
 *
 */
public class GlobalRoutingProcessor {

	private @Inject ApplicationConfiguration appConfig;
	private @Inject RoutingProcessor routingProcessor;
	
	/**
	 * @param configuration
	 * @see org.bgp4j.rib.processor.RoutingProcessor#configure(org.bgp4j.config.nodes.RoutingProcessorConfiguration)
	 */
	public void configure() {
		if(appConfig.getRoutingProcessorConfiguration() != null)
			routingProcessor.configure(appConfig.getRoutingProcessorConfiguration());
	}
	
	/**
	 * 
	 * @see org.bgp4j.rib.processor.RoutingProcessor#startService()
	 */
	public void startService() {
		routingProcessor.startService();
	}

	/**
	 * 
	 * @see org.bgp4j.rib.processor.RoutingProcessor#stopService()
	 */
	public void stopService() {
		routingProcessor.stopService();
	}
	
	/**
	 * @return
	 * @see org.bgp4j.rib.processor.RoutingProcessor#getInstances()
	 */
	public List<RoutingInstance> getInstances() {
		return routingProcessor.getInstances();
	}
}
