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
 * File: org.bgp4j.net.attributes.MultiExitDiscPathAttributeTest.java 
 */
package org.bgp4j.net.attributes;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class MultiExitDiscPathAttributeTest {

	@Test
	public void testAllEquals() {
		MultiExitDiscPathAttribute a = new MultiExitDiscPathAttribute(1);
		MultiExitDiscPathAttribute b = new MultiExitDiscPathAttribute(1);
		
		Assert.assertTrue(a.equals(b));
		Assert.assertTrue(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) == 0);
	}

	@Test
	public void testAllDiscriminatorSmaller() {
		MultiExitDiscPathAttribute a = new MultiExitDiscPathAttribute(1);
		MultiExitDiscPathAttribute b = new MultiExitDiscPathAttribute(2);
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}

	@Test
	public void testAllDiscriminatorLarger() {
		MultiExitDiscPathAttribute a = new MultiExitDiscPathAttribute(2);
		MultiExitDiscPathAttribute b = new MultiExitDiscPathAttribute(1);
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) > 0);
	}
}
