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
package org.bgp4j.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;

import java.nio.ByteOrder;

import org.bgp4j.net.packets.BGPv4Packet;
import org.junit.Assert;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class BGPv4TestBase {

	protected ByteBufAllocator allocator = new UnpooledByteBufAllocator(false);
	
	protected void assertBufferContents(byte[] expected, ByteBuf buffer) {
		byte[] packet = new byte[buffer.readableBytes()];
		
		buffer.readBytes(packet);
		
		assertArraysEquals(expected, packet);
	}

	protected void assertBufferContents(byte[] expected, IByteBufFiller filler) {
		ByteBuf buffer = allocator.buffer().order(ByteOrder.BIG_ENDIAN);

		try {
			filler.fillBuffer(buffer);
		} catch(Exception e) {
			Assert.fail();
		}
		
		byte[] array = new byte[buffer.readableBytes()];
		
		buffer.readBytes(array);
		
		assertArraysEquals(expected, array);
	}

	protected ByteBuf buildProtocolPacket(byte[] packet) {
		ByteBuf buffer = allocator.buffer(packet.length);
		
		buffer.writeBytes(packet);
		
		return buffer;
	}

	@SuppressWarnings("unchecked")
	protected <T extends BGPv4Packet> T safeDowncast(BGPv4Packet packet, Class<? extends T> downcastedTo) {
		Assert.assertEquals(downcastedTo, packet.getClass());
		
		return (T)packet;
	}
	
	protected void assertArraysEquals(byte[] a, byte[] b) {
		Assert.assertEquals("buffer length", a.length, b.length);
		
		for(int i=0; i<a.length; i++) {
			Assert.assertEquals("buffer position " + i, a[i], b[i]);
		}
	}

	protected void assertArrayByteBufEquals(byte[] a, ByteBuf b) {
		byte[] b2 = new byte[b.readableBytes()];
		
		b.readBytes(b2);
		assertArraysEquals(a, b2);
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
