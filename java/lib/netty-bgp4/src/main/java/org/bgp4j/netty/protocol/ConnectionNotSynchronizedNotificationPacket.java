/**
 * 
 */
package org.bgp4j.netty.protocol;

/**
 * @author rainer
 *
 */
public class ConnectionNotSynchronizedNotificationPacket extends MessageHeaderErrorNotificationPacket {

	public ConnectionNotSynchronizedNotificationPacket() {
		super(MessageHeaderErrorNotificationPacket.SUBCODE_CONNECTION_NOT_SYNCHRONIZED);
	}
}
