package org.bgp4j.rib;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.event.Observes;
import javax.inject.Singleton;

import org.bgp4j.net.AddressFamilyKey;

@Singleton
public class DestroyedEventCatcher {
	
	private Map<String, Integer> pribCounts = new HashMap<String, Integer>();
	private Map<AddressFamilyKey, Integer> ribCounts = new HashMap<AddressFamilyKey, Integer>();
	
	public void peerRibDestroyed(@Observes PeerRoutingInformationBaseDestroyed event) {
		addEvent(pribCounts, event.getPeerName());
	}
	
	public void ribDestroyed(@Observes RoutingInformationBaseDestroyed event) {
		addEvent(ribCounts, event.getAddressFamilyKey());
	}
	
	public int getPRIBCreatedCount(String peerName) {
		return getCount(pribCounts, peerName);
	}
	
	public int getRIBCreatedCount(AddressFamilyKey afk) {
		return getCount(ribCounts, afk);
	}
	
	public int pribSize() {
		return pribCounts.size();
	}
	
	public int ribSize() {
		return ribCounts.size();
	}
	
	public void reset() {
		pribCounts.clear();
		ribCounts.clear();
	}
	
	private <T> void addEvent(Map<T, Integer> counts, T name) {
		int count = 0;
		
		synchronized (counts) {
			if(counts.containsKey(name))
				count = counts.get(name);
			else
				count = 1;
			
			counts.put(name, count);			
		}
		
	}
	
	private <T> int getCount(Map<T, Integer> counts, T peerName) {
		synchronized (counts) {
			if(counts.containsKey(peerName))
				return counts.get(peerName);
			else
				return 0;			
		}		
	}
}