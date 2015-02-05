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

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import org.bgp4j.definitions.peer.PeerConnectionInformation;
import org.bgp4j.net.BGPv4Constants;
import org.bgp4j.net.capabilities.AutonomousSystem4Capability;
import org.bgp4j.net.packets.open.BadBgpIdentifierNotificationPacket;
import org.bgp4j.net.packets.open.BadPeerASNotificationPacket;
import org.bgp4j.net.packets.open.OpenPacket;
import org.bgp4j.netty.Attributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This channel handler checks if the OPEN packet contains the configured remote BGP identifier and autonomous system. If the configured
 * and the received BGP identifier or the configured and the received autonomous system do not match, the connection is closed.
 * The peer identifier verification is moved out of the finite state machine to make it generic for both client and server case.
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ValidateServerIdentifier extends ChannelInboundHandlerAdapter {
	private Logger log = LoggerFactory.getLogger(ValidateServerIdentifier.class);

	public static final String HANDLER_NAME ="BGP4-ValidateServerIdentifier";

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg instanceof OpenPacket) {
			OpenPacket openPacket = (OpenPacket)msg;
			PeerConnectionInformation peerConnInfo = ctx.channel().attr(Attributes.peerInfoKey).get();
			
			if(openPacket.getBgpIdentifier() != peerConnInfo.getRemoteBgpIdentifier()) {	
				log.error("expected remote BGP identifier {}, received BGP identifier {}", peerConnInfo.getRemoteBgpIdentifier(), openPacket.getBgpIdentifier());
				
				NotificationHelper.sendNotification(ctx, 
						new BadBgpIdentifierNotificationPacket(), 
						new BgpEventFireChannelFutureListener(ctx));
				return;
			}

			if(peerConnInfo.getRemoteAS() > 65535) {
				// we must have an AutonomousSystem4 capability at this point in the OPEN packet and a AS_TRANS 2-octet AS number
				if(openPacket.getAutonomousSystem() != BGPv4Constants.BGP_AS_TRANS) {
					log.error("expected remote autonomous system {}, received autonomous system {}", 
							BGPv4Constants.BGP_AS_TRANS, 
							openPacket.getAutonomousSystem());
					
					NotificationHelper.sendNotification(ctx, 
							new BadPeerASNotificationPacket(), 
							new BgpEventFireChannelFutureListener(ctx));
					return;					
				}
				
				AutonomousSystem4Capability as4cap = openPacket.findCapability(AutonomousSystem4Capability.class);
				
				if(as4cap == null) {
					log.error("missing Autonomous system 4-octet capability");
					
					NotificationHelper.sendNotification(ctx, 
							new BadPeerASNotificationPacket(), 
							new BgpEventFireChannelFutureListener(ctx));
					return;					
				}
				
				if(as4cap.getAutonomousSystem() != peerConnInfo.getRemoteAS()) {
					log.error("expected remote autonomous system {}, received autonomous system {}", 
							peerConnInfo.getRemoteAS(), 
							as4cap.getAutonomousSystem());
					
					NotificationHelper.sendNotification(ctx, 
							new BadPeerASNotificationPacket(), 
							new BgpEventFireChannelFutureListener(ctx));
					return;					
				}
			} else {
				if(openPacket.getAutonomousSystem() != peerConnInfo.getRemoteAS()) {	
					log.error("expected remote autonomous system {}, received autonomous system {}", 
							peerConnInfo.getRemoteAS(), 
							openPacket.getAutonomousSystem());
					
					NotificationHelper.sendNotification(ctx, 
							new BadPeerASNotificationPacket(), 
							new BgpEventFireChannelFutureListener(ctx));
					return;
				}
				
				// we may have an optional AS4 capability but if we have it it must carry the same AS number as the 2-octet AS number
				AutonomousSystem4Capability as4cap = openPacket.findCapability(AutonomousSystem4Capability.class);
				
				if(as4cap != null) {
					if(as4cap.getAutonomousSystem() != peerConnInfo.getRemoteAS()) {
						log.error("expected remote autonomous system {}, received autonomous system {}", 
								peerConnInfo.getRemoteAS(), 
								as4cap.getAutonomousSystem());
						
						NotificationHelper.sendNotification(ctx, 
								new BadPeerASNotificationPacket(), 
								new BgpEventFireChannelFutureListener(ctx));
						return;						
					}
				}
			}
			
		}
		
		ctx.fireChannelRead(msg);
	}

	
}
