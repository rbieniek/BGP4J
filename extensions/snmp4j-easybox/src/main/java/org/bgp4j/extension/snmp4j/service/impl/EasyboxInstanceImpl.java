/**
 * 
 */
package org.bgp4j.extension.snmp4j.service.impl;

import java.net.Inet4Address;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.bgp4j.extension.snmp4j.config.nodes.EasyboxConfiguration;
import org.bgp4j.extension.snmp4j.config.nodes.SnmpConfiguration;
import org.bgp4j.extension.snmp4j.service.EasyboxInstance;
import org.bgp4j.extension.snmp4j.service.EasyboxInterface;
import org.bgp4j.extension.snmp4j.service.EasyboxInterfaceEvent;
import org.bgp4j.extension.snmp4j.service.EasyboxInterfaceListener;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Counter32;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.UnsignedInteger32;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

/**
 * @author rainer
 *
 */
public class EasyboxInstanceImpl implements EasyboxInstance {

	private static final OID UPTIME_OID = new OID(new int[] { 1, 3, 6, 1, 2, 1, 1, 3, 0 });
	private static final OID IF_PHYS_ADDR_PREFIX = new OID(new int[] { 1, 3, 6, 1, 2, 1, 2, 2, 1, 6 });
	private static final OID IF_DESC_PREFIX = new OID(new int[] {1, 3, 6, 1, 2, 1, 2, 2, 1, 2 });
	private static final OID IF_MTU_PREFIX = new OID(new int[] {1, 3, 6, 1, 2, 1, 2, 2, 1, 4 });
	private static final OID IF_SPEED_PREFIX = new OID(new int[] {1, 3, 6, 1, 2, 1, 2, 2, 1, 5 });
	private static final OID IF_ADMIN_STATUS_PREFIX = new OID(new int[] {1, 3, 6, 1, 2, 1, 2, 2, 1, 7 });
	private static final OID IF_ADMIN_OPER_PREFIX = new OID(new int[] {1, 3, 6, 1, 2, 1, 2, 2, 1, 8 });
	private static final OID IF_IN_OCTETS_PREFIX = new OID(new int[] {1, 3, 6, 1, 2, 1, 2, 2, 1, 10 });
	private static final OID IF_OUT_OCTETS_PREFIX = new OID(new int[] {1, 3, 6, 1, 2, 1, 2, 2, 1, 16 });
	private static final OID IP_NET_TO_MEDIA_PHYS_ADDR_PREFIX = new OID(new int[] { 1, 3, 6, 1, 2, 1, 4, 22, 1, 3 });
	
	private class IfStatusListener implements ResponseListener {

		private Set<Integer> ifIndeces = new HashSet<Integer>();
		List<PDU> pdus = new LinkedList<PDU>();
		
		@Override
		public void onResponse(ResponseEvent event) {
			try {
				PDU response = event.getResponse();

				if(response != null) {
					log.debug("IfStatusListener: received PDU " + response);
					
					for(VariableBinding vb : response.getVariableBindings()) {
						OID oid = vb.getOid();
						
						if(oid.startsWith(IF_DESC_PREFIX)) {
							getInterface(oid.last()).setDescription(((OctetString)vb.getVariable()).toString());
						} else if(oid.startsWith(IF_SPEED_PREFIX)) {
							getInterface(oid.last()).setSpeed(((UnsignedInteger32)vb.getVariable()).toInt());							
						} else if(oid.startsWith(IF_MTU_PREFIX)) {
							getInterface(oid.last()).setMtu(((Integer32)vb.getVariable()).toInt());							
						} else if(oid.startsWith(IF_ADMIN_STATUS_PREFIX)) {
							getInterface(oid.last()).setAdminUp((((Integer32)vb.getVariable()).toInt() > 0));
						} else if(oid.startsWith(IF_ADMIN_OPER_PREFIX)) {
							getInterface(oid.last()).setOperUp((((Integer32)vb.getVariable()).toInt() > 0));
						} else if(oid.startsWith(IF_IN_OCTETS_PREFIX)) {
							getInterface(oid.last()).setOctetsIn((((Counter32)vb.getVariable()).getValue()));
						} else if(oid.startsWith(IF_OUT_OCTETS_PREFIX)) {
							getInterface(oid.last()).setOctetsOut((((Counter32)vb.getVariable()).getValue()));
						} else if(oid.startsWith(IP_NET_TO_MEDIA_PHYS_ADDR_PREFIX)) {
							getInterface(oid.get(IP_NET_TO_MEDIA_PHYS_ADDR_PREFIX.size())).setAddress((Inet4Address)((IpAddress)vb.getVariable()).getInetAddress());
						}
					}
				} else {
					log.info("No response (IfStatusListener) from box " + name);
					
					reset();
				}
			} catch(Exception e) {
				log.error("failed to proocess response " + event.getResponse(), e);
			} finally {
				((Snmp)event.getSource()).cancel(event.getRequest(), this);				
			}
		}

		boolean isQueryNeeded() {
			synchronized (ifIndeces) {
				return (ifIndeces.size() > 0);
			}
		}
		
