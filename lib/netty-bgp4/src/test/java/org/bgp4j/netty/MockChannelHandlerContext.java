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
package org.bgp4j.netty;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.MessageEvent;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class MockChannelHandlerContext implements ChannelHandlerContext {

	private Object attachment;
	private Object message;
	
	/**
	 * @return the message
	 */
	public Object getMessage() {
		return message;
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.ChannelHandlerContext#getChannel()
	 */
	@Override
	public Channel getChannel() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.ChannelHandlerContext#getPipeline()
	 */
	@Override
	public ChannelPipeline getPipeline() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.ChannelHandlerContext#getName()
	 */
	@Override
	public String getName() {
		return "mock-handler-context";
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.ChannelHandlerContext#getHandler()
	 */
	@Override
	public ChannelHandler getHandler() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.ChannelHandlerContext#canHandleUpstream()
	 */
	@Override
	public boolean canHandleUpstream() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.ChannelHandlerContext#canHandleDownstream()
	 */
	@Override
	public boolean canHandleDownstream() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.ChannelHandlerContext#sendUpstream(org.jboss.netty.channel.ChannelEvent)
	 */
	@Override
	public void sendUpstream(ChannelEvent e) {
		if(e instanceof MessageEvent) {
			this.message = ((MessageEvent)e).getMessage();
		} else 
			throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.ChannelHandlerContext#sendDownstream(org.jboss.netty.channel.ChannelEvent)
	 */
	@Override
	public void sendDownstream(ChannelEvent e) {
		if(e instanceof MessageEvent) {
			this.message = ((MessageEvent)e).getMessage();
		} else 
			throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.ChannelHandlerContext#getAttachment()
	 */
	@Override
	public Object getAttachment() {
		return this.attachment;
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.ChannelHandlerContext#setAttachment(java.lang.Object)
	 */
	@Override
	public void setAttachment(Object attachment) {
		this.attachment = attachment;
	}

}
