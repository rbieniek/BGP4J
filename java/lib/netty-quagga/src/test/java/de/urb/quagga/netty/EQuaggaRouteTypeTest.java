/**
 * 
 */
package de.urb.quagga.netty;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author rainer
 *
 */
public class EQuaggaRouteTypeTest {

	private void check(EQuaggaRouteType routeType, int zebraType) {
		Assert.assertEquals(routeType, EQuaggaRouteType.fromZebraType(zebraType));
		Assert.assertEquals(zebraType, routeType.toZebraType());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidType() {
		EQuaggaRouteType.fromZebraType(QuaggaConstants.ZEBRA_ROUTE_MAX+1);
	}
	
	@Test
	public void testSystem() {
		check(EQuaggaRouteType.System, QuaggaConstants.ZEBRA_ROUTE_SYSTEM);
	}
	
	@Test
	public void testKernel() {
		check(EQuaggaRouteType.Kernel, QuaggaConstants.ZEBRA_ROUTE_KERNEL);
	}
	
	@Test
	public void testConnect() {
		check(EQuaggaRouteType.Connect, QuaggaConstants.ZEBRA_ROUTE_CONNECT);
	}
	
	@Test
	public void testStatic() {
		check(EQuaggaRouteType.Static, QuaggaConstants.ZEBRA_ROUTE_STATIC);
	}
	
	@Test
	public void testRip() {
		check(EQuaggaRouteType.Rip, QuaggaConstants.ZEBRA_ROUTE_RIP);
	}
	
	@Test
	public void testRipNG() {
		check(EQuaggaRouteType.RipNG, QuaggaConstants.ZEBRA_ROUTE_RIPNG);
	}
	
	@Test
	public void testOspf() {
		check(EQuaggaRouteType.Ospf, QuaggaConstants.ZEBRA_ROUTE_OSPF);
	}
	
	@Test
	public void testOspf6() {
		check(EQuaggaRouteType.Ospf6, QuaggaConstants.ZEBRA_ROUTE_OSPF6);
	}
	
	@Test
	public void testIsis() {
		check(EQuaggaRouteType.Isis, QuaggaConstants.ZEBRA_ROUTE_ISIS);
	}
	
	@Test
	public void testBgp() {
		check(EQuaggaRouteType.Bgp, QuaggaConstants.ZEBRA_ROUTE_BGP);
	}
	
	@Test
	public void testHsls() {
		check(EQuaggaRouteType.Hsls, QuaggaConstants.ZEBRA_ROUTE_HSLS);
	}
	
	@Test
	public void testMax() {
		check(EQuaggaRouteType.Max, QuaggaConstants.ZEBRA_ROUTE_MAX);
	}
}
