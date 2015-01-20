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
 * File: org.bgp4j.netty.protocol.update.PathAttributeCodec.java 
 */
package org.bgp4j.netty.protocol.update;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;

import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

import org.bgp4j.net.attributes.ASPathAttribute;
import org.bgp4j.net.attributes.AggregatorPathAttribute;
import org.bgp4j.net.attributes.AtomicAggregatePathAttribute;
import org.bgp4j.net.attributes.ClusterListPathAttribute;
import org.bgp4j.net.attributes.CommunityPathAttribute;
import org.bgp4j.net.attributes.LocalPrefPathAttribute;
import org.bgp4j.net.attributes.MultiExitDiscPathAttribute;
import org.bgp4j.net.attributes.MultiProtocolReachableNLRI;
import org.bgp4j.net.attributes.MultiProtocolUnreachableNLRI;
import org.bgp4j.net.attributes.NextHopPathAttribute;
import org.bgp4j.net.attributes.OriginPathAttribute;
import org.bgp4j.net.attributes.OriginatorIDPathAttribute;
import org.bgp4j.net.attributes.PathAttribute;
import org.bgp4j.net.attributes.UnknownPathAttribute;
import org.bgp4j.netty.BGPv4Constants;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class PathAttributeCodec {

	private static  Map<Class<? extends PathAttribute>, PathAttributeCodecHandler<? extends PathAttribute>> codecs; 

	static {
		codecs = new HashMap<Class<? extends PathAttribute>, PathAttributeCodecHandler<? extends PathAttribute>>();
		
		codecs.put(AggregatorPathAttribute.class, new AggregatorPathAttributeCodecHandler());
		codecs.put(ASPathAttribute.class, new ASPathAttributeCodecHandler());
		codecs.put(AtomicAggregatePathAttribute.class, new AtomicAggregatePathAttributeCodecHandler());
		codecs.put(ClusterListPathAttribute.class, new ClusterListPathAttributeCodecHandler());
		codecs.put(CommunityPathAttribute.class, new CommunityPathAttributeCodecHandler());
		codecs.put(LocalPrefPathAttribute.class, new LocalPrefPathAttributeCodecHandler());
		codecs.put(MultiExitDiscPathAttribute.class, new MultiExitDiscPathAttributeCodecHandler());
		codecs.put(MultiProtocolReachableNLRI.class, new MultiProtocolReachableNLRICodecHandler());
		codecs.put(MultiProtocolUnreachableNLRI.class, new MultiProtocolUnreachableNLRICodecHandler());
		codecs.put(NextHopPathAttribute.class, new NextHopPathAttributeCodecHandler());
		codecs.put(OriginatorIDPathAttribute.class, new OriginatorIDPathAttributeCodecHandler());
		codecs.put(OriginPathAttribute.class, new OriginPathAttributeCodecHandler());
		codecs.put(UnknownPathAttribute.class, new UnknownPathAttributeCodecHandler());
	}
	
	private static ByteBufAllocator allocator = new UnpooledByteBufAllocator(true);
	
	/**
	 * encode the path attribute for network transmission
	 * 
	 * @return an encoded formatted path attribute
	 */
	public static void encodePathAttribute(ByteBuf buffer, PathAttribute attr)  {
		ByteBuf valueBuffer = allocator.buffer().order(ByteOrder.BIG_ENDIAN);

		encodeValue(valueBuffer, attr);
		
		int valueLength = valueBuffer.readableBytes();
		int attrFlagsCode = 0;
				
		if(attr.isOptional())
			attrFlagsCode |= BGPv4Constants.BGP_PATH_ATTRIBUTE_OPTIONAL_BIT;
		
		if(attr.isTransitive())
			attrFlagsCode |= BGPv4Constants.BGP_PATH_ATTRIBUTE_TRANSITIVE_BIT;

		if(attr.isPartial())
			attrFlagsCode |= BGPv4Constants.BGP_PATH_ATTRIBUTE_PARTIAL_BIT;
		
		if(valueLength > 255)
			attrFlagsCode |= BGPv4Constants.BGP_PATH_ATTRIBUTE_EXTENDED_LENGTH_BIT;
		
		attrFlagsCode |= (typeCode(attr) & BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_MASK);

		buffer.writeShort(attrFlagsCode);
		
		if(valueLength > 255)
			buffer.writeShort(valueLength);
		else
			buffer.writeByte(valueLength);
		
		if(valueLength > 0)
			buffer.writeBytes(valueBuffer);

	}
	
	/*
	public static int calculateEncodedPathAttributeLength(PathAttribute attr) {
		int size = 2; // attribute flags + type field;
		int valueLength = valueLength(attr);
		
		size += (valueLength > 255) ? 2 : 1; // length field;
		size += valueLength;
		
		return size;
	}
	 */
	
	/**
	 * get the specific type code (see RFC 4271)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	static int typeCode(PathAttribute attr) {
		if(codecs.containsKey(attr.getClass())) {
			return ((PathAttributeCodecHandler<PathAttribute>)codecs.get(attr.getClass())).typeCode(attr);
		} else {
			throw new IllegalArgumentException("cannot handle path attribute of type: " + attr.getClass().getName());			
		}
	}

	/**
	 * get the encoded attribute value
	 */
	@SuppressWarnings("unchecked")
	static void encodeValue(ByteBuf buffer, PathAttribute attr) {
		if(codecs.containsKey(attr.getClass())) {
			((PathAttributeCodecHandler<PathAttribute>)codecs.get(attr.getClass())).encodeValue(buffer, attr);
		} else {
			throw new IllegalArgumentException("cannot handle path attribute of type: " + attr.getClass().getName());			
		}
	}

}
