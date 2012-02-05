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
 * File: org.bgp4j.netty.protocol.update.UpdatePacketDecoderTest.java 
 */
package org.bgp4j.netty.protocol.update;

import junit.framework.Assert;

import org.bgp4j.netty.protocol.ProtocolPacketTestBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class UpdatePacketDecoderTest extends ProtocolPacketTestBase {
	private UpdatePacketDecoder decoder;
	
	@Before
	public void before() {
		decoder = obtainInstance(UpdatePacketDecoder.class);
	}
	
	@After
	public void after() {
		decoder = null;
	}

	@Test
	public void testEmptyUpdatePacket() {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0x03, // type code UPDATE
				0x00, 0x00, // withdrawn routes length (0 octets)
				0x00, 0x00, // Total path attributes length  (0 octets)
		})), UpdatePacket.class);

		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(0, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());
	}
}
