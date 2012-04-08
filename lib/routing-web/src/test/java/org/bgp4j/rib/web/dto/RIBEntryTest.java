/**
 * 
 */
package org.bgp4j.rib.web.dto;

import junit.framework.Assert;

import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.RIBSide;
import org.bgp4j.net.SubsequentAddressFamily;
import org.junit.Test;

/**
 * @author rainer
 *
 */
public class RIBEntryTest {

	@Test
	public void testEquals() {
		RouteInformationBaseDTO a = new RouteInformationBaseDTO("foo", AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, RIBSide.Local);
		RouteInformationBaseDTO b = new RouteInformationBaseDTO("foo", AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, RIBSide.Local);
		
		Assert.assertTrue(a.equals(b));
		Assert.assertTrue(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) == 0);
	}

	@Test
	public void testSmallerName() {
		RouteInformationBaseDTO a = new RouteInformationBaseDTO("bar", AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, RIBSide.Local);
		RouteInformationBaseDTO b = new RouteInformationBaseDTO("foo", AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, RIBSide.Local);
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}

	@Test
	public void testSmallerAddressFamily() {
		RouteInformationBaseDTO a = new RouteInformationBaseDTO("foo", AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, RIBSide.Local);
		RouteInformationBaseDTO b = new RouteInformationBaseDTO("foo", AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, RIBSide.Local);
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}

	@Test
	public void testSmallerSubsequentAddressFamily() {
		RouteInformationBaseDTO a = new RouteInformationBaseDTO("foo", AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, RIBSide.Local);
		RouteInformationBaseDTO b = new RouteInformationBaseDTO("foo", AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING, RIBSide.Local);
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}

	@Test
	public void testSmallerSide() {
		RouteInformationBaseDTO a = new RouteInformationBaseDTO("foo", AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, RIBSide.Local);
		RouteInformationBaseDTO b = new RouteInformationBaseDTO("foo", AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, RIBSide.Remote);
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}
}
