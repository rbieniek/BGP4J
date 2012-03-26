package org.bgp4j.rib;

import java.util.Arrays;

import junit.framework.Assert;

import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.Origin;
import org.bgp4j.net.SubsequentAddressFamily;
import org.bgp4j.net.attributes.LocalPrefPathAttribute;
import org.bgp4j.net.attributes.MultiExitDiscPathAttribute;
import org.bgp4j.net.attributes.OriginPathAttribute;
import org.bgp4j.net.attributes.PathAttribute;
import org.junit.Test;

public class TopologicalTreeSortingKeyTest {

	@Test
	public void testAllEquals() {
		TopologicalTreeSortingKey a = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100)));
		TopologicalTreeSortingKey b = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100)));
		TopologicalTreeSortingKey c = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), (PathAttribute)new OriginPathAttribute(Origin.IGP)));
		TopologicalTreeSortingKey d = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), (PathAttribute)new OriginPathAttribute(Origin.IGP)));
		
		Assert.assertTrue(a.equals(b));
		Assert.assertTrue(c.equals(d));
		
		Assert.assertTrue(a.hashCode() == b.hashCode());
		Assert.assertTrue(c.hashCode() == d.hashCode());
		
		Assert.assertTrue(a.compareTo(b) == 0);
		Assert.assertTrue(c.compareTo(d) == 0);
	}

	@Test
	public void testAllEqualsConstructor() {
		TopologicalTreeSortingKey a = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100)));
		TopologicalTreeSortingKey b = new TopologicalTreeSortingKey(
				AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100)));
		TopologicalTreeSortingKey c = new TopologicalTreeSortingKey(
				AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), (PathAttribute)new OriginPathAttribute(Origin.IGP)));
		TopologicalTreeSortingKey d = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), (PathAttribute)new OriginPathAttribute(Origin.IGP)));
		
		Assert.assertTrue(a.equals(b));
		Assert.assertTrue(c.equals(d));
		
		Assert.assertTrue(a.hashCode() == b.hashCode());
		Assert.assertTrue(c.hashCode() == d.hashCode());
		
		Assert.assertTrue(a.compareTo(b) == 0);
		Assert.assertTrue(c.compareTo(d) == 0);
	}
	
	@Test
	public void testAllSmallerAddressFamily() {
		TopologicalTreeSortingKey a = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100)));
		TopologicalTreeSortingKey b = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100)));
		TopologicalTreeSortingKey c = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), (PathAttribute)new OriginPathAttribute(Origin.IGP)));
		TopologicalTreeSortingKey d = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), (PathAttribute)new OriginPathAttribute(Origin.IGP)));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(c.equals(d));
		
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertFalse(c.hashCode() == d.hashCode());
		
		Assert.assertTrue(a.compareTo(b) < 0);
		Assert.assertTrue(c.compareTo(d) < 0);
	}
	
	@Test
	public void testAllLargerAddressFamily() {
		TopologicalTreeSortingKey a = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100)));
		TopologicalTreeSortingKey b = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100)));
		TopologicalTreeSortingKey c = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), (PathAttribute)new OriginPathAttribute(Origin.IGP)));
		TopologicalTreeSortingKey d = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), (PathAttribute)new OriginPathAttribute(Origin.IGP)));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(c.equals(d));
		
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertFalse(c.hashCode() == d.hashCode());
		
		Assert.assertTrue(a.compareTo(b) > 0);
		Assert.assertTrue(c.compareTo(d) > 0);
	}
	
	@Test
	public void testAllSmallerSubsequentAddressFamily() {
		TopologicalTreeSortingKey a = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100)));
		TopologicalTreeSortingKey b = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100)));
		TopologicalTreeSortingKey c = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), (PathAttribute)new OriginPathAttribute(Origin.IGP)));
		TopologicalTreeSortingKey d = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), (PathAttribute)new OriginPathAttribute(Origin.IGP)));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(c.equals(d));
		
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertFalse(c.hashCode() == d.hashCode());
		
		Assert.assertTrue(a.compareTo(b) < 0);
		Assert.assertTrue(c.compareTo(d) < 0);
	}
	
	@Test
	public void testAllLargerSubsequentAddressFamily() {
		TopologicalTreeSortingKey a = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100)));
		TopologicalTreeSortingKey b = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100)));
		TopologicalTreeSortingKey c = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), (PathAttribute)new OriginPathAttribute(Origin.IGP)));
		TopologicalTreeSortingKey d = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), (PathAttribute)new OriginPathAttribute(Origin.IGP)));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(c.equals(d));
		
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertFalse(c.hashCode() == d.hashCode());
		
		Assert.assertTrue(a.compareTo(b) >0);
		Assert.assertTrue(c.compareTo(d) > 0);
	}

	@Test
	public void testAllSmallerContent() {
		TopologicalTreeSortingKey a = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100)));
		TopologicalTreeSortingKey b = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new MultiExitDiscPathAttribute(100)));
		TopologicalTreeSortingKey c = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), (PathAttribute)new OriginPathAttribute(Origin.IGP)));
		TopologicalTreeSortingKey d = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new MultiExitDiscPathAttribute(100), (PathAttribute)new OriginPathAttribute(Origin.IGP)));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(c.equals(d));
		
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertFalse(c.hashCode() == d.hashCode());
		
		Assert.assertTrue(a.compareTo(b) < 0);
		Assert.assertTrue(c.compareTo(d) < 0);
	}

	@Test
	public void testAllLargerContent() {
		TopologicalTreeSortingKey a = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new MultiExitDiscPathAttribute(100)));
		TopologicalTreeSortingKey b = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100)));
		TopologicalTreeSortingKey c = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new MultiExitDiscPathAttribute(100), (PathAttribute)new OriginPathAttribute(Origin.IGP)));
		TopologicalTreeSortingKey d = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), (PathAttribute)new OriginPathAttribute(Origin.IGP)));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(c.equals(d));
		
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertFalse(c.hashCode() == d.hashCode());
		
		Assert.assertTrue(a.compareTo(b) > 0);
		Assert.assertTrue(c.compareTo(d) > 0);
	}

	@Test
	public void testAllSmallerSize() {
		TopologicalTreeSortingKey c = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new OriginPathAttribute(Origin.IGP)));
		TopologicalTreeSortingKey d = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new MultiExitDiscPathAttribute(100), (PathAttribute)new OriginPathAttribute(Origin.IGP)));
		
		Assert.assertFalse(c.equals(d));
		
		Assert.assertFalse(c.hashCode() == d.hashCode());
		
		Assert.assertTrue(c.compareTo(d) < 0);
	}


	@Test
	public void testAllLargerSize() {
		TopologicalTreeSortingKey c = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new MultiExitDiscPathAttribute(100), (PathAttribute)new OriginPathAttribute(Origin.IGP)));
		TopologicalTreeSortingKey d = new TopologicalTreeSortingKey(
				new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), 
				Arrays.asList((PathAttribute)new OriginPathAttribute(Origin.IGP)));
		
		Assert.assertFalse(c.equals(d));
		
		Assert.assertFalse(c.hashCode() == d.hashCode());
		
		Assert.assertTrue(c.compareTo(d) > 0);
	}
}
