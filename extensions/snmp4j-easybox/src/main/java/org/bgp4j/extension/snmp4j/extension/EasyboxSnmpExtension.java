/**
 * 
 */
package org.bgp4j.extension.snmp4j.extension;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.bgp4j.config.nodes.AddressFamilyRoutingConfiguration;
import org.bgp4j.extension.snmp4j.config.nodes.EasyboxConfiguration;
import org.bgp4j.extension.snmp4j.config.nodes.impl.EasyBoxConfigurationParser;
import org.bgp4j.extension.snmp4j.service.EasyboxInstance;
import org.bgp4j.extension.snmp4j.service.EasyboxService;
import org.bgp4j.extension.snmp4j.web.EasyboxWebApplication;
import org.bgp4j.extensions.Extension;
import org.bgp4j.extensions.ExtensionBase;
import org.bgp4j.extensions.ExtensionBeanFactory;
import org.bgp4j.extensions.ProvidedRIBs;
import org.bgp4j.extensions.ProvidedRIBs.SideKeyPair;
import org.bgp4j.net.RIBSide;
import org.bgp4j.rib.PeerRoutingInformationBase;
import org.bgp4j.rib.PeerRoutingInformationBaseManager;

/**
 * @author rainer
 *
 */
public class EasyboxSnmpExtension extends ExtensionBase implements Extension {

	private EasyBoxConfigurationParser parser;
	private EasyboxWebApplication webApplication;
	private EasyboxService service;
	private PeerRoutingInformationBaseManager pribManager;
	private List<EasyboxConfiguration> easyboxConfigurations = new LinkedList<EasyboxConfiguration>();
	private List<ProvidedRIBs> providedRibs = new LinkedList<ProvidedRIBs>();
	private ExtensionBeanFactory beanFactory;
	private boolean serviceReady;
	
	@Override
	public String getName() {
		return "snmp4j-easybox";
	}

	@Override
	public void configure(HierarchicalConfiguration config) throws ConfigurationException {
		Set<String> keys = new HashSet<String>();		
		
		for(HierarchicalConfiguration subConfig : config.configurationsAt("Easybox")) {
			EasyboxConfiguration ebc = parser.parseConfguration(subConfig);
			
			if(keys.contains(ebc.getName()))
				throw new ConfigurationException("duplicate Easybox " + ebc.getName());
			
			easyboxConfigurations.add(ebc);
			keys.add(ebc.getName());
		}
		
		service.configure(easyboxConfigurations);
		serviceReady = true;
	}

	@Override
	public void startExtension() throws Exception {
		Map<String, EasyboxInstance> instances = new HashMap<String, EasyboxInstance>();
		
		for(EasyboxInstance instance : service.getInstances()) {
			instances.put(instance.getName(), instance);
		}
		
		for(EasyboxConfiguration config : easyboxConfigurations) {
			String peerName = getName() + "-" + config.getName();
			PeerRoutingInformationBase prib = pribManager.peerRoutingInformationBase(peerName);
			List<SideKeyPair> keyPairs = new LinkedList<ProvidedRIBs.SideKeyPair>();
			
			for(AddressFamilyRoutingConfiguration routingConfig : config.getRoutingConfiguration().getRoutingConfigurations()) {
				prib.allocateRoutingInformationBase(RIBSide.Remote, routingConfig.getKey());

				// add interface status listener here 
				RIBInterfaceListener listener = beanFactory.getBeanInstance(RIBInterfaceListener.class);
				
				listener.configureRouting(routingConfig, prib.routingBase(RIBSide.Remote, routingConfig.getKey()));
				
				instances.get(config.getName()).addInterfaceListener(listener);
				
				keyPairs.add(new SideKeyPair(RIBSide.Remote, routingConfig.getKey()));
			}
			
			providedRibs.add(new ProvidedRIBs(peerName, keyPairs));
		}
		
		service.startService();
	}

	@Override
	public void stopExtension() throws Exception {
		service.stopService();
	}

	@Override
	public Collection<ProvidedRIBs> getProvidedRIBs() {
		return providedRibs;
	}

	@Override
	public boolean isReadyForService() {
		return serviceReady;
	}

	@Override
	public void initialize(ExtensionBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
		
		parser = beanFactory.getBeanInstance(EasyBoxConfigurationParser.class);
		service = beanFactory.getBeanInstance(EasyboxService.class);
		pribManager = beanFactory.getBeanInstance(PeerRoutingInformationBaseManager.class);
		webApplication = beanFactory.getBeanInstance(EasyboxWebApplication.class);
		webApplication.setService(service);
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.extensions.ExtensionBase#getManagementObjects()
	 */
	@Override
	public Set<Object> getManagementObjects() {
		Set<Object> mgmt = new HashSet<Object>();
		
		mgmt.add(webApplication);
		
		return mgmt;
	}

}
