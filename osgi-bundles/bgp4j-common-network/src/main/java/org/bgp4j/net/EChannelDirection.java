/**
 * 
 */
package org.bgp4j.net;

/**
 * @author rainer
 *
 */
public enum EChannelDirection {
	CLIENT,
	SERVER;
	
	public EChannelDirection mirror() {
		switch(this) {
		case CLIENT:
			return SERVER;
		case SERVER:
			return CLIENT;
		default:
			return null;
		}
	}
}
