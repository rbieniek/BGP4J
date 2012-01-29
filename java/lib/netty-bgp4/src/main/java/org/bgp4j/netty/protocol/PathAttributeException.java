/**
 * 
 */
package org.bgp4j.netty.protocol;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * @author rainer
 *
 */
public class PathAttributeException extends ProtocolPacketFormatException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8265508454292519918L;
	
	private byte[] offendingAttribute;

	/**
	 * 
	 */
	public PathAttributeException() {
	}

	/**
	 * 
	 */
	public PathAttributeException(byte[] offendingAttribute) {
		this.offendingAttribute = offendingAttribute;
	}

	/**
	 * @param message
	 */
	public PathAttributeException(String message) {
		super(message);
	}

	public PathAttributeException(String message, Throwable cause) {
		super(message, cause);
	}

	public PathAttributeException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 */
	public PathAttributeException(String message, byte[] offendingAttribute) {
		super(message);

		this.offendingAttribute = offendingAttribute;
	}

	public PathAttributeException(String message, byte[] offendingAttribute, Throwable cause) {
		super(message, cause);

		this.offendingAttribute = offendingAttribute;
	}

	public PathAttributeException(byte[] offendingAttribute, Throwable cause) {
		super(cause);

		this.offendingAttribute = offendingAttribute;
	}


	
	/**
	 * @return the offendingAttribute
	 */
	public byte[] getOffendingAttribute() {
		return offendingAttribute;
	}

	/**
	 * @param offendingAttribute the offendingAttribute to set
	 */
	public void setOffendingAttribute(byte[] offendingAttribute) {
		this.offendingAttribute = offendingAttribute;
	}

	/**
	 * @param offendingAttribute the offendingAttribute to set
	 */
	public void setOffendingAttribute(ChannelBuffer buffer) {
		this.offendingAttribute = new byte[buffer.readableBytes()];
		
		buffer.readBytes(offendingAttribute);
	}
}
