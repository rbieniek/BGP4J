/**
 * 
 */
package de.urb.quagga.netty;

/**
 * @author rainer
 *
 */
public class UnsupportedProtocolVersionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8255367762190462293L;

	private int protocolVersion;

	/**
	 * default constructor
	 */
	public UnsupportedProtocolVersionException() {}
	
	/**
	 * 
	 */
	public UnsupportedProtocolVersionException(int protocolVersion) {
		super("unsupported protocol version: " + protocolVersion);
	}

	/**
	 * @return the protocolVersion
	 */
	public int getProtocolVersion() {
		return protocolVersion;
	}

}
