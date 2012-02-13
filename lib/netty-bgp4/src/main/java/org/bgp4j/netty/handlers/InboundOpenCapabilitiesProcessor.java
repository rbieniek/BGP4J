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
 * File: org.bgp4j.netty.handlers.OpenCapabilitiesProcessor.java 
 */
package org.bgp4j.netty.handlers;

import javax.inject.Inject;

import org.bgp4j.netty.BGPv4Constants;
import org.bgp4j.netty.protocol.open.AutonomousSystem4Capability;
import org.bgp4j.netty.protocol.open.BadPeerASNotificationPacket;
import org.bgp4j.netty.protocol.open.OpenPacket;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;

/**
 * This handler performs inbound handling of OPEN capabilites for:
 * - 4 octet AS number handling
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class InboundOpenCapabilitiesProcessor extends SimpleChannelUpstreamHandler {
	public static final String HANDLER_NAME="BGP4-InboundOpenCapabilitiesProcessor";
	
	private @Inject Logger log;

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		if(e.getMessage() instanceof OpenPacket) {
			OpenPacket open = (OpenPacket)e.getMessage();
			AutonomousSystem4Capability as4Cap = open.findCapability(AutonomousSystem4Capability.class);
			
			if(as4Cap != null) {
				int openASNumber = open.getAutonomousSystem();
				int capASNumber = as4Cap.getAutonomousSystem();
				
				if(capASNumber > 65535) {
					if(openASNumber != BGPv4Constants.BGP_AS_TRANS) {
						log.error("4 Octet AS numbers must transit AS {} but has {}", 
								BGPv4Constants.BGP_AS_TRANS, 
								openASNumber);
						
						NotificationHelper.sendNotificationAndCloseChannel(ctx, new BadPeerASNotificationPacket());
						return;						
					}
				} else {
					if(openASNumber != capASNumber) {
						log.error("4 octet AS number {} not matching 2 octet AS number {} in 2 octet case", 
								capASNumber, 
								openASNumber);
						
						NotificationHelper.sendNotificationAndCloseChannel(ctx, new BadPeerASNotificationPacket());
						return;						
					}					
				}
				open.setAs4AutonomousSystem(capASNumber);
			}
		}
		
		ctx.sendUpstream(e);
	}

}
