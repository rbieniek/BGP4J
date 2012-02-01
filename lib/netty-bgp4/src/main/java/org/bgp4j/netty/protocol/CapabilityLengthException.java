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
 * File: org.bgp4j.netty.protocol.CapabilityLengthException.java 
 */
package org.bgp4j.netty.protocol;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class CapabilityLengthException extends CapabilityException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1363011101974431668L;

	/**
	 * 
	 */
	public CapabilityLengthException() {
	}

	/**
	 * @param message
	 * @param cause
	 */
	public CapabilityLengthException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public CapabilityLengthException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public CapabilityLengthException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param capability
	 */
	public CapabilityLengthException(String message, Throwable cause,
			byte[] capability) {
		super(message, cause, capability);
	}

	/**
	 * @param message
	 * @param capability
	 */
	public CapabilityLengthException(String message, byte[] capability) {
		super(message, capability);
	}

	/**
	 * @param cause
	 * @param capability
	 */
	public CapabilityLengthException(Throwable cause, byte[] capability) {
		super(cause, capability);
	}

}
