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
 * File: org.bgp4j.netty.fsm.InternalFSMTest.java 
 */
package org.bgp4j.netty.fsm;

import java.util.Date;

import junit.framework.Assert;

import org.apache.commons.configuration.XMLConfiguration;
import org.bgp4.config.Configuration;
import org.bgp4.config.ConfigurationParser;
import org.bgp4j.weld.WeldTestCaseBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class InternalFSMTest extends WeldTestCaseBase {

	@Before
	public void before() {
		fsm = obtainInstance(InternalFSM.class);
		callbacks = mock(InternalFSMCallbacks.class);
		parser = obtainInstance(ConfigurationParser.class);
	}
	
	@After
	public void after() {
		fsm = null;
		callbacks = null;
		parser = null;
	}
	
	private InternalFSM fsm;
	private InternalFSMCallbacks callbacks;
	private ConfigurationParser parser;

	@Test
	public void testManualStartEventInActiveMode() throws Exception {
		fsm.setup(loadConfiguration("org/bgp4j/netty/fsm/Config-With-BgpPeers.xml").getPeer("peer1"), callbacks);

		Assert.assertEquals(FSMState.Idle, fsm.getState());
		fsm.handleEvent(FSMEvent.ManualStart);
		
		verify(callbacks).fireConnectRemotePeer();
		Assert.assertEquals(FSMState.Connect, fsm.getState());
		Assert.assertEquals(0, fsm.getConnectRetryCounter());
		Assert.assertTrue(fsm.isConnectRetryTimerRunning());
		Assert.assertNotNull(fsm.getConnectRetryTimerDueWhen());
	}
	
	@Test
	public void testAutomaticStartEventInActiveMode() throws Exception {
		fsm.setup(loadConfiguration("org/bgp4j/netty/fsm/Config-With-BgpPeers.xml").getPeer("peer1"), callbacks);

		Assert.assertEquals(FSMState.Idle, fsm.getState());
		fsm.handleEvent(FSMEvent.AutomaticStart);
		
		verify(callbacks).fireConnectRemotePeer();
		Assert.assertEquals(FSMState.Connect, fsm.getState());
		Assert.assertEquals(0, fsm.getConnectRetryCounter());
		Assert.assertTrue(fsm.isConnectRetryTimerRunning());
		Assert.assertNotNull(fsm.getConnectRetryTimerDueWhen());
	}
	
	@Test
	public void testManualStartEventInPassiveMode() throws Exception {
		fsm.setup(loadConfiguration("org/bgp4j/netty/fsm/Config-With-BgpPeers.xml").getPeer("peer2"), callbacks);

		Assert.assertEquals(FSMState.Idle, fsm.getState());
		fsm.handleEvent(FSMEvent.ManualStart);
		
		verify(callbacks, never()).fireConnectRemotePeer();
		Assert.assertEquals(FSMState.Connect, fsm.getState());
		Assert.assertEquals(0, fsm.getConnectRetryCounter());
		Assert.assertFalse(fsm.isConnectRetryTimerRunning());
		Assert.assertNull(fsm.getConnectRetryTimerDueWhen());
	}
	
	@Test
	public void testAutomaticStartEventInPassiveMode() throws Exception {
		fsm.setup(loadConfiguration("org/bgp4j/netty/fsm/Config-With-BgpPeers.xml").getPeer("peer2"), callbacks);

		Assert.assertEquals(FSMState.Idle, fsm.getState());
		fsm.handleEvent(FSMEvent.AutomaticStart);
		
		verify(callbacks, never()).fireConnectRemotePeer();
		Assert.assertEquals(FSMState.Connect, fsm.getState());
		Assert.assertEquals(0, fsm.getConnectRetryCounter());
		Assert.assertFalse(fsm.isConnectRetryTimerRunning());
		Assert.assertNull(fsm.getConnectRetryTimerDueWhen());
	}

	@Test
	public void testStartEventStopEvent() throws Exception {
		fsm.setup(loadConfiguration("org/bgp4j/netty/fsm/Config-With-BgpPeers.xml").getPeer("peer1"), callbacks);

		Assert.assertEquals(FSMState.Idle, fsm.getState());
		fsm.handleEvent(FSMEvent.AutomaticStart);
		
		verify(callbacks).fireConnectRemotePeer();
		Assert.assertEquals(FSMState.Connect, fsm.getState());
		Assert.assertEquals(0, fsm.getConnectRetryCounter());
		Assert.assertTrue(fsm.isConnectRetryTimerRunning());
		Assert.assertNotNull(fsm.getConnectRetryTimerDueWhen());
		
		fsm.handleEvent(FSMEvent.AutomaticStop);
		Assert.assertEquals(FSMState.Idle, fsm.getState());
		Assert.assertEquals(0, fsm.getConnectRetryCounter());
		Assert.assertFalse(fsm.isConnectRetryTimerRunning());
		Assert.assertNull(fsm.getConnectRetryTimerDueWhen());
		verify(callbacks).fireDisconnectRemotePeer();
	}

	@Test
	public void testStartEventInActiveModeConnectRetryTimerExpiresNoPeerDampening() throws Exception {
		fsm.setup(loadConfiguration("org/bgp4j/netty/fsm/Config-With-BgpPeers.xml").getPeer("peer1"), callbacks);

		Assert.assertEquals(FSMState.Idle, fsm.getState());
		fsm.handleEvent(FSMEvent.AutomaticStart);
		
		verify(callbacks).fireConnectRemotePeer();
		Assert.assertEquals(FSMState.Connect, fsm.getState());
		Assert.assertEquals(0, fsm.getConnectRetryCounter());
		Assert.assertTrue(fsm.isConnectRetryTimerRunning());
		Assert.assertNotNull(fsm.getConnectRetryTimerDueWhen());
		
		conditionalSleep(fsm.getConnectRetryTimerDueWhen());
		
		verify(callbacks).fireDisconnectRemotePeer();
		verify(callbacks, times(2)).fireConnectRemotePeer();
		Assert.assertEquals(FSMState.Connect, fsm.getState());
		Assert.assertEquals(1, fsm.getConnectRetryCounter());
		Assert.assertTrue(fsm.isConnectRetryTimerRunning());
		Assert.assertNotNull(fsm.getConnectRetryTimerDueWhen());
		Assert.assertFalse(fsm.isIdleHoldTimerRunning());
		Assert.assertNull(fsm.getIdleHoldTimerDueWhen());
		
	}

	@Test
	public void testStartEventInActiveModeConnectRetryTimerExpiresPeerDampening() throws Exception {
		fsm.setup(loadConfiguration("org/bgp4j/netty/fsm/Config-With-BgpPeers.xml").getPeer("peer3"), callbacks);

		Assert.assertEquals(FSMState.Idle, fsm.getState());
		fsm.handleEvent(FSMEvent.AutomaticStart);
		
		verify(callbacks).fireConnectRemotePeer();
		Assert.assertEquals(FSMState.Connect, fsm.getState());
		Assert.assertEquals(0, fsm.getConnectRetryCounter());
		Assert.assertTrue(fsm.isConnectRetryTimerRunning());
		Assert.assertNotNull(fsm.getConnectRetryTimerDueWhen());
		
		conditionalSleep(fsm.getConnectRetryTimerDueWhen());
		
		verify(callbacks).fireDisconnectRemotePeer(); // from connect retry timer expired handler
		verify(callbacks).fireConnectRemotePeer(); // from initial connect
		Assert.assertEquals(FSMState.Connect, fsm.getState());
		Assert.assertEquals(0, fsm.getConnectRetryCounter());
		Assert.assertFalse(fsm.isConnectRetryTimerRunning());
		Assert.assertNull(fsm.getConnectRetryTimerDueWhen());
		Assert.assertTrue(fsm.isIdleHoldTimerRunning());
		Assert.assertNotNull(fsm.getIdleHoldTimerDueWhen());

		conditionalSleep(fsm.getIdleHoldTimerDueWhen());
	
		verify(callbacks, times(2)).fireConnectRemotePeer(); // from idle hold timer expired handler
		Assert.assertEquals(FSMState.Connect, fsm.getState());
		Assert.assertEquals(1, fsm.getConnectRetryCounter());
		Assert.assertTrue(fsm.isConnectRetryTimerRunning());
		Assert.assertNotNull(fsm.getConnectRetryTimerDueWhen());
		Assert.assertFalse(fsm.isIdleHoldTimerRunning());
		Assert.assertNull(fsm.getIdleHoldTimerDueWhen());
	}

	private Configuration loadConfiguration(String fileName) throws Exception {
		return parser.parseConfiguration(new XMLConfiguration(fileName));
	}
	
	private void conditionalSleep(Date untilWhen) throws InterruptedException {
		long sleep = (untilWhen.getTime() - System.currentTimeMillis()) + 1000L;
		
		if(sleep > 0)
			Thread.sleep(sleep);
	}
}
