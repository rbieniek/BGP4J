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
 * File: org.bgp4j.netty.fsm.FireEventTimeManagerTest.java 
 */
package org.bgp4j.netty.fsm;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.bgp4j.weld.WeldTestCaseBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class FireEventTimeManagerTest extends WeldTestCaseBase {

	public static class SimpleCaught {
		private boolean caught = false;
		
		public void caught() {
			caught = true;
		}

		/**
		 * @return the caught
		 */
		public boolean isCaught() {
			return caught;
		}
	}
	
	public static class MockFireEventTimeJob extends FireEventTimeJob {
		public static final String MOCK_KEY = "mock-key";
		
		public MockFireEventTimeJob() {
			super(null);
		}

		/* (non-Javadoc)
		 * @see org.bgp4j.netty.fsm.FireEventTimeJob#execute(org.quartz.JobExecutionContext)
		 */
		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
			((SimpleCaught)context.getMergedJobDataMap().get(MOCK_KEY)).caught();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Before
	public void before() throws Exception {
		simpleCaught = new SimpleCaught();
		fsm = obtainInstance(InternalFSM.class);
		manager = obtainInstance(FireEventTimeManager.class);
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put(MockFireEventTimeJob.MOCK_KEY, simpleCaught);
		
		manager.createJobDetail(MockFireEventTimeJob.class, fsm, map);
	}
	
	@After
	public void after() throws Exception {
		manager.shutdown();
		manager = null;
		fsm = null;
		simpleCaught = null;
	}
	
	private SimpleCaught simpleCaught;
	private InternalFSM fsm;
	private FireEventTimeManager<MockFireEventTimeJob> manager;
	
	@Test
	public void testTimerFired() throws Exception {
		Assert.assertFalse(simpleCaught.isCaught());
		
		manager.scheduleJob(10);
		Assert.assertNotNull(manager.getFiredWhen());
		Assert.assertTrue(manager.isJobScheduled());

		Thread.sleep(5*1000);
		Assert.assertFalse(simpleCaught.isCaught());
		Thread.sleep(10*1000);

		Assert.assertTrue(simpleCaught.isCaught());
		Assert.assertNull(manager.getFiredWhen());
		Assert.assertFalse(manager.isJobScheduled());

	}

	@Test
	public void testTimerCanceled() throws Exception {
		Assert.assertFalse(simpleCaught.isCaught());
		
		manager.scheduleJob(10);
		Assert.assertNotNull(manager.getFiredWhen());
		Assert.assertTrue(manager.isJobScheduled());
		
		Thread.sleep(5*1000);
		
		Assert.assertFalse(simpleCaught.isCaught());
		manager.cancelJob();
		Assert.assertFalse(manager.isJobScheduled());

		Thread.sleep(10*1000);

		Assert.assertFalse(simpleCaught.isCaught());
		Assert.assertNull(manager.getFiredWhen());
		Assert.assertFalse(manager.isJobScheduled());

	}
}
