/**
 * 
 */
package org.bgp4j.netty.protocol;

import org.bgp4j.netty.BGPv4Constants;

/**
 * @author rainer
 *
 */
public class FiniteStateMachineErrorNotificationPacket extends NotificationPacket {

	/**
	 * @param errorCode
	 * @param errorSubcode
	 */
	public FiniteStateMachineErrorNotificationPacket() {
		super(BGPv4Constants.BGP_ERROR_CODE_CEASE, 0);
	}

}
