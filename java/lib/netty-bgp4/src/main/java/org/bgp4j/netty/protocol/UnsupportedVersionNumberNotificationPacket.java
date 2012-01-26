/**
 * 
 */
package org.bgp4j.netty.protocol;

import org.bgp4j.netty.BGPv4Constants;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * @author rainer
 *
 */
public class UnsupportedVersionNumberNotificationPacket extends	OpenNotificationPacket {

	private int version = BGPv4Constants.BGP_VERSION;
	
	public UnsupportedVersionNumberNotificationPacket() {
		super(OpenNotificationPacket.SUBCODE_UNSUPPORTED_VERSION_NUMBER);
	}

	public UnsupportedVersionNumberNotificationPacket(int version) {
		super(OpenNotificationPacket.SUBCODE_UNSUPPORTED_VERSION_NUMBER);
		
		this.version = version;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.NotificationPacket#encodePayload()
	 */
	@Override
	protected ChannelBuffer encodePayload() {
		ChannelBuffer buffer = super.encodePayload();
		
		buffer.writeShort(this.version);
		
		return buffer;
	}

}
