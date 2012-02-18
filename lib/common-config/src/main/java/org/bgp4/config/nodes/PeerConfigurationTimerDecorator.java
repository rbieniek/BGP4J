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
 * File: org.bgp4.config.nodes.PeerConfigurationTimerDecorator.java 
 */
package org.bgp4.config.nodes;


/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public abstract class PeerConfigurationTimerDecorator extends PeerConfigurationDecorator {

	protected PeerConfigurationTimerDecorator(PeerConfiguration decorated) {
		super(decorated);
	}

	/**
	 * @return
	 * @see org.bgp4.config.nodes.PeerConfiguration#getHoldTime()
	 */
	public int getHoldTime() {
		int holdTime = decorated.getHoldTime();
		
		if(holdTime == 0)
			holdTime = getDefaultHoldTime();
		
		return holdTime;
	}

	/**
	 * 
	 * @return
	 */
	protected abstract int getDefaultHoldTime();
	
	/**
	 * @return
	 * @see org.bgp4.config.nodes.PeerConfiguration#getIdleHoldTime()
	 */
	public int getIdleHoldTime() {
		int retryInterval = decorated.getIdleHoldTime();
		
		if(retryInterval == 0)
			retryInterval = getDefaultIdleHoldTime();
		
		return retryInterval;
	}
	
	/**
	 * 
	 * @return
	 */
	protected abstract int  getDefaultIdleHoldTime();

	/**
	 * @return
	 * @see org.bgp4.config.nodes.PeerConfiguration#getIdleHoldTime()
	 */
	public int getDelayOpenTime() {
		int retryInterval = decorated.getDelayOpenTime();
		
		if(retryInterval == 0)
			retryInterval = getDefaultDelayOpenTime();
		
		return retryInterval;
	}
	
	/**
	 * 
	 * @return
	 */
	protected abstract int  getDefaultDelayOpenTime();
}
