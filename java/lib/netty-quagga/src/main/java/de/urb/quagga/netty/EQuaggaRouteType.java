/**
 * 
 */
package de.urb.quagga.netty;

/**
 * Enumeration for the various route types used in the zebra daemon
 * 
 * @author rainer
 *
 */
public enum EQuaggaRouteType {
	System, Kernel, Connect, Static, Rip, RipNG, Ospf, Ospf6, Isis, Bgp, Hsls, Max;
	
	public static EQuaggaRouteType fromZebraType(int type) {
		switch(type) {
		case QuaggaConstants.ZEBRA_ROUTE_SYSTEM:
			return EQuaggaRouteType.System;
		case QuaggaConstants.ZEBRA_ROUTE_KERNEL:
			return EQuaggaRouteType.Kernel;
		case QuaggaConstants.ZEBRA_ROUTE_CONNECT:
			return EQuaggaRouteType.Connect;
		case QuaggaConstants.ZEBRA_ROUTE_STATIC:
			return EQuaggaRouteType.Static;
		case QuaggaConstants.ZEBRA_ROUTE_RIP:
			return EQuaggaRouteType.Rip;
		case QuaggaConstants.ZEBRA_ROUTE_RIPNG:
			return EQuaggaRouteType.RipNG;
		case QuaggaConstants.ZEBRA_ROUTE_OSPF:
			return EQuaggaRouteType.Ospf;
		case QuaggaConstants.ZEBRA_ROUTE_OSPF6:
			return EQuaggaRouteType.Ospf6;
		case QuaggaConstants.ZEBRA_ROUTE_ISIS:
			return EQuaggaRouteType.Isis;
		case QuaggaConstants.ZEBRA_ROUTE_BGP:
			return EQuaggaRouteType.Bgp;
		case QuaggaConstants.ZEBRA_ROUTE_HSLS:
			return EQuaggaRouteType.Hsls;
		case QuaggaConstants.ZEBRA_ROUTE_MAX:
			return EQuaggaRouteType.Max;
		default:
			throw new IllegalArgumentException("unknown route type: " + type);
		}
	}
	
	public int toZebraType() {
		switch(this) {
		case System:
			return QuaggaConstants.ZEBRA_ROUTE_SYSTEM;
		case Kernel:
			return QuaggaConstants.ZEBRA_ROUTE_KERNEL;
		case Connect:
			return QuaggaConstants.ZEBRA_ROUTE_CONNECT;
		case Static:
			return QuaggaConstants.ZEBRA_ROUTE_STATIC;
		case Rip:
			return QuaggaConstants.ZEBRA_ROUTE_RIP;
		case RipNG:
			return QuaggaConstants.ZEBRA_ROUTE_RIPNG;
		case Ospf:
			return QuaggaConstants.ZEBRA_ROUTE_OSPF;
		case Ospf6:
			return QuaggaConstants.ZEBRA_ROUTE_OSPF6;
		case Isis:
			return QuaggaConstants.ZEBRA_ROUTE_ISIS;
		case Bgp:
			return QuaggaConstants.ZEBRA_ROUTE_BGP;
		case Hsls:
			return QuaggaConstants.ZEBRA_ROUTE_HSLS;
		case Max:
			return QuaggaConstants.ZEBRA_ROUTE_MAX;
		default:
			throw new IllegalArgumentException("unknown route type: " + this);			
		}
	}
}
