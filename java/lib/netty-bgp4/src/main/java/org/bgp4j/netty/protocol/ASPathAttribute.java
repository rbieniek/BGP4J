/**
 * 
 */
package org.bgp4j.netty.protocol;

import java.util.LinkedList;
import java.util.List;

import org.bgp4j.netty.BGPv4Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author rainer
 *
 */
public class ASPathAttribute extends PathAttribute {

	private static final int AS_SET_CODE = 1;
	private static final int AS_SEQUENCE_CODE = 2;
	
	
	public enum Type {
		AS_SET,      // unordered set of ASes a route in the UPDATE message has traversed
		AS_SEQUENCE; // ordered set of ASes a route in the UPDATE message has traversed
		
		int toCode() {
			switch(this) {
			case AS_SET:
				return AS_SET_CODE;
			case AS_SEQUENCE:
				return AS_SEQUENCE_CODE;
			default:
				throw new IllegalArgumentException("illegal AS_PATH type" + this);
			}
		}
		
		static Type fromCode(int code) {
			switch(code) {
			case AS_SET_CODE:
				return AS_SET;
			case AS_SEQUENCE_CODE:
				return AS_SEQUENCE;
			default:
				throw new IllegalArgumentException("illegal AS_PATH type" + code);				
			}
		}
	}
	
	private boolean fourByteASNumber;
	private List<Integer> ases = new LinkedList<Integer>(); 
	private Type type;

	public ASPathAttribute(boolean fourByteASNumber) {
		this.fourByteASNumber = fourByteASNumber;
	}
	
	@Override
	protected int getTypeCode() {
		return (isFourByteASNumber() 
				? BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH 
						: BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH);
	}

	@Override
	protected int getValueLength() {
		int size = 2; // type + length field

		if(this.ases != null)
			size += this.ases.size() * (isFourByteASNumber() ? 4 : 2);
		
		return size;
	}

	@Override
	protected ChannelBuffer encodeValue() {
		ChannelBuffer buffer = ChannelBuffers.buffer(getValueLength());
		
		buffer.writeByte(getType().toCode());
		
		if(this.ases != null) {
			buffer.writeByte(this.ases.size());
			
			for (int as : this.ases) {
				if(isFourByteASNumber())
					buffer.writeInt(as);
				else
					buffer.writeShort(as);
			}
		} else
			buffer.writeByte(0);
		
		return buffer;
	}

	/**
	 * @return the fourByteASNumber
	 */
	public boolean isFourByteASNumber() {
		return fourByteASNumber;
	}

	/**
	 * @param fourByteASNumber the fourByteASNumber to set
	 */
	public void setFourByteASNumber(boolean fourByteASNumber) {
		this.fourByteASNumber = fourByteASNumber;
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
	public Type getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

}
