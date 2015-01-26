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


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.nio.ByteOrder;

import org.bgp4j.net.packets.BGPv4Packet;
import org.bgp4j.netty.protocol.BGPv4PacketDecoder;
import org.bgp4j.netty.protocol.BGPv4PacketEncoderFactory;
import org.bgp4j.netty.protocol.ProtocolPacketException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Protocol codec which translates between protocol network packets and protocol POJOs 
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class BGPv4Codec extends ChannelDuplexHandler {
	public static final String HANDLER_NAME = "BGP4-Codec";
	
	private Logger log = LoggerFactory.getLogger(BGPv4Codec.class);
	private BGPv4PacketDecoder packetDecoder = new BGPv4PacketDecoder();
	private BGPv4PacketEncoderFactory packetEncoderFactory = new BGPv4PacketEncoderFactory();

	/**
	 * Upstream handler which takes care of the network packet to POJO translation
	 * 
	 * @param ctx the channel handler context
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg instanceof ByteBuf) {
			ByteBuf buffer = (ByteBuf)msg;
			
			try {
				BGPv4Packet packet = packetDecoder.decodePacket(buffer);
				
				log.info("received packet " + packet);
				
				if(packet != null) {
					ctx.fireChannelRead(packet);
				}
			} catch(ProtocolPacketException ex) {
				log.error("received malformed protocol packet, closing connection", ex);
				
				NotificationHelper.sendNotification(ctx, 
						ex.toNotificationPacket(), 
						new BgpEventFireChannelFutureListener(ctx));
			} catch(Exception ex) {
				log.error("generic decoding exception, closing connection", ex);
				
				ctx.close();
			}
		} else {
			log.error("expected a {} message payload, got a {} message payload", 
					ByteBuf.class.getName(), 
					msg.getClass().getName()); 
		}
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if(msg instanceof BGPv4Packet) {
			BGPv4Packet bgpPacket = (BGPv4Packet)msg;
			ByteBuf buffer = ctx.alloc().buffer().order(ByteOrder.BIG_ENDIAN);
			
			log.info("writing packet {}", bgpPacket);

			packetEncoderFactory.encoderForPacket(bgpPacket).encodePacket(bgpPacket, buffer);

			if(buffer != null) {
				ctx.writeAndFlush(buffer, promise);
			}
		} else {
			log.error("expected a {} message payload, got a {} message payload", 
					BGPv4Packet.class.getName(), 
					msg.getClass().getName()); 
		}
	}

}
