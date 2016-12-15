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
 * File: org.bgp4j.net.ORFTypeTest.java 
 */
package org.bgp4j.net;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ORFTypeTest {

	@Test
	public void testORFTypeFromCode() {
		Assert.assertEquals(ORFType.fromCode(64), ORFType.ADDRESS_PREFIX_BASED);
	}

	@Test
	public void testORFTypeToCode() {
		Assert.assertEquals(64, ORFType.ADDRESS_PREFIX_BASED.toCode());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testORFTypeFromUnknownCode() {
		ORFType.fromCode(65);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testORFTypeFromUnknownString() {
		ORFType.fromString("foo");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testORFTypeFromNullString() {
		ORFType.fromString(null);
	}
	
	@Test
	public void testWellKnownORFTypes() {
		Assert.assertEquals(ORFType.fromString("addressprefixbased"), ORFType.ADDRESS_PREFIX_BASED);
	}
}