		void forceQueryNotNeeded() {
			synchronized (ifIndeces) {
				ifIndeces.clear();
				pdus.clear();
			}
		}
		
		List<PDU> getPdus() {
			return pdus;
		}
		
		void setIfIndices(Set<Integer> ifs) {
			synchronized (ifIndeces) {
				ifIndeces.clear();
				
				ifIndeces.addAll(ifs);
				pdus.clear();
				
				for(int ifNum : ifIndeces) {
					PDU pdu;
					
					pdu = new PDU();
					pdu.setType(PDU.GET);
					pdu.add(new VariableBinding((new OID(IF_DESC_PREFIX)).append(ifNum)));
					pdu.add(new VariableBinding((new OID(IF_MTU_PREFIX)).append(ifNum)));
					pdu.add(new VariableBinding((new OID(IF_SPEED_PREFIX)).append(ifNum)));
					pdu.add(new VariableBinding((new OID(IF_ADMIN_STATUS_PREFIX)).append(ifNum)));
					pdu.add(new VariableBinding((new OID(IF_ADMIN_OPER_PREFIX)).append(ifNum)));
					pdu.add(new VariableBinding((new OID(IF_IN_OCTETS_PREFIX)).append(ifNum)));
					pdu.add(new VariableBinding((new OID(IF_OUT_OCTETS_PREFIX)).append(ifNum)));
					
					pdus.add(pdu);
					
					pdu = new PDU();
					pdu.setType(PDU.GETNEXT);
					pdu.add(new VariableBinding((new OID(IP_NET_TO_MEDIA_PHYS_ADDR_PREFIX)).append(ifNum)));
					
					pdus.add(pdu);
				}
			}
		}		
	}
	
	private class IfPhysAddrTableListener implements ResponseListener {

		private Set<Integer> ifIndeces = new HashSet<Integer>();

		@Override
		public void onResponse(ResponseEvent event) {
			try {
				PDU response = event.getResponse();
				
				if(response != null) {
					log.debug("IfPhysAddrTableListener: received PDU " + response);
					
					VariableBinding vb = response.get(0);
					
					if(vb.getOid().startsWith(IF_PHYS_ADDR_PREFIX)) {
						Variable v = vb.getVariable();
						
						if(v instanceof OctetString) {
							String physAddr = StringUtils.replace(((OctetString)v).toHexString(), ":", "");
							
							if(StringUtils.equalsIgnoreCase(physAddr, interfaceMacAddress)) {
								synchronized (ifIndeces) {
									ifIndeces.add(vb.getOid().last());
								}
							}
						}
						PDU pdu = new PDU();
						pdu.add(vb);
						pdu.setType(PDU.GETNEXT);
	
						try {
							snmp.send(pdu, target, null, this);
						} catch(Exception e) {
							log.error("Cannot send SNMP request", e);
						}
					} else {
						// end of interface table reached
						statusListener.setIfIndices(ifIndeces);
						
						for(PDU ipdu : statusListener.getPdus()) {
							
							try {
								snmp.send(ipdu, target, null, statusListener);
							} catch(Exception e) {
								log.error("Cannot send SNMP request", e);
							}
						}
					}
				} else {
					log.info("No response (IfPhysAddrTableListener) from box " + name);
					
					reset();
				}
			} finally {
				((Snmp)event.getSource()).cancel(event.getRequest(), this);				
			}
		}
		
		boolean isDiscoveryNeeded() {
			synchronized (ifIndeces) {
				return (ifIndeces.size() == 0);
			}
		}
		
		void forceDiscoveryNeeded() {
			synchronized (ifIndeces) {
				ifIndeces.clear();
			}
		}
	}
	
	private class UptimeListener implements ResponseListener {

		@Override
		public void onResponse(ResponseEvent event) {

			try {
				PDU response = event.getResponse();
				
				if(response != null) {
					log.debug("UptimeListener: received PDU " + response);
					
					Variable v = response.get(0).getVariable();
					
					if(v instanceof TimeTicks) {
						TimeTicks ticks = (TimeTicks)v;
						
						uptime = ticks.toMilliseconds();
					}
				} else {
					log.info("No response (UptimeListener) from box " + name);
					
					reset();
				}
			} finally {
				((Snmp)event.getSource()).cancel(event.getRequest(), this);
			}
		}
		
	}
	
	private String name;
	private SnmpConfiguration config;
	private String interfaceMacAddress;
	private CommunityTarget target;

	private @Inject @EasyboxContext Scheduler scheduler;
	private @Inject Logger log;
	private JobDetail jobDetail;
	private TriggerKey triggerKey;
	private JobKey jobKey;

	private Snmp snmp;
	private long uptime;
	private Map<Integer, EasyboxInterfaceImpl> interfaces = new HashMap<Integer, EasyboxInterfaceImpl>();
	private EasyboxInterface activeInterface;
	private List<EasyboxInterfaceListener> listeners = new LinkedList<EasyboxInterfaceListener>();
	
