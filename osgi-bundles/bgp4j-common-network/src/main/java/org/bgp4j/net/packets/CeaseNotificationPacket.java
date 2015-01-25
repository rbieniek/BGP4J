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

import org.bgp4j.net.BGPv4Constants;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public abstract class CeaseNotificationPacket extends NotificationPacket {

	public static final int SUBCODE_UNSPECIFIC = 0;
	public static final int SUBCODE_MAXIMUM_NUMBER_OF_PREFIXES_REACHED = 1;
	public static final int SUBCODE_ADMINSTRATIVE_SHUTDOWN = 2;
	public static final int SUBCODE_PEER_DECONFIGURED = 3;
	public static final int SUBCODE_ADMINSTRATIVE_RESET = 4;
	public static final int SUBCODE_CONNECTION_REJECTED = 5;
	public static final int SUBCODE_OTHER_CONFIGURATION_CHANGE = 6;
	public static final int SUBCODE_CONNECTION_COLLISION_RESOLUTION = 7;
	public static final int SUBCODE_OUT_OF_RESOURCES = 8;

	/**
	 * @param errorCode
	 * @param errorSubcode
	 */
	protected CeaseNotificationPacket(int subcode) {
		super(BGPv4Constants.BGP_ERROR_CODE_CEASE, subcode);
	}

}
