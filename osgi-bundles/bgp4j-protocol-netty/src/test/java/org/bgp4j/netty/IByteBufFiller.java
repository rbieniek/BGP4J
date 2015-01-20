/**
 * 
 */
package org.bgp4j.netty;

import io.netty.buffer.ByteBuf;

/**
 * @author rainer
 *
 */
public interface IByteBufFiller {

	public void fillBuffer(ByteBuf buffer) throws Exception;
}
