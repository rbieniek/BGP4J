/**
 *  Copyright 2012 Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * 
 * File: org.bgp4j.netty.fsm.OutboundRoutingUpdateQueue.java 
 */
package org.bgp4j.netty.fsm;

import java.net.Inet4Address;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.BinaryNextHop;
import org.bgp4j.net.InetAddressNextHop;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.NextHop;
import org.bgp4j.net.SubsequentAddressFamily;
import org.bgp4j.net.attributes.MultiProtocolReachableNLRI;
import org.bgp4j.net.attributes.MultiProtocolUnreachableNLRI;
import org.bgp4j.net.attributes.NextHopPathAttribute;
import org.bgp4j.net.attributes.PathAttribute;
import org.bgp4j.netty.BGPv4Constants;
import org.bgp4j.netty.NLRICodec;
import org.bgp4j.netty.protocol.update.UpdatePacket;
import org.bgp4j.rib.RIBSide;
import org.bgp4j.rib.RouteAdded;
import org.bgp4j.rib.RouteWithdrawn;
import org.bgp4j.rib.RoutingEventListener;
import org.bgp4j.rib.RoutingInformationBaseVisitor;
import org.bgp4j.rib.TopologicalTreeSortingKey;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class OutboundRoutingUpdateQueue implements RoutingEventListener {

	public static class BatchJob implements Job {

		public static final String CALLBACK_KEY = "Callback";
		public static final String QUEUE_KEY = "Queue";
		
		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
			((OutboundRoutingUpdateCallback)context.getMergedJobDataMap().get(CALLBACK_KEY))
				.sendUpdates(((OutboundRoutingUpdateQueue)context.getMergedJobDataMap().get(QUEUE_KEY))
						.buildUpdates());
		}
		
	}
	
	private class QueueingVisitor implements RoutingInformationBaseVisitor {

		@Override
		public void visitRouteNode(String ribName, AddressFamilyKey afk, RIBSide side, NetworkLayerReachabilityInformation nlri, 
				NextHop nextHop, Collection<PathAttribute> pathAttributes) {
			addRoute(ribName, afk, side, nlri, nextHop, pathAttributes);
		}
		
	}
	
	private OutboundRoutingUpdateCallback callback;
	private String peerName;
	private Set<AddressFamilyKey> updateMask;
	private boolean active;
	private Map<TopologicalTreeSortingKey, List<NetworkLayerReachabilityInformation>> addedRoutes = 
			new TreeMap<TopologicalTreeSortingKey, List<NetworkLayerReachabilityInformation>>();
	private Map<AddressFamilyKey, List<NetworkLayerReachabilityInformation>> withdrawnRoutes =
			new TreeMap<AddressFamilyKey, List<NetworkLayerReachabilityInformation>>();
	private @Inject Scheduler scheduler;
	private JobDetail jobDetail;
	private TriggerKey triggerKey;
	private JobKey jobKey;
	

	RoutingInformationBaseVisitor getImportVisitor() {
		return new QueueingVisitor();
	}
	
	public void routeAdded(RouteAdded event) {
		if(active && event.getSide() == RIBSide.Local && StringUtils.equals(event.getPeerName(), peerName) && updateMask.contains(event.getAddressFamilyKey())) {
			addRoute(peerName, event.getAddressFamilyKey(), event.getSide(), event.getNlri(), event.getNextHop(), event.getPathAttributes());
		}
	}
	
	public void routeWithdrawn(RouteWithdrawn event) {
		if(active && event.getSide() == RIBSide.Local && StringUtils.equals(event.getPeerName(), peerName) && updateMask.contains(event.getAddressFamilyKey())) {
			withdrawRoute(peerName, event.getAddressFamilyKey(), event.getSide(), event.getNlri());
		}
	}

	/**
	 * @return the peerName
	 */
	String getPeerName() {
		return peerName;
	}
	
	/**
	 * @param peerName the peerName to set
	 */
	void setPeerName(String peerName) {
		this.peerName = peerName;
	}
	
	/**
	 * @return the updateMask
	 */
	Set<AddressFamilyKey> getUpdateMask() {
		return updateMask;
	}
	
	/**
	 * @param updateMask the updateMask to set
	 */
	void setUpdateMask(Set<AddressFamilyKey> updateMask) {
		this.updateMask = updateMask;
	}
	
	/**
	 * @return the active
	 */
	boolean isActive() {
		return active;
	}
	
	void shutdown() throws SchedulerException {
		active = false;
		cancelJob();
		synchronized (addedRoutes) {
			addedRoutes.clear();
		}
	};
	
	void startSendingUpdates(int repeatInterval) throws SchedulerException {
		if(repeatInterval > 0) {
			if(isJobScheduled())
				cancelJob();

			JobDataMap map = new JobDataMap();

			map.put(BatchJob.CALLBACK_KEY, callback);
			map.put(BatchJob.QUEUE_KEY, this);
			
			jobKey = new JobKey(UUID.randomUUID().toString());
			jobDetail = JobBuilder.newJob(BatchJob.class).usingJobData(map).withIdentity(jobKey).build();
			triggerKey = TriggerKey.triggerKey(UUID.randomUUID().toString());

			scheduler.scheduleJob(jobDetail, TriggerBuilder.newTrigger()
					.withIdentity(triggerKey)
					.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(repeatInterval))
					.startAt(new Date(System.currentTimeMillis() + repeatInterval*1000L))
					.build());
		}
		active = true;
	}

	/**
	 * @param callback the callback to set
	 */
	void setCallback(OutboundRoutingUpdateCallback callback) {
		this.callback = callback;
	}

	@SuppressWarnings("unchecked")
	private void addRoute(String ribName, AddressFamilyKey afk, RIBSide side, NetworkLayerReachabilityInformation nlri,  
			NextHop nextHop, Collection<PathAttribute> pathAttributes) {
		TopologicalTreeSortingKey key ;
		Collection<PathAttribute> keyAttributes;
		
		if(afk.matches(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)) {
			// handle non-MP  IPv4 case
			keyAttributes = filterAttribute(pathAttributes, Arrays.asList(NextHopPathAttribute.class));
			keyAttributes.add(new NextHopPathAttribute((InetAddressNextHop<Inet4Address>)nextHop));
		} else {
			// handle any other case
			keyAttributes = filterAttribute(pathAttributes, Arrays.asList(MultiProtocolReachableNLRI.class, MultiProtocolReachableNLRI.class));
			keyAttributes.add(new MultiProtocolReachableNLRI(afk.getAddressFamily(), afk.getSubsequentAddressFamily(), (BinaryNextHop)nextHop));
		}
		
		key = new TopologicalTreeSortingKey(afk, keyAttributes);
		
		synchronized (addedRoutes) {
			if(!addedRoutes.containsKey(key))
				addedRoutes.put(key, new LinkedList<NetworkLayerReachabilityInformation>());
			addedRoutes.get(key).add(nlri);
		}
	}

	private void withdrawRoute(String ribName, AddressFamilyKey addressFamilyKey, RIBSide side, NetworkLayerReachabilityInformation nlri) {
		// remove the NLRI from any scheduled route add updates
		synchronized (addedRoutes) {
			Set<TopologicalTreeSortingKey> removeableKeys = new HashSet<TopologicalTreeSortingKey>();
			
			for(TopologicalTreeSortingKey key : addedRoutes.keySet()) {
				if(key.getAddressFamilyKey().equals(addressFamilyKey)) {
					addedRoutes.get(key).remove(nlri);
					
					if(addedRoutes.get(key).size() == 0)
						removeableKeys.add(key);
				}
			}
			
			for(TopologicalTreeSortingKey key : removeableKeys)
				addedRoutes.remove(key);
		}
		
		synchronized (withdrawnRoutes) {
			if(!withdrawnRoutes.containsKey(addressFamilyKey))
				withdrawnRoutes.put(addressFamilyKey, new LinkedList<NetworkLayerReachabilityInformation>());
			
			withdrawnRoutes.get(addressFamilyKey).add(nlri);
		}
	}

	private Collection<PathAttribute> filterAttribute(Collection<PathAttribute> source, 
			Collection<? extends Class<? extends PathAttribute>> filteredClasses) {
		LinkedList<PathAttribute> result = new LinkedList<PathAttribute>();
		
		for(PathAttribute pa : source) {
			if(!filteredClasses.contains(pa.getClass()))
				result.add(pa);
		}
		
		return result;
	}

	List<UpdatePacket> buildUpdates() {
		List<UpdatePacket> updates = new LinkedList<UpdatePacket>();
		UpdatePacket current = null;
		
		synchronized (addedRoutes) {
			for(Entry<TopologicalTreeSortingKey, List<NetworkLayerReachabilityInformation>> addedRouteEntry : addedRoutes.entrySet()) {
				TopologicalTreeSortingKey key = addedRouteEntry.getKey();
				List<NetworkLayerReachabilityInformation> nlris = addedRouteEntry.getValue();
				MultiProtocolReachableNLRI mpNLRI = null;
				
				if(!key.getAddressFamilyKey().matches(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)) {
					for(PathAttribute pathAttribute : key.getPathAttributes()) {
						if(pathAttribute instanceof MultiProtocolReachableNLRI) {
							mpNLRI = (MultiProtocolReachableNLRI)pathAttribute;
							break;
						}
					}
				}
				
				for(NetworkLayerReachabilityInformation nlri: nlris) {
					if((current == null  
							|| (current.calculatePacketSize() + NLRICodec.calculateEncodedNLRILength(nlri) 
									> (BGPv4Constants.BGP_PACKET_MAX_LENGTH - BGPv4Constants.BGP_PACKET_HEADER_LENGTH)))) {
						current = new UpdatePacket();
						current.getPathAttributes().addAll(key.getPathAttributes());
						updates.add(current);
					}
					
					if(key.getAddressFamilyKey().matches(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)) {					
						current.getNlris().add(nlri);
					} else {
						mpNLRI.getNlris().add(nlri);
					}
				}
				
				current = null;
			}
			
			addedRoutes.clear();
		}
		
		synchronized (withdrawnRoutes) {
			for(Entry<AddressFamilyKey, List<NetworkLayerReachabilityInformation>> withdrawnRouteEntry : withdrawnRoutes.entrySet()) {
				for(NetworkLayerReachabilityInformation nlri : withdrawnRouteEntry.getValue()) {
					MultiProtocolUnreachableNLRI mpUnreachable = null;
					AddressFamilyKey afk = withdrawnRouteEntry.getKey();
					
					if((current == null  
							|| (current.calculatePacketSize() + NLRICodec.calculateEncodedNLRILength(nlri) 
									> (BGPv4Constants.BGP_PACKET_MAX_LENGTH - BGPv4Constants.BGP_PACKET_HEADER_LENGTH)))) {
						current = new UpdatePacket();
						
						if(!afk.equals(AddressFamilyKey.IPV4_UNICAST_FORWARDING)) {
							mpUnreachable = new MultiProtocolUnreachableNLRI(afk.getAddressFamily(), afk.getSubsequentAddressFamily());
							
							current.getPathAttributes().add(mpUnreachable);
						}
						updates.add(current);
					}
					
					if(afk.matches(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)) {					
						current.getWithdrawnRoutes().add(nlri);
					} else {
						mpUnreachable.getNlris().add(nlri);
					}
				}
				current = null;
			}
			
			withdrawnRoutes.clear();
		}
		
		return updates;
	}
	
	int getNumberOfPendingUpdates() {
		int result = 0;
		
		synchronized (addedRoutes) {
			result += addedRoutes.size();
		}
		
		synchronized (withdrawnRoutes) {
			result += withdrawnRoutes.size();
		}

		return result;
	}
	
	boolean isJobScheduled() throws SchedulerException {
		if(triggerKey == null)
			return false;
		return scheduler.checkExists(triggerKey);
	}

	public Date getNextFireWhen() throws SchedulerException {
		if(!isJobScheduled())
			return null;
		
		return scheduler.getTrigger(triggerKey).getFireTimeAfter(new Date(System.currentTimeMillis()));
	}
	
	void cancelJob() throws SchedulerException {
		if(triggerKey != null) {
			scheduler.unscheduleJob(triggerKey);
			triggerKey = null;
		}
	}

}
