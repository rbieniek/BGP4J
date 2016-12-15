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
 * File: org.bgp4j.net.ORFSendReceiveTest.java 
 */
package org.bgp4j.net;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ORFSendReceiveTest {

	@Test
	public void testORFSendReceiveFromCode() {
		Assert.assertEquals(ORFSendReceive.fromCode(1), ORFSendReceive.RECEIVE);
		Assert.assertEquals(ORFSendReceive.fromCode(2), ORFSendReceive.SEND);
		Assert.assertEquals(ORFSendReceive.fromCode(3), ORFSendReceive.BOTH);
	}

	@Test
	public void testORFSendReceiveToCode() {
		Assert.assertEquals(1, ORFSendReceive.RECEIVE.toCode());
		Assert.assertEquals(2, ORFSendReceive.SEND.toCode());
		Assert.assertEquals(3, ORFSendReceive.BOTH.toCode());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testORFSendReceiveFromUnknownCode() {
		ORFSendReceive.fromCode(4);
	}

	@Test
	public void testORFSendReceiveFromWellKnownString() {
		Assert.assertEquals(ORFSendReceive.fromString("receive"), ORFSendReceive.RECEIVE);
		Assert.assertEquals(ORFSendReceive.fromString("send"), ORFSendReceive.SEND);
		Assert.assertEquals(ORFSendReceive.fromString("both"), ORFSendReceive.BOTH);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testORFSendReceiveFromUnknownString() {
		ORFSendReceive.fromString("foo");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testORFSendReceiveFromNullString() {
		ORFSendReceive.fromString(null);
	}
}
