/**
 * 
 */
package org.bgp4j.netty.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.New;
import javax.inject.Inject;

import org.bgp4j.netty.BGPv4Configuration;
import org.bgp4j.netty.BGPv4PeerConfiguration;
import org.bgp4j.netty.PeerConfigurationChangedListener;
import org.slf4j.Logger;


/**
 * @author rainer
 *
 */
public class BGPv4Service {
	
	private class ConfigurationChangeListener implements PeerConfigurationChangedListener {

		@Override
		public void peerAdded(BGPv4PeerConfiguration peerConfiguration) {
			BGPv4Client client = clientProvider.get();
			
			client.setPeerConfiguration(peerConfiguration);
			peerInstances.put(client.getClientUuid(), client);

			client.startClient();
		}

		@Override
		public void peerRemoved(BGPv4PeerConfiguration peerConfiguration) {
			synchronized (scheduledReconnectInstances) {
				List<ReconnectSchedule> removedInstances = new LinkedList<ReconnectSchedule>();
				
				for(ReconnectSchedule schedule : scheduledReconnectInstances) {
					if(schedule.getClient().getPeerConfiguration().equals(peerConfiguration))
						removedInstances.add(schedule);
				}
				
				for(ReconnectSchedule schedule : removedInstances) {
					scheduledReconnectInstances.remove(schedule);
				}
			}
		
			List<String> removedClientIds = new LinkedList<String>();
			
			for(String clientUuid : peerInstances.keySet()) {
				if(peerInstances.get(clientUuid).getPeerConfiguration().equals(peerConfiguration))
					removedClientIds.add(clientUuid);
			}
			
			for(String clientUuid : removedClientIds) {
				peerInstances.get(clientUuid).stopClient();
				peerInstances.remove(clientUuid);
			}
		}		
	}
	
	private class ReconnectClientTask extends TimerTask {

		@Override
		public void run() {
			synchronized (scheduledReconnectInstances) {
				long stamp = System.currentTimeMillis();
				List<ReconnectSchedule> dueInstances = new LinkedList<ReconnectSchedule>();
				
				for(ReconnectSchedule schedule : scheduledReconnectInstances) {
					if(stamp > schedule.getRescheduleWhen()) {
						dueInstances.add(schedule);
					}
				}
				
				for(ReconnectSchedule schedule : dueInstances) {
					BGPv4Client client = schedule.getClient();
					
					if(!peerConnectionRegistry.isPeerRegistered(client.getPeerConfiguration().getRemotePeerAddress())) {
						log.info("attempt to reconnect client " + client.getClientUuid());
						client.startClient();
					}
					
					scheduledReconnectInstances.remove(schedule);
				}
			}
		}

	}
	
	private @Inject Logger log;

	private @Inject @New Instance<BGPv4Client> clientProvider;
	private @Inject Instance<BGPv4Server> serverProvider;
	private @Inject PeerConnectionRegistry peerConnectionRegistry;
	
	private Map<String, BGPv4Client> peerInstances = new HashMap<String, BGPv4Client>();
	private BGPv4Server serverInstance;
	private List<ReconnectSchedule> scheduledReconnectInstances = new LinkedList<ReconnectSchedule>();
	private Timer timer = new Timer(true);

	/**
	 * start the service
	 * 
	 * @param configuration the initial service configuration
	 */
	public void startService(BGPv4Configuration configuration) {
		configuration.addListener(new ConfigurationChangeListener());
		
		timer.scheduleAtFixedRate(new ReconnectClientTask(), 5000L, 5000L);
		
		if(configuration.getBgpv4Server() != null) {
			log.info("starting local BGPv4 server");
			
			this.serverInstance = serverProvider.get();
			serverInstance.setConfiguration(configuration);
			
			serverInstance.startServer();
		}
		
		for(BGPv4PeerConfiguration peerConfiguration : configuration.getPeers()) {
			BGPv4Client client = clientProvider.get();
			
			client.setPeerConfiguration(peerConfiguration);
			peerInstances.put(client.getClientUuid(), client);

			client.startClient();
		}
	}

	/**
	 * stop the running service
	 * 
	 */
	public void stopService() {
		if(serverInstance != null)
			serverInstance.stopServer();

		List<String> clientUuids = new LinkedList<String>(peerInstances.keySet());
		
		for(String clientUuid : clientUuids) {
			BGPv4Client client = peerInstances.remove(clientUuid);
			
			client.stopClient();
		}
		
	}
	
	public void handleReconnectNeeded(@Observes ClientNeedReconnectEvent event) {
		if(peerInstances.containsKey(event.getClientUuid())) {
			BGPv4Client peerInstance = peerInstances.get(event.getClientUuid()); 
			
			this.scheduledReconnectInstances.add(new ReconnectSchedule(peerInstance));
		}
	}
}
