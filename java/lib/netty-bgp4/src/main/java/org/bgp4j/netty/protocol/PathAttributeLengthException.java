/**
 * 
 */
package org.bgp4j.netty.protocol;

/**
 * @author rainer
 *
 */
public class PathAttributeLengthException extends PathAttributeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3386494133413403227L;

	/**
	 * 
	 */
	public PathAttributeLengthException() {
	}

	/**
	 * @param message
	 */
	public PathAttributeLengthException(String message) {
		super(message);
	}

	public PathAttributeLengthException(byte[] offendingAttribute) {
		super(offendingAttribute);
	}

	public PathAttributeLengthException(String message, byte[] offendingAttribute) {
		super(message, offendingAttribute);
	}

}
