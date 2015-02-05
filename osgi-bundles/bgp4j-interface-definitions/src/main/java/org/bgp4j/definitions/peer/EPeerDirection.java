/**
 * 
 */
package org.bgp4j.definitions.peer;

/**
 * @author rainer
 *
 */
public enum EPeerDirection {
	ServerOnly,
	ClientOnly,
	ClientAndServer;
	
	public boolean matches(EPeerDirection direction) {
		switch(direction) {
		case ClientAndServer:
			return true;
		case ClientOnly:
			return (this == ClientOnly || this == ClientAndServer);
		case ServerOnly:
			return (this == ServerOnly || this == ClientAndServer);
		default:
			return false;
		}
	}
}
