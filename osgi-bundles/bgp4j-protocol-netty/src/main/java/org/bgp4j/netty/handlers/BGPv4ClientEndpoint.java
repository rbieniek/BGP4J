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

import java.net.InetSocketAddress;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bgp4j.definitions.PeerConnectionInformation;
import org.bgp4j.definitions.PeerConnectionInformationAware;
import org.bgp4j.net.events.BgpEvent;
import org.bgp4j.net.packets.BGPv4Packet;
import org.bgp4j.netty.fsm.BGPv4FSM;
import org.bgp4j.netty.fsm.FSMRegistry;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;

/**
 * This handler acts as the client side pipeline end. It attaches the peer connection info to the channel context of all insterested 
 * handlers when the channel is connected. Each message it receives is forwarded to the appropiate finite state machine instance.
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
@Singleton
public class BGPv4ClientEndpoint extends SimpleChannelHandler {
	public static final String HANDLER_NAME ="BGP4-ClientEndpoint";

	private @Inject Logger log;
	private @Inject FSMRegistry fsmRegistry;
	
	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		
		BGPv4FSM fsm = fsmRegistry.lookupFSM((InetSocketAddress)e.getRemoteAddress());
			
		if(fsm == null) {
			log.error("Internal Error: client for address " + e.getRemoteAddress() + " is unknown");
			
			ctx.getChannel().close();
		} else {
			if(e.getMessage() instanceof BGPv4Packet) {
				fsm.handleMessage(ctx.getChannel(), (BGPv4Packet)e.getMessage());
			} else if(e.getMessage() instanceof BgpEvent) {
				fsm.handleEvent(ctx.getChannel(), (BgpEvent)e.getMessage());
			} else {
				log.error("unknown payload class " + e.getMessage().getClass().getName() + " received for peer " + e.getRemoteAddress());
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelHandler#channelConnected(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
	 */
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		log.info("connected to client " + e.getChannel().getRemoteAddress());
		
		BGPv4FSM fsm = fsmRegistry.lookupFSM((InetSocketAddress)e.getChannel().getRemoteAddress());
		
		if(fsm == null) {
			log.error("Internal Error: client for address " + e.getChannel().getRemoteAddress() + " is unknown");
			
			ctx.getChannel().close();
		} else {
			ChannelPipeline pipeline = ctx.getPipeline();
			PeerConnectionInformation pci = fsm.getPeerConnectionInformation();
			
			for(String handlerName : pipeline.getNames()) {
				ChannelHandler handler = pipeline.get(handlerName);

				if(handler.getClass().isAnnotationPresent(PeerConnectionInformationAware.class)) {
					log.info("attaching peer connection information " + pci + " to handler " + handlerName + " for client " + e.getChannel().getRemoteAddress());
					
					pipeline.getContext(handlerName).setAttachment(pci);
				}
			}
			
			fsm.handleClientConnected(e.getChannel());
			ctx.sendUpstream(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelHandler#channelConnected(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
	 */
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		log.info("disconnected from client " + e.getChannel().getRemoteAddress());
		
		BGPv4FSM fsm = fsmRegistry.lookupFSM((InetSocketAddress)e.getChannel().getRemoteAddress());
		
		if(fsm == null) {
			log.error("Internal Error: client for address " + e.getChannel().getRemoteAddress() + " is unknown");
			
			ctx.getChannel().close();
		} else {
			
			fsm.handleDisconnected(e.getChannel());
			ctx.sendUpstream(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelHandler#channelClosed(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
	 */
	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		log.info("closed channel to client " + e.getChannel().getRemoteAddress());
		
		BGPv4FSM fsm = fsmRegistry.lookupFSM((InetSocketAddress)e.getChannel().getRemoteAddress());
		
		if(fsm == null) {
			log.error("Internal Error: client for address " + e.getChannel().getRemoteAddress() + " is unknown");
		} else {
			fsm.handleClosed(e.getChannel());
			ctx.sendUpstream(e);
		}
	}
}
