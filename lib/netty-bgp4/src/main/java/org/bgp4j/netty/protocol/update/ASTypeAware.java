package org.bgp4j.netty.protocol.update;

import org.bgp4j.netty.ASType;

public interface ASTypeAware {

	/**
	 * @return the asType
	 */
	public abstract ASType getAsType();

}