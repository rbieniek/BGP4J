/**
 * 
 */
package org.bgp4j.definitions.peer;

/**
 * @author rainer
 *
 */
public enum EPeerDirection {
	Server,
	Client,
	ClientAndServer;
	
	public boolean matches(EPeerDirection direction) {
		switch(direction) {
		case ClientAndServer:
			return true;
		case Client:
			return (this == Client || this == ClientAndServer);
		case Server:
			return (this == Server || this == ClientAndServer);
		default:
			return false;
		}
	}
}
