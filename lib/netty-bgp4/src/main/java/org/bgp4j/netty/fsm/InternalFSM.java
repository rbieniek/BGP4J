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

import java.util.Date;

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
	private @Inject FireEventTimeManager<FireDelayOpenTimerExpired> fireDelayOpenTimerExpired;
	private @Inject FireEventTimeManager<FireHoldTimerExpired> fireHoldTimerExpired;
	
	InternalFSM() {
	}
	
	void setup(PeerConfiguration peerConfiguration, InternalFSMCallbacks callbacks) throws SchedulerException {
		this.peerConfiguration = peerConfiguration;
		this.callbacks = callbacks;
		
		fireConnectRetryTimeExpired.createJobDetail(FireConnectRetryTimerExpired.class, this);
		fireIdleHoldTimerExpired.createJobDetail(FireIdleHoldTimerExpired.class, this);
		fireDelayOpenTimerExpired.createJobDetail(FireDelayOpenTimerExpired.class, this);
		fireHoldTimerExpired.createJobDetail(FireHoldTimerExpired.class, this);
	}
	
	void destroyFSM() {
		try {
			fireConnectRetryTimeExpired.shutdown();
			fireIdleHoldTimerExpired.shutdown();
			fireDelayOpenTimerExpired.shutdown();
			fireHoldTimerExpired.shutdown();
		} catch (SchedulerException e) {
			log.error("Internal error: failed to shutdown internal FSM for peer " + peerConfiguration.getPeerName(), e);
		}
	}

	void handleEvent(FSMEvent event) {
		switch(event) {
		case AutomaticStart:
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
		case TcpConnection_Valid:
		case Tcp_CR_Invalid:
			// do nothing for now
			break;
		case Tcp_CR_Acked:
		case TcpConnectionConfirmed:
			handleTcpConnectionEstablished();
			break;
		case TcpConnectionFails:
			handleTcpConnectionFails();
			break;
		case DelayOpenTimer_Expires:
			handleDelayOpenTimerExpiredEvent();
			break;
		case HoldTimer_Expires:
		case BGPOpen:
		case BGPOpenMsgErr:
		case KeepAliveMsg:
		case KeepaliveTimer_Expires:
		case NotifyMsg:
		case NotifyMsgVerErr:
		case OpenCollisionDump:
		case UpdateMsg:
		case UpdateMsgErr:
		}
	}
	
	/**
	 * handle any kind of start event. Unless the FSM is in <code>Idle</code> state the event is ignored
	 * <ul>
	 * <li>If passive TCP estalishment is disabled then fire the connect remote peer callback and move to <code>Connect</code> state</li>
	 * <li>If passive TCP estalishment is ensabled then move to <code>Connect</code> state</li>
	 * </ul>
	 */
	private void handleStartEvent() {		
		if(state == FSMState.Idle) {
			this.connectRetryCounter = 0;
			canAcceptConnection = true;

			if(!peerConfiguration.isPassiveTcpEstablishment())
				moveStateToConnect();
			else
				moveStateToActive();
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
	 * handle the connect retry timer fired event.
	 * <ol>
	 * <li>Fire disconnect remote peer callback</li>
	 * <li>perform actions based on current state:
	 * <ul>
	 * <li>If state is <code>Connect</code>:
	 * <ul>
	 * <li>If peer dampening is enabled then restart the idle hold timer and move state to <code>Idle</code></li>
	 * <li>If peer dampening is disabled then restart the connect retry timer then fire the connect remote peer callback and stay <code>Connect</code> state</li>
	 * </ul>
	 * </li>
	 * <li>If the state is <code>Active</code> then fire the connect remote peer callback and  move the state to <code>Connect</code>.
	 * <li>If the state is <code>Idle</code>:<ul>
	 * <li>If passive TCP estalishment is disabled then fire the connect remote peer callback and move to <code>Connect</code> state</li>
	 * <li>If passive TCP estalishment is ensabled then move to <code>Connect</code> state</li>
	 * </ul>
	 * </li>
	 * </ul></li>
	 * </ol>
	 */
	private void handleConnectRetryTimerExpiredEvent() {
		if(state == FSMState.Connect) {
			callbacks.fireDisconnectRemotePeer();
			
			if(peerConfiguration.isDampPeerOscillation()) {
				state = FSMState.Idle;
				
				try {
					fireIdleHoldTimerExpired.scheduleJob(peerConfiguration.getIdleHoldTime() << connectRetryCounter);
				} catch (SchedulerException e) {
					log.error("Interal Error: cannot schedule idle hold timer for peer " + peerConfiguration.getPeerName(), e);
				}
			} else {
				this.connectRetryCounter++;
				
				moveStateToConnect();
			}
		} else if(state == FSMState.Active) {
			this.connectRetryCounter++;
			
			moveStateToConnect();
		} else if(state == FSMState.Idle) {
			if(!peerConfiguration.isPassiveTcpEstablishment())
				moveStateToConnect();
			else
				moveStateToActive();			
		}
	}
	
	/**
	 * handle the idle hold timer expired event. If the current state is <code>Idle</code> then the machine is moved into state <code>Connect</code>
	 */
	private void handleIdleHoldTimerExpiredEvent() {
		if(state == FSMState.Idle) {			
			this.connectRetryCounter++;
			
			moveStateToConnect();
		}
	}

	/**
	 * handle the delay open timer expired event. Depending on the current state the following actions are performed:
	 * <ul>
	 * <li>If the current state is <code>Connect</code> then send an <code>OPEN</code> message to the peer, set the hold timer to 600 seconds
	 * and move the state to <code>OpenSent</code>
	 * </ul>
	 */
	private void handleDelayOpenTimerExpiredEvent() {
		if(state == FSMState.Connect) {
			moveStateToOpenSent();
		}
	}
	
	/**
	 * Handle TCP connections failures. 
	 * <ul>
	 * <li>if the current state is <code>Connect</code> then move to <code>Active</code>
	 * <li>if the current state is <code>Active</code> then move to <code>Idle</code>
	 * </ul>
	 */
	private void handleTcpConnectionFails() {
		switch(state) {
		case Connect:
			try {
				if (isDelayOpenTimerRunning()) {
					moveStateToActive();
				} else {
					moveStateToIdle();
				}
			} catch(SchedulerException e) {
				log.error("Internal Error: Failed to query delay open timer for peer " + peerConfiguration.getPeerName());
				
				moveStateToIdle();
			}
			break;
		case Active:
			moveStateToIdle();
			break;
		}
	}

	/**
	 * Handle the connection being established. 
	 * <ul>
	 * <li>If the current state is <code>Connect</code>:<ul>
	 * <li>If the delay open flag is set then the connect retry timer is canceled, the delay open timer is started with the configured value.
	 * The state stay at <code>Connect</code></li>
	 * <li>If the delay open flag is not set then the connect retry timer is canceled, an <code>OPEN</code> message is sent to the peer and
	 * the state is moved to <code>OpenSent</code></li>
	 * </ul>
	 * </li>
	 * </ul>
	 */
	private void handleTcpConnectionEstablished() {
		switch(state) {
		case Connect:
			if(peerConfiguration.isDelayOpen()) {
				try {
					fireConnectRetryTimeExpired.cancelJob();
				} catch (SchedulerException e) {
					log.error("Internal Error: cannot cancel connect retry timer for peer " + peerConfiguration.getPeerName(), e);
				}
				
				try {
					fireDelayOpenTimerExpired.scheduleJob(peerConfiguration.getDelayOpenTime());
				} catch (SchedulerException e) {
					log.error("Internal Error: cannot schedule open delay timer for peer " + peerConfiguration.getPeerName(), e);
				}
			} else {
				moveStateToOpenSent();
			}
		}
	}
		
	/**
	 * check if connections can be accepted
	 * 
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

	/**
	 * check if the delay open timer is currently running
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	boolean isDelayOpenTimerRunning() throws SchedulerException {
		return fireDelayOpenTimerExpired.isJobScheduled();
	}
	
	/**
	 * get the date when the delay open timer will fire
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	public Date getDelayOpenTimerDueWhen() throws SchedulerException {
		return fireDelayOpenTimerExpired.getFiredWhen();
	}

	/**
	 * Check if the hold timer is running
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	boolean isHoldTimerRunning() throws SchedulerException {
		return fireHoldTimerExpired.isJobScheduled();
	}
	
	/**
	 * get the date when the hold timer will fire.
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	Date getHoldTimerDueWhen() throws SchedulerException {
		return fireHoldTimerExpired.getFiredWhen();
	}
	
	/**
	 * Move from any other state to <code>Connect</code> state. It performs the following actions:
	 * <ol>
	 * <li>cancel the idle hold timer</li>
	 * <li>cancel the connect retry timer</li>
	 * <li>restart the connect retry timer with the configured value</li>
	 * <li>fire the connect to remote peer callback</li>
	 * <li>set the state to <code>Connect</code></li>
	 * </ol>
	 */
	private void moveStateToConnect() {
		try {
			fireIdleHoldTimerExpired.cancelJob();
			fireConnectRetryTimeExpired.cancelJob();
			
			fireConnectRetryTimeExpired.scheduleJob(peerConfiguration.getConnectRetryTime());
		} catch (SchedulerException e) {
			log.error("Interal Error: cannot schedule connect retry timer for peer " + peerConfiguration.getPeerName(), e);
		}
		
		callbacks.fireConnectRemotePeer();
		
		this.state = FSMState.Connect;				
	}

	/**
	 * Move from any other state to <code>Active</code> state. It performs the following actions:
	 * <ol>
	 * <li>cancel the idle hold timer</li>
	 * <li>cancel the connect retry timer</li>
	 * <li>cancel the delay open timer</li>
	 * <li>restart the connect retry timer with the configured value</li>
	 * <li>fire the connect to remote peer callback</li>
	 * <li>set the state to <code>Active</code></li>
	 * </ol>
	 */
	private void moveStateToActive() {
		try {
			fireIdleHoldTimerExpired.cancelJob();
			fireConnectRetryTimeExpired.cancelJob();
			fireDelayOpenTimerExpired.cancelJob();
			
			fireConnectRetryTimeExpired.scheduleJob(peerConfiguration.getConnectRetryTime());
		} catch (SchedulerException e) {
			log.error("Interal Error: cannot schedule connect retry timer for peer " + peerConfiguration.getPeerName(), e);
		}
		this.state = FSMState.Active;		
	}

	/**
	 * Move from any other state to <code>Idle</code> state. It performs the following actions:
	 * <ol>
	 * <li>cancel the idle hold timer</li>
	 * <li>cancel the connect retry timer</li>
	 * <li>cancel the delay open timer</li>
	 * <li>cancel the hold timer</li>
	 * <li>restart the connect retry timer with the configured value if peer dampening is disabled</li>
	 * <li>restart the idle hold timer with the configured value if peer dampening is enabled</li>
	 * <li>set the state to <code>Idle</code></li>
	 * </ol>
	 */
	private void moveStateToIdle() {
		try {
			fireIdleHoldTimerExpired.cancelJob();
			fireConnectRetryTimeExpired.cancelJob();
			fireDelayOpenTimerExpired.cancelJob();
			fireHoldTimerExpired.cancelJob();
		} catch(SchedulerException e) {
			log.error("Interal Error: cannot cancel timers for peer " + peerConfiguration.getPeerName(), e);			
		}
		
		if(peerConfiguration.isDampPeerOscillation()) {
			try {
				fireIdleHoldTimerExpired.scheduleJob(peerConfiguration.getIdleHoldTime() << connectRetryCounter);
			} catch (SchedulerException e) {
				log.error("Interal Error: cannot schedule idle hold timer for peer " + peerConfiguration.getPeerName(), e);
			}
		} else {
			try {
				fireConnectRetryTimeExpired.scheduleJob(peerConfiguration.getConnectRetryTime());
			} catch (SchedulerException e) {
				log.error("Interal Error: cannot schedule connect retry timer for peer " + peerConfiguration.getPeerName(), e);
			}
		}
		this.state = FSMState.Idle;		
	}

	/**
	 * Move from any other state to <code>OpenSent</code> state. It performs the following actions:
	 * <ol>
	 * <li>cancel the idle hold timer</li>
	 * <li>cancel the connect retry timer</li>
	 * <li>start the hold timer with 600 seconds</li>
	 * <li>fire the send <code>OPEN</code> message to remote peer callback</li>
	 * <li>set the state to <code>OpenSent</code></li>
	 * </ol>
	 */
	private void moveStateToOpenSent() {
		try {
			fireIdleHoldTimerExpired.cancelJob();
			fireConnectRetryTimeExpired.cancelJob();
			
			fireHoldTimerExpired.scheduleJob(600);
		} catch (SchedulerException e) {
			log.error("Interal Error: cannot schedule connect retry timer for peer " + peerConfiguration.getPeerName(), e);
		}
		
		callbacks.fireSendOpenMessage();
		
		this.state = FSMState.OpenSent;		
	}

}
