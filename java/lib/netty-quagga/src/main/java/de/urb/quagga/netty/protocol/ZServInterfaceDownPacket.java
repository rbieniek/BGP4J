/**
 * 
 */
package de.urb.quagga.netty.protocol;

import de.urb.quagga.netty.QuaggaConstants;

/**
 * @author rainer
 *
 */
public class ZServInterfaceDownPacket extends QuaggaPacket {
	
	private String interfaceName;
	private int interfaceIndex;
	private int statusFlags;
	private long interfaceFlags;
	private int interfaceMetric;
	private int ipV4Mtu;
	private int ipV6Mtu;
	private int bandwidth;
	
	public ZServInterfaceDownPacket(int protocolVersion) {
		super(protocolVersion);
	}

	/**
	 * @return the interfaceName
	 */
	public String getInterfaceName() {
		return interfaceName;
	}

	/**
	 * @param interfaceName the interfaceName to set
	 */
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
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
	 * @return the interfaceFlags
	 */
	public long getInterfaceFlags() {
		return interfaceFlags;
	}

	/**
	 * @param interfaceFlags the interfaceFlags to set
	 */
	public void setInterfaceFlags(long interfaceFlags) {
		this.interfaceFlags = interfaceFlags;
	}

	/**
	 * @return the interfaceMetric
	 */
	public int getInterfaceMetric() {
		return interfaceMetric;
	}

	/**
	 * @param interfaceMetric the interfaceMetric to set
	 */
	public void setInterfaceMetric(int interfaceMetric) {
		this.interfaceMetric = interfaceMetric;
	}

	/**
	 * @return the ipV4Mtu
	 */
	public int getIpV4Mtu() {
		return ipV4Mtu;
	}

	/**
	 * @param ipV4Mtu the ipV4Mtu to set
	 */
	public void setIpV4Mtu(int ipV4Mtu) {
		this.ipV4Mtu = ipV4Mtu;
	}

	/**
	 * @return the ipV6Mtu
	 */
	public int getIpV6Mtu() {
		return ipV6Mtu;
	}

	/**
	 * @param ipV6Mtu the ipV6Mtu to set
	 */
	public void setIpV6Mtu(int ipV6Mtu) {
		this.ipV6Mtu = ipV6Mtu;
	}

	/**
	 * @return the bandwidth
	 */
	public int getBandwidth() {
		return bandwidth;
	}

	/**
	 * @param bandwidth the bandwidth to set
	 */
	public void setBandwidth(int bandwidth) {
		this.bandwidth = bandwidth;
	}
	
	/**
	 * @return the statusFlags
	 */
	public int getStatusFlags() {
		return statusFlags;
	}

	/**
	 * @param statusFlags the statusFlags to set
	 */
	public void setStatusFlags(int statusFlags) {
		this.statusFlags = statusFlags;
	}

	public boolean isInterfaceActive() {
		return ((this.statusFlags & QuaggaConstants.ZEBRA_INTERFACE_ACTIVE) != 0);
	}
	
	public void setInterfaceActive(boolean flag) {
		if(flag) 
			this.statusFlags |= QuaggaConstants.ZEBRA_INTERFACE_ACTIVE;
		else
			this.statusFlags &= ~QuaggaConstants.ZEBRA_INTERFACE_ACTIVE;
	}

	public boolean isInterfaceSub() {
		return ((this.statusFlags & QuaggaConstants.ZEBRA_INTERFACE_SUB) != 0);
	}
	
	public void setInterfaceSub(boolean flag) {
		if(flag) 
			this.statusFlags |= QuaggaConstants.ZEBRA_INTERFACE_SUB;
		else
			this.statusFlags &= ~QuaggaConstants.ZEBRA_INTERFACE_SUB;
	}

	public boolean isInterfaceLinkDetection() {
		return ((this.statusFlags & QuaggaConstants.ZEBRA_INTERFACE_LINKDETECTION) != 0);
	}
	
	public void setInterfaceLinkDetection(boolean flag) {
		if(flag) 
			this.statusFlags |= QuaggaConstants.ZEBRA_INTERFACE_LINKDETECTION;
		else
			this.statusFlags &= ~QuaggaConstants.ZEBRA_INTERFACE_LINKDETECTION;
	}

}
