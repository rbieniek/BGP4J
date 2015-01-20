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
 * File: org.bgp4j.netty.protocol.update.PathAttributeCodecHandler.java 
 */
package org.bgp4j.netty.protocol.update;

import io.netty.buffer.ByteBuf;

import org.bgp4j.net.attributes.PathAttribute;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 * Base class for handling encoding and decoding of a path attribute type
 */
public abstract class PathAttributeCodecHandler<T extends PathAttribute> {

	/**
	 * return the type code of the path attribute
	 * 
	 * @param attr the path attribute for which the type code shall be calculated
	 * @return the type code of the path attribute
	 */
	public abstract int typeCode(T attr);

	/**
	 * encode the given path attribute into its binary representation
	 * @param buffer
	 * @param attr
	 */
	public abstract void encodeValue(ByteBuf buffer, T attr);
}
