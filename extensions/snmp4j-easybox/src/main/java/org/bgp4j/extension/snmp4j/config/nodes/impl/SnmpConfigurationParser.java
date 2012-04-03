/**
 * 
 */
package org.bgp4j.extension.snmp4j.config.nodes.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.inject.Singleton;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.bgp4j.extension.snmp4j.config.nodes.SnmpConfiguration;

/**
 * @author rainer
 *
 */
@Singleton
public class SnmpConfigurationParser {

	public SnmpConfiguration parseConfiguration(HierarchicalConfiguration config) throws ConfigurationException {
		SnmpConfigurationImpl impl = new SnmpConfigurationImpl();
		
		try {
			String addr = config.getString("[@targetAddress]");
			
			if(StringUtils.isBlank(addr))
				throw new ConfigurationException("empty SNMP address given");
			
			impl.setAddress(InetAddress.getByName(addr));
		} catch (UnknownHostException e) {
			throw new ConfigurationException("cannot parse easybox IP address", e);
		}
		impl.setCommunity(config.getString("[@community]"));
		if(StringUtils.isBlank(impl.getCommunity()))
			throw new ConfigurationException("empty SNMP community name given");

		impl.setLocalPort(config.getInt("[@localPort]", 0));
		
		return impl;
	}
}
