/**
 * 
 */
package de.urb.quagga.netty.protocol;

/**
 * @author rainer
 *
 */
public class QuaggaPacket {
	private int protocolVersion;
	
	protected QuaggaPacket(int protocolVersion) {
		this.setProtocolVersion(protocolVersion);
	}

	/**
	 * @return the protocolVersion
	 */
	public int getProtocolVersion() {
		return protocolVersion;
	}

	/**
	 * @param protocolVersion the protocolVersion to set
	 */
	private void setProtocolVersion(int protocolVersion) {
		this.protocolVersion = protocolVersion;
	}
	
	
}
