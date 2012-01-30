/**
 * 
 */
package org.bgp4j.netty.protocol;

/**
 * @author rainer
 *
 */
public class InvalidNextHopException extends PathAttributeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8535948955861003621L;

	/**
	 * 
	 */
	public InvalidNextHopException() {
	}

	/**
	 * @param offendingAttribute
	 */
	public InvalidNextHopException(byte[] offendingAttribute) {
		super(offendingAttribute);
	}

	/**
	 * @param message
	 */
	public InvalidNextHopException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InvalidNextHopException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public InvalidNextHopException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param offendingAttribute
	 */
	public InvalidNextHopException(String message, byte[] offendingAttribute) {
		super(message, offendingAttribute);
	}

	/**
	 * @param message
	 * @param offendingAttribute
	 * @param cause
	 */
	public InvalidNextHopException(String message, byte[] offendingAttribute,
			Throwable cause) {
		super(message, offendingAttribute, cause);
	}

	/**
	 * @param offendingAttribute
	 * @param cause
	 */
	public InvalidNextHopException(byte[] offendingAttribute, Throwable cause) {
		super(offendingAttribute, cause);
	}

}
