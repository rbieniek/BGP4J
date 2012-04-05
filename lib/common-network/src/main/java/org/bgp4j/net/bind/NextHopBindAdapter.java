/**
 * 
 */
package org.bgp4j.net.bind;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.bgp4j.net.BinaryNextHop;
import org.bgp4j.net.InetAddressNextHop;
import org.bgp4j.net.NextHop;

/**
 * @author rainer
 *
 */
public class NextHopBindAdapter extends XmlAdapter<NextHopDTO, NextHop> {

	/* (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public NextHop unmarshal(NextHopDTO v) throws Exception {
		NextHop nextHop = null;
		
		switch(v.getType()) {
		case Binary:
			nextHop = new BinaryNextHop(v.getBinary());
			break;
		case Ipv4:
			nextHop = new InetAddressNextHop<Inet4Address>((Inet4Address)InetAddress.getByAddress(v.getBinary()));
			break;
		case Ipv6:
			nextHop = new InetAddressNextHop<Inet6Address>((Inet6Address)InetAddress.getByAddress(v.getBinary()));
			break;
		}
		
		return nextHop;
	}

	/* (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NextHopDTO marshal(NextHop v) throws Exception {
		NextHopDTO dto = new NextHopDTO();
		
		if(v instanceof BinaryNextHop) {
			dto.setType(NextHopType.Binary);
			dto.setBinary(((BinaryNextHop)v).getAddress());
		} else if(v instanceof InetAddressNextHop) {
			InetAddress addr = ((InetAddressNextHop<InetAddress>)v).getAddress();
			
			if(addr instanceof Inet4Address) {
				dto.setType(NextHopType.Ipv4);
			} else if(addr instanceof Inet6Address) {
				dto.setType(NextHopType.Ipv6);
			}
			dto.setBinary(addr.getAddress());
		}
		
		return dto;
	}

}
