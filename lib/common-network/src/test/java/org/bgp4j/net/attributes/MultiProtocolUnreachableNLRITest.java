package org.bgp4j.net.attributes;

import java.util.Arrays;
import java.util.List;

import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.SubsequentAddressFamily;
import org.junit.Assert;
import org.junit.Test;

public class MultiProtocolUnreachableNLRITest {

	@Test
	public void testToAddressFamilyKey() {
		List<AddressFamily> afList = Arrays.asList(AddressFamily.APPLETALK, 
				AddressFamily.BANYAN, 
				AddressFamily.BBN1882,
				AddressFamily.DECNET4,
				AddressFamily.E163,
				AddressFamily.E164,
				AddressFamily.F69,
				AddressFamily.HDLC,
				AddressFamily.IEEE802,
				AddressFamily.IPv4,
				AddressFamily.IPv6,
				AddressFamily.IPX,
				AddressFamily.NSAP,
				AddressFamily.X121);
		List<SubsequentAddressFamily> safList = Arrays.asList(SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING,
				SubsequentAddressFamily.NLRI_UNICAST_FORWARDING,
				SubsequentAddressFamily.NLRI_UNICAST_MULTICAST_FORWARDING);
		
		for(AddressFamily afi : afList) {
			for(SubsequentAddressFamily safi : safList) {
				MultiProtocolUnreachableNLRI pa = new MultiProtocolUnreachableNLRI(afi, safi);
				
				Assert.assertEquals(new AddressFamilyKey(afi, safi), pa.addressFamilyKey());
			}
		}
	}

}
