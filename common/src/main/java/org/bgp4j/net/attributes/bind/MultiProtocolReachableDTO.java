/**
 * 
 */
package org.bgp4j.net.attributes.bind;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.BinaryNextHop;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.SubsequentAddressFamily;
import org.bgp4j.net.attributes.MultiProtocolReachableNLRI;

/**
 * @author rainer
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MultiProtocolReachableDTO {
	private AddressFamily addressFamily;
	private SubsequentAddressFamily subsequentAddressFamily;
	private BinaryNextHop nextHop;
	private List<NetworkLayerReachabilityInformation> nlris = new LinkedList<NetworkLayerReachabilityInformation>();
	
	public MultiProtocolReachableDTO() {}
	
	public MultiProtocolReachableDTO(MultiProtocolReachableNLRI pa) {
		setAddressFamily(pa.getAddressFamily());
		setSubsequentAddressFamily(pa.getSubsequentAddressFamily());
		setNextHop(pa.getNextHop());
		setNlris(pa.getNlris());
	}
	
	/**
	 * @return the addressFamily
	 */
	public AddressFamily getAddressFamily() {
		return addressFamily;
	}
	/**
	 * @param addressFamily the addressFamily to set
	 */
	public void setAddressFamily(AddressFamily addressFamily) {
		this.addressFamily = addressFamily;
	}
	/**
	 * @return the subsequentAddressFamily
	 */
	public SubsequentAddressFamily getSubsequentAddressFamily() {
		return subsequentAddressFamily;
	}
	/**
	 * @param subsequentAddressFamily the subsequentAddressFamily to set
	 */
	public void setSubsequentAddressFamily(
			SubsequentAddressFamily subsequentAddressFamily) {
		this.subsequentAddressFamily = subsequentAddressFamily;
	}
	/**
	 * @return the nextHop
	 */
	public BinaryNextHop getNextHop() {
		return nextHop;
	}
	/**
	 * @param nextHop the nextHop to set
	 */
	public void setNextHop(BinaryNextHop nextHop) {
		this.nextHop = nextHop;
	}
	/**
	 * @return the nlris
	 */
	public List<NetworkLayerReachabilityInformation> getNlris() {
		return nlris;
	}
	/**
	 * @param nlris the nlris to set
	 */
	public void setNlris(List<NetworkLayerReachabilityInformation> nlris) {
		this.nlris = nlris;
	}
}
