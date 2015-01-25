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
 * File: org.bgp4j.netty.handlers.UpdateAttributeCheckerTest.java 
 */
package org.bgp4j.netty.handlers;

import static org.fest.assertions.api.Assertions.assertThat;
import io.netty.channel.embedded.EmbeddedChannel;

import java.net.Inet4Address;

import org.bgp4j.net.ASType;
import org.bgp4j.net.Origin;
import org.bgp4j.net.attributes.ASPathAttribute;
import org.bgp4j.net.attributes.AggregatorPathAttribute;
import org.bgp4j.net.attributes.LocalPrefPathAttribute;
import org.bgp4j.net.attributes.NextHopPathAttribute;
import org.bgp4j.net.attributes.OriginPathAttribute;
import org.bgp4j.net.attributes.OriginatorIDPathAttribute;
import org.bgp4j.net.attributes.PathAttribute;
import org.bgp4j.net.events.update.AttributeFlagsNotificationEvent;
import org.bgp4j.net.events.update.MalformedAttributeListNotificationEvent;
import org.bgp4j.net.events.update.MissingWellKnownAttributeNotificationEvent;
import org.bgp4j.net.packets.BGPv4Packet;
import org.bgp4j.net.packets.update.AttributeFlagsNotificationPacket;
import org.bgp4j.net.packets.update.MalformedAttributeListNotificationPacket;
import org.bgp4j.net.packets.update.MissingWellKnownAttributeNotificationPacket;
import org.bgp4j.net.packets.update.UpdatePacket;
import org.bgp4j.netty.Attributes;
import org.bgp4j.netty.BGPv4TestBase;
import org.bgp4j.netty.MockPeerConnectionInformation;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class UpdateAttributeCheckerTest extends BGPv4TestBase {

	private EmbeddedChannel channel;
	private UserEventInboundHandler eventHandler;

	@Before
	public void before() {
		eventHandler = new UserEventInboundHandler();		
		channel = new EmbeddedChannel(new UpdateAttributeChecker(), eventHandler);
		peerInfo = new MockPeerConnectionInformation();
		
		channel.attr(Attributes.peerInfoKey).set(peerInfo);
	}
	
	@After
	public void after() {
		channel.close();
		channel = null;
		peerInfo = null;
	}

	private MockPeerConnectionInformation peerInfo;
	
	@Test
	public void testPassAllRequiredAttributes2OctetsASIBGPConnection() throws Exception {
		peerInfo.setAsTypeInUse(ASType.AS_NUMBER_2OCTETS);
		peerInfo.setLocalAS(64172);
		peerInfo.setRemoteAS(64172);
		
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new OriginPathAttribute(Origin.INCOMPLETE));
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS));
		update.getPathAttributes().add(new NextHopPathAttribute((Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x4, 0x1 })));
		update.getPathAttributes().add(new LocalPrefPathAttribute(100));
		
		channel.writeInbound(update);
		
		assertThat(channel.outboundMessages()).isEmpty();
		assertThat(channel.inboundMessages()).hasSize(1);
		
		UpdatePacket consumed = safeDowncast((BGPv4Packet)channel.readInbound(), UpdatePacket.class);

		Assert.assertEquals(4, consumed.getPathAttributes().size());
	}
	
	@Test
	public void testPassAllRequiredAttributes4OctetsASIBGPConnection() throws Exception {
		peerInfo.setAsTypeInUse(ASType.AS_NUMBER_4OCTETS);
		peerInfo.setLocalAS(64172);
		peerInfo.setRemoteAS(64172);
		
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new OriginPathAttribute(Origin.INCOMPLETE));
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS));
		update.getPathAttributes().add(new NextHopPathAttribute((Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x4, 0x1 })));
		update.getPathAttributes().add(new LocalPrefPathAttribute(100));
		
		channel.writeInbound(update);
		
		assertThat(channel.outboundMessages()).isEmpty();
		assertThat(channel.inboundMessages()).hasSize(1);
	
		UpdatePacket consumed = safeDowncast((BGPv4Packet)channel.readInbound(), UpdatePacket.class);

		Assert.assertEquals(4, consumed.getPathAttributes().size());
	}

	
	@Test
	public void testPassAllRequiredAttributes2OctetsASEBGPConnection() throws Exception {
		peerInfo.setAsTypeInUse(ASType.AS_NUMBER_2OCTETS);
		peerInfo.setLocalAS(64172);
		peerInfo.setRemoteAS(64173);
		
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new OriginPathAttribute(Origin.INCOMPLETE));
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS));
		update.getPathAttributes().add(new NextHopPathAttribute((Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x4, 0x1 })));
		
		channel.writeInbound(update);
		
		assertThat(channel.outboundMessages()).isEmpty();
		assertThat(channel.inboundMessages()).hasSize(1);
	
		UpdatePacket consumed = safeDowncast((BGPv4Packet)channel.readInbound(), UpdatePacket.class);

		Assert.assertEquals(3, consumed.getPathAttributes().size());
	}
	
	@Test
	public void testPassAllRequiredAttributes4OctetsASEBGPConnection() throws Exception {
		peerInfo.setAsTypeInUse(ASType.AS_NUMBER_4OCTETS);
		peerInfo.setLocalAS(64172);
		peerInfo.setRemoteAS(64173);
		
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new OriginPathAttribute(Origin.INCOMPLETE));
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS));
		update.getPathAttributes().add(new NextHopPathAttribute((Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x4, 0x1 })));
		
		channel.writeInbound(update);
		
		assertThat(channel.outboundMessages()).isEmpty();
		assertThat(channel.inboundMessages()).hasSize(1);
	
		UpdatePacket consumed = safeDowncast((BGPv4Packet)channel.readInbound(), UpdatePacket.class);

		Assert.assertEquals(3, consumed.getPathAttributes().size());
	}
	
	@Test
	public void testPassAllRequiredAttributesMissing2OctetsASIBGPConnection() throws Exception {
		peerInfo.setAsTypeInUse(ASType.AS_NUMBER_2OCTETS);
		peerInfo.setLocalAS(64172);
		peerInfo.setRemoteAS(64172);
		
		UpdatePacket update = new UpdatePacket();
		
		channel.writeInbound(update);

		assertThat(channel.inboundMessages()).isEmpty();;
		assertThat(channel.outboundMessages()).hasSize(4);
		assertThat(eventHandler.events()).hasSize(4);

		assertThat(channel.readOutbound()).isInstanceOf(MissingWellKnownAttributeNotificationPacket.class);
		assertThat(channel.readOutbound()).isInstanceOf(MissingWellKnownAttributeNotificationPacket.class);
		assertThat(channel.readOutbound()).isInstanceOf(MissingWellKnownAttributeNotificationPacket.class);
		assertThat(channel.readOutbound()).isInstanceOf(MissingWellKnownAttributeNotificationPacket.class);
		
		assertThat(eventHandler.readEvent()).isInstanceOf(MissingWellKnownAttributeNotificationEvent.class);
		assertThat(eventHandler.readEvent()).isInstanceOf(MissingWellKnownAttributeNotificationEvent.class);
		assertThat(eventHandler.readEvent()).isInstanceOf(MissingWellKnownAttributeNotificationEvent.class);
		assertThat(eventHandler.readEvent()).isInstanceOf(MissingWellKnownAttributeNotificationEvent.class);
	}
	
	@Test
	public void testPassOneRequiredAttributesMissing4OctetsASIBGPConnection() throws Exception {
		UpdatePacket update;

		peerInfo.setAsTypeInUse(ASType.AS_NUMBER_4OCTETS);
		peerInfo.setLocalAS(64172);
		peerInfo.setRemoteAS(64172);
		
		update = new UpdatePacket();
		// update.getPathAttributes().add(new OriginPathAttribute(Origin.INCOMPLETE));
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS));
		update.getPathAttributes().add(new NextHopPathAttribute((Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x4, 0x1 })));
		update.getPathAttributes().add(new LocalPrefPathAttribute(100));

		channel.writeInbound(update);
		
		assertThat(channel.inboundMessages()).isEmpty();;
		assertThat(channel.outboundMessages()).hasSize(1);
		assertThat(eventHandler.events()).hasSize(1);

		assertThat(channel.readOutbound()).isInstanceOf(MissingWellKnownAttributeNotificationPacket.class);
		assertThat(eventHandler.readEvent()).isInstanceOf(MissingWellKnownAttributeNotificationEvent.class);

		update = new UpdatePacket();
		update.getPathAttributes().add(new OriginPathAttribute(Origin.INCOMPLETE));
		// update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS));
		update.getPathAttributes().add(new NextHopPathAttribute((Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x4, 0x1 })));
		update.getPathAttributes().add(new LocalPrefPathAttribute(100));

		channel.writeInbound(update);
		
		assertThat(channel.inboundMessages()).isEmpty();;
		assertThat(channel.outboundMessages()).hasSize(1);
		assertThat(eventHandler.events()).hasSize(1);

		assertThat(channel.readOutbound()).isInstanceOf(MissingWellKnownAttributeNotificationPacket.class);
		assertThat(eventHandler.readEvent()).isInstanceOf(MissingWellKnownAttributeNotificationEvent.class);
		
		update = new UpdatePacket();
		update.getPathAttributes().add(new OriginPathAttribute(Origin.INCOMPLETE));
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS));
		// update.getPathAttributes().add(new NextHopPathAttribute((Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x4, 0x1 })));
		update.getPathAttributes().add(new LocalPrefPathAttribute(100));

		channel.writeInbound(update);
		
		assertThat(channel.inboundMessages()).isEmpty();;
		assertThat(channel.outboundMessages()).hasSize(1);
		assertThat(eventHandler.events()).hasSize(1);

		assertThat(channel.readOutbound()).isInstanceOf(MissingWellKnownAttributeNotificationPacket.class);
		assertThat(eventHandler.readEvent()).isInstanceOf(MissingWellKnownAttributeNotificationEvent.class);
		
		update = new UpdatePacket();
		update.getPathAttributes().add(new OriginPathAttribute(Origin.INCOMPLETE));
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS));
		update.getPathAttributes().add(new NextHopPathAttribute((Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x4, 0x1 })));
		// update.getPathAttributes().add(new LocalPrefPathAttribute(100));

		channel.writeInbound(update);
		
		assertThat(channel.inboundMessages()).isEmpty();;
		assertThat(channel.outboundMessages()).hasSize(1);
		assertThat(eventHandler.events()).hasSize(1);

		assertThat(channel.readOutbound()).isInstanceOf(MissingWellKnownAttributeNotificationPacket.class);
		assertThat(eventHandler.readEvent()).isInstanceOf(MissingWellKnownAttributeNotificationEvent.class);
	}

	
	@Test
	public void testPassOneRequiredAttributesMissing2OctetsASIBGPConnection() throws Exception {
		UpdatePacket update;

		peerInfo.setAsTypeInUse(ASType.AS_NUMBER_2OCTETS);
		peerInfo.setLocalAS(64172);
		peerInfo.setRemoteAS(64172);
		
		update = new UpdatePacket();
		// update.getPathAttributes().add(new OriginPathAttribute(Origin.INCOMPLETE));
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS));
		update.getPathAttributes().add(new NextHopPathAttribute((Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x4, 0x1 })));
		update.getPathAttributes().add(new LocalPrefPathAttribute(100));

		channel.writeInbound(update);
		
		assertThat(channel.inboundMessages()).isEmpty();;
		assertThat(channel.outboundMessages()).hasSize(1);
		assertThat(eventHandler.events()).hasSize(1);

		assertThat(channel.readOutbound()).isInstanceOf(MissingWellKnownAttributeNotificationPacket.class);
		assertThat(eventHandler.readEvent()).isInstanceOf(MissingWellKnownAttributeNotificationEvent.class);
		
		update = new UpdatePacket();
		update.getPathAttributes().add(new OriginPathAttribute(Origin.INCOMPLETE));
		// update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS));
		update.getPathAttributes().add(new NextHopPathAttribute((Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x4, 0x1 })));
		update.getPathAttributes().add(new LocalPrefPathAttribute(100));

		channel.writeInbound(update);
		
		assertThat(channel.inboundMessages()).isEmpty();;
		assertThat(channel.outboundMessages()).hasSize(1);
		assertThat(eventHandler.events()).hasSize(1);

		assertThat(channel.readOutbound()).isInstanceOf(MissingWellKnownAttributeNotificationPacket.class);
		assertThat(eventHandler.readEvent()).isInstanceOf(MissingWellKnownAttributeNotificationEvent.class);
		
		update = new UpdatePacket();
		update.getPathAttributes().add(new OriginPathAttribute(Origin.INCOMPLETE));
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS));
		// update.getPathAttributes().add(new NextHopPathAttribute((Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x4, 0x1 })));
		update.getPathAttributes().add(new LocalPrefPathAttribute(100));

		channel.writeInbound(update);
		
		assertThat(channel.inboundMessages()).isEmpty();;
		assertThat(channel.outboundMessages()).hasSize(1);
		assertThat(eventHandler.events()).hasSize(1);

		assertThat(channel.readOutbound()).isInstanceOf(MissingWellKnownAttributeNotificationPacket.class);
		assertThat(eventHandler.readEvent()).isInstanceOf(MissingWellKnownAttributeNotificationEvent.class);
		
		update = new UpdatePacket();
		update.getPathAttributes().add(new OriginPathAttribute(Origin.INCOMPLETE));
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS));
		update.getPathAttributes().add(new NextHopPathAttribute((Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x4, 0x1 })));
		// update.getPathAttributes().add(new LocalPrefPathAttribute(100));

		channel.writeInbound(update);
		
		assertThat(channel.inboundMessages()).isEmpty();;
		assertThat(channel.outboundMessages()).hasSize(1);
		assertThat(eventHandler.events()).hasSize(1);

		assertThat(channel.readOutbound()).isInstanceOf(MissingWellKnownAttributeNotificationPacket.class);
		assertThat(eventHandler.readEvent()).isInstanceOf(MissingWellKnownAttributeNotificationEvent.class);
	}
	
	@Test
	public void testInvalidAttributeFlags() throws Exception {
		UpdatePacket update;
		PathAttribute attr;
		
		peerInfo.setAsTypeInUse(ASType.AS_NUMBER_2OCTETS);
		peerInfo.setLocalAS(64172);
		peerInfo.setRemoteAS(64172);
		
		// well-known mandatory
		update = new UpdatePacket();
		attr = new LocalPrefPathAttribute(100);
		attr.setTransitive(false); // bogus flag, must be true according to RFC 4271
		update.getPathAttributes().add(attr);
		channel.writeInbound(update);
		
		assertThat(channel.inboundMessages()).isEmpty();;
		assertThat(channel.outboundMessages()).hasSize(1);
		assertThat(eventHandler.events()).hasSize(1);

		assertThat(channel.readOutbound()).isInstanceOf(AttributeFlagsNotificationPacket.class);
		assertThat(eventHandler.readEvent()).isInstanceOf(AttributeFlagsNotificationEvent.class);

		// well-known mandatory
		update = new UpdatePacket();
		attr = new LocalPrefPathAttribute(100);
		attr.setOptional(true); // bogus flag, must be false according to RFC 4271
		update.getPathAttributes().add(attr);
		channel.writeInbound(update);
		
		assertThat(channel.inboundMessages()).isEmpty();;
		assertThat(channel.outboundMessages()).hasSize(1);
		assertThat(eventHandler.events()).hasSize(1);

		assertThat(channel.readOutbound()).isInstanceOf(AttributeFlagsNotificationPacket.class);
		assertThat(eventHandler.readEvent()).isInstanceOf(AttributeFlagsNotificationEvent.class);

		// optional transitive
		update = new UpdatePacket();
		attr = new AggregatorPathAttribute(ASType.AS_NUMBER_2OCTETS, 64173, (Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x04, 0x01 } ));
		attr.setTransitive(false); // bogus flag, must be true according to RFC 4271
		update.getPathAttributes().add(attr);
		channel.writeInbound(update);
		
		assertThat(channel.inboundMessages()).isEmpty();;
		assertThat(channel.outboundMessages()).hasSize(1);
		assertThat(eventHandler.events()).hasSize(1);

		assertThat(channel.readOutbound()).isInstanceOf(AttributeFlagsNotificationPacket.class);
		assertThat(eventHandler.readEvent()).isInstanceOf(AttributeFlagsNotificationEvent.class);
		
		// optional transitive
		update = new UpdatePacket();
		attr = new AggregatorPathAttribute(ASType.AS_NUMBER_2OCTETS, 64173, (Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x04, 0x01 } ));
		attr.setOptional(false); // bogus flag, must be true according to RFC 4271
		update.getPathAttributes().add(attr);
		channel.writeInbound(update);
		
		assertThat(channel.inboundMessages()).isEmpty();;
		assertThat(channel.outboundMessages()).hasSize(1);
		assertThat(eventHandler.events()).hasSize(1);

		assertThat(channel.readOutbound()).isInstanceOf(AttributeFlagsNotificationPacket.class);
		assertThat(eventHandler.readEvent()).isInstanceOf(AttributeFlagsNotificationEvent.class);

		// optional non-transitive
		update = new UpdatePacket();
		attr = new OriginatorIDPathAttribute(1);
		attr.setOptional(false); // bogus flag, must be true according to RFC 4271
		update.getPathAttributes().add(attr);
		channel.writeInbound(update);
		
		assertThat(channel.inboundMessages()).isEmpty();;
		assertThat(channel.outboundMessages()).hasSize(1);
		assertThat(eventHandler.events()).hasSize(1);

		assertThat(channel.readOutbound()).isInstanceOf(AttributeFlagsNotificationPacket.class);
		assertThat(eventHandler.readEvent()).isInstanceOf(AttributeFlagsNotificationEvent.class);

		// optional non-transitive
		update = new UpdatePacket();
		attr = new OriginatorIDPathAttribute(1);
		attr.setTransitive(true); // bogus flag, must be false according to RFC 4271
		update.getPathAttributes().add(attr);
		channel.writeInbound(update);
		
		assertThat(channel.inboundMessages()).isEmpty();;
		assertThat(channel.outboundMessages()).hasSize(1);
		assertThat(eventHandler.events()).hasSize(1);

		assertThat(channel.readOutbound()).isInstanceOf(AttributeFlagsNotificationPacket.class);
		assertThat(eventHandler.readEvent()).isInstanceOf(AttributeFlagsNotificationEvent.class);
	}
	
	@Test
	public void testMismatchASNumberSizesFlags() throws Exception {
		UpdatePacket update;
		
		peerInfo.setAsTypeInUse(ASType.AS_NUMBER_2OCTETS);
		peerInfo.setLocalAS(64172);
		peerInfo.setRemoteAS(64172);
		
		update = new UpdatePacket();
		update.getPathAttributes().add(new OriginPathAttribute(Origin.INCOMPLETE));
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS));
		update.getPathAttributes().add(new NextHopPathAttribute((Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x4, 0x1 })));
		update.getPathAttributes().add(new LocalPrefPathAttribute(100));
		update.getPathAttributes().add(new AggregatorPathAttribute(ASType.AS_NUMBER_4OCTETS));

		channel.writeInbound(update);

		assertThat(channel.inboundMessages()).isEmpty();;
		assertThat(channel.outboundMessages()).hasSize(1);
		assertThat(eventHandler.events()).hasSize(1);

		assertThat(channel.readOutbound()).isInstanceOf(MalformedAttributeListNotificationPacket.class);
		assertThat(eventHandler.readEvent()).isInstanceOf(MalformedAttributeListNotificationEvent.class);
	}
}
