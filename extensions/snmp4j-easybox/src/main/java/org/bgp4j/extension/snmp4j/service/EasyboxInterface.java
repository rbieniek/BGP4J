/**
 * 
 */
package org.bgp4j.extension.snmp4j.service;

import java.net.Inet4Address;

/**
 * @author rainer
 *
 */
public interface EasyboxInterface {

	public Inet4Address getAddress();

	public long getOctetsOut();

	public long getOctetsIn();

	public boolean isOperUp();

	public boolean isAdminUp();

	public int getSpeed();

	public int getMtu();

	public String getDescription();

	boolean isChanged(EasyboxInterface o);

}
