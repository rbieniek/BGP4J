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
package org.bgp4j.netty.service;

import java.net.InetSocketAddress;

import javax.inject.Inject;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;

/**
 * This upstream handler is used to block a duplicated connection to a BGP peer.  
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class DuplicateConnectionBlocker extends SimpleChannelUpstreamHandler {
	private @Inject Logger log;
	private @Inject PeerConnectionRegistry connectionRegistry;

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelUpstreamHandler#channelConnected(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
	 */
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		if(e.getValue() != null) { 
			InetSocketAddress peerAddress = (InetSocketAddress)e.getValue();
			
			if(connectionRegistry.tryRegisterConnection(peerAddress, ctx.getChannel().getId()))
				ctx.sendUpstream(e);
			else {
				log.info("rejected duplicate connection with peer " + peerAddress);
				
				ctx.getChannel().close();
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelUpstreamHandler#channelDisconnected(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
	 */
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		connectionRegistry.unregisterChannelId(ctx.getChannel().getId());
		
		ctx.sendUpstream(e);
	}
	
	
}
