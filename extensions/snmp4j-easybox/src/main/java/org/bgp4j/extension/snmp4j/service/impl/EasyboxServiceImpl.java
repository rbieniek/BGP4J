/**
 * 
 */
package org.bgp4j.extension.snmp4j.service.impl;

import java.util.LinkedList;
import java.util.List;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.bgp4j.extension.snmp4j.config.nodes.EasyboxConfiguration;
import org.bgp4j.extension.snmp4j.service.EasyboxInstance;
import org.bgp4j.extension.snmp4j.service.EasyboxService;

/**
 * @author rainer
 *
 */
public class EasyboxServiceImpl implements EasyboxService {

	private @Inject Instance<EasyboxInstanceImpl> provider;
	private List<EasyboxInstance> instances = new LinkedList<EasyboxInstance>();
	
	@Override
	public void configure(List<EasyboxConfiguration> configurations) {
		for(EasyboxConfiguration config : configurations) {
			EasyboxInstanceImpl instance = provider.get();
			
			instance.configure(config);
			instances.add(instance);
		}
	}

	@Override
	public void startService() throws Exception {
		for(EasyboxInstance instance : instances)
			((EasyboxInstanceImpl)instance).startInstance();
	}

	@Override
	public void stopService() throws Exception {
		for(EasyboxInstance instance : instances)
			((EasyboxInstanceImpl)instance).stopInstance();
	}

	@Override
	public List<EasyboxInstance> getInstances() {
		return instances;
	}

}
