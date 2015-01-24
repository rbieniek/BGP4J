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
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import org.bgp4j.net.BGPv4Constants;
import org.bgp4j.net.packets.BadMessageLengthNotificationPacket;
import org.bgp4j.net.packets.ConnectionNotSynchronizedNotificationPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Reframing decoder to ensure that a complete BGPv4 packet is processed in the subsequent decoder.
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class BGPv4Reframer extends ByteToMessageDecoder {
	private Logger log = LoggerFactory.getLogger(BGPv4Reframer.class);

	public static final String HANDLER_NAME = "BGP4-REFRAMER";
	
	/**
	 * reframe the received packet to completely contain the next BGPv4 packet(s). 
	 * For each packet, it peeks into the first four bytes of the 
	 * TCP stream which contain a 16-byte marker and a 16-bit length field. 
	 * The marker must be all one's and the length value must be between 19 and 4096 according to RFC 4271. The marker and length
	 * constraints are verified and if either is violated the connection is closed early.
	 * 
	 * @param ctx
	 * @param in
	 * @param out
	 */
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		while(in.readableBytes() >= (BGPv4Constants.BGP_PACKET_MIN_LENGTH-1)) {
			in.markReaderIndex();
	
			byte[] marker = new byte[BGPv4Constants.BGP_PACKET_MARKER_LENGTH];

			in.readBytes(marker);
			
			for(int i=0; i<marker.length; i++) {
				if(marker[i] != (byte)0xff) {
					log.error("received invalid marker {}, closing connection", marker[i]);
					
					NotificationHelper.sendEncodedNotification(ctx, 
							new ConnectionNotSynchronizedNotificationPacket(),
							new BgpEventFireChannelFutureListener(ctx));
					
					return;
				}
			}		
			int length = in.readUnsignedShort();
	
			if(length < BGPv4Constants.BGP_PACKET_MIN_LENGTH || length > BGPv4Constants.BGP_PACKET_MAX_LENGTH) {
				log.error("received illegal packet size {}, must be between {} and {}. closing connection", 
						new Object[] { length, BGPv4Constants.BGP_PACKET_MIN_LENGTH, BGPv4Constants.BGP_PACKET_MAX_LENGTH });
	
				NotificationHelper.sendEncodedNotification(ctx, 
						new BadMessageLengthNotificationPacket(length),
						new BgpEventFireChannelFutureListener(ctx));
				
				return;
			}
			
			int mustRead = (length - (BGPv4Constants.BGP_PACKET_MARKER_LENGTH + 2)); // we have consumed marker and length at this point
			
			if (in.readableBytes() < mustRead) {
				in.resetReaderIndex();
				
				break;
			}
	
			out.add(in.readSlice(mustRead).retain());
		}
	}

}
