/**
 * 
 */
package org.bgp4j.netty.protocol;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author rainer
 *
 */
public class UnknownPathAttribute extends PathAttribute {

	private int typeCode;
	private ChannelBuffer value;
	
	public UnknownPathAttribute(int typeCode, ChannelBuffer valueBuffer) {
		this.typeCode = typeCode;
		this.value = valueBuffer;
	}

	@Override
	protected int getTypeCode() {
		return typeCode;
	}

	@Override
	protected int getValueLength() {
		return value.readableBytes();
	}

	@Override
	protected ChannelBuffer encodeValue() {
		return getValue();
	}

	/**
	 * @return the value
	 */
	public ChannelBuffer getValue() {
		return ChannelBuffers.copiedBuffer(value);
	}

}
