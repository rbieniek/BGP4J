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
 * File: org.bgp4j.netty.fsm.FSMEvent.java 
 */
package org.bgp4j.netty.fsm;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public enum FSMEvent {
	// Administrative events
	ManualStart,
	ManualStop,
	AutomaticStart,
	AutomaticStop,
	
	// Timer events
	ConnectRetryTimer_Expires,
	HoldTimer_Expires,
	KeepaliveTimer_Expires,
	DelayOpenTimer_Expires,
	IdleHoldTimer_Expires,
	
	// TCP connection-based events
	TcpConnection_Valid,
	Tcp_CR_Invalid,
	Tcp_CR_Acked,
	TcpConnectionConfirmed,
	TcpConnectionFails,
	
	// BGP Message-based events
	BGPOpen,
	BGPOpen_with_DelayOpenTimer_Running,
	BGPOpenMsgErr,
	OpenCollisionDump,
	NotifyMsgVerErr,
	NotifyMsg,
	KeepAliveMsg,
	UpdateMsg,
	UpdateMsgErr,
}
