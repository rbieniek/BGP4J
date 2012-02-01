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
 * File: org.bgp4j.netty.protocol.CapabilityException.java 
 */
package org.bgp4j.netty.protocol;


/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class CapabilityException extends ProtocolPacketFormatException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5564369816036195511L;

	private byte[] capability;
	
	public CapabilityException() {
		super();
	}

	public CapabilityException(String message, Throwable cause) {
		super(message, cause);
	}

	public CapabilityException(String message) {
		super(message);
	}

	public CapabilityException(Throwable cause) {
		super(cause);
	}

	public CapabilityException(String message, Throwable cause, byte[] capability) {
		super(message, cause);
		
		this.capability = capability;
	}

	public CapabilityException(String message, byte[] capability) {
		super(message);
		
		this.capability = capability;
	}

	public CapabilityException(Throwable cause, byte[] capability) {
		super(cause);
		
		this.capability = capability;
	}

	/**
	 * @return the capability
	 */
	public byte[] getCapability() {
		return capability;
	}

	/**
	 * @param capability the capability to set
	 */
	public void setCapability(byte[] capability) {
		this.capability = capability;
	}

}
