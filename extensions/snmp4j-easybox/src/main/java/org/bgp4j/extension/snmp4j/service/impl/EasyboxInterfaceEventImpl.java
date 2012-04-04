/**
 * 
 */
package org.bgp4j.extension.snmp4j.service.impl;

import org.bgp4j.extension.snmp4j.service.EasyboxInterface;
import org.bgp4j.extension.snmp4j.service.EasyboxInterfaceEvent;

/**
 * @author rainer
 *
 */
class EasyboxInterfaceEventImpl implements EasyboxInterfaceEvent {

	EasyboxInterfaceEventImpl(EasyboxInterface droppedInterface, EasyboxInterface currentInterface) {
		this.currentInterface = currentInterface;
		this.droppedInterface = droppedInterface;
	}
	
	private EasyboxInterface droppedInterface;
	private EasyboxInterface currentInterface;
	
	/* (non-Javadoc)
	 * @see org.bgp4j.extension.snmp4j.service.EasyboxInterfaceEvent#getDroppedInterface()
	 */
	@Override
	public EasyboxInterface getDroppedInterface() {
		return droppedInterface;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.extension.snmp4j.service.EasyboxInterfaceEvent#getCurrentInterface()
	 */
	@Override
	public EasyboxInterface getCurrentInterface() {
		return currentInterface;
	}

}
