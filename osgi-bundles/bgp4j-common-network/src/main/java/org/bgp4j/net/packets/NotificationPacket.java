/**
 *  Copyright 2012 Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  
 */
package org.bgp4j.net.packets;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bgp4j.net.BGPv4Constants;
import org.bgp4j.net.EChannelDirection;
import org.bgp4j.net.events.NotificationEvent;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public abstract class NotificationPacket extends BGPv4Packet {
	private int errorCode;
	private int errorSubcode;
	
	protected NotificationPacket(int errorCode, int errorSubcode) {
		this.errorCode = errorCode;
		this.errorSubcode = errorSubcode;
	}
	
	@Override
	public final int getType() {
		return BGPv4Constants.BGP_PACKET_TYPE_NOTIFICATION;
	}

	/**
	 * @return the errorCode
	 */
	public final int getErrorCode() {
		return errorCode;
	}

	/**
	 * @return the errorSubcode
	 */
	public final int getErrorSubcode() {
		return errorSubcode;
	}	
	
	/**
	 * 
	 * @param diection
	 * @return
	 */
	public abstract NotificationEvent event(EChannelDirection direction);
	
	@Override
	public String toString() {
		return (new ToStringBuilder(this))
				.append("type", getType())
				.append("errorCode", errorCode)
				.append("errorSubcode", errorSubcode).toString();
	}
}
