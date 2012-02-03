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
 * File: org.bgp4j.netty.protocol.ProtocolPacketTestBase.java 
 */
package org.bgp4j.netty.protocol;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.bgp4j.weld.WeldTestCaseBase;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.UpstreamMessageEvent;
import org.junit.Assert;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ProtocolPacketTestBase extends WeldTestCaseBase {

	protected void assertBufferContents(byte[] expected, ChannelBuffer buffer) {
		byte[] packet = new byte[buffer.readableBytes()];
		
		buffer.readBytes(packet);
		
		assertArraysEquals(expected, packet);
	}
	
	protected void assertMessageEventContents(byte[] expected, MessageEvent me) {
		Assert.assertTrue("expected class " + me.getMessage().getClass().getName() + " is assignable from " + ChannelBuffer.class.getName(), 
				ChannelBuffer.class.isAssignableFrom(me.getMessage().getClass()));
		
		assertBufferContents(expected, (ChannelBuffer)me.getMessage());
	}
	
	protected void assertChannelEventContents(byte[] expected, ChannelEvent ce) {
		Assert.assertTrue("expected class " + ce.getClass().getName() + " is assignable from " + MessageEvent.class.getName(),
				MessageEvent.class.isAssignableFrom(ce.getClass()));
		
		assertMessageEventContents(expected, (MessageEvent)ce);
	}
	
	protected ChannelBuffer buildProtocolPacket(byte[] packet) {
		ChannelBuffer buffer = ChannelBuffers.buffer(packet.length);
		
		buffer.writeBytes(packet);
		
		return buffer;
	}

	protected UpstreamMessageEvent buildProtocolPacketUpstreamMessageEvent(Channel channel, byte[] packet)throws Exception  {
		return new UpstreamMessageEvent(channel, buildProtocolPacket(packet), new InetSocketAddress(InetAddress.getLocalHost(), 1));
	}
	

	@SuppressWarnings("unchecked")
	protected <T extends BGPv4Packet> T safeDowncast(BGPv4Packet packet, Class<? extends T> downcastedTo) {
		Assert.assertEquals(downcastedTo, packet.getClass());
		
		return (T)packet;
	}
	
	protected BGPv4Packet safeExtractChannelEvent(ChannelEvent ce) {
		Assert.assertTrue("expected class " + ce.getClass().getName() + " is assignable from " + MessageEvent.class.getName(),
				MessageEvent.class.isAssignableFrom(ce.getClass()));
		
		return safeExtractMessageEvent((MessageEvent)ce);
	}
	
	protected BGPv4Packet safeExtractMessageEvent(MessageEvent me) {
		Assert.assertTrue("expected class " + me.getMessage().getClass().getName() + " is assignable from " + BGPv4Packet.class.getName(), 
				BGPv4Packet.class.isAssignableFrom(me.getMessage().getClass()));
		
		return (BGPv4Packet)me.getMessage();
	}
	
	
	protected void assertArraysEquals(byte[] a, byte[] b) {
		Assert.assertEquals("buffer length", a.length, b.length);
		
		for(int i=0; i<a.length; i++) {
			Assert.assertEquals("buffer position " + i, a[i], b[i]);
		}
	}

	public abstract class AssertExecption {
		@SuppressWarnings("unchecked")
		public final <T extends Exception> T  execute(Class<T> exceptionClass) throws Exception {
			boolean caught = false;
			T caughtException = null;
			
			try {
				doExecute();
			} catch(Exception e) {
				if(exceptionClass.isAssignableFrom(e.getClass())) {
					caught = true;
					caughtException = (T)e;
				} else
					throw e;
			}
			
			Assert.assertTrue("expected to catch exception of type " + exceptionClass.getName(), caught);
			
			return caughtException;
		}
		
		protected abstract void doExecute();
	}
}
