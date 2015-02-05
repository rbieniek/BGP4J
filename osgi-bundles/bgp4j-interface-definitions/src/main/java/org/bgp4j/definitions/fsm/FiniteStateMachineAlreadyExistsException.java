/**
 * 
 */
package org.bgp4j.definitions.fsm;

/**
 * Exception to be thrown if a FSM for a peer address and a channel direction already exists
 * 
 * @author rainer
 *
 */
public class FiniteStateMachineAlreadyExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7965443238489056502L;

	/**
	 * 
	 */
	public FiniteStateMachineAlreadyExistsException() {
	}

	/**
	 * @param message
	 */
	public FiniteStateMachineAlreadyExistsException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public FiniteStateMachineAlreadyExistsException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public FiniteStateMachineAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}
}
