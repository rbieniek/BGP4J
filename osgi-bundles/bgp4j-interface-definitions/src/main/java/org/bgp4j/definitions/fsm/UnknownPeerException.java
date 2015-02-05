/**
 * 
 */
package org.bgp4j.definitions.fsm;

/**
 * Exception to be thrown if a remote peer is not known
 * 
 * @author rainer
 *
 */
public class UnknownPeerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7965443238489056502L;

	/**
	 * 
	 */
	public UnknownPeerException() {
	}

	/**
	 * @param message
	 */
	public UnknownPeerException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public UnknownPeerException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UnknownPeerException(String message, Throwable cause) {
		super(message, cause);
	}
}
