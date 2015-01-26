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
 * File: org.bgp4j.netty.protocol.UnrecognizedWellKnownAttributeNotificationPacket.java 
 */
package org.bgp4j.net.packets.update;

import org.bgp4j.net.EChannelDirection;
import org.bgp4j.net.attributes.PathAttribute;
import org.bgp4j.net.events.NotificationEvent;
import org.bgp4j.net.events.update.OptionalAttributeErrorNotificationEvent;


/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class OptionalAttributeErrorNotificationPacket extends
		AttributeNotificationPacket {

	@Override
	public NotificationEvent event(EChannelDirection direction) {
		return new OptionalAttributeErrorNotificationEvent(direction);
	}

	/**
	 * @param subcode
	 * @param offendingAttribute
	 */
	public OptionalAttributeErrorNotificationPacket(PathAttribute offendingAttributes) {
		super(UpdateNotificationPacket.SUBCODE_OPTIONAL_ATTRIBUTE_ERROR, offendingAttributes);
	}

	/**
	 * @param subcode
	 * @param offendingAttribute
	 */
	public OptionalAttributeErrorNotificationPacket(byte[] offendingAttributes) {
		super(UpdateNotificationPacket.SUBCODE_OPTIONAL_ATTRIBUTE_ERROR, offendingAttributes);
	}
}
