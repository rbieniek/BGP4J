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
 */
package org.bgp4j.net.attributes;

import java.util.LinkedList;
import java.util.List;

import org.bgp4j.net.ASType;
import org.bgp4j.net.ASTypeAware;
import org.bgp4j.net.PathSegmentType;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ASPathAttribute extends PathAttribute implements ASTypeAware {

	public static class PathSegment {
		private ASType asType;
		private List<Integer> ases = new LinkedList<Integer>(); 
		private PathSegmentType pathSegmentType;
		
		public PathSegment(ASType asType) {
			this.asType = asType;
		}
		
		public PathSegment(ASType asType, PathSegmentType pathSegmentType, int[] asArray) {
			this(asType);

			this.pathSegmentType = pathSegmentType;
			
			for(int as : asArray)
				ases.add(as);
		}

		/**
		 * @return the asType
		 */
		public ASType getAsType() {
			return asType;
		}

		/**
		 * @return the ases
		 */
		public List<Integer> getAses() {
			return ases;
		}

		/**
		 * @param ases the ases to set
		 */
		public void setAses(List<Integer> ases) {
			this.ases = ases;
		}

		/**
		 * @return the type
		 */
		public PathSegmentType getPathSegmentType() {
			return pathSegmentType;
		}

		/**
		 * @param type the type to set
		 */
		public void setPathSegmentType(PathSegmentType type) {
			this.pathSegmentType = type;
		}

	}
	
	private ASType asType;
	private List<PathSegment> pathSegments = new LinkedList<PathSegment>(); 

	public ASPathAttribute(ASType asType) {
		super(Category.WELL_KNOWN_MANDATORY);
		
		this.asType = asType;
	}
	
	public ASPathAttribute(ASType asType, PathSegment[] segs) {
		this(asType);
		
		for(PathSegment seg : segs) {
			this.pathSegments.add(seg);
		}
	}

	/**
	 * @return the fourByteASNumber
	 */
	public boolean isFourByteASNumber() {
		return (this.asType == ASType.AS_NUMBER_4OCTETS);
	}

	/**
	 * @return the pathSegments
	 */
	public List<PathSegment> getPathSegments() {
		return pathSegments;
	}

	/**
	 * @param pathSegments the pathSegments to set
	 */
	public void setPathSegments(List<PathSegment> pathSegments) {
		this.pathSegments = pathSegments;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.update.ASTypeAware#getAsType()
	 */
	@Override
	public ASType getAsType() {
		return asType;
	}
}
