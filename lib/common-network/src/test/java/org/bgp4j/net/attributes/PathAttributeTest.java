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
 * File: org.bgp4j.net.attributes.PathAttributeTest.java 
 */
package org.bgp4j.net.attributes;

import junit.framework.Assert;

import org.bgp4j.net.ASType;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class PathAttributeTest {

	@Test
	public void testSortedAscending()  {
		PathAttribute[] attrs = new PathAttribute[] {
				new AggregatorPathAttribute(ASType.AS_NUMBER_2OCTETS),
				new ASPathAttribute(ASType.AS_NUMBER_2OCTETS),
				new AtomicAggregatePathAttribute(),
				new ClusterListPathAttribute(),
				new CommunityPathAttribute(),
				new LocalPrefPathAttribute(),
				new MultiExitDiscPathAttribute(),
				new MultiProtocolReachableNLRI(),
				new MultiProtocolUnreachableNLRI(),
				new NextHopPathAttribute(),
				new OriginatorIDPathAttribute(),
				new OriginPathAttribute(),
				new UnknownPathAttribute(0, null)
		};
		
		for(int i=0; i<attrs.length; i++) {
			for(int j=0; j<attrs.length; j++) {
				if(i != j) {
					Assert.assertFalse(attrs[i].equals(attrs[j]));
					Assert.assertFalse(attrs[i].hashCode() != attrs[j].hashCode());
				}
			}
		}
		for(int i=0; i<attrs.length; i++) {
			for(int j=i+1; j<attrs.length; j++) {
				Assert.assertTrue(attrs[i].compareTo(attrs[j]) < 0);
			}
		}
	}
	
}
