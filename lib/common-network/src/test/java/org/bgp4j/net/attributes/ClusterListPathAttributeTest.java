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
 * File: org.bgp4j.net.attributes.ClusterListPathAttributeTest.java 
 */
package org.bgp4j.net.attributes;

import org.junit.Test;

import junit.framework.Assert;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ClusterListPathAttributeTest {

	@Test
	public void testAllEquals() {
		ClusterListPathAttribute a = new ClusterListPathAttribute();
		ClusterListPathAttribute b = new ClusterListPathAttribute();
		ClusterListPathAttribute c = new ClusterListPathAttribute(new int [] { 1 });
		ClusterListPathAttribute d = new ClusterListPathAttribute(new int [] { 1 });
		ClusterListPathAttribute e = new ClusterListPathAttribute(new int [] { 1, 2 });
		ClusterListPathAttribute f = new ClusterListPathAttribute(new int [] { 1, 2 });
		
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
	public void testSmallerArraySize() {
		ClusterListPathAttribute a = new ClusterListPathAttribute();
		ClusterListPathAttribute b = new ClusterListPathAttribute(new int [] { 1 });
		ClusterListPathAttribute c = new ClusterListPathAttribute(new int [] { 1, 1 });

		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.equals(c));
		Assert.assertFalse(b.equals(c));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertFalse(a.hashCode() == c.hashCode());
		Assert.assertFalse(b.hashCode() == c.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
		Assert.assertTrue(a.compareTo(c) < 0);
		Assert.assertTrue(b.compareTo(c) < 0);
	}
	
	@Test
	public void testLargerArraySize() {
		ClusterListPathAttribute a = new ClusterListPathAttribute(new int [] { 1, 1 });
		ClusterListPathAttribute b = new ClusterListPathAttribute(new int [] { 1 });
		ClusterListPathAttribute c = new ClusterListPathAttribute();

		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.equals(c));
		Assert.assertFalse(b.equals(c));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertFalse(a.hashCode() == c.hashCode());
		Assert.assertFalse(b.hashCode() == c.hashCode());
		Assert.assertTrue(a.compareTo(b) > 0);
		Assert.assertTrue(a.compareTo(c) > 0);
		Assert.assertTrue(b.compareTo(c) > 0);
	}
	
	@Test
	public void testSmallerContent() {
		ClusterListPathAttribute a = new ClusterListPathAttribute(new int [] { 1 });
		ClusterListPathAttribute b = new ClusterListPathAttribute(new int [] { 2 });
		ClusterListPathAttribute c = new ClusterListPathAttribute(new int [] { 1, 1 });
		ClusterListPathAttribute d = new ClusterListPathAttribute(new int [] { 2, 1 });
		ClusterListPathAttribute e = new ClusterListPathAttribute(new int [] { 1, 2 });

		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(c.equals(d));
		Assert.assertFalse(c.equals(e));
		Assert.assertFalse(d.equals(e));

		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertFalse(c.hashCode() == d.hashCode());
		Assert.assertFalse(c.hashCode() == e.hashCode());
		Assert.assertFalse(d.hashCode() == e.hashCode());
		
		Assert.assertTrue(a.compareTo(b) < 0);
		Assert.assertTrue(c.compareTo(d) < 0);
		Assert.assertTrue(c.compareTo(e) < 0);
	}

	@Test
	public void testLargerContent() {
		ClusterListPathAttribute a = new ClusterListPathAttribute(new int [] { 2 });
		ClusterListPathAttribute b = new ClusterListPathAttribute(new int [] { 1 });
		ClusterListPathAttribute c = new ClusterListPathAttribute(new int [] { 2, 1 });
		ClusterListPathAttribute d = new ClusterListPathAttribute(new int [] { 1, 2 });
		ClusterListPathAttribute e = new ClusterListPathAttribute(new int [] { 1, 1 });

		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(c.equals(d));
		Assert.assertFalse(c.equals(e));
		Assert.assertFalse(d.equals(e));

		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertFalse(c.hashCode() == d.hashCode());
		Assert.assertFalse(c.hashCode() == e.hashCode());
		Assert.assertFalse(d.hashCode() == e.hashCode());
		
		Assert.assertTrue(a.compareTo(b) > 0);
		Assert.assertTrue(c.compareTo(e) > 0);
		Assert.assertTrue(d.compareTo(e) > 0);
	}
}
