/**
 * 
 */
package org.bgp4j.extension.snmp4j.service.impl;

import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import org.bgp4j.extension.snmp4j.config.nodes.EasyboxConfiguration;
import org.bgp4j.extension.snmp4j.config.nodes.SnmpConfiguration;
import org.bgp4j.extension.snmp4j.service.EasyboxInstance;
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
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

/**
 * @author rainer
 *
 */
public class EasyboxInstanceImpl implements EasyboxInstance {

	private class UptimeListener implements ResponseListener {

		@Override
		public void onResponse(ResponseEvent event) {

			try {
				PDU response = event.getResponse();
					if(response != null) {
					Variable v = response.get(0).getVariable();
					
					if(v instanceof TimeTicks) {
						TimeTicks ticks = (TimeTicks)v;
						
						uptime = ticks.toMilliseconds();
					}
				} else
					uptime = -1;
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

		pdu.add(new VariableBinding(new OID(new int[] { 1, 3, 6, 1, 2, 1, 1, 3, 0 })));
		pdu.setType(PDU.GET);
		
		try {
			snmp.send(pdu, target, null, new UptimeListener());
		} catch(Exception e) {
			log.error("Cannot send SNMP request", e);
		}
	}

	@Override
	public long getUptime() {
		return uptime;
	}

}
