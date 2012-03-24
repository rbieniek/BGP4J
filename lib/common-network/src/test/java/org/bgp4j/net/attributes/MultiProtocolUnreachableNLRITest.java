package org.bgp4j.net.attributes;

import java.util.Arrays;
import java.util.List;

import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
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

	@Test
	public void testAllEquals() {
		MultiProtocolUnreachableNLRI a = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		MultiProtocolUnreachableNLRI b = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		MultiProtocolUnreachableNLRI e = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, 
				new NetworkLayerReachabilityInformation[] {});
		MultiProtocolUnreachableNLRI f = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, 
				new NetworkLayerReachabilityInformation[] {});
		MultiProtocolUnreachableNLRI g = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, 
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		MultiProtocolUnreachableNLRI h = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, 
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		
		Assert.assertTrue(a.equals(b));
		Assert.assertTrue(e.equals(f));
		Assert.assertTrue(g.equals(h));
		
		Assert.assertTrue(a.hashCode() == b.hashCode());
		Assert.assertTrue(e.hashCode() == f.hashCode());
		Assert.assertTrue(g.hashCode() == h.hashCode());

		Assert.assertTrue(a.compareTo(b) == 0);
		Assert.assertTrue(e.compareTo(f) == 0);
		Assert.assertTrue(g.compareTo(h) == 0);
	}

	@Test
	public void testAddressFamilySmaller() {
		MultiProtocolUnreachableNLRI a = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		MultiProtocolUnreachableNLRI b = new MultiProtocolUnreachableNLRI(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		MultiProtocolUnreachableNLRI e = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, 
				new NetworkLayerReachabilityInformation[] {});
		MultiProtocolUnreachableNLRI f = new MultiProtocolUnreachableNLRI(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING,
				new NetworkLayerReachabilityInformation[] {});
		MultiProtocolUnreachableNLRI g = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING,
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		MultiProtocolUnreachableNLRI h = new MultiProtocolUnreachableNLRI(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING,
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(e.equals(f));
		Assert.assertFalse(g.equals(h));
		
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertFalse(e.hashCode() == f.hashCode());
		Assert.assertFalse(g.hashCode() == h.hashCode());

		Assert.assertTrue(a.compareTo(b) < 0);
		Assert.assertTrue(e.compareTo(f) < 0);
		Assert.assertTrue(g.compareTo(h) < 0);
	}

	@Test
	public void testAddressFamilyLarger() {
		MultiProtocolUnreachableNLRI a = new MultiProtocolUnreachableNLRI(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		MultiProtocolUnreachableNLRI b = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		MultiProtocolUnreachableNLRI e = new MultiProtocolUnreachableNLRI(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING,
				new NetworkLayerReachabilityInformation[] {});
		MultiProtocolUnreachableNLRI f = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING,
				new NetworkLayerReachabilityInformation[] {});
		MultiProtocolUnreachableNLRI g = new MultiProtocolUnreachableNLRI(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING,
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		MultiProtocolUnreachableNLRI h = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING,
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(e.equals(f));
		Assert.assertFalse(g.equals(h));
		
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertFalse(e.hashCode() == f.hashCode());
		Assert.assertFalse(g.hashCode() == h.hashCode());

		Assert.assertTrue(a.compareTo(b) > 0);
		Assert.assertTrue(e.compareTo(f) > 0);
		Assert.assertTrue(g.compareTo(h) > 0);
	}

	@Test
	public void testSubsequentAddressFamilySmaller() {
		MultiProtocolUnreachableNLRI a = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		MultiProtocolUnreachableNLRI b = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING);
		MultiProtocolUnreachableNLRI e = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING,
				new NetworkLayerReachabilityInformation[] {});
		MultiProtocolUnreachableNLRI f = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING,
				new NetworkLayerReachabilityInformation[] {});
		MultiProtocolUnreachableNLRI g = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING,
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		MultiProtocolUnreachableNLRI h = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING,
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(e.equals(f));
		Assert.assertFalse(g.equals(h));
		
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertFalse(e.hashCode() == f.hashCode());
		Assert.assertFalse(g.hashCode() == h.hashCode());

		Assert.assertTrue(a.compareTo(b) < 0);
		Assert.assertTrue(e.compareTo(f) < 0);
		Assert.assertTrue(g.compareTo(h) < 0);
	}

	@Test
	public void testSubsequentAddressFamilyLarger() {
		MultiProtocolUnreachableNLRI a = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING);
		MultiProtocolUnreachableNLRI b = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		MultiProtocolUnreachableNLRI e = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING,
				new NetworkLayerReachabilityInformation[] {});
		MultiProtocolUnreachableNLRI f = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING,
				new NetworkLayerReachabilityInformation[] {});
		MultiProtocolUnreachableNLRI g = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING,
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		MultiProtocolUnreachableNLRI h = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING,
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(e.equals(f));
		Assert.assertFalse(g.equals(h));
		
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertFalse(e.hashCode() == f.hashCode());
		Assert.assertFalse(g.hashCode() == h.hashCode());

		Assert.assertTrue(a.compareTo(b) > 0);
		Assert.assertTrue(e.compareTo(f) > 0);
		Assert.assertTrue(g.compareTo(h) > 0);
	}

	@Test
	public void testNLRIContentSmaller() {
		MultiProtocolUnreachableNLRI g = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING,
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		MultiProtocolUnreachableNLRI h = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING,
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(1, new byte[] { 1 })
		});
		
		Assert.assertFalse(g.equals(h));
		
		Assert.assertFalse(g.hashCode() == h.hashCode());

		Assert.assertTrue(g.compareTo(h) < 0);
	}

	@Test
	public void testNLRIContentLarger() {
		MultiProtocolUnreachableNLRI g = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING,
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(1, new byte[] { 1 })
		});
		MultiProtocolUnreachableNLRI h = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING,
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		
		Assert.assertFalse(g.equals(h));
		
		Assert.assertFalse(g.hashCode() == h.hashCode());

		Assert.assertTrue(g.compareTo(h) > 0);
	}

	@Test
	public void testNLRIArraySmaller() {
		MultiProtocolUnreachableNLRI g = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING,
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		MultiProtocolUnreachableNLRI h = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING,
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null),
				new NetworkLayerReachabilityInformation(1, new byte[] { 1 })
		});
		
		Assert.assertFalse(g.equals(h));
		
		Assert.assertFalse(g.hashCode() == h.hashCode());

		Assert.assertTrue(g.compareTo(h) < 0);
	}

	@Test
	public void testNLRIArrayLarger() {
		MultiProtocolUnreachableNLRI g = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING,
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null),
				new NetworkLayerReachabilityInformation(1, new byte[] { 1 })
		});
		MultiProtocolUnreachableNLRI h = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING,
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		
		Assert.assertFalse(g.equals(h));
		
		Assert.assertFalse(g.hashCode() == h.hashCode());

		Assert.assertTrue(g.compareTo(h) > 0);
	}

}
