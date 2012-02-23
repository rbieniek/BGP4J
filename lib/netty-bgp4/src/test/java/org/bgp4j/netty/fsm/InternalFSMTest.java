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
import org.bgp4j.netty.FSMState;
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
		fsm.destroyFSM();
		
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
		fsm.handleEvent(FSMEvent.manualStart());
		
		Assert.assertEquals(0, fsm.getConnectRetryCounter());

		verify(callbacks).fireConnectRemotePeer();
		assertMachineInConnectState(false);
		Assert.assertEquals(0, fsm.getConnectRetryCounter());
	}
	
	
	@Test
	public void testAutomaticStartEventInActiveModeWithoutEvent() throws Exception {
		fsm.setup(loadConfiguration("org/bgp4j/netty/fsm/Config-With-BgpPeers.xml").getPeer("peer1"), callbacks);

		Assert.assertEquals(FSMState.Idle, fsm.getState());
		Assert.assertTrue(fsm.isAutomaticStartRunning());
		Assert.assertNotNull(fsm.getAutomaticStartDueWhen());
		
		conditionalSleep(fsm.getAutomaticStartDueWhen());
		
		verify(callbacks).fireConnectRemotePeer();
		assertMachineInConnectState(false);
		Assert.assertEquals(0, fsm.getConnectRetryCounter());
	}

	@Test
	public void testAutomaticStartEventInPassiveModeWithoutEvent() throws Exception {
		fsm.setup(loadConfiguration("org/bgp4j/netty/fsm/Config-With-BgpPeers.xml").getPeer("peer2"), callbacks);

		Assert.assertEquals(FSMState.Idle, fsm.getState());
		Assert.assertTrue(fsm.isAutomaticStartRunning());
		Assert.assertNotNull(fsm.getAutomaticStartDueWhen());
		
		conditionalSleep(fsm.getAutomaticStartDueWhen());
		
		verify(callbacks, never()).fireConnectRemotePeer();
		Assert.assertEquals(0, fsm.getConnectRetryCounter());
		assertMachineInActiveState(false);
	}

	@Test
	public void testAutomaticStartEventInActiveMode() throws Exception {
		initializeFSMToConnectState("peer1");
		assertMachineInConnectState(false);
	}
	
	@Test
	public void testManualStartEventInPassiveMode() throws Exception {
		fsm.setup(loadConfiguration("org/bgp4j/netty/fsm/Config-With-BgpPeers.xml").getPeer("peer2"), callbacks);

		Assert.assertEquals(FSMState.Idle, fsm.getState());
		fsm.handleEvent(FSMEvent.manualStart());
		
		verify(callbacks, never()).fireConnectRemotePeer();
		Assert.assertEquals(0, fsm.getConnectRetryCounter());
		assertMachineInActiveState(false);
	}
	
	@Test
	public void testAutomaticStartEventInPassiveMode() throws Exception {
		initializeFSMToActiveState("peer2");
		assertMachineInActiveState(false);
	}

	@Test
	public void testStartEventStopEvent() throws Exception {
		initializeFSMToConnectState("peer1");
		
		fsm.handleEvent(FSMEvent.automaticStop());

		Assert.assertEquals(0, fsm.getConnectRetryCounter());
		assertMachineInIdleState(false);
	}

	@Test
	public void testStartEventInActiveModeConnectRetryTimerExpires() throws Exception {
		initializeFSMToConnectState("peer1");
		
		conditionalSleep(fsm.getConnectRetryTimerDueWhen());
		
		verify(callbacks).fireDisconnectRemotePeer();
		verify(callbacks, times(2)).fireConnectRemotePeer();
		Assert.assertEquals(1, fsm.getConnectRetryCounter());
		assertMachineInConnectState(false);		
	}

	@Test
	public void testStartEventInPassiveModeConnectRetryTimerExpires() throws Exception {
		initializeFSMToActiveState("peer2");

		conditionalSleep(fsm.getConnectRetryTimerDueWhen());

		verify(callbacks, never()).fireDisconnectRemotePeer(); // from connect retry timer expired handler
		verify(callbacks).fireConnectRemotePeer(); // from initial connect
		assertMachineInConnectState(false);
	}

	@Test
	public void testStartEventInActiveModeConnectStateTcpConnectionFailsNoPeerDampening() throws Exception {
		initializeFSMToConnectState("peer1");
		
		conditionalSleepShort(fsm.getConnectRetryTimerDueWhen(), 50);
		fsm.handleEvent(FSMEvent.tcpConnectionFails());

		assertMachineInIdleState(false);
	}

	@Test
	public void testStartEventInActiveModeConnectStateTcpConnectionFailsPeerDampening() throws Exception {
		initializeFSMToConnectState("peer3");
		
		conditionalSleepShort(fsm.getConnectRetryTimerDueWhen(), 50);
		fsm.handleEvent(FSMEvent.tcpConnectionFails());

		assertMachineInIdleState(true);
	}

	@Test
	public void testStartEventInActiveModeConnectionSuccessNoOpenDelay() throws Exception {
		initializeFSMToConnectState("peer1");
		
		conditionalSleepShort(fsm.getConnectRetryTimerDueWhen(), 50);
		fsm.handleEvent(FSMEvent.tcpConnectionConfirmed());

		verify(callbacks).fireConnectRemotePeer();
		assertMachineInOpenSentState();
	}

	@Test
	public void testStartEventInActiveModeConnectionSuccessOpenDelay() throws Exception {
		initializeFSMToConnectState("peer4");
		fsm.setup(loadConfiguration("org/bgp4j/netty/fsm/Config-With-BgpPeers.xml").getPeer("peer4"), callbacks);

		conditionalSleepShort(fsm.getConnectRetryTimerDueWhen(), 50);
		fsm.handleEvent(FSMEvent.tcpConnectionConfirmed());

		verify(callbacks).fireConnectRemotePeer();
		verify(callbacks, never()).fireSendOpenMessage();
		assertMachineInConnectState(true);
		
		conditionalSleep(fsm.getDelayOpenTimerDueWhen());
		
		verify(callbacks).fireConnectRemotePeer();
		assertMachineInOpenSentState();
	}

	@Test
	public void testStartEventInActiveModeConnectStateTcpConnectionFailsOpenDelay() throws Exception {
		initializeFSMToConnectState("peer4");
		fsm.setup(loadConfiguration("org/bgp4j/netty/fsm/Config-With-BgpPeers.xml").getPeer("peer4"), callbacks);

		conditionalSleepShort(fsm.getConnectRetryTimerDueWhen(), 50);
		fsm.handleEvent(FSMEvent.tcpConnectionConfirmed());

		verify(callbacks).fireConnectRemotePeer();
		verify(callbacks, never()).fireSendOpenMessage();
		assertMachineInConnectState(true);
		
		conditionalSleepShort(fsm.getDelayOpenTimerDueWhen(), 50);
		fsm.handleEvent(FSMEvent.tcpConnectionFails());
		
		verify(callbacks).fireConnectRemotePeer();
		assertMachineInActiveState(false);
	}

	@Test
	public void testTransitionConnectByOpenEventOpenDelayTimerNotRunning() throws Exception {
		initializeFSMToConnectState("peer1");
		
		conditionalSleepShort(fsm.getConnectRetryTimerDueWhen(), 50);
		fsm.handleEvent(FSMEvent.bgpOpen());
		
		Assert.assertEquals(1, fsm.getConnectRetryCounter());
		assertMachineInIdleState(false);
	}

	@Test
	public void testTransitionConnectByOpenEventOpenDelayTimerRunningWithHoldTimer() throws Exception {
		initializeFSMToConnectState("peer4");

		conditionalSleepShort(fsm.getConnectRetryTimerDueWhen(), 50);
		fsm.handleEvent(FSMEvent.tcpConnectionConfirmed());

		verify(callbacks).fireConnectRemotePeer();
		verify(callbacks, never()).fireSendOpenMessage();
		assertMachineInConnectState(true);

		fsm.handleEvent(FSMEvent.bgpOpen());

		verify(callbacks).fireConnectRemotePeer();
		assertMachineInOpenConfirm(true);
	}

	@Test
	public void testTransitionConnectByOpenEventOpenDelayTimerRunningWithoutHoldTimer() throws Exception {
		initializeFSMToConnectState("peer5");

		conditionalSleepShort(fsm.getConnectRetryTimerDueWhen(), 50);
		fsm.handleEvent(FSMEvent.tcpConnectionConfirmed());

		verify(callbacks).fireConnectRemotePeer();
		verify(callbacks, never()).fireSendOpenMessage();
		assertMachineInConnectState(true);

		fsm.handleEvent(FSMEvent.bgpOpen());

		verify(callbacks).fireConnectRemotePeer();
		assertMachineInOpenConfirm(false);
	}

	@Test
	public void testTransitionConnectByOpenMessageError() throws Exception {
		initializeFSMToConnectState("peer1");
		
		conditionalSleepShort(fsm.getConnectRetryTimerDueWhen(), 50);
		fsm.handleEvent(FSMEvent.bgpOpenMessageError());
		
		Assert.assertEquals(1, fsm.getConnectRetryCounter());
		assertMachineInIdleState(false);
	}

	@Test
	public void testTransitionConnectByBgpHeaderError() throws Exception {
		initializeFSMToConnectState("peer1");
		
		conditionalSleepShort(fsm.getConnectRetryTimerDueWhen(), 50);
		fsm.handleEvent(FSMEvent.bgpHeaderError());
		
		Assert.assertEquals(1, fsm.getConnectRetryCounter());
		assertMachineInIdleState(false);
	}

	@Test
	public void testTransitionConnectByNotifiyVersionError() throws Exception {
		initializeFSMToConnectState("peer1");
		
		conditionalSleepShort(fsm.getConnectRetryTimerDueWhen(), 50);
		fsm.handleEvent(FSMEvent.notifyMessageVersionError());
		
		Assert.assertEquals(1, fsm.getConnectRetryCounter());
		assertMachineInIdleState(false);
	}

	@Test
	public void testTransitionConnectByAutomaticStop() throws Exception {
		initializeFSMToConnectState("peer1");
		
		conditionalSleepShort(fsm.getConnectRetryTimerDueWhen(), 50);
		fsm.handleEvent(FSMEvent.automaticStop());
		
		Assert.assertEquals(0, fsm.getConnectRetryCounter());
		assertMachineInIdleState(false);
	}

	@Test
	public void testTransitionConnectByHoldTimerExpires() throws Exception {
		initializeFSMToConnectState("peer1");
		
		conditionalSleepShort(fsm.getConnectRetryTimerDueWhen(), 50);
		fsm.handleEvent(FSMEvent.holdTimerExpires());
		
		Assert.assertEquals(1, fsm.getConnectRetryCounter());
		assertMachineInIdleState(false);
	}

	@Test
	public void testTransitionConnectByKeepaliveTimerExpires() throws Exception {
		initializeFSMToConnectState("peer1");
		
		conditionalSleepShort(fsm.getConnectRetryTimerDueWhen(), 50);
		fsm.handleEvent(FSMEvent.keepaliveTimerExpires());
		
		Assert.assertEquals(1, fsm.getConnectRetryCounter());
		assertMachineInIdleState(false);
	}

	@Test
	public void testTransitionConnectByIdleHoldTimerExpires() throws Exception {
		initializeFSMToConnectState("peer1");
		
		conditionalSleepShort(fsm.getConnectRetryTimerDueWhen(), 50);
		fsm.handleEvent(FSMEvent.idleHoldTimerExpires());
		
		Assert.assertEquals(1, fsm.getConnectRetryCounter());
		assertMachineInIdleState(false);
	}

	@Test
	public void testTransitionConnectByOpenCollisionDump() throws Exception {
		initializeFSMToConnectState("peer1");
		
		conditionalSleepShort(fsm.getConnectRetryTimerDueWhen(), 50);
		fsm.handleEvent(FSMEvent.openCollisionDump());
		
		Assert.assertEquals(1, fsm.getConnectRetryCounter());
		assertMachineInIdleState(false);
	}

	@Test
	public void testTransitionConnectByNotifiyMessage() throws Exception {
		initializeFSMToConnectState("peer1");
		
		conditionalSleepShort(fsm.getConnectRetryTimerDueWhen(), 50);
		fsm.handleEvent(FSMEvent.notifyMessage());
		
		Assert.assertEquals(1, fsm.getConnectRetryCounter());
		assertMachineInIdleState(false);
	}

	@Test
	public void testTransitionConnectByKeepaliveMessage() throws Exception {
		initializeFSMToConnectState("peer1");
		
		conditionalSleepShort(fsm.getConnectRetryTimerDueWhen(), 50);
		fsm.handleEvent(FSMEvent.keepAliveMessage());
		
		Assert.assertEquals(1, fsm.getConnectRetryCounter());
		assertMachineInIdleState(false);
	}

	@Test
	public void testTransitionConnectByUpdateMessage() throws Exception {
		initializeFSMToConnectState("peer1");
		
		conditionalSleepShort(fsm.getConnectRetryTimerDueWhen(), 50);
		fsm.handleEvent(FSMEvent.updateMessage());
		
		Assert.assertEquals(1, fsm.getConnectRetryCounter());
		assertMachineInIdleState(false);
	}

	@Test
	public void testTransitionConnectByUpdateMessageError() throws Exception {
		initializeFSMToConnectState("peer1");
		
		conditionalSleepShort(fsm.getConnectRetryTimerDueWhen(), 50);
		fsm.handleEvent(FSMEvent.updateMessageError());
		
		Assert.assertEquals(1, fsm.getConnectRetryCounter());
		assertMachineInIdleState(false);
	}

	private Configuration loadConfiguration(String fileName) throws Exception {
		return parser.parseConfiguration(new XMLConfiguration(fileName));
	}
	
	private void conditionalSleep(Date untilWhen) throws InterruptedException {
		long sleep = (untilWhen.getTime() - System.currentTimeMillis()) + 1000L;
		
		if(sleep > 0)
			Thread.sleep(sleep);
	}

	/**
	 * Sleep a fractional time of the interval between now and the given date.
	 * 
	 * @param untilWhen the date to sleep until if the fraction is 100
	 * @param fraction the fractional value as percentage between 0 and 100
	 * 
	 * @throws InterruptedException
	 */
	private void conditionalSleepShort(Date untilWhen, long fraction) throws InterruptedException {
		long sleep = (((untilWhen.getTime() - System.currentTimeMillis()) * fraction) / 100L);
		
		if(sleep > 0)
			Thread.sleep(sleep);
	}
	
	/**
	 * Configure the machine and bring it to the connect state.
	 * 
	 * @param peerName
	 * @throws Exception
	 */
	private void initializeFSMToConnectState(String peerName) throws Exception {
		fsm.setup(loadConfiguration("org/bgp4j/netty/fsm/Config-With-BgpPeers.xml").getPeer(peerName), callbacks);

		Assert.assertEquals(FSMState.Idle, fsm.getState());
		fsm.handleEvent(FSMEvent.automaticStart());
		
		verify(callbacks).fireConnectRemotePeer();
		Assert.assertEquals(0, fsm.getConnectRetryCounter());

		assertMachineInConnectState(false);
	}
	
	/**
	 * Configure the machine and bring it to the active state.
	 * 
	 * @param peerName
	 * @throws Exception
	 */
	private void initializeFSMToActiveState(String peerName) throws Exception {
		fsm.setup(loadConfiguration("org/bgp4j/netty/fsm/Config-With-BgpPeers.xml").getPeer(peerName), callbacks);

		Assert.assertEquals(FSMState.Idle, fsm.getState());
		fsm.handleEvent(FSMEvent.automaticStart());
		
		verify(callbacks,never()).fireConnectRemotePeer();
		Assert.assertEquals(0, fsm.getConnectRetryCounter());

		assertMachineInActiveState(false);
	}
	
	/**
	 * check if the machine is in connect state and that the timer are in the following states:
	 * <ul>
	 * <li><b>Connect retry timer:</b> Running if the delay open timer must be not running, not running if the open dealy timer must be running 
	 * <li><b>Delay open timer:</b> Conditionally checked
	 * <li><b>Idle hold timer:</b>Not running
	 * <li><b>hold timer:</b>Not running
	 * <li><b>Keepalive timer:</b>Not running
	 * </ul>
	 * This method does not check the connect retry counter because it is irrelevant for the machine to transition to the idle state.
	 * @throws Exception
	 */
	private void assertMachineInConnectState(boolean mustHaveOpenDelayTimer) throws Exception {
		Assert.assertEquals(FSMState.Connect, fsm.getState());
		
		if(mustHaveOpenDelayTimer) {
			Assert.assertFalse(fsm.isConnectRetryTimerRunning());
			Assert.assertNull(fsm.getConnectRetryTimerDueWhen());		
			Assert.assertTrue(fsm.isDelayOpenTimerRunning());
			Assert.assertNotNull(fsm.getDelayOpenTimerDueWhen());
		} else {
			Assert.assertTrue(fsm.isConnectRetryTimerRunning());
			Assert.assertNotNull(fsm.getConnectRetryTimerDueWhen());		
			Assert.assertFalse(fsm.isDelayOpenTimerRunning());
			Assert.assertNull(fsm.getDelayOpenTimerDueWhen());			
		}
		
		Assert.assertFalse(fsm.isHoldTimerRunning());
		Assert.assertNull(fsm.getIdleHoldTimerDueWhen());
		
		Assert.assertFalse(fsm.isHoldTimerRunning());
		Assert.assertNull(fsm.getHoldTimerDueWhen());
		
		Assert.assertFalse(fsm.isKeepaliveTimerRunning());
		Assert.assertNull(fsm.getKeepaliveTimerDueWhen());
	}

	/**
	 * check if the machine is in active state and that the timer are in the following states:
	 * <ul>
	 * <li><b>Connect retry timer:</b> Running
	 * <li><b>Delay open timer:</b> Conditionally checked
	 * <li><b>Idle hold timer:</b>Not running
	 * <li><b>Hold timer:</b>Not running
	 * <li><b>Keepalive timer:</b>Not running
	 * </ul>
	 * This method does not check the connect retry counter because it is irrelevant for the machine to transition to the active state.
	 * @throws Exception
	 */
	private void assertMachineInActiveState(boolean mustHaveOpenDelayTimer) throws Exception {
		Assert.assertEquals(FSMState.Active, fsm.getState());
		Assert.assertTrue(fsm.isConnectRetryTimerRunning());
		Assert.assertNotNull(fsm.getConnectRetryTimerDueWhen());		
		
		if(mustHaveOpenDelayTimer) {
			Assert.assertTrue(fsm.isDelayOpenTimerRunning());
			Assert.assertNotNull(fsm.getDelayOpenTimerDueWhen());
		} else {
			Assert.assertFalse(fsm.isDelayOpenTimerRunning());
			Assert.assertNull(fsm.getDelayOpenTimerDueWhen());			
		}
		
		Assert.assertFalse(fsm.isHoldTimerRunning());
		Assert.assertNull(fsm.getIdleHoldTimerDueWhen());
		
		Assert.assertFalse(fsm.isHoldTimerRunning());
		Assert.assertNull(fsm.getHoldTimerDueWhen());
		
		Assert.assertFalse(fsm.isKeepaliveTimerRunning());
		Assert.assertNull(fsm.getKeepaliveTimerDueWhen());
	}

	/**
	 * check if the machine is in active state and that the timer are in the following states:
	 * <ul>
	 * <li><b>Connect retry timer:</b> Not running
	 * <li><b>Delay open timer:</b> Not running
	 * <li><b>Idle hold timer:</b>Not running
	 * <li><b>Hold timer:</b>Running
	 * <li><b>Keepalive timer:</b>Not running
	 * </ul>
	 * This method does not check the connect retry counter because it is irrelevant for the machine to transition to the active state.
	 * @throws Exception
	 */
	private void assertMachineInOpenSentState() throws Exception {
		Assert.assertEquals(FSMState.OpenSent, fsm.getState());
		Assert.assertFalse(fsm.isConnectRetryTimerRunning());
		Assert.assertNull(fsm.getConnectRetryTimerDueWhen());		
		Assert.assertFalse(fsm.isDelayOpenTimerRunning());
		Assert.assertNull(fsm.getDelayOpenTimerDueWhen());			
		Assert.assertTrue(fsm.isHoldTimerRunning());
		Assert.assertNotNull(fsm.getHoldTimerDueWhen());		
		Assert.assertFalse(fsm.isIdleHoldTimerRunning());
		Assert.assertNull(fsm.getIdleHoldTimerDueWhen());		
		Assert.assertFalse(fsm.isKeepaliveTimerRunning());
		Assert.assertNull(fsm.getKeepaliveTimerDueWhen());
		
		verify(callbacks, atLeastOnce()).fireSendOpenMessage();
		verify(callbacks, atLeastOnce()).fireCompleteBGPInitialization();
	}

	/**
	 * check if the machine is in active state and that the timer are in the following states:
	 * <ul>
	 * <li><b>Connect retry timer:</b> Not running
	 * <li><b>Delay open timer:</b> Not running
	 * <li><b>Idle hold timer:</b>Not running
	 * <li><b>Hold timer:</b>Conditionally checked
	 * <li><b>Keepalive timer:</b>Conditonally checked
	 * </ul>
	 * This method does not check the connect retry counter because it is irrelevant for the machine to transition to the active state.
	 * @throws Exception
	 */
	private void assertMachineInOpenConfirm(boolean mustHaveHoldAndKeepaliveTimer) throws Exception {
		Assert.assertEquals(FSMState.OpenConfirm, fsm.getState());
		Assert.assertFalse(fsm.isConnectRetryTimerRunning());
		Assert.assertNull(fsm.getConnectRetryTimerDueWhen());		
		Assert.assertFalse(fsm.isDelayOpenTimerRunning());
		Assert.assertNull(fsm.getDelayOpenTimerDueWhen());			
		Assert.assertFalse(fsm.isIdleHoldTimerRunning());
		Assert.assertNull(fsm.getIdleHoldTimerDueWhen());		
		
		if(mustHaveHoldAndKeepaliveTimer) {
			Assert.assertTrue(fsm.isHoldTimerRunning());
			Assert.assertNotNull(fsm.getHoldTimerDueWhen());
			Assert.assertTrue(fsm.isKeepaliveTimerRunning());
			Assert.assertNotNull(fsm.getKeepaliveTimerDueWhen());
		} else {
			Assert.assertFalse(fsm.isHoldTimerRunning());
			Assert.assertNull(fsm.getHoldTimerDueWhen());
			Assert.assertFalse(fsm.isKeepaliveTimerRunning());
			Assert.assertNull(fsm.getKeepaliveTimerDueWhen());
		}
		
		verify(callbacks).fireSendOpenMessage();
		verify(callbacks).fireSendKeepaliveMessage();
		verify(callbacks).fireCompleteBGPInitialization();
	}

	/**
	 * check if the machine is in idle state and that the timers are in the following state
	 * <ul>
	 * <li><b>Connect retry timer:</b> Not running
	 * <li><b>Delay open timer:</b> Not running
	 * <li><b>Idle hold timer:</b>Condintially checked
	 * <li><b>Hold timer:</b>Not running
	 * <li><b>Keepalive timer:</b>Not running
	 * </ul>
	 * This method does not check the connect retry counter because it is irrelevant for the machine to transition to the active state.

	 * @throws Exception
	 */
	private void assertMachineInIdleState(boolean musthaveIdleHoldTimer) throws Exception  {
		Assert.assertEquals(FSMState.Idle, fsm.getState());
		
		Assert.assertFalse(fsm.isConnectRetryTimerRunning());
		Assert.assertNull(fsm.getConnectRetryTimerDueWhen());
		Assert.assertFalse(fsm.isHoldTimerRunning());
		Assert.assertNull(fsm.getHoldTimerDueWhen());
		Assert.assertFalse(fsm.isDelayOpenTimerRunning());
		Assert.assertNull(fsm.getDelayOpenTimerDueWhen());
		Assert.assertFalse(fsm.isKeepaliveTimerRunning());
		Assert.assertNull(fsm.getKeepaliveTimerDueWhen());

		if(musthaveIdleHoldTimer) {
			Assert.assertTrue(fsm.isIdleHoldTimerRunning());
			Assert.assertNotNull(fsm.getIdleHoldTimerDueWhen());
		} else {
			Assert.assertFalse(fsm.isIdleHoldTimerRunning());
			Assert.assertNull(fsm.getIdleHoldTimerDueWhen());			
		}
		
		verify(callbacks, atLeastOnce()).fireDisconnectRemotePeer();		
		verify(callbacks, atLeastOnce()).fireReleaseBGPResources();		
	}
}
