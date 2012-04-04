/**
 * 
 */
package org.bgp4j.apps.easybox;

import javax.inject.Inject;

import org.bgp4j.extension.snmp4j.service.EasyboxInstance;
import org.bgp4j.extension.snmp4j.service.EasyboxInterfaceEvent;
import org.bgp4j.extension.snmp4j.service.EasyboxInterfaceListener;
import org.slf4j.Logger;

/**
 * @author rainer
 *
 */
public class LoggingInterfaceListener implements EasyboxInterfaceListener {

	private @Inject Logger log;
	
	/* (non-Javadoc)
	 * @see org.bgp4j.extension.snmp4j.service.EasyboxInterfaceListener#interfaceChanged(org.bgp4j.extension.snmp4j.service.EasyboxInstance, org.bgp4j.extension.snmp4j.service.EasyboxInterfaceEvent)
	 */
	@Override
	public void interfaceChanged(EasyboxInstance instance, EasyboxInterfaceEvent event) {
		if(event.getDroppedInterface() != null) {
			log.info("Easybox " + instance.getName() + ": Interface with address " + event.getDroppedInterface().getAddress() + " went down");
		}
		if(event.getCurrentInterface() != null) {
			log.info("Easybox " + instance.getName() + ": Interface with address " + event.getCurrentInterface().getAddress() + " went up");
		}
	}

}
