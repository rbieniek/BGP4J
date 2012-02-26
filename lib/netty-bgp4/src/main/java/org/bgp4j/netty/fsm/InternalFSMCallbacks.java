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
 * File: org.bgp4j.netty.fsm.InternalFSMCallbacks.java 
 */
package org.bgp4j.netty.fsm;

/**
 * Callback interface used for triggering actions from the internal state machine to
 * the connection management and messagng code
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public interface InternalFSMCallbacks {

	/**
	 * The remote connection to the remote peer shall be initiated
	 */
	void fireConnectRemotePeer();
	
	/**
	 * The remote connection to the peer (if established) shall be disconnected and closed
	 */
	public void fireDisconnectRemotePeer();

	/**
	 * Sent an <code>OPEN</code> message to the remote peer.
	 */
	public void fireSendOpenMessage();

	/**
	 * send an FSM error notification to the remote peer
	 */
	public void fireSendInternalErrorNotification();

	/**
	 * send a CEASE notification to the remote peer
	 */
	public void fireSendCeaseNotification();

	/**
	 * send a keepalive message to the remote peer
	 */
	void fireSendKeepaliveMessage();

	/**
	 * release all resources hold on behalf of the remote peer
	 */
	void fireReleaseBGPResources();
	
	/**
	 * complete the initialization
	 */
	void fireCompleteBGPInitialization();

	/**
	 * fire a notification to the peer that the hold timer expired
	 */
	void fireSendHoldTimerExpiredNotification();

	/**
	 * fire an notification to the peer that it sent a bad update
	 */
	void fireSendUpdateErrorNotification();
}
