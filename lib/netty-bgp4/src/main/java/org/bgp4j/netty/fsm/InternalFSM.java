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
 * File: org.bgp4j.netty.fsm.InternalFSM.java 
 */
package org.bgp4j.netty.fsm;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.inject.Inject;

import org.bgp4.config.nodes.PeerConfiguration;
import org.quartz.SchedulerException;
import org.slf4j.Logger;

/**
 * Internal FSM to seperate FSM logic from the connection management and message handling code.
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class InternalFSM {
	private @Inject Logger log;
	
	private FSMState state = FSMState.Idle;
	private PeerConfiguration peerConfiguration;
	private InternalFSMCallbacks callbacks;

	private int connectRetryCounter = 0;
	private boolean canAcceptConnection = false;
	
	private @Inject FireEventTimeManager<FireConnectRetryTimerExpired> fireConnectRetryTimeExpired;
	private @Inject FireEventTimeManager<FireIdleHoldTimerExpired> fireIdleHoldTimerExpired;
	
	InternalFSM() {
	}
	
	void setup(PeerConfiguration peerConfiguration, InternalFSMCallbacks callbacks) throws SchedulerException {
		this.peerConfiguration = peerConfiguration;
		this.callbacks = callbacks;
		
		fireConnectRetryTimeExpired.createJobDetail(FireConnectRetryTimerExpired.class, this);
		fireIdleHoldTimerExpired.createJobDetail(FireIdleHoldTimerExpired.class, this);
		
	}
	
	void destroyFSM() {
		try {
			fireConnectRetryTimeExpired.shutdown();
			fireIdleHoldTimerExpired.shutdown();
		} catch (SchedulerException e) {
			log.error("Internal error: failed to shutdown internal FSM for peer " + peerConfiguration.getPeerName(), e);
		}
	}

	void handleEvent(FSMEvent event) {
		switch(event) {
		case AutomaticStart:
//		case AutomaticStart_with_DampPeerOscillations:
//		case AutomaticStart_with_DampPeerOscillations_and_PassiveTcpEstablishment:
//		case AutomaticStart_with_PassiveTcpEstablishment:
//		case ManualStart_with_PassiveTcpEstablishment:
		case ManualStart:
			handleStartEvent();
			break;
		case AutomaticStop:
		case ManualStop:
			handleStopEvent();
			break;
		case ConnectRetryTimer_Expires:
			handleConnectRetryTimerExpiredEvent();
			break;
		case IdleHoldTimer_Expires:
			handleIdleHoldTimerExpiredEvent();
			break;
		case HoldTimer_Expires:
		case BGPOpen:
		case BGPOpen_with_DelayOpenTimer_Running:
		case BGPOpenMsgErr:
		case DelayOpenTimer_Expires:
		case KeepAliveMsg:
		case KeepaliveTimer_Expires:
		case NotifyMsg:
		case NotifyMsgVerErr:
		case OpenCollisionDump:
		case Tcp_CR_Acked:
		case Tcp_CR_Invalid:
		case TcpConnection_Valid:
		case TcpConnectionConfirmed:
		case TcpConnectionFails:
		case UpdateMsg:
		case UpdateMsgErr:
		}
	}
	
	/**
	 * handle any kind of start event. Unless the FSM is in <code>Stopped</code> state the event is ignored
	 */
	private void handleStartEvent() {		
		if(state == FSMState.Idle) {
			this.connectRetryCounter = 0;
			
			if(!peerConfiguration.isPassiveTcpEstablishment()) {
				callbacks.fireConnectRemotePeer();
				
				try {
					fireConnectRetryTimeExpired.scheduleJob(peerConfiguration.getConnectRetryTime());
				} catch (SchedulerException e) {
					log.error("Interal Error: cannot schedule connect retry timer for peer " + peerConfiguration.getPeerName(), e);
				}
			}
			canAcceptConnection = true;
			
			this.state = FSMState.Connect;
		}
	}

	/**
	 * handle any kind of stop event
	 */
	private void handleStopEvent() {
		if(state == FSMState.Connect) {
			try {
				fireConnectRetryTimeExpired.cancelJob();
			} catch (SchedulerException e) {
				log.error("Internal error: failed to cancel connect retry timer for peer " + peerConfiguration.getPeerName(), e);
			}
			callbacks.fireDisconnectRemotePeer();
		}
		
		this.connectRetryCounter = 0;
		this.state = FSMState.Idle;
	}
	
	/**
	 * handle the connect retry timer fired
	 */
	private void handleConnectRetryTimerExpiredEvent() {
		if(state == FSMState.Connect) {
			callbacks.fireDisconnectRemotePeer();
			
			if(peerConfiguration.isDampPeerOscillation()) {
				try {
					fireIdleHoldTimerExpired.scheduleJob(peerConfiguration.getIdleHoldTime() << connectRetryCounter);
				} catch (SchedulerException e) {
					log.error("Interal Error: cannot schedule idle hold timer for peer " + peerConfiguration.getPeerName(), e);
				}
			} else {
				this.connectRetryCounter++;
				callbacks.fireConnectRemotePeer();
				
				try {
					fireConnectRetryTimeExpired.scheduleJob(peerConfiguration.getConnectRetryTime());
				} catch (SchedulerException e) {
					log.error("Interal Error: cannot schedule connect retry timer for peer " + peerConfiguration.getPeerName(), e);
				}
			}
		}
	}
	
	private void handleIdleHoldTimerExpiredEvent() {
		if(state == FSMState.Connect) {
			this.connectRetryCounter++;
			callbacks.fireConnectRemotePeer();
			
			try {
				fireConnectRetryTimeExpired.scheduleJob(peerConfiguration.getConnectRetryTime());
			} catch (SchedulerException e) {
				log.error("Interal Error: cannot schedule connect retry timer for peer " + peerConfiguration.getPeerName(), e);
			}
		}
	}
	
	/**
	 * handle the connect retry time expired event.
	 * @return
	 */
	
	boolean isCanAcceptConnection() {
		return this.canAcceptConnection;
	}

	/**
	 * @return the state
	 */
	FSMState getState() {
		return state;
	}

	/**
	 * @return the connectRetryCounter
	 */
	int getConnectRetryCounter() {
		return connectRetryCounter;
	}

	/**
	 * check if the connect retry timer is currently running
	 * 
	 * @return true if the timer is running
	 * @throws SchedulerException
	 */
	boolean isConnectRetryTimerRunning() throws SchedulerException {
		return fireConnectRetryTimeExpired.isJobScheduled();
	}
	
	/**
	 * get the date when the connect retry timer will fire
	 * 
	 * @return the date when the timmer will fire
	 * @throws SchedulerException
	 */
	Date getConnectRetryTimerDueWhen() throws SchedulerException {
		return fireConnectRetryTimeExpired.getFiredWhen();
	}

	/**
	 * check if the idle hold timer is currently running
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	boolean isIdleHoldTimerRunning() throws SchedulerException {
		return fireIdleHoldTimerExpired.isJobScheduled();
	}
	
	/**
	 * get the date when then idle hold timer will fire
	 * @return
	 * @throws SchedulerException
	 */
	Date getIdleHoldTimerDueWhen() throws SchedulerException {
		return fireIdleHoldTimerExpired.getFiredWhen();
	}
}
