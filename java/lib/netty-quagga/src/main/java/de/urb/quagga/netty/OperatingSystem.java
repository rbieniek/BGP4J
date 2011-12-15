/**
 * 
 */
package de.urb.quagga.netty;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang3.StringUtils;

/**
 * @author rainer
 *
 */
@ApplicationScoped
public class OperatingSystem {
	
	private EOperatingSystem os;
	
	public OperatingSystem() {
		String osName = System.getProperty("os.name");
		
		if(StringUtils.equalsIgnoreCase(osName, "linux"))
			os = EOperatingSystem.Linux;
		else if(StringUtils.equalsIgnoreCase(osName, "macos"))
			os = EOperatingSystem.MacOS;
		else 
			throw new RuntimeException("unsupported o/s: " + osName);
	}

	public int getAddressFamilyInet4() {
		switch(os) {
		case FreeBSD:
		case Linux:
		case MacOS:
		case NetBSD:
		case OpenBSD:
		case Windows:
			return QuaggaConstants.AF_INET;
		default:
			throw new RuntimeException("unsupported o/s: " + os);
		}
	}
	
	public int getAddressFamilyInet6() {
		switch(os) {
		case FreeBSD:
			return QuaggaConstants.AF_INET6_FREEBSD;
		case Linux:
			return QuaggaConstants.AF_INET6_LINUX;
		case MacOS:
			return QuaggaConstants.AF_INET6_DARWIN;
		case NetBSD:
		case OpenBSD:
			return QuaggaConstants.AF_INET6_BSD;
		case Windows:
			return QuaggaConstants.AF_INET6_WINSOCK;
		default:
			throw new RuntimeException("unsupported o/s: " + os);
		}
	}
}
