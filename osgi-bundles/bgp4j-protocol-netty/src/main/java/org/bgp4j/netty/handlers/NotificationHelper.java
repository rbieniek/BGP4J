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
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

import java.nio.ByteOrder;

import org.bgp4j.net.packets.NotificationPacket;
import org.bgp4j.netty.Attributes;
import org.bgp4j.netty.protocol.BGPv4PacketEncoderFactory;

/**
 * This helper class contains static methods for sending notifications.
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class NotificationHelper {

	private static BGPv4PacketEncoderFactory encoderFactor = new BGPv4PacketEncoderFactory();

	/**
	 * send a notification and close the channel after the message was sent.
	 * 
	 * @param ctx the channel handler context containing the channel.
	 * @param notification the notification to send
	 */
	public static void sendNotification(ChannelHandlerContext ctx, NotificationPacket notification, ChannelFutureListener listener) {
		sendNotification(ctx.channel(), notification, listener);
	}
	
	/**
	 * send a notification and close the channel after the message was sent.
	 * 
	 * @param channel the channel.
	 * @param notification the notification to send
	 */
	public static void sendNotification(Channel channel, NotificationPacket notification, ChannelFutureListener listener) {
		if(listener instanceof BgpEventFireChannelFutureListener)
			((BgpEventFireChannelFutureListener)listener).addBgpEvent(notification.event(channel.attr(Attributes.channelDirectionKey).get()));
		
		if(listener != null)
			channel.writeAndFlush(notification).addListener(listener);
		else
			channel.writeAndFlush(notification);
	}

	/**
	 * send a notification and close the channel after the message was sent.
	 * 
	 * @param ctx the channel handler context containing the channel.
	 * @param notification the notification to send
	 */
	public static void sendEncodedNotification(ChannelHandlerContext ctx, NotificationPacket notification, ChannelFutureListener listener) {
		sendEncodedNotification(ctx.channel(), notification, listener);
		
	}
	
	/**
	 * send a notification and close the channel after the message was sent.
	 * 
	 * @param channel the channel.
	 * @param notification the notification to send
	 */
	public static void sendEncodedNotification(Channel channel, NotificationPacket notification, ChannelFutureListener listener) {
		if(listener instanceof BgpEventFireChannelFutureListener)
			((BgpEventFireChannelFutureListener)listener).addBgpEvent(notification.event(channel.attr(Attributes.channelDirectionKey).get()));
		ByteBuf buffer = channel.alloc().buffer().order(ByteOrder.BIG_ENDIAN);
		
		encoderFactor.encoderForPacket(notification).encodePacket(notification, buffer);
		
		if(listener != null) 
			channel.writeAndFlush(buffer).addListener(listener);
		else
			channel.writeAndFlush(buffer);
	}
}
