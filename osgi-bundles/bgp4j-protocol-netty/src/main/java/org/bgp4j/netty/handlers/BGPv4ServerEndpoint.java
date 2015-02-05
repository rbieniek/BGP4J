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

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;

import org.bgp4j.definitions.fsm.BGPv4FSM;
import org.bgp4j.definitions.fsm.BGPv4FSMRegistry;
import org.bgp4j.definitions.fsm.FiniteStateMachineAlreadyExistsException;
import org.bgp4j.definitions.fsm.MessageWriter;
import org.bgp4j.definitions.fsm.UnknownPeerException;
import org.bgp4j.definitions.peer.EPeerDirection;
import org.bgp4j.net.EChannelDirection;
import org.bgp4j.net.events.BgpEvent;
import org.bgp4j.net.packets.BGPv4Packet;
import org.bgp4j.net.packets.ConnectionRejectedNotificationPacket;
import org.bgp4j.net.packets.FiniteStateMachineErrorNotificationPacket;
import org.bgp4j.netty.Attributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This handler acts as the client side pipeline end. It attaches the peer connection info to the channel context of all insterested 
 * handlers when the channel is connected. Each message it receives is forwarded to the appropiate finite state machine instance.
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class BGPv4ServerEndpoint extends ChannelInboundHandlerAdapter {
	private static class ChannelMeesageWriter implements MessageWriter {
		private ChannelHandlerContext ctx;
		
		private ChannelMeesageWriter(ChannelHandlerContext ctx) {
			this.ctx = ctx;
		}

		@Override
		public void sendPacket(BGPv4Packet packet) {
			ctx.writeAndFlush(packet);
		}
		
	}
	
	public static final String HANDLER_NAME ="BGP4-ServerEndpoint";
	private Logger logger = LoggerFactory.getLogger(BGPv4ServerEndpoint.class);	
	
	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelActive(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel ch = ctx.channel();
		BGPv4FSMRegistry fsmRegistry = ch.attr(Attributes.fsmRegistryKey).get();
		
		logger.info("Server channel became active for local address {} and remote address {}", ch.localAddress(), ch.remoteAddress());
		
		try {
			BGPv4FSM fsm = fsmRegistry.createFsm(((InetSocketAddress)ch.remoteAddress()).getAddress(), EChannelDirection.SERVER);
		
			if(fsm.peerConnectionInformation().peerDirection().matches(EPeerDirection.ServerOnly)) {
				ch.attr(Attributes.peerInfoKey).set(fsm.peerConnectionInformation());
				ctx.attr(Attributes.fsmKey).set(fsm);				
				fsm.messageWriter(new ChannelMeesageWriter(ctx));
				fsm.handleConnectionOpened();
			} else {
				logger.info("Cannot accept connect from remote peer {}", ch.remoteAddress());
				
				rejectAndClose(ctx);
			}
		} catch(FiniteStateMachineAlreadyExistsException e) {
			logger.error("Duplicate inbound connection to peer {} detected, closing connection", ch.remoteAddress());
			
			rejectAndClose(ctx);
		} catch(UnknownPeerException e) {
			logger.error("Unknown remote peer {} detected, closing connection", ch.remoteAddress());
			
			rejectAndClose(ctx);
		}
	}

	private void rejectAndClose(ChannelHandlerContext ctx) {
		ctx.writeAndFlush(new ConnectionRejectedNotificationPacket()).addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				future.channel().close();
			}
		});		
	}

	private void internalErrorAndClose(ChannelHandlerContext ctx) {
		ctx.writeAndFlush(new FiniteStateMachineErrorNotificationPacket()).addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				future.channel().close();
			}
		});		
	}

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRead(io.netty.channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.info("Received message for peer {}: {}", ctx.channel().remoteAddress(), msg);
		
		if(msg instanceof BGPv4Packet) {
			BGPv4FSM fsm = ctx.attr(Attributes.fsmKey).get();
			
			fsm.handlePacket((BGPv4Packet)msg);
		} else {
			logger.error("Internal error: Received unexpected message for peer {}: {}", ctx.channel().remoteAddress(), msg);
			
			internalErrorAndClose(ctx);
		}
	}

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#userEventTriggered(io.netty.channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		logger.info("Received event for peer {}: {}", ctx.channel().remoteAddress(), evt);
		
		if(evt instanceof BgpEvent) {
			BGPv4FSM fsm = ctx.attr(Attributes.fsmKey).get();
			
			fsm.handleEvent((BgpEvent)evt);
		} else {
			logger.error("Internal error: Received unexpected event for peer {}: {}", ctx.channel().remoteAddress(), evt);
			
			internalErrorAndClose(ctx);
		}
	}

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#exceptionCaught(io.netty.channel.ChannelHandlerContext, java.lang.Throwable)
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("Internal error: Caught exception for peer {}: {}", ctx.channel().remoteAddress(), cause);
		
		internalErrorAndClose(ctx);
	}

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelInactive(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel ch = ctx.channel();
		BGPv4FSMRegistry fsmRegistry = ch.attr(Attributes.fsmRegistryKey).get();
		BGPv4FSM fsm = ctx.attr(Attributes.fsmKey).getAndSet(null);

		logger.info("Server channel became inactive for local address {} and remote address {}", ch.localAddress(), ch.remoteAddress());
		
		if(fsm != null) {
			fsm.messageWriter(null);
			fsm.handleConnectionClosed();
			
			fsmRegistry.disposeFSM(fsm);
		}
	}
}
