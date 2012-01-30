/**
 * 
 */
package org.bgp4j.netty.protocol;

/**
 * @author rainer
 *
 */
public class MalformedASPathAttributeException extends PathAttributeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 835955227257193451L;

	/**
	 * 
	 */
	public MalformedASPathAttributeException() {
	}

	/**
	 * @param offendingAttribute
	 */
	public MalformedASPathAttributeException(byte[] offendingAttribute) {
		super(offendingAttribute);
	}

	/**
	 * @param message
	 */
	public MalformedASPathAttributeException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MalformedASPathAttributeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public MalformedASPathAttributeException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param offendingAttribute
	 */
	public MalformedASPathAttributeException(String message,
			byte[] offendingAttribute) {
		super(message, offendingAttribute);
	}

	/**
	 * @param message
	 * @param offendingAttribute
	 * @param cause
	 */
	public MalformedASPathAttributeException(String message,
			byte[] offendingAttribute, Throwable cause) {
		super(message, offendingAttribute, cause);
	}

	/**
	 * @param offendingAttribute
	 * @param cause
	 */
	public MalformedASPathAttributeException(byte[] offendingAttribute,
			Throwable cause) {
		super(offendingAttribute, cause);
	}

}
