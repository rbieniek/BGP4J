/**
 * 
 */
package de.urb.quagga.netty;

/**
 * @author rainer
 *
 */
public class MalformedPacketException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7592862080157402471L;

	/**
	 * 
	 */
	public MalformedPacketException() {
	}

	/**
	 * 
	 */
	public MalformedPacketException(int wanted, int received) {
		super("received " + received + " bytes, wanted " + wanted + " bytes");
	}

	/**
	 * @param message
	 */
	public MalformedPacketException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public MalformedPacketException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MalformedPacketException(String message, Throwable cause) {
		super(message, cause);
	}

}
