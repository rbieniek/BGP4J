package org.bgp4j.rib;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.event.Observes;
import javax.inject.Singleton;

@Singleton
public class CreatedEventCatcher {
	
	private Map<String, Integer> pribCounts = new HashMap<String, Integer>();
	
	public void peerRibCreated(@Observes PeerRoutingInformationBaseCreated event) {
		addEvent(pribCounts, event.getPeerName());
	}
	
	public int getPRIBCreatedCount(String peerName) {
		return getCount(pribCounts, peerName);
	}
	
	public int pribSize() {
		return pribCounts.size();
	}
	
	public void reset() {
		pribCounts.clear();
	}
	
	private void addEvent(Map<String, Integer> counts, String name) {
		int count = 0;
		
		synchronized (counts) {
			if(counts.containsKey(name))
				count = counts.get(name);
			else
				count = 1;
			
			counts.put(name, count);			
		}
		
	}
	
	private int getCount(Map<String, Integer> counts, String peerName) {
		synchronized (counts) {
			if(counts.containsKey(peerName))
				return counts.get(peerName);
			else
				return 0;			
		}		
	}
}