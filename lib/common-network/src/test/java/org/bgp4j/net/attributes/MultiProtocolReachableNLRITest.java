package org.bgp4j.net.attributes;

import java.util.Arrays;
import java.util.List;

import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.SubsequentAddressFamily;
import org.junit.Assert;
import org.junit.Test;

public class MultiProtocolReachableNLRITest {

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
				MultiProtocolReachableNLRI pa = new MultiProtocolReachableNLRI(afi, safi);
				
				Assert.assertEquals(new AddressFamilyKey(afi, safi), pa.addressFamilyKey());
			}
		}
	}

	@Test
	public void testAllEquals() {
		MultiProtocolReachableNLRI a = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		MultiProtocolReachableNLRI b = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		MultiProtocolReachableNLRI c = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 });
		MultiProtocolReachableNLRI d = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 });
		MultiProtocolReachableNLRI e = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {});
		MultiProtocolReachableNLRI f = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {});
		MultiProtocolReachableNLRI g = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		MultiProtocolReachableNLRI h = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		
		Assert.assertTrue(a.equals(b));
		Assert.assertTrue(c.equals(d));
		Assert.assertTrue(e.equals(f));
		Assert.assertTrue(g.equals(h));
		
		Assert.assertTrue(a.hashCode() == b.hashCode());
		Assert.assertTrue(c.hashCode() == d.hashCode());
		Assert.assertTrue(e.hashCode() == f.hashCode());
		Assert.assertTrue(g.hashCode() == h.hashCode());

		Assert.assertTrue(a.compareTo(b) == 0);
		Assert.assertTrue(c.compareTo(d) == 0);
		Assert.assertTrue(e.compareTo(f) == 0);
		Assert.assertTrue(g.compareTo(h) == 0);
	}

	@Test
	public void testAddressFamilySmaller() {
		MultiProtocolReachableNLRI a = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		MultiProtocolReachableNLRI b = new MultiProtocolReachableNLRI(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		MultiProtocolReachableNLRI c = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 });
		MultiProtocolReachableNLRI d = new MultiProtocolReachableNLRI(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 });
		MultiProtocolReachableNLRI e = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {});
		MultiProtocolReachableNLRI f = new MultiProtocolReachableNLRI(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {});
		MultiProtocolReachableNLRI g = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		MultiProtocolReachableNLRI h = new MultiProtocolReachableNLRI(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(c.equals(d));
		Assert.assertFalse(e.equals(f));
		Assert.assertFalse(g.equals(h));
		
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertFalse(c.hashCode() == d.hashCode());
		Assert.assertFalse(e.hashCode() == f.hashCode());
		Assert.assertFalse(g.hashCode() == h.hashCode());

		Assert.assertTrue(a.compareTo(b) < 0);
		Assert.assertTrue(c.compareTo(d) < 0);
		Assert.assertTrue(e.compareTo(f) < 0);
		Assert.assertTrue(g.compareTo(h) < 0);
	}

	@Test
	public void testAddressFamilyLarger() {
		MultiProtocolReachableNLRI a = new MultiProtocolReachableNLRI(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		MultiProtocolReachableNLRI b = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		MultiProtocolReachableNLRI c = new MultiProtocolReachableNLRI(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 });
		MultiProtocolReachableNLRI d = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 });
		MultiProtocolReachableNLRI e = new MultiProtocolReachableNLRI(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {});
		MultiProtocolReachableNLRI f = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {});
		MultiProtocolReachableNLRI g = new MultiProtocolReachableNLRI(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		MultiProtocolReachableNLRI h = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(c.equals(d));
		Assert.assertFalse(e.equals(f));
		Assert.assertFalse(g.equals(h));
		
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertFalse(c.hashCode() == d.hashCode());
		Assert.assertFalse(e.hashCode() == f.hashCode());
		Assert.assertFalse(g.hashCode() == h.hashCode());

		Assert.assertTrue(a.compareTo(b) > 0);
		Assert.assertTrue(c.compareTo(d) > 0);
		Assert.assertTrue(e.compareTo(f) > 0);
		Assert.assertTrue(g.compareTo(h) > 0);
	}

	@Test
	public void testSubsequentAddressFamilySmaller() {
		MultiProtocolReachableNLRI a = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		MultiProtocolReachableNLRI b = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING);
		MultiProtocolReachableNLRI c = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 });
		MultiProtocolReachableNLRI d = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING, new byte[] { 1 });
		MultiProtocolReachableNLRI e = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {});
		MultiProtocolReachableNLRI f = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {});
		MultiProtocolReachableNLRI g = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		MultiProtocolReachableNLRI h = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(c.equals(d));
		Assert.assertFalse(e.equals(f));
		Assert.assertFalse(g.equals(h));
		
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertFalse(c.hashCode() == d.hashCode());
		Assert.assertFalse(e.hashCode() == f.hashCode());
		Assert.assertFalse(g.hashCode() == h.hashCode());

		Assert.assertTrue(a.compareTo(b) < 0);
		Assert.assertTrue(c.compareTo(d) < 0);
		Assert.assertTrue(e.compareTo(f) < 0);
		Assert.assertTrue(g.compareTo(h) < 0);
	}

	@Test
	public void testSubsequentAddressFamilyLarger() {
		MultiProtocolReachableNLRI a = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING);
		MultiProtocolReachableNLRI b = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		MultiProtocolReachableNLRI c = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING, new byte[] { 1 });
		MultiProtocolReachableNLRI d = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 });
		MultiProtocolReachableNLRI e = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {});
		MultiProtocolReachableNLRI f = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {});
		MultiProtocolReachableNLRI g = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		MultiProtocolReachableNLRI h = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(c.equals(d));
		Assert.assertFalse(e.equals(f));
		Assert.assertFalse(g.equals(h));
		
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertFalse(c.hashCode() == d.hashCode());
		Assert.assertFalse(e.hashCode() == f.hashCode());
		Assert.assertFalse(g.hashCode() == h.hashCode());

		Assert.assertTrue(a.compareTo(b) > 0);
		Assert.assertTrue(c.compareTo(d) > 0);
		Assert.assertTrue(e.compareTo(f) > 0);
		Assert.assertTrue(g.compareTo(h) > 0);
	}

	@Test
	public void testNextHopContentSmaller() {
		MultiProtocolReachableNLRI c = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 });
		MultiProtocolReachableNLRI d = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 2 });
		MultiProtocolReachableNLRI e = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {});
		MultiProtocolReachableNLRI f = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 2 },
				new NetworkLayerReachabilityInformation[] {});
		MultiProtocolReachableNLRI g = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		MultiProtocolReachableNLRI h = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 2 },
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		
		Assert.assertFalse(c.equals(d));
		Assert.assertFalse(e.equals(f));
		Assert.assertFalse(g.equals(h));
		
		Assert.assertFalse(c.hashCode() == d.hashCode());
		Assert.assertFalse(e.hashCode() == f.hashCode());
		Assert.assertFalse(g.hashCode() == h.hashCode());

		Assert.assertTrue(c.compareTo(d) < 0);
		Assert.assertTrue(e.compareTo(f) < 0);
		Assert.assertTrue(g.compareTo(h) < 0);
	}

	@Test
	public void testNextHopContentLarger() {
		MultiProtocolReachableNLRI c = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 2 });
		MultiProtocolReachableNLRI d = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 });
		MultiProtocolReachableNLRI e = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 2 },
				new NetworkLayerReachabilityInformation[] {});
		MultiProtocolReachableNLRI f = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {});
		MultiProtocolReachableNLRI g = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 2 },
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		MultiProtocolReachableNLRI h = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		
		Assert.assertFalse(c.equals(d));
		Assert.assertFalse(e.equals(f));
		Assert.assertFalse(g.equals(h));
		
		Assert.assertFalse(c.hashCode() == d.hashCode());
		Assert.assertFalse(e.hashCode() == f.hashCode());
		Assert.assertFalse(g.hashCode() == h.hashCode());

		Assert.assertTrue(c.compareTo(d) > 0);
		Assert.assertTrue(e.compareTo(f) > 0);
		Assert.assertTrue(g.compareTo(h) > 0);
	}

	@Test
	public void testNextHopArraySmaller() {
		MultiProtocolReachableNLRI c = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 });
		MultiProtocolReachableNLRI d = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1, 1 });
		MultiProtocolReachableNLRI e = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {});
		MultiProtocolReachableNLRI f = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1, 1 },
				new NetworkLayerReachabilityInformation[] {});
		MultiProtocolReachableNLRI g = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		MultiProtocolReachableNLRI h = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1, 1 },
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		
		Assert.assertFalse(c.equals(d));
		Assert.assertFalse(e.equals(f));
		Assert.assertFalse(g.equals(h));
		
		Assert.assertFalse(c.hashCode() == d.hashCode());
		Assert.assertFalse(e.hashCode() == f.hashCode());
		Assert.assertFalse(g.hashCode() == h.hashCode());

		Assert.assertTrue(c.compareTo(d) < 0);
		Assert.assertTrue(e.compareTo(f) < 0);
		Assert.assertTrue(g.compareTo(h) < 0);
	}

	@Test
	public void testNextHopArrayLarger() {
		MultiProtocolReachableNLRI c = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1, 1 });
		MultiProtocolReachableNLRI d = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 });
		MultiProtocolReachableNLRI e = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1, 1 },
				new NetworkLayerReachabilityInformation[] {});
		MultiProtocolReachableNLRI f = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {});
		MultiProtocolReachableNLRI g = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1, 1 },
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		MultiProtocolReachableNLRI h = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		
		Assert.assertFalse(c.equals(d));
		Assert.assertFalse(e.equals(f));
		Assert.assertFalse(g.equals(h));
		
		Assert.assertFalse(c.hashCode() == d.hashCode());
		Assert.assertFalse(e.hashCode() == f.hashCode());
		Assert.assertFalse(g.hashCode() == h.hashCode());

		Assert.assertTrue(c.compareTo(d) > 0);
		Assert.assertTrue(e.compareTo(f) > 0);
		Assert.assertTrue(g.compareTo(h) > 0);
	}

	@Test
	public void testNLRIContentSmaller() {
		MultiProtocolReachableNLRI g = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		MultiProtocolReachableNLRI h = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(1, new byte[] { 1 })
		});
		
		Assert.assertFalse(g.equals(h));
		
		Assert.assertFalse(g.hashCode() == h.hashCode());

		Assert.assertTrue(g.compareTo(h) < 0);
	}

	@Test
	public void testNLRIContentLarger() {
		MultiProtocolReachableNLRI g = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(1, new byte[] { 1 })
		});
		MultiProtocolReachableNLRI h = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		
		Assert.assertFalse(g.equals(h));
		
		Assert.assertFalse(g.hashCode() == h.hashCode());

		Assert.assertTrue(g.compareTo(h) > 0);
	}

	@Test
	public void testNLRIArraySmaller() {
		MultiProtocolReachableNLRI g = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		MultiProtocolReachableNLRI h = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
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
		MultiProtocolReachableNLRI g = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null),
				new NetworkLayerReachabilityInformation(1, new byte[] { 1 })
		});
		MultiProtocolReachableNLRI h = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, new byte[] { 1 },
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(0, null)
		});
		
		Assert.assertFalse(g.equals(h));
		
		Assert.assertFalse(g.hashCode() == h.hashCode());

		Assert.assertTrue(g.compareTo(h) > 0);
	}
}
