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
 * File: org.bgp4.config.extensions.IPv4ConverterLookupTest.java 
 */
package org.bgp4j.config.extensions;

import junit.framework.Assert;

import org.bgp4j.config.extensions.IPv4ConverterLookup;
import org.bgp4j.weld.WeldTestCaseBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class IPv4ConverterLookupTest extends WeldTestCaseBase  {

	@Before
	public void before() {
		lookup = obtainInstance(IPv4ConverterLookup.class);
	}
	
	@After
	public void after() {
		lookup = null;
	}
	
	private IPv4ConverterLookup lookup;
	
	@Test
	public void testGoodIpv4Address() {
		String value = lookup.lookup("192.168.4.1");
		
		Assert.assertEquals("3232236545", value);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testTooLittleParts() {
		lookup.lookup("192.168.4,1");
	}

	
	@Test(expected=IllegalArgumentException.class)
	public void testPartTooLarge() {
		lookup.lookup("192.168.256.1");
	}
}
