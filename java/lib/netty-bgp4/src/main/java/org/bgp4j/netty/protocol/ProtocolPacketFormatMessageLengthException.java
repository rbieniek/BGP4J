/**
 * 
 */
package org.bgp4j.netty.protocol;

/**
 * @author rainer
 *
 */
public class ProtocolPacketFormatMessageLengthException extends ProtocolPacketFormatException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9163595214018114221L;

	private int length;
	
	/**
	 * 
	 */
	public ProtocolPacketFormatMessageLengthException() {
	}

	/**
	 * 
	 */
	public ProtocolPacketFormatMessageLengthException(int length) {
		this.length = length;
	}

	/**
	 * @param message
	 */
	public ProtocolPacketFormatMessageLengthException(String message, int length) {
		super(message);
		
		this.length = length;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

}
