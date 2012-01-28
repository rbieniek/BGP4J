/**
 * 
 */
package org.bgp4j.netty.protocol;

import org.bgp4j.netty.BGPv4Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * ORIGIN (type code 1) BGPv4 path attribute
 * 
 * @author rainer
 *
 */
public class OriginPathAttribute extends PathAttribute {

	/**
	 * Discrete origin types as specified in RFC 4271
	 * 
	 * @author rainer
	 *
	 */
	public enum Origin {
		
		/** NLRI is interior to the originating AS (RFC 4271) */
		IGP,

		/** NLRI learned via EGP protocol (RFC 4271, RFC 904) */
		EGP,
		
		/** NLRI learned by some other means (RFC 4271)*/
		INCOMPLETE;
		
		int toCode() {
			switch(this) {
				case IGP:
					return 1;
				case EGP:
					return 2;
				case INCOMPLETE:
					return 3;
				default:
					throw new IllegalArgumentException("unknown origin code: " +this);
			}
		}
		
		static Origin fromCode(int code) {
			switch(code) {
			case 1:
				return IGP;
			case 2:
				return EGP;
			case 3:
				return INCOMPLETE;
			default:
				throw new IllegalArgumentException("unknown origin code: " + code);
			}
		}
	}

	private Origin origin;
	
	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.PathAttribute#getTypeCode()
	 */
	@Override
	protected int getTypeCode() {
		return BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_ORIGIN;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.PathAttribute#getValueLength()
	 */
	@Override
	protected int getValueLength() {
		return 1; // fixed one byte
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.PathAttribute#encodeValue()
	 */
	@Override
	protected ChannelBuffer encodeValue() {
		ChannelBuffer buffer = ChannelBuffers.buffer(1);
		
		buffer.writeByte(getOrigin().toCode());
		
		return buffer;
	}

	/**
	 * @return the origin
	 */
	public Origin getOrigin() {
		return origin;
	}

	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(Origin origin) {
		this.origin = origin;
	}

}
