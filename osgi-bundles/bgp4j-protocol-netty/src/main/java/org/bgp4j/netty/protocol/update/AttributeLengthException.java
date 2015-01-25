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
package org.bgp4j.netty.protocol.update;

import org.bgp4j.net.attributes.PathAttribute;
import org.bgp4j.net.packets.NotificationPacket;
import org.bgp4j.net.packets.update.AttributeLengthNotificationPacket;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class AttributeLengthException extends AttributeException {

	public AttributeLengthException() {
		super();
	}

	public AttributeLengthException(String message) {
		super(message);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3386494133413403227L;


	public AttributeLengthException(PathAttribute offendingAttributes) {
		super(offendingAttributes);
	}

	public AttributeLengthException(String message, PathAttribute offendingAttributes) {
		super(message, offendingAttributes);
	}

	public AttributeLengthException(byte[] offendingAttributes) {
		super(offendingAttributes);
	}

	public AttributeLengthException(String message, byte[] offendingAttributes) {
		super(message, offendingAttributes);
	}

	@Override
	public NotificationPacket toNotificationPacketUsingAttributes () {
		return new AttributeLengthNotificationPacket(getOffendingAttribute());
	}

	@Override
	protected NotificationPacket toNotificationPacketUsingBytes() {
		return new AttributeLengthNotificationPacket(getRawOffendingAttributes());
	}

}
