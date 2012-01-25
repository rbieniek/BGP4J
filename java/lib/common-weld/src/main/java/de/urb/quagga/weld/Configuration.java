/**
 * 
 */
package de.urb.quagga.weld;

import javax.inject.Singleton;

/**
 * @author rainer
 *
 */
@Singleton
public class Configuration {
	public static final int DEFAULT_ZEBRA_PORT = 2600;
	
	private int zebraPort = DEFAULT_ZEBRA_PORT;

	/**
	 * @return the zebraPort
	 */
	public int getZebraPort() {
		return zebraPort;
	}

	/**
	 * @param zebraPort the zebraPort to set
	 */
	public void setZebraPort(int zebraPort) {
		this.zebraPort = zebraPort;
	}

	
}
