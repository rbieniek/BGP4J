/**
 * 
 */
package org.bgp4j.config.nodes.impl;

import java.util.Arrays;

import junit.framework.Assert;

import org.bgp4j.config.nodes.PathAttributeConfiguration;
import org.bgp4j.config.nodes.impl.PathAttributeConfigurationImpl;
import org.bgp4j.net.attributes.LocalPrefPathAttribute;
import org.bgp4j.net.attributes.MultiExitDiscPathAttribute;
import org.bgp4j.net.attributes.PathAttribute;
import org.junit.Test;

/**
 * @author rainer
 *
 */
public class PathAttributeConfigurationImplTest {

	@Test
	public void testEquals() {
		PathAttributeConfiguration a = new PathAttributeConfigurationImpl();
		PathAttributeConfiguration b = new PathAttributeConfigurationImpl();
		PathAttributeConfiguration c = new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100)));
		PathAttributeConfiguration d = new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100)));
		PathAttributeConfiguration e = new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100),
				(PathAttribute)new MultiExitDiscPathAttribute(10)));
		PathAttributeConfiguration f = new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new MultiExitDiscPathAttribute(10),
				(PathAttribute)new LocalPrefPathAttribute(100)));
		
		Assert.assertTrue(a.equals(b));
		Assert.assertTrue(c.equals(d));
		Assert.assertTrue(e.equals(f));
		
		Assert.assertFalse(a.equals(c));
		Assert.assertFalse(a.equals(e));
		Assert.assertFalse(c.equals(e));
	}

	@Test
	public void testHashCode() {
		PathAttributeConfiguration a = new PathAttributeConfigurationImpl();
		PathAttributeConfiguration b = new PathAttributeConfigurationImpl();
		PathAttributeConfiguration c = new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100)));
		PathAttributeConfiguration d = new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100)));
		PathAttributeConfiguration e = new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100),
				(PathAttribute)new MultiExitDiscPathAttribute(10)));
		PathAttributeConfiguration f = new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new MultiExitDiscPathAttribute(10),
				(PathAttribute)new LocalPrefPathAttribute(100)));
		
		Assert.assertTrue(a.hashCode() == b.hashCode());
		Assert.assertTrue(c.hashCode() == d.hashCode());
		Assert.assertTrue(e.hashCode() == f.hashCode());
		
		Assert.assertFalse(a.hashCode() == c.hashCode());
		Assert.assertFalse(a.hashCode() == e.hashCode());
		Assert.assertFalse(c.hashCode() == e.hashCode());
	}
}
