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
package org.bgp4j.netty.protocol;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class MalformedASPathAttributeException extends PathAttributeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 835955227257193451L;

	/**
	 * 
	 */
	public MalformedASPathAttributeException() {
	}

	/**
	 * @param offendingAttribute
	 */
	public MalformedASPathAttributeException(byte[] offendingAttribute) {
		super(offendingAttribute);
	}

	/**
	 * @param message
	 */
	public MalformedASPathAttributeException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MalformedASPathAttributeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public MalformedASPathAttributeException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param offendingAttribute
	 */
	public MalformedASPathAttributeException(String message,
			byte[] offendingAttribute) {
		super(message, offendingAttribute);
	}

	/**
	 * @param message
	 * @param offendingAttribute
	 * @param cause
	 */
	public MalformedASPathAttributeException(String message,
			byte[] offendingAttribute, Throwable cause) {
		super(message, offendingAttribute, cause);
	}

	/**
	 * @param offendingAttribute
	 * @param cause
	 */
	public MalformedASPathAttributeException(byte[] offendingAttribute,
			Throwable cause) {
		super(offendingAttribute, cause);
	}

}
