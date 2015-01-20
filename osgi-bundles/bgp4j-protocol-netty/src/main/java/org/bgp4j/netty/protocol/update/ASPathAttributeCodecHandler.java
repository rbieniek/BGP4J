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
 * File: org.bgp4j.netty.protocol.update.ASPathAttributeCodecHandler.java 
 */
package org.bgp4j.netty.protocol.update;

import io.netty.buffer.ByteBuf;

import org.bgp4j.net.ASType;
import org.bgp4j.net.PathSegment;
import org.bgp4j.net.attributes.ASPathAttribute;
import org.bgp4j.netty.BGPv4Constants;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ASPathAttributeCodecHandler extends PathAttributeCodecHandler<ASPathAttribute> {

	static class PathSegmentCodec {
		static void encodeValue(ByteBuf buffer, PathSegment segment) {
			buffer.writeByte(PathSegmentTypeCodec.toCode(segment.getPathSegmentType()));

			if(segment.getAses() != null && segment.getAses().size() > 0) {
				buffer.writeByte(segment.getAses().size());
				
				for(int as : segment.getAses()) {
					if(segment.getAsType() == ASType.AS_NUMBER_4OCTETS) 
						buffer.writeInt(as);
					else
						buffer.writeShort(as);
				}
					
				
			} else {
				buffer.writeByte(0);
			}
		}

	}
	
	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.update.PathAttributeCodecHandler#typeCode(org.bgp4j.netty.protocol.update.PathAttribute)
	 */
	@Override
	public int typeCode(ASPathAttribute attr) {
		return (attr.isFourByteASNumber() 
				? BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH 
						: BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH);
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.update.PathAttributeCodecHandler#encodeValue(org.bgp4j.netty.protocol.update.PathAttribute)
	 */
	@Override
	public void encodeValue(ByteBuf buffer, ASPathAttribute attr) {
		
		if(attr.getPathSegments() != null && attr.getPathSegments().size() > 0) {
			for(PathSegment seg : attr.getPathSegments())
				PathSegmentCodec.encodeValue(buffer, seg);
		}
	}

}
