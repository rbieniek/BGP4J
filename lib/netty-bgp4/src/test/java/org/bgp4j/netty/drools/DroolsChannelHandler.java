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
 * File: org.bgp4j.netty.DroolsChannelHandler.java 
 */
package org.bgp4j.netty.drools;

import junit.framework.Assert;

import org.bgp4.config.nodes.PeerConfiguration;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class DroolsChannelHandler extends SimpleChannelHandler {

	private class FactUpdateinvokerImpl implements FactUpdateInvoker {

		@Override
		public void invokeFactUpdate() {
			session.update(channelHandle, channel);
		}
		
	}
	
	private KnowledgeBase knowledgeBase;
	private StatefulKnowledgeSession session;
	private FactHandle channelHandle;
	NetworkChannel channel;
	
	public DroolsChannelHandler(String rulesFile) {
		KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		
		knowledgeBuilder.add(ResourceFactory.newFileResource(rulesFile), ResourceType.DRL);
		
		Assert.assertFalse(knowledgeBuilder.hasErrors());
		
		this.knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
		this.knowledgeBase.addKnowledgePackages(knowledgeBuilder.getKnowledgePackages());
		
	}
	
	public void initialize(PeerConfiguration peerConfiguration) {
		session = this.knowledgeBase.newStatefulKnowledgeSession();
		
		session.insert(peerConfiguration);
	}

	public void shutdown() {
		if(session != null)
			session.dispose();
	}
	
	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelHandler#channelOpen(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
	 */
	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		this.channel = new NetworkChannel(e.getChannel());
		this.channel.setUpdater(new FactUpdateinvokerImpl());
		this.channelHandle = session.insert(this.channel);
		this.session.fireAllRules();
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelHandler#channelClosed(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
	 */
	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		this.session.retract(channelHandle);
		this.channelHandle = null;
		this.channel = null;
		this.channel.setUpdater(null);
		session.fireAllRules();
	}
}
