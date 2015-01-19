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
 * File: org.bgp4j.net.attributes.UnknownPathAttributeTest.java 
 */
package org.bgp4j.net.attributes;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class UnknownPathAttributeTest {

	@Test
	public void testEquals() {
		UnknownPathAttribute a = new UnknownPathAttribute(1, null);
		UnknownPathAttribute b = new UnknownPathAttribute(1, null);
		UnknownPathAttribute c = new UnknownPathAttribute(1, new byte[] { 1 });
		UnknownPathAttribute d = new UnknownPathAttribute(1, new byte[] { 1 });
		UnknownPathAttribute e = new UnknownPathAttribute(1, new byte[] { 1, 1 });
		UnknownPathAttribute f = new UnknownPathAttribute(1, new byte[] { 1, 1 });
		
		Assert.assertTrue(a.equals(b));
		Assert.assertTrue(c.equals(d));
		Assert.assertTrue(e.equals(f));
		
		Assert.assertTrue(a.hashCode() == b.hashCode());
		Assert.assertTrue(c.hashCode() == d.hashCode());
		Assert.assertTrue(e.hashCode() == f.hashCode());
		
		Assert.assertTrue(a.compareTo(b) == 0);
		Assert.assertTrue(c.compareTo(d) == 0);
		Assert.assertTrue(e.compareTo(f) == 0);
	}

	@Test
	public void testSmallerType() {
		UnknownPathAttribute a = new UnknownPathAttribute(1, null);
		UnknownPathAttribute b = new UnknownPathAttribute(2, null);
		UnknownPathAttribute c = new UnknownPathAttribute(1, new byte[] { 1 });
		UnknownPathAttribute d = new UnknownPathAttribute(2, new byte[] { 1 });
		UnknownPathAttribute e = new UnknownPathAttribute(1, new byte[] { 1, 1 });
		UnknownPathAttribute f = new UnknownPathAttribute(2, new byte[] { 1, 1 });
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(c.equals(d));
		Assert.assertFalse(e.equals(f));
		
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertFalse(c.hashCode() == d.hashCode());
		Assert.assertFalse(e.hashCode() == f.hashCode());
		
		Assert.assertTrue(a.compareTo(b) < 0);
		Assert.assertTrue(c.compareTo(d) < 0);
		Assert.assertTrue(e.compareTo(f) < 0);
	}

	@Test
	public void testLargerType() {
		UnknownPathAttribute a = new UnknownPathAttribute(2, null);
		UnknownPathAttribute b = new UnknownPathAttribute(1, null);
		UnknownPathAttribute c = new UnknownPathAttribute(2, new byte[] { 1 });
		UnknownPathAttribute d = new UnknownPathAttribute(1, new byte[] { 1 });
		UnknownPathAttribute e = new UnknownPathAttribute(2, new byte[] { 1, 1 });
		UnknownPathAttribute f = new UnknownPathAttribute(1, new byte[] { 1, 1 });
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(c.equals(d));
		Assert.assertFalse(e.equals(f));
		
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertFalse(c.hashCode() == d.hashCode());
		Assert.assertFalse(e.hashCode() == f.hashCode());
		
		Assert.assertTrue(a.compareTo(b) > 0);
		Assert.assertTrue(c.compareTo(d) > 0);
		Assert.assertTrue(e.compareTo(f) > 0);
	}

	@Test
	public void testSmallerContent() {
		UnknownPathAttribute c = new UnknownPathAttribute(1, new byte[] { 1 });
		UnknownPathAttribute d = new UnknownPathAttribute(1, new byte[] { 2 });
		UnknownPathAttribute e = new UnknownPathAttribute(1, new byte[] { 1, 1 });
		UnknownPathAttribute f = new UnknownPathAttribute(1, new byte[] { 2, 1 });
		UnknownPathAttribute g = new UnknownPathAttribute(1, new byte[] { 1, 1 });
		UnknownPathAttribute h = new UnknownPathAttribute(1, new byte[] { 1, 2 });
		
		Assert.assertFalse(c.equals(d));
		Assert.assertFalse(e.equals(f));
		Assert.assertFalse(g.equals(h));
		
		Assert.assertFalse(c.hashCode() == d.hashCode());
		Assert.assertFalse(e.hashCode() == f.hashCode());
		Assert.assertFalse(g.hashCode() == h.hashCode());
		
		Assert.assertTrue(c.compareTo(d) < 0);
		Assert.assertTrue(e.compareTo(f) < 0);
		Assert.assertTrue(g.compareTo(h) < 0);
	}

	@Test
	public void testLargerContent() {
		UnknownPathAttribute c = new UnknownPathAttribute(1, new byte[] { 2 });
		UnknownPathAttribute d = new UnknownPathAttribute(1, new byte[] { 1 });
		UnknownPathAttribute e = new UnknownPathAttribute(1, new byte[] { 2, 1 });
		UnknownPathAttribute f = new UnknownPathAttribute(1, new byte[] { 1, 1 });
		UnknownPathAttribute g = new UnknownPathAttribute(1, new byte[] { 1, 2 });
		UnknownPathAttribute h = new UnknownPathAttribute(1, new byte[] { 1, 1 });
		
		Assert.assertFalse(c.equals(d));
		Assert.assertFalse(e.equals(f));
		Assert.assertFalse(g.equals(h));
		
		Assert.assertFalse(c.hashCode() == d.hashCode());
		Assert.assertFalse(e.hashCode() == f.hashCode());
		Assert.assertFalse(g.hashCode() == h.hashCode());
		
		Assert.assertTrue(c.compareTo(d) > 0);
		Assert.assertTrue(e.compareTo(f) > 0);
		Assert.assertTrue(g.compareTo(h) > 0);
	}

	@Test
	public void testSmallerArraySize() {
		UnknownPathAttribute c = new UnknownPathAttribute(1, new byte[] { 1 });
		UnknownPathAttribute e = new UnknownPathAttribute(1, new byte[] { 1, 1 });
		
		Assert.assertFalse(c.equals(e));
		
		Assert.assertFalse(c.hashCode() == e.hashCode());
		
		Assert.assertTrue(c.compareTo(e) < 0);
	}


	@Test
	public void testLargerArraySize() {
		UnknownPathAttribute c = new UnknownPathAttribute(1, new byte[] { 1, 1 });
		UnknownPathAttribute e = new UnknownPathAttribute(1, new byte[] { 1 });
		
		Assert.assertFalse(c.equals(e));
		
		Assert.assertFalse(c.hashCode() == e.hashCode());
		
		Assert.assertTrue(c.compareTo(e) > 0);
	}
}
