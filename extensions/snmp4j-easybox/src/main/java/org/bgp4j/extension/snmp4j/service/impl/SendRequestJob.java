/**
 * 
 */
package org.bgp4j.extension.snmp4j.service.impl;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author rainer
 *
 */
public class SendRequestJob implements Job {

	static final String KEY = "EasyboxInstanceKey";
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		((EasyboxInstanceImpl)context.getMergedJobDataMap().get(KEY)).fireSnmpRequest();
	}

}
