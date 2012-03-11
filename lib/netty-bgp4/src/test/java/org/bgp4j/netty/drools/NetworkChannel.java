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
 * File: org.bgp4j.netty.drools.NetworkChannel.java 
 */
package org.bgp4j.netty.drools;

import java.util.LinkedList;
import java.util.List;

import org.bgp4j.netty.protocol.BGPv4Packet;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class NetworkChannel {

	private Channel channel;
	private FactUpdateInvoker updater;
	private List<BGPv4Packet> sentStream = new LinkedList<BGPv4Packet>();
	private List<BGPv4Packet> receivedStream = new LinkedList<BGPv4Packet>();
	
	public NetworkChannel(Channel channel) {
		this.channel = channel;
	}

	public void sentPacket(final BGPv4Packet packet) {
		channel.write(packet).addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				sentStream.add(packet);
				updater.invokeFactUpdate();
			}
		});
	}
	
	public void receivePacket(BGPv4Packet packet) {
		this.receivedStream.add(packet);
		updater.invokeFactUpdate();
	}

	/**
	 * @param updater the updater to set
	 */
	void setUpdater(FactUpdateInvoker updater) {
		this.updater = updater;
	}
}
