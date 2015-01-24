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
 * File: org.bgp4j.netty.protocol.WellKnownAttributeMissingNotificationPacket.java 
 */
package org.bgp4j.net.packets.update;



/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class MissingWellKnownAttributeNotificationPacket extends UpdateNotificationPacket {

	private int attributeCode;
	
	public int getAttributeCode() {
		return attributeCode;
	}

	/**
	 * @param attributeCode 
	 * @param subcode
	 */
	public MissingWellKnownAttributeNotificationPacket(int attributeCode) {
		super(UpdateNotificationPacket.SUBCODE_MISSING_WELL_KNOWN_ATTRIBUTE);
		
		this.attributeCode = attributeCode;
	}

}
