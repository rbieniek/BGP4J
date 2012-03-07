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

import junit.framework.Assert;

import org.bgp4j.netty.BGPv4TestBase;
import org.bgp4j.netty.protocol.open.OpenPacket;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class BGPv4PacketDecoderTest extends BGPv4TestBase {

	private BGPv4PacketDecoder decoder;
	
	@Before
	public void before() {
		decoder = obtainInstance(BGPv4PacketDecoder.class);
	}
	
	@After
	public void after() {
		decoder = null;
	}
	
	@Test
	public void testDecodeBasicOpenPacket() {
		OpenPacket open = safeDowncast(decoder.decodePacket(buildProtocolPacket(new byte[] {
				(byte)0x01, // type code OPEN
				(byte)0x04, // BGP version 4 
				(byte)0xfc, (byte)0x00, // Autonomous system 64512 
				(byte)0x00, (byte)0xb4, // hold time 180 seconds
				(byte)0xc0, (byte)0xa8, (byte)0x09, (byte)0x01, /// BGP identifier 192.168.9.1 
				(byte)0x0, // optional parameter length 0 
		})), OpenPacket.class);
		
		Assert.assertEquals(4, open.getProtocolVersion());
		Assert.assertEquals(64512, open.getAutonomousSystem());
		Assert.assertEquals(180, open.getHoldTime());
		Assert.assertEquals(0, open.getCapabilities().size());
		Assert.assertEquals(((192L<<24) | (168L << 16) | (9L << 8) | 1), open.getBgpIdentifier());
	}

	@Test
	public void testDecodeKeepalivePacket() {
		KeepalivePacket keep = safeDowncast(decoder.decodePacket(buildProtocolPacket(new byte[] {
				(byte)0x04, // type code KEEP
		})), KeepalivePacket.class);
		
		Assert.assertNotNull(keep);
	}
}
