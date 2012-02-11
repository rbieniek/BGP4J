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

import java.util.Collection;

import org.bgp4j.netty.protocol.NotificationPacket;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;

/**
 * This helper class contains static methods for sending notifications.
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class NotificationHelper {

	private static class CloseChannelFuture implements ChannelFutureListener {

		/* (non-Javadoc)
		 * @see org.jboss.netty.channel.ChannelFutureListener#operationComplete(org.jboss.netty.channel.ChannelFuture)
		 */
		@Override
		public void operationComplete(ChannelFuture future) throws Exception {
			future.getChannel().close();
		}
		
	}
	
	/**
	 * send a notification and close the channel after the message was sent.
	 * 
	 * @param ctx the channel handler context containing the channel.
	 * @param notification the notification to send
	 */
	public static void sendNotificationAndCloseChannel(ChannelHandlerContext ctx, NotificationPacket notification) {
		sendNotificationAndCloseChannel(ctx.getChannel(), notification);
	}
	
	/**
	 * send a notification and close the channel after the message was sent.
	 * 
	 * @param channel the channel.
	 * @param notification the notification to send
	 */
	public static void sendNotificationAndCloseChannel(Channel channel, NotificationPacket notification) {
		channel.write(notification).addListener(new CloseChannelFuture());
	}

	/**
	 * send a list of notifications and close the channel after the last message was sent.
	 * 
	 * @param channel the channel.
	 * @param notification the notification to send
	 */
	public static void sendNotificationAndCloseChannel(Channel channel, Collection<NotificationPacket> notifications) {
		// TODO implement
	}

	/**
	 * send a notification and close the channel after the message was sent.
	 * 
	 * @param ctx the channel handler context containing the channel.
	 * @param notification the notification to send
	 */
	public static void sendEncodedNotificationAndCloseChannel(ChannelHandlerContext ctx, NotificationPacket notification) {
		sendEncodedNotificationAndCloseChannel(ctx.getChannel(), notification);
	}
	
	/**
	 * send a notification and close the channel after the message was sent.
	 * 
	 * @param channel the channel.
	 * @param notification the notification to send
	 */
	public static void sendEncodedNotificationAndCloseChannel(Channel channel, NotificationPacket notification) {
		channel.write(notification.encodePacket()).addListener(new CloseChannelFuture());
	}
}
