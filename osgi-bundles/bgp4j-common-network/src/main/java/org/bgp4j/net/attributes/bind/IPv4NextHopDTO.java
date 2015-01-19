/**
 * 
 */
package org.bgp4j.net.attributes.bind;

import java.net.Inet4Address;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.bgp4j.net.InetAddressNextHop;
import org.bgp4j.net.attributes.NextHopPathAttribute;

/**
 * @author rainer
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class IPv4NextHopDTO {

	private InetAddressNextHop<Inet4Address> value;
	
	public IPv4NextHopDTO() {}
	
	public IPv4NextHopDTO(NextHopPathAttribute pa) {
		setValue(pa.getNextHop());
	}

	/**
	 * @return the nextHop
	 */
	public InetAddressNextHop<Inet4Address> getValue() {
		return value;
	}

	/**
	 * @param nextHop the nextHop to set
	 */
	public void setValue(InetAddressNextHop<Inet4Address> nextHop) {
		this.value = nextHop;
	}
}
