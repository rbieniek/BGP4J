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
package org.bgp4j.netty.protocol.open;

import junit.framework.Assert;

import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.AutonomousSystem4Capability;
import org.bgp4j.net.Capability;
import org.bgp4j.net.MultiProtocolCapability;
import org.bgp4j.net.RouteRefreshCapability;
import org.bgp4j.net.SubsequentAddressFamily;
import org.bgp4j.net.UnknownCapability;
import org.bgp4j.netty.BGPv4TestBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class OpenPacketDecoderTest extends BGPv4TestBase {

	private OpenPacketDecoder decoder;
	
	@Before
	public void before() {
		decoder = obtainInstance(OpenPacketDecoder.class);
	}
	
	@After
	public void after() {
		decoder = null;
	}
	
	@Test
	public void testDecodeBasicOpenPacket() {
		OpenPacket open = safeDowncast(decoder.decodeOpenPacket(buildProtocolPacket(new byte[] {
				// (byte)0x01, // type code OPEN
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
		Assert.assertEquals(((192<<24) | (168 << 16) | (9 << 8) | 1), open.getBgpIdentifier());
	}

	@Test
	public void testDecodeBasicOpenPacketBadProtocolVersion() throws Exception {
		UnsupportedVersionNumberException ex = (new AssertExecption() {
			
			@Override
			protected void doExecute() {
				safeDowncast(decoder.decodeOpenPacket(buildProtocolPacket(new byte[] {
						// (byte)0x01, // type code OPEN
						(byte)0x05, // BGP version 5 
						(byte)0xfc, (byte)0x00, // Autonomous system 64512 
						(byte)0x00, (byte)0xb4, // hold time 180 seconds
						(byte)0xc0, (byte)0xa8, (byte)0x09, (byte)0x01, /// BGP identifier 192.168.9.1 
						(byte)0x0, // optional parameter length 0 
				})), OpenPacket.class);
			}
		}).execute(UnsupportedVersionNumberException.class);
		
		Assert.assertEquals(4, ex.getSupportedProtocolVersion());
	}

	@Test
	public void testDecodeBasicOpenPacketBadBgpIdentifier() throws Exception {
		(new AssertExecption() {
			
			@Override
			protected void doExecute() {
				safeDowncast(decoder.decodeOpenPacket(buildProtocolPacket(new byte[] {
						// (byte)0x01, // type code OPEN
						(byte)0x04, // BGP version 4 
						(byte)0xfc, (byte)0x00, // Autonomous system 64512 
						(byte)0x00, (byte)0xb4, // hold time 180 seconds
						(byte)0xe0, (byte)0x0, (byte)0x0, (byte)0x01, /// BGP identifier 224.0.0.1 (Multicast IP) 
						(byte)0x0, // optional parameter length 0 
				})), OpenPacket.class);
			}
		}).execute(BadBgpIdentifierException.class);
	}

	@Test
	public void testDecodeBasicOpenPacketUnsupportedOptionaParameter() throws Exception {
		(new AssertExecption() {
			
			@Override
			protected void doExecute() {
				safeDowncast(decoder.decodeOpenPacket(buildProtocolPacket(new byte[] {
						// (byte)0x01, // type code OPEN
						(byte)0x04, // BGP version 4 
						(byte)0xfc, (byte)0x00, // Autonomous system 64512 
						(byte)0x00, (byte)0xb4, // hold time 180 seconds
						(byte)0xc0, (byte)0xa8, (byte)0x09, (byte)0x01, /// BGP identifier 192.168.9.1 
						(byte)0x2, // optional parameter length 0
						(byte)0x03, (byte)0x00 // bogus optional parameter type code 3
				})), OpenPacket.class);
			}
		}).execute(UnsupportedOptionalParameterException.class);
	}

	@Test
	public void testEncodeBasicOpenPacket() {
		OpenPacket open = new OpenPacket();

		open.setProtocolVersion(4);
		open.setAutonomousSystem(64512);
		open.setHoldTime(180);
		open.setBgpIdentifier(((192<<24) | (168 << 16) | (9 << 8) | 1));
		
		assertBufferContents(new byte[] { 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x1d, // length 29
				(byte)0x01, // type code OPEN
				(byte)0x04, // BGP version 4 
				(byte)0xfc, (byte)0x00, // Autonomous system 64512 
				(byte)0x00, (byte)0xb4, // hold time 180 seconds
				(byte)0xc0, (byte)0xa8, (byte)0x09, (byte)0x01, /// BGP identifier 192.168.9.1 
				(byte)0x0, // optional parameter length 0 
				}, open.encodePacket());
	}
	
	@Test
	public void testDecodeFullOpenPacket() {
		Capability cap; 
		OpenPacket open = safeDowncast(decoder.decodeOpenPacket(buildProtocolPacket(new byte[] {
				/*
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x35, // length 53
				*/
				// (byte)0x01, // type code OPEN
				(byte)0x04, // BGP version 4 
				(byte)0xfc, (byte)0x00, // Autonomous system 64512 
				(byte)0x00, (byte)0xb4, // hold time 180 seconds
				(byte)0xc0, (byte)0xa8, (byte)0x09, (byte)0x01, /// BGP identifier 192.168.9.1 
				(byte)0x18, // optional parameter length 
				(byte)0x02, (byte)0x06, // parameter type 2 (capability), length 6 octets 
				(byte)0x01, (byte)0x04, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x01, // Multi-Protocol capability (type 1), IPv4, Unicast 
				(byte)0x02, (byte)0x02, // parameter type 2 (capability), length 2 octets 
				(byte)0x80, (byte)0x00, // Route-Refresh capability according to Wireshark, length 0 octets
				(byte)0x02, (byte)0x02, // parameter type 2 (capability), length 2 octets
				(byte)0x02, (byte)0x00, // Route-Refresh capability, length 0 octets
				(byte)0x02, (byte)0x06, // parameter type 2 (capability), length 6 octets
				(byte)0x41,	(byte)0x04, (byte)0x00, (byte)0x00, (byte)0xfc, (byte)0x00 // 4 octet AS capability, AS 64512
		})), OpenPacket.class);
		
		Assert.assertEquals(4, open.getProtocolVersion());
		Assert.assertEquals(64512, open.getAutonomousSystem());
		Assert.assertEquals(180, open.getHoldTime());
		Assert.assertEquals(((192<<24) | (168 << 16) | (9 << 8) | 1), open.getBgpIdentifier());
		Assert.assertEquals(4, open.getCapabilities().size());

		cap = open.getCapabilities().remove(0);
		Assert.assertEquals(MultiProtocolCapability.class, cap.getClass());
		Assert.assertEquals(AddressFamily.IPv4, ((MultiProtocolCapability)cap).getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, ((MultiProtocolCapability)cap).getSafi());

		cap = open.getCapabilities().remove(0);
		Assert.assertEquals(UnknownCapability.class, cap.getClass());
		Assert.assertEquals(128, ((UnknownCapability)cap).getCapabilityType());
		
		cap = open.getCapabilities().remove(0);
		Assert.assertEquals(RouteRefreshCapability.class, cap.getClass());

		cap = open.getCapabilities().remove(0);
		Assert.assertEquals(AutonomousSystem4Capability.class, cap.getClass());
		Assert.assertEquals(64512, ((AutonomousSystem4Capability)cap).getAutonomousSystem());
	}
	@Test
	public void testDecodeFullOpenPacketOneParameter() {
		Capability cap; 
		OpenPacket open = safeDowncast(decoder.decodeOpenPacket(buildProtocolPacket(new byte[] {
				/*
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x35, // length 53
				*/
				// (byte)0x01, // type code OPEN
				(byte)0x04, // BGP version 4 
				(byte)0xfc, (byte)0x00, // Autonomous system 64512 
				(byte)0x00, (byte)0xb4, // hold time 180 seconds
				(byte)0xc0, (byte)0xa8, (byte)0x09, (byte)0x01, /// BGP identifier 192.168.9.1 
				(byte)0x10, // optional parameter length 16 octets 
				(byte)0x02, (byte)0x0e, // parameter type 2 (capability), length 14 octets 
				(byte)0x01, (byte)0x04, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x01, // Multi-Protocol capability (type 1), IPv4, Unicast  
				(byte)0x02, (byte)0x00, // Route-Refresh capability, length 0 octets
				(byte)0x41,	(byte)0x04, (byte)0x00, (byte)0x00, (byte)0xfc, (byte)0x00 // 4 octet AS capability, AS 64512				
		})), OpenPacket.class);
		
		Assert.assertEquals(4, open.getProtocolVersion());
		Assert.assertEquals(64512, open.getAutonomousSystem());
		Assert.assertEquals(180, open.getHoldTime());
		Assert.assertEquals(((192<<24) | (168 << 16) | (9 << 8) | 1), open.getBgpIdentifier());
		Assert.assertEquals(3, open.getCapabilities().size());

		cap = open.getCapabilities().remove(0);
		Assert.assertEquals(MultiProtocolCapability.class, cap.getClass());
		Assert.assertEquals(AddressFamily.IPv4, ((MultiProtocolCapability)cap).getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, ((MultiProtocolCapability)cap).getSafi());

		cap = open.getCapabilities().remove(0);
		Assert.assertEquals(RouteRefreshCapability.class, cap.getClass());

		cap = open.getCapabilities().remove(0);
		Assert.assertEquals(AutonomousSystem4Capability.class, cap.getClass());
		Assert.assertEquals(64512, ((AutonomousSystem4Capability)cap).getAutonomousSystem());
	}
	
	@Test
	public void testEncodeFullPacket() {
		OpenPacket open = new OpenPacket();
		MultiProtocolCapability multiCap = new MultiProtocolCapability();
		RouteRefreshCapability routeRefreshCap = new RouteRefreshCapability();
		AutonomousSystem4Capability as4cap = new AutonomousSystem4Capability();
		
		open.setProtocolVersion(4);
		open.setAutonomousSystem(64512);
		open.setHoldTime(180);
		open.setBgpIdentifier(((192<<24) | (168 << 16) | (9 << 8) | 1));

		multiCap.setAfi(AddressFamily.IPv4);
		multiCap.setSafi(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		open.getCapabilities().add(multiCap);
		
		open.getCapabilities().add(routeRefreshCap);
		
		as4cap.setAutonomousSystem(64512);
		open.getCapabilities().add(as4cap);
		
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x2d, // length 53
				(byte)0x01, // type code OPEN
				(byte)0x04, // BGP version 4 
				(byte)0xfc, (byte)0x00, // Autonomous system 64512 
				(byte)0x00, (byte)0xb4, // hold time 180 seconds
				(byte)0xc0, (byte)0xa8, (byte)0x09, (byte)0x01, /// BGP identifier 192.168.9.1 
				(byte)0x10, // optional parameter length 16 octets 
				(byte)0x02, (byte)0x0e, // parameter type 2 (capability), length 14 octets 
				(byte)0x01, (byte)0x04, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x01, // Multi-Protocol capability (type 1), IPv4, Unicast  
				(byte)0x02, (byte)0x00, // Route-Refresh capability, length 0 octets
				(byte)0x41,	(byte)0x04, (byte)0x00, (byte)0x00, (byte)0xfc, (byte)0x00 // 4 octet AS capability, AS 64512				
		}, open.encodePacket());
	}
}
