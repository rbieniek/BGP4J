/**
 * 
 */
package org.bgp4j.netty.protocol;

/**
 * @author rainer
 *
 */
public class InvalidOriginException extends PathAttributeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4983725877677879162L;

	public InvalidOriginException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidOriginException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 */
	public InvalidOriginException() {
	}

	/**
	 * @param offendingAttribute
	 */
	public InvalidOriginException(byte[] offendingAttribute) {
		super(offendingAttribute);
	}

	/**
	 * @param message
	 */
	public InvalidOriginException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param offendingAttribute
	 */
	public InvalidOriginException(String message, byte[] offendingAttribute) {
		super(message, offendingAttribute);
	}

	public InvalidOriginException(byte[] offendingAttribute, Throwable cause) {
		super(offendingAttribute, cause);
	}

	public InvalidOriginException(String message, byte[] offendingAttribute,
			Throwable cause) {
		super(message, offendingAttribute, cause);
	}

}
