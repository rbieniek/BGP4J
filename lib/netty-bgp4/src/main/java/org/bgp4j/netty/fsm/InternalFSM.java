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
import org.bgp4j.netty.FSMState;
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
	private @Inject FireRepeatedEventTimeManager<FireAutomaticStart> fireRepeatedAutomaticStart;
	private @Inject FireRepeatedEventTimeManager<FireSendKeepalive> fireKeepaliveTimerExpired;
	
	private int peerProposedHoldTime = 0;
	private boolean haveFSMError = false;
	
	InternalFSM() {
	}
	
	void setup(PeerConfiguration peerConfiguration, InternalFSMCallbacks callbacks) throws SchedulerException {
		this.peerConfiguration = peerConfiguration;
		this.callbacks = callbacks;
		
		fireConnectRetryTimeExpired.createJobDetail(FireConnectRetryTimerExpired.class, this);
		fireIdleHoldTimerExpired.createJobDetail(FireIdleHoldTimerExpired.class, this);
		fireDelayOpenTimerExpired.createJobDetail(FireDelayOpenTimerExpired.class, this);
		fireHoldTimerExpired.createJobDetail(FireHoldTimerExpired.class, this);
		fireKeepaliveTimerExpired.createJobDetail(FireSendKeepalive.class, this);
		
		fireRepeatedAutomaticStart.createJobDetail(FireAutomaticStart.class, this);
		
		if(peerConfiguration.isAllowAutomaticStart())
			fireRepeatedAutomaticStart.startRepeatedJob(peerConfiguration.getAutomaticStartInterval()); 
	}
	
	void destroyFSM() {
		try {
			fireConnectRetryTimeExpired.shutdown();
			fireIdleHoldTimerExpired.shutdown();
			fireDelayOpenTimerExpired.shutdown();
			fireHoldTimerExpired.shutdown();
			fireRepeatedAutomaticStart.shutdown();
			fireKeepaliveTimerExpired.shutdown();
		} catch (SchedulerException e) {
			log.error("Internal error: failed to shutdown internal FSM for peer " + peerConfiguration.getPeerName(), e);
		}
	}

	void handleEvent(FSMEvent event) {
		switch(event.getType()) {
		case AutomaticStart:
		case ManualStart:
			handleStartEvent();
			break;
		case AutomaticStop:
		case ManualStop:
			handleStopEvent(event.getType());
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
			handleHoldTimerExpiredEvent();
			break;
		case BGPOpen:
			handleBgpOpenEvent();
			break;
		case KeepAliveMsg:
			handleKeepaliveMessageEvent();
			break;
		case KeepaliveTimer_Expires:
			handleKeepaliveTimerExpiresEvent();
			break;
		case NotifyMsg:
			handleNotifyMessageEvent();
			break;
		case NotifyMsgVerErr:
			handleNotifyMessageVersionErrorEvent();
			break;
		case OpenCollisionDump:
			handleOpenCollisionDumpEvent();
			break;
		case BGPOpenMsgErr:
			handleBGPOpenMessageErrorEvent();
			break;
		case BGPHeaderErr:
			handleBGPHeaderErrorEvent();
			break;
		case UpdateMsg:
			handleUpdateMessageEvent();
			break;
		case UpdateMsgErr:
			handleUpdateMessageErrorEvent();
			break;
		}
		
		if(haveFSMError) {
			callbacks.fireSendInternalErrorNotification();
			connectRetryCounter++;
			 
			moveStateToIdle();
			
			haveFSMError = false;
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

			try {
			if(peerConfiguration.isDampPeerOscillation() && fireIdleHoldTimerExpired.isJobScheduled())
				return;
			} catch(SchedulerException e) {
				log.error("cannot query idel hold timer for peer " + peerConfiguration.getPeerName(), e);
				
				haveFSMError = true;
			}
			
			if(!peerConfiguration.isPassiveTcpEstablishment())
				moveStateToConnect();
			else
				moveStateToActive();
		}
	}

	/**
	 * handle any kind of stop event
	 */
	private void handleStopEvent(FSMEventType type) {
		callbacks.fireDisconnectRemotePeer();
		
		switch(type) {
		case AutomaticStop:
			this.connectRetryCounter++;	
			break;
		case ManualStop:
			this.connectRetryCounter = 0;
			break;
		}
		
		moveStateToIdle();
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
		switch(state) {
		case Connect:
			callbacks.fireDisconnectRemotePeer();
			
			if(peerConfiguration.isDampPeerOscillation()) {
				state = FSMState.Idle;
				
				try {
					fireIdleHoldTimerExpired.scheduleJob(peerConfiguration.getIdleHoldTime() << connectRetryCounter);
				} catch (SchedulerException e) {
					log.error("Interal Error: cannot schedule idle hold timer for peer " + peerConfiguration.getPeerName(), e);
					
					haveFSMError = true;
				}
			} else {
				this.connectRetryCounter++;
				
				moveStateToConnect();
			}
			break;
		case Active:
			this.connectRetryCounter++;
			
			moveStateToConnect();
			break;
		case Idle:
			if(!peerConfiguration.isPassiveTcpEstablishment())
				moveStateToConnect();
			else
				moveStateToActive();			
			break;
		default:
			haveFSMError=true;
			break;
		}
	}
	
	/**
	 * handle the idle hold timer expired event. If the current state is <code>Idle</code> then the machine is moved into state <code>Connect</code>
	 */
	private void handleHoldTimerExpiredEvent() {
		switch(state) {
		case Connect:
		case Active:
			this.connectRetryCounter++;
			moveStateToIdle();		
			break;
		case OpenSent:
		case OpenConfirm:
		case Established:
			callbacks.fireSendHoldTimerExpiredNotification();
			this.connectRetryCounter++;
			moveStateToIdle();		
			break;
		case Idle:
			// do nothing
			break;
		}
	}

	/**
	 * handle the idle hold timer expired event. If the current state is <code>Idle</code> then the machine is moved into state <code>Connect</code>
	 */
	private void handleIdleHoldTimerExpiredEvent() {
		switch(state) {
		case Connect:
		case Active:
			this.connectRetryCounter++;
			moveStateToIdle();		
			break;
		case Idle:
			this.connectRetryCounter++;
			moveStateToConnect();
			break;
		case OpenSent:
		case OpenConfirm:
		case Established:
			haveFSMError = true;
			break;
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
		switch(state) {
		case Connect:
		case Active:
			moveStateToOpenSent();
			break;
		case OpenSent:
		case OpenConfirm:
		case Established:
			haveFSMError = true;
			break;
		case Idle:
			break;
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
				
				haveFSMError = true;
			}
			break;
		case Active:
			this.connectRetryCounter++;
			moveStateToIdle();
			break;
		case OpenSent:
			moveStateToActive();
			break;
		case OpenConfirm:
		case Established:
			moveStateToIdle();
			break;
		case Idle:
			// do nothing
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
		case Active:
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
					
					haveFSMError = true;
				}
			} else {
				moveStateToOpenSent();
			}
			break;
		case OpenSent:
		case OpenConfirm:
		case Established:
			haveFSMError = true;
			break;
		case Idle:
			// do nothing
			break;
		}
	}
		
	/**
	 * handle an inbound <code>OPEN</code> mesage from the remote peer
	 */
	private void handleBgpOpenEvent() {
		switch(state) {
		case Connect:
		case Active:
			try {
				if(fireDelayOpenTimerExpired.isJobScheduled()) {
					moveStateToOpenConfirm(true);
				} else {
					connectRetryCounter++;
					moveStateToIdle();
				}
			} catch(SchedulerException e) {
				log.error("cannot query delay openn timer for peer " + peerConfiguration.getPeerName(), e);
				
				haveFSMError = true;
			}
			break;
		case OpenSent:
			moveStateToOpenConfirm(false);
			break;
		case OpenConfirm:
			// TODO what to do here?
			break;
		case Established:
			// TODO what to do here?
			break;
		case Idle:
			// do nothing here
			break;
		}
	}
	
	/**
	 * handle an <code>KEEPALIVE</CODE> message sent from the remote peer
	 */
	private void handleKeepaliveMessageEvent() {
		switch(state) {
		case Connect:
		case Active:
			connectRetryCounter++;
			moveStateToIdle();
			break;
		case OpenSent:
			haveFSMError = true;
			break;
		case OpenConfirm:
			moveStateToEstablished();
			break;
		case Established:
			try {
				fireHoldTimerExpired.cancelJob();
				fireHoldTimerExpired.scheduleJob(getNegotiatedHoldTime());
			} catch (SchedulerException e) {
				log.error("Interal Error: cannot schedule connect retry timer for peer " + peerConfiguration.getPeerName(), e);
				
				haveFSMError = true;
			}
			break;
		case Idle:
			// do nothing
			break;
		}		
	}
	
	/**
	 * handle the expired keepalive timer on the local side
	 */
	private void handleKeepaliveTimerExpiresEvent() {
		switch(state) {
		case Connect:
		case Active:
			connectRetryCounter++;
			moveStateToIdle();
			break;
		case OpenSent:
			haveFSMError=true;
			break;
		case OpenConfirm:
		case Established:
			callbacks.fireSendKeepaliveMessage();
			break;
		case Idle:
			// do nothing
			break;
		}
	}

	/**
	 * handle a <code>NOTIFY</code> message sent from the remote peer
	 */
	private void handleNotifyMessageEvent() {
		switch(state) {
		case Connect:
		case Active:
			connectRetryCounter++;
			moveStateToIdle();
			break;
		case OpenSent:
		case OpenConfirm:
		case Established:
			haveFSMError = true;
			break;
		case Idle:
			// do nothing
			break;
		}
	}
	
	/**
	 * handle a malformed <code>NOTIFY</code> message sent from the remote peer
	 */
	private void handleNotifyMessageVersionErrorEvent() {
		switch(state) {
		case Connect:
		case Active:
		case OpenSent:
		case OpenConfirm:
		case Established:
			moveStateToIdle();
			break;
		case Idle:
			// do nothing
			break;
		}
	}
	
	/**
	 * 
	 */
	private void handleOpenCollisionDumpEvent()  {
		switch(state) {
		case Connect:
		case Active:
			connectRetryCounter++;
			moveStateToIdle();
			break;
		case OpenSent:
		case OpenConfirm:
		case Established:
			callbacks.fireSendCeaseNotification();
			connectRetryCounter++;
			moveStateToIdle();
			break;
		case Idle:
			// do nothing
			break;
		}		
	}

	/**
	 * handle a malformed <code>OPEN</code> message sent from the remote peer
	 */
	private void handleBGPOpenMessageErrorEvent() {
		switch(state) {
		case Connect:
		case Active:
		case OpenSent:
		case OpenConfirm:
		case Established:
			connectRetryCounter++;
			moveStateToIdle();
			break;
		case Idle:
			// do nothing
			break;
		}		
	}
	
	/**
	 * handle a malformed BGP packet where the initial header checks failed
	 */
	private void handleBGPHeaderErrorEvent() {
		switch(state) {
		case Connect:
		case Active:
		case OpenSent:
		case OpenConfirm:
		case Established:
			connectRetryCounter++;
			moveStateToIdle();
			break;
		case Idle:
			// do nothing
			break;
		}
	}
	
	/**
	 * handle an <code>UPDATE</code> message sent from the remote peer
	 */
	private void handleUpdateMessageEvent() {
		switch(state) {
		case Connect:
		case Active:
			connectRetryCounter++;
			moveStateToIdle();
			break;
		case OpenSent:
		case OpenConfirm:
			haveFSMError=true;
			break;
		case Established:
			try {
				fireHoldTimerExpired.cancelJob();
				fireHoldTimerExpired.scheduleJob(getNegotiatedHoldTime());
			} catch (SchedulerException e) {
				log.error("Interal Error: cannot schedule connect retry timer for peer " + peerConfiguration.getPeerName(), e);
				
				haveFSMError = true;
			}
			break;
		case Idle:
			// do nothing
			break;
		}		
	}
	
	/**
	 * handle a malformed <code>UPDATE</code> message sent from the remote peer
	 */
	private void handleUpdateMessageErrorEvent() {
		switch(state) {
		case Connect:
		case Active:
			connectRetryCounter++;
			moveStateToIdle();
			break;
		case OpenSent:
		case OpenConfirm:
			haveFSMError = true;
			break;
		case Established:
			callbacks.fireSendUpdateErrorNotification();
			connectRetryCounter++;
			moveStateToIdle();
			break;
		case Idle:
			// do nothing
			break;
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
	 * check if the send keeplives timer is running 
	 * @return
	 * @throws SchedulerException
	 */
	public boolean isKeepaliveTimerRunning() throws SchedulerException {
		return fireKeepaliveTimerExpired.isJobScheduled();
	}
	
	/**
	 * get the date when the next keepalive packket is to be sent
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	public Date getKeepaliveTimerDueWhen() throws SchedulerException {
		return fireKeepaliveTimerExpired.getNextFireWhen();
	}
	
	/**
	 * Check if the automatic start event generator is running
	 * 
	 */
	boolean isAutomaticStartRunning() throws SchedulerException {
		return fireRepeatedAutomaticStart.isJobScheduled();
	}
	
	/**
	 * get the date the automatic start timer will fire the next time.
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	Date getAutomaticStartDueWhen() throws SchedulerException {
		return fireRepeatedAutomaticStart.getNextFireWhen();
	}
	
	/**
	 * @return the proposedHoldTimer
	 */
	int getPeerProposedHoldTime() {
		return peerProposedHoldTime;
	}

	/**
	 * @param proposedHoldTimer the proposedHoldTimer to set
	 */
	void setPeerProposedHoldTime(int proposedHoldTime) {
		this.peerProposedHoldTime= proposedHoldTime;
	}

	/**
	 * get the negotiated hold time. This is the minimum of the locally configured hold time and the
	 * hold time value received from the remote peer in the initial open packet. 
	 * It is assured that the negotiated hold time cannot be less than 3 seconds as specified by RFC4271. 
	 *  
	 * @return
	 */
	int getNegotiatedHoldTime() {
		int negotiatedHoldTime = Math.min(peerConfiguration.getHoldTime(), peerProposedHoldTime);
		
		if(negotiatedHoldTime < 3)
			negotiatedHoldTime = 3;
		
		return negotiatedHoldTime;
	}
	
	/**
	 * get the keepalive interval which is 1/3 of the negotiated hold time. It is assured that the interval
	 * cannot be less than 1 second as specified by RFC4271
	 * 
	 * @return
	 */
	private int getSendKeepaliveTime() {
		return Math.max(getNegotiatedHoldTime() / 3, 1);
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
			
			haveFSMError = true;
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
	 * <li>cancal the hold timer</li>
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
			fireHoldTimerExpired.cancelJob();
			
			fireConnectRetryTimeExpired.scheduleJob(peerConfiguration.getConnectRetryTime());
		} catch (SchedulerException e) {
			log.error("Interal Error: cannot schedule connect retry timer for peer " + peerConfiguration.getPeerName(), e);
			
			haveFSMError = true;
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
	 * <li>cancel the send keepalive timer</li>
	 * <li>release all BGP resources</li>
	 * <li>disconnect the remote peer</li>
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
			fireKeepaliveTimerExpired.cancelJob();
		} catch(SchedulerException e) {
			log.error("Interal Error: cannot cancel timers for peer " + peerConfiguration.getPeerName(), e);			
			
			haveFSMError = true;
		}
		
		callbacks.fireReleaseBGPResources();
		callbacks.fireDisconnectRemotePeer();
		
		if(peerConfiguration.isDampPeerOscillation()) {
			try {
				fireIdleHoldTimerExpired.scheduleJob(peerConfiguration.getIdleHoldTime() << connectRetryCounter);
			} catch (SchedulerException e) {
				log.error("Interal Error: cannot schedule idle hold timer for peer " + peerConfiguration.getPeerName(), e);
				
				haveFSMError = true;
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
			
			haveFSMError = true;
		}
		
		callbacks.fireCompleteBGPInitialization();
		callbacks.fireSendOpenMessage();
		
		this.state = FSMState.OpenSent;		
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
	private void moveStateToEstablished() {
		try {
			fireIdleHoldTimerExpired.cancelJob();
			fireConnectRetryTimeExpired.cancelJob();
			
			fireHoldTimerExpired.cancelJob();
			fireHoldTimerExpired.scheduleJob(getNegotiatedHoldTime());
		} catch (SchedulerException e) {
			log.error("Interal Error: cannot schedule connect retry timer for peer " + peerConfiguration.getPeerName(), e);
			
			haveFSMError = true;
		}
		
		this.state = FSMState.Established;		
	}

	/**
	 * move the state to open confirm.
	 * <ul>
	 * <li>If called from the states <code>CONNECT</code> or <code>ACTIVE</code> then complete BGP initialization and send the peer an <code>OPEN</code> message</li>
	 * <li>If called from state <code>OPEN SENT</code> then do <b>not</b> complete BGP initialization and send the peer an <code>OPEN</code> message</li>
	 * </ul>
	 * @param sendOpenMessage
	 */
	private void moveStateToOpenConfirm(boolean sendOpenMessage) {
		if(sendOpenMessage) {
			callbacks.fireCompleteBGPInitialization();
			callbacks.fireSendOpenMessage();
		}
		
		callbacks.fireSendKeepaliveMessage();

		try {
			fireConnectRetryTimeExpired.cancelJob();
		}  catch(SchedulerException e) {
			log.error("cannont cancel connect retry timer", e);
			
			haveFSMError = true;
		}
		try {
			fireDelayOpenTimerExpired.cancelJob();
		} catch(SchedulerException e){
			log.error("cannont cancel open delay timer", e);
			
			haveFSMError = true;
		}

		if(!peerConfiguration.isHoldTimerDisabled()) {
			try {
				fireKeepaliveTimerExpired.startRepeatedJob(getSendKeepaliveTime());
			} catch(SchedulerException e) {
				log.error("cannont start send keepalive timer", e);
				
				haveFSMError = true;				
			}
			
			try {
				fireHoldTimerExpired.cancelJob();
				fireHoldTimerExpired.scheduleJob(getNegotiatedHoldTime());
			} catch(SchedulerException e) {
				
			}
		}
		this.state = FSMState.OpenConfirm;
	}
	
}
