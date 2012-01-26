/**
 * 
 */
package org.bgp4j.netty.protocol;

import java.util.LinkedList;
import java.util.List;

/**
 * @author rainer
 *
 */
public class OpenPacket extends BGPv4Packet {
	private int protocolVersion;
	private int autonomuosSystem;
	private int holdTime;
	private int bgpIdentifier;
	private List<Capability> capabilities = new LinkedList<Capability>();
	
	/**
	 * @return the protocolVersion
	 */
	public int getProtocolVersion() {
		return protocolVersion;
	}
	
	/**
	 * @param protocolVersion the protocolVersion to set
	 */
	public void setProtocolVersion(int protocolVersion) {
		this.protocolVersion = protocolVersion;
	}
	
	/**
	 * @return the autonomuosSystem
	 */
	public int getAutonomuosSystem() {
		return autonomuosSystem;
	}
	
	/**
	 * @param autonomuosSystem the autonomuosSystem to set
	 */
	public void setAutonomuosSystem(int autonomuosSystem) {
		this.autonomuosSystem = autonomuosSystem;
	}
	
	/**
	 * @return the holdTime
	 */
	public int getHoldTime() {
		return holdTime;
	}
	
	/**
	 * @param holdTime the holdTime to set
	 */
	public void setHoldTime(int holdTime) {
		this.holdTime = holdTime;
	}
	
	/**
	 * @return the bgpIdentifier
	 */
	public int getBgpIdentifier() {
		return bgpIdentifier;
	}
	
	/**
	 * @param bgpIdentifier the bgpIdentifier to set
	 */
	public void setBgpIdentifier(int bgpIdentifier) {
		this.bgpIdentifier = bgpIdentifier;
	}
	
	/**
	 * @return the capabilities
	 */
	public List<Capability> getCapabilities() {
		return capabilities;
	}
	
	/**
	 * @param capabilities the capabilities to set
	 */
	public void setCapabilities(List<Capability> capabilities) {
		this.capabilities = capabilities;
	}
}
