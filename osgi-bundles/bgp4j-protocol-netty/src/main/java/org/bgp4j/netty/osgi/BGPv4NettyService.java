package org.bgp4j.netty.osgi;

import org.bgp4j.definitions.config.ServerConfigurationProvider;
import org.bgp4j.definitions.fsm.BGPv4FSMRegistry;
import org.bgp4j.definitions.services.BGPv4ProtocolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BGPv4NettyService  implements BGPv4ProtocolService {
	private static final Logger logger = LoggerFactory.getLogger(BGPv4NettyService.class);
	
	private BGPv4FSMRegistry fsmRegistry;
	private ServerConfigurationProvider serverConfigurationProvider;
	
	/**
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception {
		logger.info("starting service");
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void stop() throws Exception {
		logger.info("stopping service");
	}
	
	/**
	 * @param fsmRegistry the fsmRegistry to set
	 */
	public void setFsmRegistry(BGPv4FSMRegistry fsmRegistry) {
		this.fsmRegistry = fsmRegistry;
	}
	
	/**
	 * @param serverConfigurationProvider the serverConfigurationProvider to set
	 */
	public void setServerConfigurationProvider(ServerConfigurationProvider serverConfigurationProvider) {
		this.serverConfigurationProvider = serverConfigurationProvider;
	}
		
	public void bindServerConfigurationProvider(ServerConfigurationProvider serverConfigurationProvider) {
		logger.info("binding server configuration provider");
		
		this.serverConfigurationProvider = serverConfigurationProvider;
	}

	public void unbindServerConfigurationProvider(ServerConfigurationProvider serverConfigurationProvider) {
		logger.info("unbinding server configuration provider");

		this.serverConfigurationProvider = null;
	}
	
	public void bindFsmRegistry(BGPv4FSMRegistry fsmRegistry) {
		logger.info("binding finite state machine registry");
		
		this.fsmRegistry = fsmRegistry;
	}

	public void unbindFsmRegistry(BGPv4FSMRegistry fsmRegistry) {
		logger.info("binding finite state machine registry");

		this.fsmRegistry = null;
	}
}
