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

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public abstract class AttributeException extends UpdatePacketException {

	public enum EAttributeMode {
		NONE,
		PATH_ATTRIBUTES,
		RAW_BYTES;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8265508454292519918L;
	
	private PathAttribute offendingAttributes;
	private byte[] rawOffendingAttributes;
	private EAttributeMode attributeMode = EAttributeMode.NONE;
	
	protected AttributeException() {}
	
	protected AttributeException(String message) {
		super(message);
	}
	
	/**
	 * 
	 */
	protected AttributeException(PathAttribute offendingAttributes) {
		setOffendingAttribute(offendingAttributes);
		
		this.attributeMode = EAttributeMode.PATH_ATTRIBUTES;
	}

	/**
	 * @param message
	 */
	protected AttributeException(String message, PathAttribute offendingAttributes) {
		super(message);

		setOffendingAttribute(offendingAttributes);
		this.attributeMode = EAttributeMode.PATH_ATTRIBUTES;
	}

	/**
	 * 
	 */
	protected AttributeException(byte[] offendingAttributes) {
		setRawOffendingAttributes(offendingAttributes);
		
		this.attributeMode = EAttributeMode.PATH_ATTRIBUTES;
	}

	/**
	 * @param message
	 */
	protected AttributeException(String message, byte[] offendingAttributes) {
		super(message);

		setRawOffendingAttributes(offendingAttributes);
		
		this.attributeMode = EAttributeMode.PATH_ATTRIBUTES;
	}

	/**
	 * @return the offendingAttribute
	 */
	public PathAttribute getOffendingAttribute() {
		return offendingAttributes;
	}

	/**
	 * @param offendingAttribute the offendingAttribute to set
	 */
	public void setOffendingAttribute(PathAttribute offendingAttributes) {
		this.offendingAttributes = offendingAttributes;
		this.attributeMode = EAttributeMode.PATH_ATTRIBUTES;
	}

	public byte[] getRawOffendingAttributes() {
		return rawOffendingAttributes;
	}

	public void setRawOffendingAttributes(byte[] rawOffendingAttributes) {
		this.rawOffendingAttributes = rawOffendingAttributes;
		this.attributeMode = EAttributeMode.RAW_BYTES;
		
	}

	public EAttributeMode getAttributeMode() {
		return attributeMode;
	}

	@Override
	public final NotificationPacket toNotificationPacket() {
		switch(this.attributeMode) {
		case PATH_ATTRIBUTES:
			return toNotificationPacketUsingAttributes();
		case RAW_BYTES:
			return toNotificationPacketUsingBytes();
		default:
			return null;
		}
	}
	
	protected abstract NotificationPacket toNotificationPacketUsingAttributes();

	protected abstract NotificationPacket toNotificationPacketUsingBytes();
}
