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
 * File: org.bgp4j.net.attributes.CommonitypathAttributeTest.java 
 */
package org.bgp4j.net.attributes;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class CommonityPathAttributeTest {

	@Test
	public void testCommunityMemberAllEquals() {
		CommunityMember a = new CommunityMember(1, 2);
		CommunityMember b = new CommunityMember(1, 2);
		
		Assert.assertTrue(a.equals(b));
		Assert.assertTrue(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) == 0);
	}
	
	@Test
	public void testCommunityMemberAsNumberSmaller() {
		CommunityMember a = new CommunityMember(1, 2);
		CommunityMember b = new CommunityMember(2, 2);
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}
	
	@Test
	public void testCommunityMemberAsNumberLarger() {
		CommunityMember a = new CommunityMember(2, 2);
		CommunityMember b = new CommunityMember(1, 2);
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) > 0);
	}
	
	@Test
	public void testCommunityMemberMemberFlagsrSmaller() {
		CommunityMember a = new CommunityMember(1, 1);
		CommunityMember b = new CommunityMember(1, 2);
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}
	@Test
	public void testCommunityMemberMemberFlagsLarger() {
		CommunityMember a = new CommunityMember(1, 2);
		CommunityMember b = new CommunityMember(1, 1);
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) > 0);
	}
	
	@Test
	public void testAllEquals() {
		CommunityPathAttribute a = new CommunityPathAttribute(1);
		CommunityPathAttribute b = new CommunityPathAttribute(1);
		CommunityPathAttribute c = new CommunityPathAttribute(1, Arrays.asList(new CommunityMember(1, 1)));
		CommunityPathAttribute d = new CommunityPathAttribute(1, Arrays.asList(new CommunityMember(1, 1)));
		CommunityPathAttribute e = new CommunityPathAttribute(1, Arrays.asList(new CommunityMember(1, 1), new CommunityMember(2, 2)));
		CommunityPathAttribute f = new CommunityPathAttribute(1, Arrays.asList(new CommunityMember(1, 1), new CommunityMember(2, 2)));
		
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
	public void testSmallerCommunity() {
		CommunityPathAttribute a = new CommunityPathAttribute(1);
		CommunityPathAttribute b = new CommunityPathAttribute(2);
		CommunityPathAttribute c = new CommunityPathAttribute(1, Arrays.asList(new CommunityMember(1, 1)));
		CommunityPathAttribute d = new CommunityPathAttribute(2, Arrays.asList(new CommunityMember(1, 1)));
		CommunityPathAttribute e = new CommunityPathAttribute(1, Arrays.asList(new CommunityMember(1, 1), new CommunityMember(2, 2)));
		CommunityPathAttribute f = new CommunityPathAttribute(2, Arrays.asList(new CommunityMember(1, 1), new CommunityMember(2, 2)));
		
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
	public void testLargerCommunity() {
		CommunityPathAttribute a = new CommunityPathAttribute(2);
		CommunityPathAttribute b = new CommunityPathAttribute(1);
		CommunityPathAttribute c = new CommunityPathAttribute(2, Arrays.asList(new CommunityMember(1, 1)));
		CommunityPathAttribute d = new CommunityPathAttribute(1, Arrays.asList(new CommunityMember(1, 1)));
		CommunityPathAttribute e = new CommunityPathAttribute(2, Arrays.asList(new CommunityMember(1, 1), new CommunityMember(2, 2)));
		CommunityPathAttribute f = new CommunityPathAttribute(1, Arrays.asList(new CommunityMember(1, 1), new CommunityMember(2, 2)));
		
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
	public void testSmallerCommunityMember() {
		CommunityPathAttribute c = new CommunityPathAttribute(1, Arrays.asList(new CommunityMember(1, 1)));
		CommunityPathAttribute d = new CommunityPathAttribute(1, Arrays.asList(new CommunityMember(2, 1)));
		CommunityPathAttribute e = new CommunityPathAttribute(1, Arrays.asList(new CommunityMember(1, 1), new CommunityMember(1, 2)));
		CommunityPathAttribute f = new CommunityPathAttribute(1, Arrays.asList(new CommunityMember(1, 1), new CommunityMember(2, 2)));
		
		Assert.assertFalse(c.equals(d));
		Assert.assertFalse(e.equals(f));
		Assert.assertFalse(c.hashCode() == d.hashCode());
		Assert.assertFalse(e.hashCode() == f.hashCode());
		Assert.assertTrue(c.compareTo(d) < 0);
		Assert.assertTrue(e.compareTo(f) < 0);
	}

	@Test
	public void testSmallerCommunityMemberArray() {
		CommunityPathAttribute a = new CommunityPathAttribute(1, Arrays.asList(new CommunityMember(1, 1)));
		CommunityPathAttribute b = new CommunityPathAttribute(1, Arrays.asList(new CommunityMember(1, 1), new CommunityMember(2, 2)));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}

	@Test
	public void testSmallerLargerMember() {
		CommunityPathAttribute c = new CommunityPathAttribute(1, Arrays.asList(new CommunityMember(2, 1)));
		CommunityPathAttribute d = new CommunityPathAttribute(1, Arrays.asList(new CommunityMember(1, 1)));
		CommunityPathAttribute e = new CommunityPathAttribute(1, Arrays.asList(new CommunityMember(1, 1), new CommunityMember(2, 2)));
		CommunityPathAttribute f = new CommunityPathAttribute(1, Arrays.asList(new CommunityMember(1, 1), new CommunityMember(1, 2)));
		
		Assert.assertFalse(c.equals(d));
		Assert.assertFalse(e.equals(f));
		Assert.assertFalse(c.hashCode() == d.hashCode());
		Assert.assertFalse(e.hashCode() == f.hashCode());
		Assert.assertTrue(c.compareTo(d) > 0);
		Assert.assertTrue(e.compareTo(f) > 0);
	}

	@Test
	public void testLargerCommunityMemberArray() {
		CommunityPathAttribute a = new CommunityPathAttribute(1, Arrays.asList(new CommunityMember(1, 1), new CommunityMember(2, 2)));
		CommunityPathAttribute b = new CommunityPathAttribute(1, Arrays.asList(new CommunityMember(1, 1)));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) > 0);
	}

}
