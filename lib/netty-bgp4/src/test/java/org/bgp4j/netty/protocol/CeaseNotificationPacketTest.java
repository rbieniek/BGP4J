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
 * File: org.bgp4j.netty.protocol.CeaseNotificationPacketTest.java 
 */
package org.bgp4j.netty.protocol;

import junit.framework.Assert;

import org.bgp4j.netty.BGPv4Constants.AddressFamily;
import org.bgp4j.netty.BGPv4Constants.SubsequentAddressFamily;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class CeaseNotificationPacketTest extends ProtocolPacketTestBase {

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
	public void testDecodeUnspecificCeaseNotificationPacket() {
		safeDowncast(decoder.decodePacket(buildProtocolPacket(new byte[] {
				(byte)0x03, // type code NOTIFICATION
				(byte)0x06, // CEASE error code
				(byte)0x00, // UNSPECIFIC
		})), UnspecifiedCeaseNotificationPacket.class);
	}

	@Test
	public void testEncodeUnspecificCeaseNotificationPacket() {
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, 
				0x00, 0x15, // length 21 octets
				(byte)0x03, // type code NOTIFICATION
				(byte)0x06, // CEASE error code
				(byte)0x00, // UNSPECIFIC
		}, (new UnspecifiedCeaseNotificationPacket()).encodePacket());
	}

	@Test
	public void testDecodeUnknownCeaseNotificationPacket() {
		safeDowncast(decoder.decodePacket(buildProtocolPacket(new byte[] {
				(byte)0x03, // type code NOTIFICATION
				(byte)0x06, // CEASE error code
				(byte)0x0f, // Unknown subcode
		})), UnspecifiedCeaseNotificationPacket.class);
	}

	@Test
	public void testDecodeMaximumNumberOfPrefixesReachedNotificationPacket() {
		MaximumNumberOfPrefixesReachedNotificationPacket packet = safeDowncast(decoder.decodePacket(buildProtocolPacket(new byte[] {
				(byte)0x03, // type code NOTIFICATION
				(byte)0x06, // CEASE error code
				(byte)0x01, // Maximum number of prefixes reached
		})), MaximumNumberOfPrefixesReachedNotificationPacket.class);
		
		Assert.assertNull(packet.getAddressFamily());
		Assert.assertNull(packet.getSubsequentAddressFamily());
		Assert.assertEquals(0, packet.getPrefixUpperBound());

		packet = safeDowncast(decoder.decodePacket(buildProtocolPacket(new byte[] {
				(byte)0x03, // type code NOTIFICATION
				(byte)0x06, // CEASE error code
				(byte)0x01, // Maximum number of prefixes reached
				0x01,       // AFI IPv4
		})), MaximumNumberOfPrefixesReachedNotificationPacket.class);
		
		Assert.assertNull(packet.getAddressFamily());
		Assert.assertNull(packet.getSubsequentAddressFamily());
		Assert.assertEquals(0, packet.getPrefixUpperBound());

		packet = safeDowncast(decoder.decodePacket(buildProtocolPacket(new byte[] {
				(byte)0x03, // type code NOTIFICATION
				(byte)0x06, // CEASE error code
				(byte)0x01, // Maximum number of prefixes reached
				0x00, 0x01, // AFI IPv4
				0x01,       // SAI Unicast forwarding  
		})), MaximumNumberOfPrefixesReachedNotificationPacket.class);
		
		Assert.assertNull(packet.getAddressFamily());
		Assert.assertNull(packet.getSubsequentAddressFamily());
		Assert.assertEquals(0, packet.getPrefixUpperBound());

		packet = safeDowncast(decoder.decodePacket(buildProtocolPacket(new byte[] {
				(byte)0x03, // type code NOTIFICATION
				(byte)0x06, // CEASE error code
				(byte)0x01, // Maximum number of prefixes reached
				0x00, 0x7f, // AFI Illegal
				0x01,       // SAFI Unicast routing
				0x00, 0x00, 0x12, 0x34, // upper bound 0x1234
		})), MaximumNumberOfPrefixesReachedNotificationPacket.class);
		
		Assert.assertNull(packet.getAddressFamily());
		Assert.assertNull(packet.getSubsequentAddressFamily());
		Assert.assertEquals(0, packet.getPrefixUpperBound());

		packet = safeDowncast(decoder.decodePacket(buildProtocolPacket(new byte[] {
				(byte)0x03, // type code NOTIFICATION
				(byte)0x06, // CEASE error code
				(byte)0x01, // Maximum number of prefixes reached
				0x00, 0x01, // AFI IPv4
				0x01,       // SAFI Unicast routing
				0x00, 0x00, 0x12, 0x34, // upper bound 0x1234
		})), MaximumNumberOfPrefixesReachedNotificationPacket.class);
		
		Assert.assertEquals(AddressFamily.IPv4, packet.getAddressFamily());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, packet.getSubsequentAddressFamily());
		Assert.assertEquals(0x1234, packet.getPrefixUpperBound());
	}

	@Test
	public void testEncodeMaximumNumberOfPrefixesReachedPacket() {
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, 
				0x00, 0x15, // length 21 octets
				(byte)0x03, // type code NOTIFICATION
				(byte)0x06, // CEASE error code
				(byte)0x01, // Maximum number of prefixes reached
		}, (new MaximumNumberOfPrefixesReachedNotificationPacket()).encodePacket());

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, 
				0x00, 0x1c, // length 28 octets
				(byte)0x03, // type code NOTIFICATION
				(byte)0x06, // CEASE error code
				(byte)0x01, // Maximum number of prefixes reached
				0x00, 0x01, // AFI IPv4
				0x01,       // SAFI Unicast routing
				0x00, 0x00, 0x12, 0x34, // upper bound 0x1234
		}, (new MaximumNumberOfPrefixesReachedNotificationPacket(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, 0x1234)).encodePacket());
	}

	@Test
	public void testDecodeAdministrativeShutdownNotificationPacket() {
		safeDowncast(decoder.decodePacket(buildProtocolPacket(new byte[] {
				(byte)0x03, // type code NOTIFICATION
				(byte)0x06, // CEASE error code
				(byte)0x02, // ADMINISTRATIVE SHUTDOWN
		})), AdministrativeShutdownNotificationPacket.class);
	}

	@Test
	public void testEncodeAdministrativeShutdownNotificationPacket() {
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, 
				0x00, 0x15, // length 21 octets
				(byte)0x03, // type code NOTIFICATION
				(byte)0x06, // CEASE error code
				(byte)0x02, // Administrative shutodwn
		}, (new AdministrativeShutdownNotificationPacket()).encodePacket());
	}
	
	@Test
	public void testDecodePeerDecofiguredNotificationPacket() {
		safeDowncast(decoder.decodePacket(buildProtocolPacket(new byte[] {
				(byte)0x03, // type code NOTIFICATION
				(byte)0x06, // CEASE error code
				(byte)0x03, // Peer deconfigured
		})), PeerDeconfiguredNotificationPacket.class);
	}

	@Test
	public void testEncodePeerDeconfiguredNotificationPacket() {
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, 
				0x00, 0x15, // length 21 octets
				(byte)0x03, // type code NOTIFICATION
				(byte)0x06, // CEASE error code
				(byte)0x03, // Peer deconfigured
		}, (new PeerDeconfiguredNotificationPacket()).encodePacket());
	}
	
	@Test
	public void testDecodeAdministrativeResetNotificationPacket() {
		safeDowncast(decoder.decodePacket(buildProtocolPacket(new byte[] {
				(byte)0x03, // type code NOTIFICATION
				(byte)0x06, // CEASE error code
				(byte)0x04, // Administrative reset
		})), AdministrativeResetNotificationPacket.class);
	}

	@Test
	public void testEncodeAdministrativeResetNotificationPacket() {
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, 
				0x00, 0x15, // length 21 octets
				(byte)0x03, // type code NOTIFICATION
				(byte)0x06, // CEASE error code
				(byte)0x04, // administrative reset
		}, (new AdministrativeResetNotificationPacket()).encodePacket());
	}

	@Test
	public void testDecodeConnectionRejectedNotificationPacket() {
		safeDowncast(decoder.decodePacket(buildProtocolPacket(new byte[] {
				(byte)0x03, // type code NOTIFICATION
				(byte)0x06, // CEASE error code
				(byte)0x05, // Connection rejected
		})), ConnectionRejectedNotificationPacket.class);
	}

	@Test
	public void testEncodeConnectionRejectedNotificationPacket() {
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, 
				0x00, 0x15, // length 21 octets
				(byte)0x03, // type code NOTIFICATION
				(byte)0x06, // CEASE error code
				(byte)0x05, // connection rejected
		}, (new ConnectionRejectedNotificationPacket()).encodePacket());
	}

	@Test
	public void testDecodeOtherConfigurationChangeNotificationPacket() {
		safeDowncast(decoder.decodePacket(buildProtocolPacket(new byte[] {
				(byte)0x03, // type code NOTIFICATION
				(byte)0x06, // CEASE error code
				(byte)0x06, // Other configuration change
		})), OtherConfigurationChangeNotificationPacket.class);
	}

	@Test
	public void testEncodeOtherConfigurationChangeNotificationPacket() {
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, 
				0x00, 0x15, // length 21 octets
				(byte)0x03, // type code NOTIFICATION
				(byte)0x06, // CEASE error code
				(byte)0x06, // other configuration change
		}, (new OtherConfigurationChangeNotificationPacket()).encodePacket());
	}

	@Test
	public void testDecodeConnectionCollisionResolutionNotificationPacket() {
		safeDowncast(decoder.decodePacket(buildProtocolPacket(new byte[] {
				(byte)0x03, // type code NOTIFICATION
				(byte)0x06, // CEASE error code
				(byte)0x07, // Connection collision resolution
		})), ConnectionCollisionResolutionNotificationPacket.class);
	}

	@Test
	public void testEncodeConnectionCollisionResolutionNotificationPacket() {
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, 
				0x00, 0x15, // length 21 octets
				(byte)0x03, // type code NOTIFICATION
				(byte)0x06, // CEASE error code
				(byte)0x07, // Connection collision resolution
		}, (new ConnectionCollisionResolutionNotificationPacket()).encodePacket());
	}
	
	@Test
	public void testDecodeOutOfResourcesNotificationPacket() {
		safeDowncast(decoder.decodePacket(buildProtocolPacket(new byte[] {
				(byte)0x03, // type code NOTIFICATION
				(byte)0x06, // CEASE error code
				(byte)0x08, // UNSPECIFIC
		})), OutOfResourcesNotificationPacket.class);
	}

	@Test
	public void testEncodeOutOfResourcesNotificationPacket() {
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, 
				0x00, 0x15, // length 21 octets
				(byte)0x03, // type code NOTIFICATION
				(byte)0x06, // CEASE error code
				(byte)0x08, // Out of resources
		}, (new OutOfResourcesNotificationPacket()).encodePacket());
	}
}
