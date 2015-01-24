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
 * File: org.bgp4j.netty.protocol.update.OriginPathAttributeCodecHandler.java 
 */
package org.bgp4j.netty.protocol.update;

import io.netty.buffer.ByteBuf;

import org.bgp4j.net.BGPv4Constants;
import org.bgp4j.net.attributes.OriginPathAttribute;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class OriginPathAttributeCodecHandler extends
		PathAttributeCodecHandler<OriginPathAttribute> {

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.update.PathAttributeCodecHandler#typeCode(org.bgp4j.netty.protocol.update.PathAttribute)
	 */
	@Override
	public int typeCode(OriginPathAttribute attr) {
		return BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_ORIGIN;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.update.PathAttributeCodecHandler#encodeValue(org.bgp4j.netty.protocol.update.PathAttribute)
	 */
	@Override
	public void encodeValue(ByteBuf buffer, OriginPathAttribute attr) {
		buffer.writeByte(OriginCodec.toCode(attr.getOrigin()));
	}

}
