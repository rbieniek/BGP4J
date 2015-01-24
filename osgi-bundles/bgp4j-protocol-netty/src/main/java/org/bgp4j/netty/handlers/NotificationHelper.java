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
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

import java.nio.ByteOrder;
import java.util.Iterator;
import java.util.List;

import org.bgp4j.net.events.NotificationEvent;
import org.bgp4j.net.packets.NotificationPacket;
import org.bgp4j.netty.protocol.BGPv4PacketEncoderFactory;

/**
 * This helper class contains static methods for sending notifications.
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class NotificationHelper {

	private static class ConcatenatedWrite implements ChannelFutureListener {
		private NotificationPacket notification;
		private ChannelFutureListener next;
		
		private ConcatenatedWrite(NotificationPacket notification, ChannelFutureListener next) {
			this.next = next;
			this.notification = notification;
		}
		
		/* (non-Javadoc)
		 * @see org.jboss.netty.channel.ChannelFutureListener#operationComplete(org.jboss.netty.channel.ChannelFuture)
		 */
		@Override
		public void operationComplete(ChannelFuture future) throws Exception {
			send(future.channel());
		}
		
		private void send(Channel channel) {
			if(next != null)
				channel.write(notification).addListener(next);
			else
				channel.write(notification);
		}
	}

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
			((BgpEventFireChannelFutureListener)listener).setBgpEvent(new NotificationEvent(notification));
		
		if(listener != null)
			channel.write(notification).addListener(listener);
		else
			channel.write(notification);
	}

	/**
	 * send a list of notifications and close the channel after the last message was sent.
	 * 
	 * @param channel the channel.
	 * @param notification the notification to send
	 */
	public static void sendNotifications(Channel channel, List<NotificationPacket> notifications, ChannelFutureListener listener) {
		if(listener instanceof BgpEventFireChannelFutureListener)
			((BgpEventFireChannelFutureListener)listener).setBgpEvent(new NotificationEvent(notifications));
		
		Iterator<NotificationPacket> it = notifications.iterator();
		
		if(it.hasNext()) {
			ConcatenatedWrite next = new ConcatenatedWrite(it.next(), listener);
	
			while(it.hasNext()) {
				ConcatenatedWrite current = new ConcatenatedWrite(it.next(), next);
				
				next = current;
			}
			
			next.send(channel);
		}
	}

	/**
	 * send a list of notifications and close the channel after the last message was sent.
	 * 
	 * @param channel the channel.
	 * @param notification the notification to send
	 */
	public static void sendNotifications(ChannelHandlerContext ctx, List<NotificationPacket> notifications, ChannelFutureListener listener) {
		sendNotifications(ctx.channel(), notifications, listener);
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
			((BgpEventFireChannelFutureListener)listener).setBgpEvent(new NotificationEvent(notification));
		ByteBuf buffer = channel.alloc().buffer().order(ByteOrder.BIG_ENDIAN);
		
		encoderFactor.encoderForPacket(notification).encodePacket(notification, buffer);
		
		if(listener != null) 
			channel.writeAndFlush(buffer).addListener(listener);
		else
			channel.writeAndFlush(buffer);
	}
}
