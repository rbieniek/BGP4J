/**
 * 
 */
package org.bgp4j.netty.protocol;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;

/**
 * This helper class contains static methods for sending notifications.
 * 
 * @author rainer
 *
 */
public class NotificationHelper {

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
		channel.write(notification.encodePacket()).addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				future.getChannel().close();
			}
		});
	}
}
