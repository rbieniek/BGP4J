/**
 * 
 */
package org.bgp4j.netty.protocol;

/**
 * @author rainer
 *
 */
public class ProtocolTypeException extends ProtocolPacketFormatException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1997353015198092763L;
	
	private int type;
	
	/**
	 * 
	 */
	public ProtocolTypeException() {
	}

	/**
	 * 
	 */
	public ProtocolTypeException(int type) {
		this.type = type;
	}

	/**
	 * @param message
	 */
	public ProtocolTypeException(String message, int type) {
		super(message);
		
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

}
