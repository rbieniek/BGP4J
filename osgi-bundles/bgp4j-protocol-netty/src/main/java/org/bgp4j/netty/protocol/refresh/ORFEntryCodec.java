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
 * File: org.bgp4j.netty.protocol.refresh.ORFEntryCodec.java 
 */
package org.bgp4j.netty.protocol.refresh;

import io.netty.buffer.ByteBuf;

import org.bgp4j.net.AddressPrefixBasedORFEntry;
import org.bgp4j.net.ORFAction;
import org.bgp4j.net.ORFEntry;
import org.bgp4j.netty.util.NLRICodec;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ORFEntryCodec {
	/**
	 * encode the ORF entry
	 * 
	 * @return
	 */
	public static final void encodeORFEntry(ByteBuf buffer, ORFEntry entry) {
		buffer.writeByte(entry.getAction().toCode() << 6 | entry.getMatch().toCode() << 5);

		if(entry instanceof AddressPrefixBasedORFEntry) {
			encodeAddressPrefixBasedORFPayload(buffer, (AddressPrefixBasedORFEntry)entry);			
		} else
			throw new IllegalArgumentException("cannot handle ORFEntry of type " + entry.getClass().getName());
	}

	private static void encodeAddressPrefixBasedORFPayload(ByteBuf buffer, AddressPrefixBasedORFEntry entry) {
		if(entry.getAction() != ORFAction.REMOVE_ALL) {
			buffer.writeInt(entry.getSequence());
			buffer.writeByte(entry.getMinLength());
			buffer.writeByte(entry.getMaxLength());
			NLRICodec.encodeNLRI(buffer, entry.getPrefix());
		}
	}

}
