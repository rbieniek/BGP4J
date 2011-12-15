/**
 * 
 */
package de.urb.quagga.netty.protocol;

import java.net.InetAddress;

import de.urb.quagga.netty.QuaggaConstants;


/**
 * @author rainer
 *
 */
public class ZServInterfaceAddressAddPacket extends QuaggaPacket {

	private int interfaceIndex;
	private int flags;
	private int prefixLength;
	private InetAddress address;
	private InetAddress destination;
	
	/**
	 * @param protocolVersion
	 */
	public ZServInterfaceAddressAddPacket(int protocolVersion) {
		super(protocolVersion);
	}

	/**
	 * @return the interfaceIndex
	 */
	public int getInterfaceIndex() {
		return interfaceIndex;
	}

	/**
	 * @param interfaceIndex the interfaceIndex to set
	 */
	public void setInterfaceIndex(int interfaceIndex) {
		this.interfaceIndex = interfaceIndex;
	}

	/**
	 * @return the flags
	 */
	public int getFlags() {
		return flags;
	}

	/**
	 * @param flags the flags to set
	 */
	public void setFlags(int flags) {
		this.flags = flags;
	}

	/**
	 * @return the prefixLength
	 */
	public int getPrefixLength() {
		return prefixLength;
	}

	/**
	 * @param prefixLength the prefixLength to set
	 */
	public void setPrefixLength(int prefixLength) {
		this.prefixLength = prefixLength;
	}

	/**
	 * @return the address
	 */
	public InetAddress getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(InetAddress address) {
		this.address = address;
	}

	/**
	 * @return the destination
	 */
	public InetAddress getDestination() {
		return destination;
	}

	/**
	 * @param destination the destination to set
	 */
	public void setDestination(InetAddress destination) {
		this.destination = destination;
	}

	public boolean isSecondaryAddress() {
		return ((this.flags & QuaggaConstants.ZEBRA_IFA_SECONDARY) != 0);
	}
	
	public void setSecondaryAddress(boolean flag) {
		if(flag)
			this.flags |= QuaggaConstants.ZEBRA_IFA_SECONDARY;
		else
			this.flags &= ~QuaggaConstants.ZEBRA_IFA_SECONDARY;
	}

	public boolean isPeerAddress() {
		return ((this.flags & QuaggaConstants.ZEBRA_IFA_PEER) != 0);
	}
	
	public void setPeerAddress(boolean flag) {
		if(flag)
			this.flags |= QuaggaConstants.ZEBRA_IFA_PEER;
		else
			this.flags &= ~QuaggaConstants.ZEBRA_IFA_PEER;
	}
}
