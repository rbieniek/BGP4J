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
 * File: org.bgp4.config.nodes.AS4OctetPeerConfigurationDecorator.java 
 */
package org.bgp4.config.nodes;

import org.bgp4.config.nodes.impl.AS4OctetRequiredCapabiltiesDecorator;

/**
 * Decorator which assures that the Autonomous System 4 Octets capabilitiy is implictly
 * set when either the local or remote AS number is larger than 65535. 
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class AS4OctetPeerConfigurationDecorator extends PeerConfigurationDecorator {

	public AS4OctetPeerConfigurationDecorator(PeerConfiguration decorated) {
		super(decorated);
	}

	/* (non-Javadoc)
	 * @see org.bgp4.config.nodes.PeerConfigurationDecorator#getCapabilities()
	 */
	@Override
	public Capabilities getCapabilities() {
		if(getLocalAS() > 65535 || getRemoteAS() > 65535)
			return new AS4OctetRequiredCapabiltiesDecorator(super.getCapabilities(), getLocalAS());
		else
			return super.getCapabilities();
	}

}