	private IfPhysAddrTableListener physAddrTableListener = new IfPhysAddrTableListener();
	private UptimeListener upTimerListener = new UptimeListener();
	private IfStatusListener statusListener = new IfStatusListener();
	
	@Override
	public String getName() {
		return name;
	}

	void startInstance() throws Exception {
		target = new CommunityTarget(new UdpAddress(config.getTargetAddress(), 161), new OctetString(config.getCommunity()));
		target.setRetries(2);
		target.setTimeout(1000);
		target.setVersion(SnmpConstants.version2c);
		
		TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping(new UdpAddress(config.getLocalPort()));

		snmp = new Snmp(transport);
		transport.listen();
		
		JobDataMap map = new JobDataMap();
		
		map.put(SendRequestJob.KEY, this);

		jobKey = new JobKey(UUID.randomUUID().toString());
		jobDetail = JobBuilder.newJob(SendRequestJob.class).usingJobData(map).withIdentity(jobKey).build();		

		triggerKey = TriggerKey.triggerKey(UUID.randomUUID().toString());

		scheduler.scheduleJob(jobDetail, TriggerBuilder.newTrigger()
		.withIdentity(triggerKey)
		.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(15))
		.startAt(new Date(System.currentTimeMillis() + 15*1000L))
		.build());
	}

	void stopInstance() throws Exception {
		if(triggerKey != null) {
			scheduler.unscheduleJob(triggerKey);
			triggerKey = null;
		}
	}

	void configure(EasyboxConfiguration config) {
		this.name = config.getName();
		this.config = config.getSnmpConfiguration();
		this.interfaceMacAddress = config.getInterfaceMacAddress();
	}

	void fireSnmpRequest() {
		
		PDU pdu = new PDU();

		pdu.add(new VariableBinding(UPTIME_OID));
		pdu.setType(PDU.GET);
		
		try {
			snmp.send(pdu, target, null, upTimerListener);
		} catch(Exception e) {
			log.error("Cannot send SNMP request", e);
		}
		
		if(physAddrTableListener.isDiscoveryNeeded()) {
			// if we don't have the interface indeces yet, start discovery process			
			pdu = new PDU();
			pdu.add(new VariableBinding(IF_PHYS_ADDR_PREFIX));
			pdu.setType(PDU.GETNEXT);
	
			try {
				snmp.send(pdu, target, null, physAddrTableListener);
			} catch(Exception e) {
				log.error("Cannot send SNMP request", e);
			}
		}
		if(statusListener.isQueryNeeded()) {
			for(PDU ipdu : statusListener.getPdus()) {
				
				try {
					snmp.send(ipdu, target, null, statusListener);
				} catch(Exception e) {
					log.error("Cannot send SNMP request", e);
				}
			}
		}

		boolean hasActive= false;
		
		for(EasyboxInterface ifp : getInterfaces()) {
			if(ifp.isAdminUp() && ifp.isOperUp()) {
				hasActive = true;
				
				if(ifp.isChanged(activeInterface)) {
					EasyboxInterfaceEvent event = new EasyboxInterfaceEventImpl(activeInterface , ifp);
					
					for(EasyboxInterfaceListener listener : listeners) {
						try {
							listener.interfaceChanged(this, event);
						} catch(Exception e) {
							log.error("failed to send interface event to listener", e);
						}
					}
					
					activeInterface = new EasyboxInterfaceImpl(ifp);
					break;
				}
			}
		}
		
		if(!hasActive && activeInterface != null) {
			EasyboxInterfaceEvent event = new EasyboxInterfaceEventImpl(activeInterface , null);
			
			for(EasyboxInterfaceListener listener : listeners) {
				try {
					listener.interfaceChanged(this, event);
				} catch(Exception e) {
					log.error("failed to send interface event to listener", e);
				}
			}
			
			activeInterface = null;
		}
	}

	private void reset() {
		uptime = -1;
		
		physAddrTableListener.forceDiscoveryNeeded();
		statusListener.forceQueryNotNeeded();
	}
	
	@Override
	public long getUptime() {
		return uptime;
	}

	private EasyboxInterfaceImpl getInterface(int ifNum) {
		synchronized (interfaces) {
			if(!interfaces.containsKey(ifNum))
				interfaces.put(ifNum, new EasyboxInterfaceImpl());
			
			return interfaces.get(ifNum);
		}
	}
	
	@Override
	public List<EasyboxInterface> getInterfaces() {
		List<EasyboxInterface> result = new LinkedList<EasyboxInterface>();
		
		synchronized (interfaces) {
			for(Entry<Integer, EasyboxInterfaceImpl> entry : interfaces.entrySet()) {
				if(entry.getValue().isAdminUp())
					result.add(entry.getValue());
			}
		}
		
		return Collections.unmodifiableList(result);
	}

	/**
	 * @return the activeInterface
	 */
	@Override
	public EasyboxInterface getActiveInterface() {
		return activeInterface;
	}

	@Override
	public void addInterfaceListener(EasyboxInterfaceListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeInterfaceListener(EasyboxInterfaceListener listener) {
		listeners.remove(listener);
	}

}
