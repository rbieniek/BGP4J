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

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.UUID;

import junit.framework.Assert;

import org.bgp4j.net.ASType;
import org.bgp4j.net.Origin;
import org.bgp4j.net.attributes.ASPathAttribute;
import org.bgp4j.net.attributes.AggregatorPathAttribute;
import org.bgp4j.net.attributes.LocalPrefPathAttribute;
import org.bgp4j.net.attributes.NextHopPathAttribute;
import org.bgp4j.net.attributes.OriginPathAttribute;
import org.bgp4j.net.attributes.OriginatorIDPathAttribute;
import org.bgp4j.net.attributes.PathAttribute;
import org.bgp4j.net.packets.NotificationPacket;
import org.bgp4j.net.packets.update.AttributeFlagsNotificationPacket;
import org.bgp4j.net.packets.update.MalformedAttributeListNotificationPacket;
import org.bgp4j.net.packets.update.MissingWellKnownAttributeNotificationPacket;
import org.bgp4j.net.packets.update.UpdatePacket;
import org.bgp4j.netty.LocalChannelBGPv4TestBase;
import org.bgp4j.netty.MessageRecordingChannelHandler;
import org.bgp4j.netty.MockPeerConnectionInformation;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.local.LocalAddress;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class UpdateAttributeCheckerTest extends LocalChannelBGPv4TestBase {

	@Before
	public void before() {
		peerInfo = new MockPeerConnectionInformation();

		messageRecorder = obtainInstance(MessageRecordingChannelHandler.class);
		messageRecorder.setPeerInfo(peerInfo);
		
		LocalAddress codecOnlyAddress = new LocalAddress(UUID.randomUUID().toString());
		
		serverBootstrap = buildLocalServerBootstrap(new ChannelPipelineFactory() {
			
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new ChannelHandler[] { 
						obtainInstance(UpdateAttributeChecker.class), 
						messageRecorder });
			}
		});
		serverChannel = serverBootstrap.bind(codecOnlyAddress);

		clientBootstrap = buildLocalClientBootstrap(Channels.pipeline(new ChannelHandler[] {
				messageRecorder 
				}));
		clientChannel = clientBootstrap.connect(codecOnlyAddress).getChannel();		

	}
	
	@After
	public void after() {
		if(clientChannel != null)
			clientChannel.close();
		if(serverChannel != null)
			serverChannel.close();
		clientChannel = null;
		clientBootstrap.releaseExternalResources();
		clientBootstrap = null;
		serverBootstrap.releaseExternalResources();
		serverBootstrap = null;

		peerInfo = null;
	}

	private MessageRecordingChannelHandler messageRecorder;
	private MockPeerConnectionInformation peerInfo;
	
	private ServerBootstrap serverBootstrap;
	private ClientBootstrap clientBootstrap;
	private Channel clientChannel;
	private Channel serverChannel;
	
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
		
		clientChannel.write(update);
		
		Assert.assertEquals(0, messageRecorder.getWaitingEventNumber(clientChannel));
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(serverChannel));
	
		UpdatePacket consumed = safeDowncast(safeExtractChannelEvent(messageRecorder.nextEvent(serverChannel)), UpdatePacket.class);

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
		
		clientChannel.write(update);
		
		Assert.assertEquals(0, messageRecorder.getWaitingEventNumber(clientChannel));
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(serverChannel));
	
		UpdatePacket consumed = safeDowncast(safeExtractChannelEvent(messageRecorder.nextEvent(serverChannel)), UpdatePacket.class);

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
		
		clientChannel.write(update);
		
		Assert.assertEquals(0, messageRecorder.getWaitingEventNumber(clientChannel));
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(serverChannel));
	
		UpdatePacket consumed = safeDowncast(safeExtractChannelEvent(messageRecorder.nextEvent(serverChannel)), UpdatePacket.class);

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
		
		clientChannel.write(update);
		
		Assert.assertEquals(0, messageRecorder.getWaitingEventNumber(clientChannel));
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(serverChannel));
	
		UpdatePacket consumed = safeDowncast(safeExtractChannelEvent(messageRecorder.nextEvent(serverChannel)), UpdatePacket.class);

		Assert.assertEquals(3, consumed.getPathAttributes().size());
	}
	
	@Test
	public void testPassAllRequiredAttributesMissing2OctetsASIBGPConnection() throws Exception {
		peerInfo.setAsTypeInUse(ASType.AS_NUMBER_2OCTETS);
		peerInfo.setLocalAS(64172);
		peerInfo.setRemoteAS(64172);
		
		UpdatePacket update = new UpdatePacket();
		
		clientChannel.write(update);
		
		Assert.assertEquals(4, messageRecorder.getWaitingEventNumber(clientChannel));
		Assert.assertEquals(MissingWellKnownAttributeNotificationPacket.class, safeExtractChannelEvent(messageRecorder.nextEvent(clientChannel)).getClass());
		Assert.assertEquals(MissingWellKnownAttributeNotificationPacket.class, safeExtractChannelEvent(messageRecorder.nextEvent(clientChannel)).getClass());
		Assert.assertEquals(MissingWellKnownAttributeNotificationPacket.class, safeExtractChannelEvent(messageRecorder.nextEvent(clientChannel)).getClass());
		Assert.assertEquals(MissingWellKnownAttributeNotificationPacket.class, safeExtractChannelEvent(messageRecorder.nextEvent(clientChannel)).getClass());
		
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(serverChannel));
		
		ArrayList<Class<? extends NotificationPacket>> packetClasses = new ArrayList<Class<? extends NotificationPacket>>();
				
		packetClasses.add(MissingWellKnownAttributeNotificationPacket.class);
		packetClasses.add(MissingWellKnownAttributeNotificationPacket.class);
		packetClasses.add(MissingWellKnownAttributeNotificationPacket.class);
		packetClasses.add(MissingWellKnownAttributeNotificationPacket.class);
		
		assertNotificationEvent(packetClasses, messageRecorder.nextEvent(serverChannel));
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

		clientChannel.write(update);
		
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(clientChannel));
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(serverChannel));
	
		Assert.assertEquals(MissingWellKnownAttributeNotificationPacket.class, safeExtractChannelEvent(messageRecorder.nextEvent(clientChannel)).getClass());
		assertNotificationEvent(MissingWellKnownAttributeNotificationPacket.class, messageRecorder.nextEvent(serverChannel));
		
		update = new UpdatePacket();
		update.getPathAttributes().add(new OriginPathAttribute(Origin.INCOMPLETE));
		// update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS));
		update.getPathAttributes().add(new NextHopPathAttribute((Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x4, 0x1 })));
		update.getPathAttributes().add(new LocalPrefPathAttribute(100));

		clientChannel.write(update);
		
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(clientChannel));
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(serverChannel));
	
		Assert.assertEquals(MissingWellKnownAttributeNotificationPacket.class, safeExtractChannelEvent(messageRecorder.nextEvent(clientChannel)).getClass());
		assertNotificationEvent(MissingWellKnownAttributeNotificationPacket.class, messageRecorder.nextEvent(serverChannel));
		
		update = new UpdatePacket();
		update.getPathAttributes().add(new OriginPathAttribute(Origin.INCOMPLETE));
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS));
		// update.getPathAttributes().add(new NextHopPathAttribute((Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x4, 0x1 })));
		update.getPathAttributes().add(new LocalPrefPathAttribute(100));

		clientChannel.write(update);
		
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(clientChannel));
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(serverChannel));
	
		Assert.assertEquals(MissingWellKnownAttributeNotificationPacket.class, safeExtractChannelEvent(messageRecorder.nextEvent(clientChannel)).getClass());
		assertNotificationEvent(MissingWellKnownAttributeNotificationPacket.class, messageRecorder.nextEvent(serverChannel));
		
		update = new UpdatePacket();
		update.getPathAttributes().add(new OriginPathAttribute(Origin.INCOMPLETE));
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS));
		update.getPathAttributes().add(new NextHopPathAttribute((Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x4, 0x1 })));
		// update.getPathAttributes().add(new LocalPrefPathAttribute(100));

		clientChannel.write(update);
		
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(clientChannel));
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(serverChannel));
	
		Assert.assertEquals(MissingWellKnownAttributeNotificationPacket.class, safeExtractChannelEvent(messageRecorder.nextEvent(clientChannel)).getClass());
		assertNotificationEvent(MissingWellKnownAttributeNotificationPacket.class, messageRecorder.nextEvent(serverChannel));
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

		clientChannel.write(update);
		
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(clientChannel));
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(serverChannel));
	
		Assert.assertEquals(MissingWellKnownAttributeNotificationPacket.class, safeExtractChannelEvent(messageRecorder.nextEvent(clientChannel)).getClass());
		assertNotificationEvent(MissingWellKnownAttributeNotificationPacket.class, messageRecorder.nextEvent(serverChannel));
		
		update = new UpdatePacket();
		update.getPathAttributes().add(new OriginPathAttribute(Origin.INCOMPLETE));
		// update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS));
		update.getPathAttributes().add(new NextHopPathAttribute((Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x4, 0x1 })));
		update.getPathAttributes().add(new LocalPrefPathAttribute(100));

		clientChannel.write(update);
		
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(clientChannel));
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(serverChannel));
	
		Assert.assertEquals(MissingWellKnownAttributeNotificationPacket.class, safeExtractChannelEvent(messageRecorder.nextEvent(clientChannel)).getClass());
		assertNotificationEvent(MissingWellKnownAttributeNotificationPacket.class, messageRecorder.nextEvent(serverChannel));
		
		update = new UpdatePacket();
		update.getPathAttributes().add(new OriginPathAttribute(Origin.INCOMPLETE));
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS));
		// update.getPathAttributes().add(new NextHopPathAttribute((Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x4, 0x1 })));
		update.getPathAttributes().add(new LocalPrefPathAttribute(100));

		clientChannel.write(update);
		
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(clientChannel));
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(serverChannel));
	
		Assert.assertEquals(MissingWellKnownAttributeNotificationPacket.class, safeExtractChannelEvent(messageRecorder.nextEvent(clientChannel)).getClass());
		assertNotificationEvent(MissingWellKnownAttributeNotificationPacket.class, messageRecorder.nextEvent(serverChannel));
		
		update = new UpdatePacket();
		update.getPathAttributes().add(new OriginPathAttribute(Origin.INCOMPLETE));
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS));
		update.getPathAttributes().add(new NextHopPathAttribute((Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x4, 0x1 })));
		// update.getPathAttributes().add(new LocalPrefPathAttribute(100));

		clientChannel.write(update);
		
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(clientChannel));
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(serverChannel));
	
		Assert.assertEquals(MissingWellKnownAttributeNotificationPacket.class, safeExtractChannelEvent(messageRecorder.nextEvent(clientChannel)).getClass());
		assertNotificationEvent(MissingWellKnownAttributeNotificationPacket.class, messageRecorder.nextEvent(serverChannel));
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
		clientChannel.write(update);
		
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(clientChannel));
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(serverChannel));
		Assert.assertEquals(AttributeFlagsNotificationPacket.class, safeExtractChannelEvent(messageRecorder.nextEvent(clientChannel)).getClass());
		assertNotificationEvent(AttributeFlagsNotificationPacket.class, messageRecorder.nextEvent(serverChannel));
		
		// well-known mandatory
		update = new UpdatePacket();
		attr = new LocalPrefPathAttribute(100);
		attr.setOptional(true); // bogus flag, must be false according to RFC 4271
		update.getPathAttributes().add(attr);
		clientChannel.write(update);
		
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(clientChannel));
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(serverChannel));
		Assert.assertEquals(AttributeFlagsNotificationPacket.class, safeExtractChannelEvent(messageRecorder.nextEvent(clientChannel)).getClass());
		assertNotificationEvent(AttributeFlagsNotificationPacket.class, messageRecorder.nextEvent(serverChannel));

		// optional transitive
		update = new UpdatePacket();
		attr = new AggregatorPathAttribute(ASType.AS_NUMBER_2OCTETS, 64173, (Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x04, 0x01 } ));
		attr.setTransitive(false); // bogus flag, must be true according to RFC 4271
		update.getPathAttributes().add(attr);
		clientChannel.write(update);
		
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(clientChannel));
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(serverChannel));
		Assert.assertEquals(AttributeFlagsNotificationPacket.class, safeExtractChannelEvent(messageRecorder.nextEvent(clientChannel)).getClass());
		assertNotificationEvent(AttributeFlagsNotificationPacket.class, messageRecorder.nextEvent(serverChannel));
		
		// optional transitive
		update = new UpdatePacket();
		attr = new AggregatorPathAttribute(ASType.AS_NUMBER_2OCTETS, 64173, (Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x04, 0x01 } ));
		attr.setOptional(false); // bogus flag, must be true according to RFC 4271
		update.getPathAttributes().add(attr);
		clientChannel.write(update);
		
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(clientChannel));
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(serverChannel));
		Assert.assertEquals(AttributeFlagsNotificationPacket.class, safeExtractChannelEvent(messageRecorder.nextEvent(clientChannel)).getClass());
		assertNotificationEvent(AttributeFlagsNotificationPacket.class, messageRecorder.nextEvent(serverChannel));

		// optional non-transitive
		update = new UpdatePacket();
		attr = new OriginatorIDPathAttribute(1);
		attr.setOptional(false); // bogus flag, must be true according to RFC 4271
		update.getPathAttributes().add(attr);
		clientChannel.write(update);
		
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(clientChannel));
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(serverChannel));
		Assert.assertEquals(AttributeFlagsNotificationPacket.class, safeExtractChannelEvent(messageRecorder.nextEvent(clientChannel)).getClass());
		assertNotificationEvent(AttributeFlagsNotificationPacket.class, messageRecorder.nextEvent(serverChannel));

		// optional non-transitive
		update = new UpdatePacket();
		attr = new OriginatorIDPathAttribute(1);
		attr.setTransitive(true); // bogus flag, must be false according to RFC 4271
		update.getPathAttributes().add(attr);
		clientChannel.write(update);
		
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(clientChannel));
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(serverChannel));
		Assert.assertEquals(AttributeFlagsNotificationPacket.class, safeExtractChannelEvent(messageRecorder.nextEvent(clientChannel)).getClass());
		assertNotificationEvent(AttributeFlagsNotificationPacket.class, messageRecorder.nextEvent(serverChannel));
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

		clientChannel.write(update);
		
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(clientChannel));
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(serverChannel));
	
		Assert.assertEquals(MalformedAttributeListNotificationPacket.class, safeExtractChannelEvent(messageRecorder.nextEvent(clientChannel)).getClass());
		assertNotificationEvent(MalformedAttributeListNotificationPacket.class, messageRecorder.nextEvent(serverChannel));
	}
}
