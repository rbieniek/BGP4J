/**
 * 
 */
package org.bgp4j.rib.processor;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.bgp4j.config.nodes.RoutingInstanceConfiguration;
import org.bgp4j.config.nodes.RoutingProcessorConfiguration;
import org.slf4j.Logger;

/**
 * @author rainer
 *
 */
public class RoutingProcessor {
	private @Inject Instance<RoutingInstance> instanceProvider;
	private @Inject Logger log;

	private List<RoutingInstance> instances = new LinkedList<RoutingInstance>();
	
	public void configure(RoutingProcessorConfiguration configuration) {
		for(RoutingInstanceConfiguration instConfig : configuration.getRoutingInstances()) {
			RoutingInstance instance = instanceProvider.get();
			
			instance.configure(instConfig);
			instances.add(instance);
		}
		
		instances = Collections.unmodifiableList(instances);
	}
	
	public void startService() {
		for(RoutingInstance instance : instances) {
			log.info("Starting routing instance between " + instance.getFirstPeerName() + " and " + instance.getSecondPeerName()); 
			
			try {
				instance.startInstance();
				
				log.info("Starting routing instance between " + instance.getFirstPeerName() + " and " + instance.getSecondPeerName() + " in state " + instance.getState()); 
			}  catch(Throwable t) {
				log.error("failed to start routing instances between " + instance.getFirstPeerName() + " and " + instance.getSecondPeerName(), t);
			}
		}
	}

	public void stopService() {
		for(RoutingInstance instance : instances) {
			log.info("Stopping routing instance between " + instance.getFirstPeerName() + " and " + instance.getSecondPeerName()); 
			
			try {
				instance.stopInstance();
			}  catch(Throwable t) {
				log.error("failed to stop routing instances between " + instance.getFirstPeerName() + " and " + instance.getSecondPeerName(), t);
			}
		}
	}

	/**
	 * @return the instances
	 */
	public List<RoutingInstance> getInstances() {
		return instances;
	}

	
}
