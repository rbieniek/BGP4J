/**
 * 
 */
package org.bgp4j.rib.filter;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.bgp4j.config.nodes.PrefixRoutingFilterConfiguration;
import org.bgp4j.config.nodes.RoutingFilterConfiguration;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.weld.WeldTestCaseBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author rainer
 *
 */
public class PrefixRoutingFilterTest extends WeldTestCaseBase {

	@Before
	public void before() {
		filter = obtainInstance(PrefixRoutingFilter.class);
	}
	
	@After
	public void after() {
		filter = null;
	}
	
	private PrefixRoutingFilter filter;
	
	@Test
	public void testEmptyFilter() {
		Assert.assertFalse(filter.matchFilter(new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, 0x01 }), null, null));
	}
	
	@Test
	public void testNonPrefix() {
		filter.configure(new PrefixRoutingFilterConfiguration() {
			
			@Override
			public int compareTo(RoutingFilterConfiguration o) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Set<NetworkLayerReachabilityInformation> getFilterPrefixes() {
				Set<NetworkLayerReachabilityInformation> nlris = new HashSet<NetworkLayerReachabilityInformation>();
				
				nlris.add(new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, 0x02 }));
				
				return nlris;
			}
		});
		
		Assert.assertFalse(filter.matchFilter(new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, 0x01 }), null, null));
	}

	
	@Test
	public void testPrefix() {
		filter.configure(new PrefixRoutingFilterConfiguration() {
			
			@Override
			public int compareTo(RoutingFilterConfiguration o) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Set<NetworkLayerReachabilityInformation> getFilterPrefixes() {
				Set<NetworkLayerReachabilityInformation> nlris = new HashSet<NetworkLayerReachabilityInformation>();
				
				nlris.add(new NetworkLayerReachabilityInformation(16, new byte[] { (byte)0xc0, (byte)0xa8 }));
				
				return nlris;
			}
		});
		
		Assert.assertTrue(filter.matchFilter(new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, 0x01 }), null, null));
	}
}
