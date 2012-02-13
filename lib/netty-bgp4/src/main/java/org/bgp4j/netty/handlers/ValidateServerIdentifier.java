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
 */
package org.bgp4j.netty.handlers;

import javax.inject.Inject;

import org.bgp4j.netty.PeerConnectionInformation;
import org.bgp4j.netty.protocol.open.BadBgpIdentifierNotificationPacket;
import org.bgp4j.netty.protocol.open.BadPeerASNotificationPacket;
import org.bgp4j.netty.protocol.open.OpenPacket;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;


/**
 * This channel handler checks if the OPEN packet contains the configured remote BGP identifier and autonomous system. If the configured
 * and the received BGP identifier or the configured and the received autonomous system do not match, the connection is closed.
 * The peer identifier verification is moved out of the finite state machine to make it generic for both client and server case.
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ValidateServerIdentifier extends SimpleChannelUpstreamHandler {
	public static final String HANDLER_NAME ="BGP4-ValidateServerIdentifier";
	
	private @Inject Logger log;

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelUpstreamHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		
		if(e.getMessage() instanceof OpenPacket) {
			OpenPacket openPacket = (OpenPacket)e.getMessage();
			PeerConnectionInformation peerConnInfo = (PeerConnectionInformation)ctx.getAttachment();
			
			if(openPacket.getBgpIdentifier() != peerConnInfo.getRemoteBgpIdentifier()) {	
				log.error("expected remote BGP identifier {}, received BGP identifier {}", peerConnInfo.getRemoteBgpIdentifier(), openPacket.getBgpIdentifier());
				
				NotificationHelper.sendNotificationAndCloseChannel(ctx, new BadBgpIdentifierNotificationPacket());
				return;
			}

			if(openPacket.getEffectiveAutonomousSystem() != peerConnInfo.getRemoteAS()) {	
				log.error("expected remote autonomous systemr {}, received autonomous system {}", 
						peerConnInfo.getRemoteAS(), 
						openPacket.getAutonomousSystem());
				
				NotificationHelper.sendNotificationAndCloseChannel(ctx, new BadPeerASNotificationPacket());
				return;
			}
		}
		
		ctx.sendUpstream(e);
	}
	
	
}
