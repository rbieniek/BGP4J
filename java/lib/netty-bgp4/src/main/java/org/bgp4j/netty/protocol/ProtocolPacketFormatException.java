/**
 * 
 */
package org.bgp4j.netty.protocol;

/**
 * @author rainer
 *
 */
public class ProtocolPacketFormatException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -973351159524540205L;

	public ProtocolPacketFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProtocolPacketFormatException(Throwable cause) {
		super(cause);
	}

	public ProtocolPacketFormatException() {
		super();
	}

	public ProtocolPacketFormatException(String message) {
		super(message);
	}
}
