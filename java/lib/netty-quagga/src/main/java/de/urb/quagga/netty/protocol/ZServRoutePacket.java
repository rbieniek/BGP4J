/**
 * 
 */
package de.urb.quagga.netty.protocol;

import java.net.InetAddress;

import de.urb.quagga.netty.EQuaggaRouteType;
import de.urb.quagga.netty.QuaggaConstants;

/**
 * @author rainer
 *
 */
public class ZServRoutePacket extends QuaggaPacket {

	public enum RoutingPacketType {
		IPv4, IPv6;
	}

	private RoutingPacketType routingPacketType;
	private int flags;
	private EQuaggaRouteType type;
	private int zapiFlags;
	private int prefixLength;
	private InetAddress prefix;
	private int nextHopNumber;
	private InetAddress nextHop;
	private int interfaceIndex;
	
	protected ZServRoutePacket(int protocolVersion, RoutingPacketType routingPacketType) {
		super(protocolVersion);
		
		setRoutingPacketType(routingPacketType);
	}

	/**
	 * @return the routingPacketType
	 */
	public RoutingPacketType getRoutingPacketType() {
		return routingPacketType;
	}

	/**
	 * @param routingPacketType the routingPacketType to set
	 */
	private void setRoutingPacketType(RoutingPacketType routingPacketType) {
		this.routingPacketType = routingPacketType;
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
	 * @return the type
	 */
	public EQuaggaRouteType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(EQuaggaRouteType type) {
		this.type = type;
	}

	/**
	 * @return the zapiFlags
	 */
	public int getZapiFlags() {
		return zapiFlags;
	}

	/**
	 * @param zapiFlags the zapiFlags to set
	 */
	public void setZapiFlags(int zapiFlags) {
		this.zapiFlags = zapiFlags;
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
	 * @return the prefix
	 */
	public InetAddress getPrefix() {
		return prefix;
	}

	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(InetAddress prefix) {
		this.prefix = prefix;
	}

	/**
	 * @return the nextHopNumber
	 */
	public int getNextHopNumber() {
		return nextHopNumber;
	}

	/**
	 * @param nextHopNumber the nextHopNumber to set
	 */
	public void setNextHopNumber(int nextHopNumber) {
		this.nextHopNumber = nextHopNumber;
	}

	/**
	 * @return the nextHop
	 */
	public InetAddress getNextHop() {
		return nextHop;
	}

	/**
	 * @param nextHop the nextHop to set
	 */
	public void setNextHop(InetAddress nextHop) {
		this.nextHop = nextHop;
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
	
	public boolean isInternal() {
		return checkBit(this.flags, QuaggaConstants.ZEBRA_FLAG_INTERNAL);
	}
	
	public void setInternal(boolean flag) {
		this.flags = setBit(this.flags, QuaggaConstants.ZEBRA_FLAG_INTERNAL, flag);
	}
	
	public boolean isSelfRoute() {
		return checkBit(this.flags, QuaggaConstants.ZEBRA_FLAG_SELFROUTE);
	}
	
	public void setSelfRoute(boolean flag) {
		this.flags = setBit(this.flags, QuaggaConstants.ZEBRA_FLAG_SELECTED, flag);
	}
	
	public boolean isBlackHole() {
		return checkBit(this.flags, QuaggaConstants.ZEBRA_FLAG_BLACKHOLE);
	}
	
	public void setBlackHole(boolean flag) {
		this.flags = setBit(this.flags, QuaggaConstants.ZEBRA_FLAG_BLACKHOLE, flag);
	}
	
	public boolean isIbgb() {
		return checkBit(this.flags, QuaggaConstants.ZEBRA_FLAG_IBGP);
	}
	
	public void setIbgp(boolean flag) {
		this.flags = setBit(this.flags, QuaggaConstants.ZEBRA_FLAG_IBGP, flag);
	}

	public boolean isSelected() {
		return checkBit(this.flags, QuaggaConstants.ZEBRA_FLAG_SELECTED);
	}
	
	public void setSelected(boolean flag) {
		this.flags = setBit(this.flags, QuaggaConstants.ZEBRA_FLAG_SELECTED, flag);
	}
	
	public boolean isChanged() {
		return checkBit(this.flags, QuaggaConstants.ZEBRA_FLAG_CHANGED);
	}
	
	public void setChanged(boolean flag) {
		this.flags = setBit(this.flags, QuaggaConstants.ZEBRA_FLAG_CHANGED, flag);
	}
	
	public boolean isStatic() {
		return checkBit(this.flags, QuaggaConstants.ZEBRA_FLAG_STATIC);
	}
	
	public void setStatic(boolean flag) {
		this.flags = setBit(this.flags, QuaggaConstants.ZEBRA_FLAG_STATIC, flag);
	}
	
	public boolean isReject() {
		return checkBit(this.flags, QuaggaConstants.ZEBRA_FLAG_REJECT);
	}
	
	public void setReject(boolean flag) {
		this.flags = setBit(this.flags, QuaggaConstants.ZEBRA_FLAG_REJECT, flag);
	}	

	private boolean checkBit(int value, int mask) {
		return ((value & mask) != 0);
	}
	
	private int setBit(int value, int mask, boolean flag) {
		if(flag)
			return value | mask;
		else
			return (value & ~mask);
	}
}
