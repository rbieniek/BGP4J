/**
 * 
 */
package org.bgp4j.extension.snmp4j.service;

import java.util.List;

import org.bgp4j.extension.snmp4j.config.nodes.EasyboxConfiguration;

/**
 * @author rainer
 *
 */
public interface EasyboxService {

	/**
	 * configure all instances
	 * 
	 * @param configuration
	 */
	public void configure(List<EasyboxConfiguration> configurations);
	
	/**
	 * start all instances
	 * @throws Exception
	 */
	public void startService() throws Exception;
	
	/**
	 * stop all instances
	 * @throws Exception
	 */
	public void stopService() throws Exception;
	
	/**
	 * list all instances
	 * 
	 * @return
	 */
	public List<EasyboxInstance> getInstances();
}
