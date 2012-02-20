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

import javax.inject.Inject;

import org.bgp4.config.nodes.PeerConfiguration;
import org.slf4j.Logger;

/**
 * Internal FSM to seperate FSM logic from the connection management and message handling code.
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class InternalFSM {
	private @Inject Logger log;
	
	private class FireEventTimerTask {
		private FSMEvent event;
		
		private FireEventTimerTask(FSMEvent event) {
			this.event = event;
		}
	}
	
	private FSMState state = FSMState.Stopped;
	private PeerConfiguration peerConfiguration;
	private InternalFSMCallbacks callbacks;
	
	private int connectRetryCounter = 0;
	private boolean canAcceptConnection = false;
	
	public InternalFSM() {
		
	}
	
	void setup(PeerConfiguration peerConfiguration, InternalFSMCallbacks callbacks) {
		this.peerConfiguration = peerConfiguration;
		this.callbacks = callbacks;
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
			break;
		case BGPOpen:
		case BGPOpen_with_DelayOpenTimer_Running:
		case BGPOpenMsgErr:
		case ConnectRetryTimer_Expires:
		case DelayOpenTimer_Expires:
		case HoldTimer_Expires:
		case IdleHoldTimer_Expires:
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
	
	private void handleStartEvent() {
		if(state == FSMState.Stopped) {
			this.state = FSMState.Idle;
			
			this.connectRetryCounter = 0;
			
			if(!peerConfiguration.isPassiveTcpEstablishment())
				callbacks.fireConnectRemotePeer();
			canAcceptConnection = true;
			
			this.state = FSMState.Connect;
		}
	}
	
	public boolean isCanAcceptConnection() {
		return this.canAcceptConnection;
	}

	/**
	 * @return the state
	 */
	public FSMState getState() {
		return state;
	}

}
