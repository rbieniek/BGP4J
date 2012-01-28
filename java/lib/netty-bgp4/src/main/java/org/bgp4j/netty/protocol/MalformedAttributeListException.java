/**
 * 
 */
package org.bgp4j.netty.protocol;

/**
 * @author rainer
 *
 */
public class MalformedAttributeListException extends ProtocolPacketFormatException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3338743494817410009L;

	/**
	 * 
	 */
	public MalformedAttributeListException() {
	}

	/**
	 * @param message
	 */
	public MalformedAttributeListException(String message) {
		super(message);
	}

}
