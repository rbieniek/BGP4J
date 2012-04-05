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
		RIBEntry a = new RIBEntry("foo", AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, RIBSide.Local);
		RIBEntry b = new RIBEntry("foo", AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, RIBSide.Local);
		
		Assert.assertTrue(a.equals(b));
		Assert.assertTrue(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) == 0);
	}

	@Test
	public void testSmallerName() {
		RIBEntry a = new RIBEntry("bar", AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, RIBSide.Local);
		RIBEntry b = new RIBEntry("foo", AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, RIBSide.Local);
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}

	@Test
	public void testSmallerAddressFamily() {
		RIBEntry a = new RIBEntry("foo", AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, RIBSide.Local);
		RIBEntry b = new RIBEntry("foo", AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, RIBSide.Local);
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}

	@Test
	public void testSmallerSubsequentAddressFamily() {
		RIBEntry a = new RIBEntry("foo", AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, RIBSide.Local);
		RIBEntry b = new RIBEntry("foo", AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING, RIBSide.Local);
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}

	@Test
	public void testSmallerSide() {
		RIBEntry a = new RIBEntry("foo", AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, RIBSide.Local);
		RIBEntry b = new RIBEntry("foo", AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, RIBSide.Remote);
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}
}
