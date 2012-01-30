/**
 * 
 */
package org.bgp4j.netty.protocol;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.MessageEvent;

/**
 * @author rainer
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
