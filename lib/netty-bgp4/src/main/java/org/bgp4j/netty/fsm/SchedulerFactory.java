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
 * File: org.bgp4j.netty.fsm.SchedulerFactory.java 
 */
package org.bgp4j.netty.fsm;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.bgp4j.weld.ApplicationBootstrapEvent;
import org.bgp4j.weld.ApplicationShutdownEvent;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
@Singleton
public class SchedulerFactory {

	private Scheduler scheduler;
	
	public SchedulerFactory() throws SchedulerException {
		StdSchedulerFactory factory = new StdSchedulerFactory();
		
		factory.initialize();
		scheduler = factory.getScheduler();
	}
	
	@Produces Scheduler producerScheduler()  {
		return this.scheduler;
	}
	
	public void startScheduler(@Observes ApplicationBootstrapEvent event) throws SchedulerException {
		scheduler.start();
	}
	
	public void stopScheduler(@Observes ApplicationShutdownEvent event) throws SchedulerException {
		scheduler.shutdown();
	}
}
