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
package org.bgp4j.net.packets.open;

import org.bgp4j.net.BGPv4Constants;
import org.bgp4j.net.packets.NotificationPacket;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public abstract class OpenNotificationPacket extends NotificationPacket {

	public static final int SUBCODE_UNSPECIFIC = 0;
	public static final int SUBCODE_UNSUPPORTED_VERSION_NUMBER = 1;
	public static final int SUBCODE_BAD_PEER_AS = 2;
	public static final int SUBCODE_BAD_BGP_IDENTIFIER = 3;
	public static final int SUBCODE_UNSUPPORTED_OPTIONAL_PARAMETER = 4;
	public static final int SUBCODE_UNACCEPTABLE_HOLD_TIMER = 6;
	public static final int SUBCODE_UNSUPPORTED_CAPABILITY = 7;
	
	protected OpenNotificationPacket(int subcode) {
		super(BGPv4Constants.BGP_ERROR_CODE_OPEN, subcode);
	}
}
