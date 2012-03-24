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
 * File: org.bgp4j.net.attributes.ASPathAttributeTest.java 
 */
package org.bgp4j.net.attributes;

import junit.framework.Assert;

import org.bgp4j.net.ASType;
import org.bgp4j.net.PathSegmentType;
import org.bgp4j.net.attributes.ASPathAttribute.PathSegment;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ASPathAttributeTest {

	@Test
	public void testPathSegmentAllEquals() {
		PathSegment a = new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 });
		PathSegment b = new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 });
		
		Assert.assertTrue(a.equals(b));
		Assert.assertTrue(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) == 0);
	}

	@Test
	public void testPathSegmentASTypeSmaller() {
		PathSegment a = new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 });
		PathSegment b = new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 });
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}


	@Test
	public void testPathSegmentASTypeLarger() {
		PathSegment a = new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 });
		PathSegment b = new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 });
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) > 0);
	}

	@Test
	public void testPathSegmentPathSegmentTypeSmaller() {
		PathSegment a = new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SET, new int[] { 1 });
		PathSegment b = new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] { 1 });
		PathSegment c = new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SET, new int[] { 1 });
		PathSegment d = new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 });
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(b.equals(c));
		Assert.assertFalse(c.equals(d));
		Assert.assertFalse(d.equals(a));
		
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertFalse(b.hashCode() == c.hashCode());
		Assert.assertFalse(c.hashCode() == d.hashCode());
		Assert.assertFalse(d.hashCode() == a.hashCode());
		
		Assert.assertTrue(a.compareTo(b) < 0);
		Assert.assertTrue(b.compareTo(c) < 0);
		Assert.assertTrue(c.compareTo(d) < 0);
	}

	@Test
	public void testPathSegmentPathSegmentTypeLarger() {
		PathSegment a = new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 });
		PathSegment b = new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SET, new int[] { 1 });
		PathSegment c = new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] { 1 });
		PathSegment d = new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SET, new int[] { 1 });
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(b.equals(c));
		Assert.assertFalse(c.equals(d));
		Assert.assertFalse(d.equals(a));
		
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertFalse(b.hashCode() == c.hashCode());
		Assert.assertFalse(c.hashCode() == d.hashCode());
		Assert.assertFalse(d.hashCode() == a.hashCode());
		
		Assert.assertTrue(a.compareTo(b) > 0);
		Assert.assertTrue(b.compareTo(c) > 0);
		Assert.assertTrue(c.compareTo(d) > 0);
	}

	@Test
	public void testPathSegmentASArraySmaller() {
		PathSegment a = new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 });
		PathSegment b = new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1, 1 });
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}

	@Test
	public void testPathSegmentASArrayLarger() {
		PathSegment a = new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1, 1 });
		PathSegment b = new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 });
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) > 0);
	}

	@Test
	public void testPathSegmentASArrayContentSmaller() {
		PathSegment a = new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 });
		PathSegment b = new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 2 });
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}

	@Test
	public void testPathSegmentASArrayContentLarger() {
		PathSegment a = new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 2 });
		PathSegment b = new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 });
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) > 0);
	}
	
	@Test
	public void testASPathAttributeAllEquals() {
		ASPathAttribute a = new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 })
		});
		ASPathAttribute b = new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 })
		});
				
		Assert.assertTrue(a.equals(b));
		Assert.assertTrue(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) == 0);
	}
	
	@Test
	public void testASPathAttributeASTypeSmaller() {
		ASPathAttribute a = new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 })
		});
		ASPathAttribute b = new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 })
		});
				
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}
	
	@Test
	public void testASPathAttributeASTypeLarger() {
		ASPathAttribute a = new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 })
		});
		ASPathAttribute b = new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 })
		});
				
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) > 0);
	}
	
	@Test
	public void testASPathAttributePathSegmentArraySmaller() {
		ASPathAttribute a = new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 })
		});
		ASPathAttribute b = new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 }),
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 })
		});
				
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}
	
	@Test
	public void testASPathAttributePathSegmentArrayLarger() {
		ASPathAttribute a = new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 }),
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 })
		});
		ASPathAttribute b = new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 })
		});
				
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) > 0);
	}
	
	@Test
	public void testASPathAttributePathSegmentOneContentSmaller() {
		ASPathAttribute a = new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 })
		});
		ASPathAttribute b = new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 2 })
		});
				
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}
		
	@Test
	public void testASPathAttributePathSegmentOneContentLarger() {
		ASPathAttribute a = new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 2 })
		});
		ASPathAttribute b = new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 })
		});
				
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) > 0);
	}
	
	@Test
	public void testASPathAttributePathSegmentSecondContentSmaller() {
		ASPathAttribute a = new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 }),
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 })
		});
		ASPathAttribute b = new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 }),
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 2 })
		});
				
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}
		
	@Test
	public void testASPathAttributePathSegmentSecondContentLarger() {
		ASPathAttribute a = new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 }),
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 2 })
		});
		ASPathAttribute b = new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 }),
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] { 1 })
		});
				
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) > 0);
	}
}
