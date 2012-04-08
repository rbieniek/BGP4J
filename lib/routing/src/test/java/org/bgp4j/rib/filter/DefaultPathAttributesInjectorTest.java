/**
 * 
 */
package org.bgp4j.rib.filter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.Assert;

import org.bgp4j.config.nodes.PathAttributeConfiguration;
import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.Origin;
import org.bgp4j.net.attributes.LocalPrefPathAttribute;
import org.bgp4j.net.attributes.MultiExitDiscPathAttribute;
import org.bgp4j.net.attributes.OriginPathAttribute;
import org.bgp4j.net.attributes.PathAttribute;
import org.bgp4j.rib.Route;
import org.bgp4j.weld.WeldTestCaseBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author rainer
 *
 */
public class DefaultPathAttributesInjectorTest extends WeldTestCaseBase {

	@Before
	public void before() {
		injector = obtainInstance(DefaultPathAttributesInjector.class);
		localPref = new LocalPrefPathAttribute(100);
		localPref2 = new LocalPrefPathAttribute(200);
		multiExit = new MultiExitDiscPathAttribute(1);
		origin = new OriginPathAttribute(Origin.INCOMPLETE);
	}
	
	@After
	public void after() {
		injector = null;
		localPref = null;
		localPref2 = null;
		multiExit = null;
		origin = null;
	}
	
	private DefaultPathAttributesInjector injector;
	private PathAttribute localPref;
	private PathAttribute localPref2;
	private PathAttribute multiExit;
	private PathAttribute origin;
	
	@Test
	public void testEmptyInjector() {
		TreeSet<PathAttribute> attrs = new TreeSet<PathAttribute>();
		
		attrs.add(localPref);
		attrs.add(multiExit);
		attrs.add(origin);
		
		Iterator<PathAttribute> it = injector.injectMissingPathAttribute(new Route(AddressFamilyKey.IPV4_UNICAST_FORWARDING, null, attrs, null)).getPathAttributes().iterator();
		
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(localPref, it.next());
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(multiExit, it.next());
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(origin, it.next());
		Assert.assertFalse(it.hasNext());
	}

	@Test
	public void testInjectMissing() {
		injector.configure(new PathAttributeConfiguration() {
			
			@Override
			public int compareTo(PathAttributeConfiguration o) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Set<PathAttribute> getAttributes() {
				Set<PathAttribute> pas = new HashSet<PathAttribute>();
				
				pas.add(localPref2);
				
				return pas;
			}
		});
		
		TreeSet<PathAttribute> attrs = new TreeSet<PathAttribute>();
		
		attrs.add(multiExit);
		attrs.add(origin);
		
		Iterator<PathAttribute> it = injector.injectMissingPathAttribute(new Route(AddressFamilyKey.IPV4_UNICAST_FORWARDING, null, attrs, null)).getPathAttributes().iterator();
		
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(localPref2, it.next());
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(multiExit, it.next());
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(origin, it.next());
		Assert.assertFalse(it.hasNext());
	}


	@Test
	public void testInjectDontOverwrite() {
		injector.configure(new PathAttributeConfiguration() {
			
			@Override
			public int compareTo(PathAttributeConfiguration o) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Set<PathAttribute> getAttributes() {
				Set<PathAttribute> pas = new HashSet<PathAttribute>();
				
				pas.add(localPref2);
				
				return pas;
			}
		});
		
		TreeSet<PathAttribute> attrs = new TreeSet<PathAttribute>();
		
		attrs.add(localPref);
		attrs.add(multiExit);
		attrs.add(origin);
		
		Iterator<PathAttribute> it = injector.injectMissingPathAttribute(new Route(AddressFamilyKey.IPV4_UNICAST_FORWARDING, null, attrs, null)).getPathAttributes().iterator();
		
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(localPref, it.next());
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(multiExit, it.next());
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(origin, it.next());
		Assert.assertFalse(it.hasNext());
	}
}
