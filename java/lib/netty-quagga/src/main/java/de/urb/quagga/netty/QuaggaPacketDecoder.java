/**
 * 
 */
package de.urb.quagga.netty;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.inject.Inject;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.UpstreamMessageEvent;
import org.slf4j.Logger;

import de.urb.quagga.netty.protocol.QuaggaPacket;
import de.urb.quagga.netty.protocol.ZServAddInterfacePacket;
import de.urb.quagga.netty.protocol.ZServDeleteInterfacePacket;
import de.urb.quagga.netty.protocol.ZServInterfaceAddressAddPacket;
import de.urb.quagga.netty.protocol.ZServInterfaceAddressDeletePacket;

/**
 * Byte-array to POJO decoder for handling Quagga protocol packages sent from the Zebra server to the client.
 * The decoder does not contain fraing logic, it expects to be called in the pipeline with properly framed packets.
 * 
 * @author rainer
 *
 */
public class QuaggaPacketDecoder extends SimpleChannelUpstreamHandler {

	private @Inject Logger log;
	private @Inject OperatingSystem os;

	/**
	 * the main packet decoding logic. This method expects to receive ChannelBuffer containing one payload
	 * packet each stripped of any header information before the marker or command field.
	 * The decoded packet is sent upstream as a POJO  
	 * 
	 * @param ctx the channel handler context
	 * @param e the message event. The event payload must be a ChannelBuffer containing a single Quagga protocol packet.
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		ChannelBuffer buffer = (ChannelBuffer)e.getMessage();
		int bytesReadable = buffer.readableBytes();
		QuaggaPacket result = null;
		
		if(bytesReadable < 1) {
			throw new MalformedPacketException();
		}
		
		int markerOrCommand = buffer.readUnsignedByte();
		
		if(markerOrCommand == QuaggaConstants.ZEBRA_HEADER_MARKER) {
			// possible protocal version 1 or above
			bytesReadable = buffer.readableBytes();
			
			if(bytesReadable < 3)
				throw new MalformedPacketException();
			
			byte version = buffer.readByte();
			if(version != QuaggaConstants.ZEBRA_PROTOCOL_VERSION)
				throw new UnsupportedProtocolVersionException(version);
			
			short command = buffer.readShort();

			// protocol version 0 packet
			switch(command) {
			case QuaggaConstants.ZEBRA_INTERFACE_ADD:
				result = decodeZServAddInterfacePacket(buffer, version);
				break;
			case QuaggaConstants.ZEBRA_INTERFACE_DELETE:
				result = decodeZServDeleteInterfacePacket(buffer, version);
				break;
			case QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_ADD:
				result = decodeZServInterfaceAddressAddPacket(buffer, version);
				break;
			case QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_DELETE:
				result = decodeZServInterfaceAddressDeletePacket(buffer, version);
				break;
			default:
				log.info("cannot handle protocol packet version {}, command {}", version, command);
				break;
			}

		} else {
			// protocol version 0 packet
			switch(markerOrCommand) {
			case QuaggaConstants.ZEBRA_INTERFACE_ADD:
				result = decodeZServAddInterfacePacket(buffer, 0);
				break;
			case QuaggaConstants.ZEBRA_INTERFACE_DELETE:
				result = decodeZServDeleteInterfacePacket(buffer, 0);
				break;
			case QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_ADD:
				result = decodeZServInterfaceAddressAddPacket(buffer, 0);
				break;
			case QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_DELETE:
				result = decodeZServInterfaceAddressDeletePacket(buffer, 0);
				break;
			default:
				log.info("cannot handle protocol packet version 0, command {}", markerOrCommand);
				break;
			}
		}
		
		if(result != null)
			ctx.sendUpstream(new UpstreamMessageEvent(e.getChannel(), result, e.getRemoteAddress()));
	}

	
	private ZServAddInterfacePacket decodeZServAddInterfacePacket(ChannelBuffer buffer, int protocolVersion) throws MalformedPacketException {
		ZServAddInterfacePacket packet = new ZServAddInterfacePacket(protocolVersion);
		if(buffer.readableBytes() < QuaggaConstants.ZEBRA_INTERFACE_ADD_PKT_MIN)
			throw new MalformedPacketException(QuaggaConstants.ZEBRA_INTERFACE_ADD_PKT_MIN, buffer.readableBytes());
		
		packet.setInterfaceName(decodeString(buffer, QuaggaConstants.INTERFACE_NAMSIZ));
		packet.setInterfaceIndex((int)buffer.readUnsignedInt());
		packet.setStatusFlags((short)buffer.readUnsignedByte());
		packet.setInterfaceFlags(buffer.readLong());
		packet.setInterfaceMetric((int)buffer.readUnsignedInt());
		packet.setIpV4Mtu((int)buffer.readUnsignedInt());
		packet.setIpV6Mtu((int)buffer.readUnsignedInt());
		packet.setBandwidth((int)buffer.readUnsignedInt());
		
		return packet;
	}
	
	private ZServDeleteInterfacePacket decodeZServDeleteInterfacePacket(ChannelBuffer buffer, int protocolVersion) throws MalformedPacketException {
		ZServDeleteInterfacePacket packet = new ZServDeleteInterfacePacket(protocolVersion);
		if(buffer.readableBytes() < QuaggaConstants.ZEBRA_INTERFACE_DEL_PKT_MIN)
			throw new MalformedPacketException(QuaggaConstants.ZEBRA_INTERFACE_DEL_PKT_MIN, buffer.readableBytes());
		
		packet.setInterfaceName(decodeString(buffer, QuaggaConstants.INTERFACE_NAMSIZ));
		packet.setInterfaceIndex((int)buffer.readUnsignedInt());
		packet.setStatusFlags((short)buffer.readUnsignedByte());
		packet.setInterfaceFlags(buffer.readLong());
		packet.setInterfaceMetric((int)buffer.readUnsignedInt());
		packet.setIpV4Mtu((int)buffer.readUnsignedInt());
		packet.setIpV6Mtu((int)buffer.readUnsignedInt());
		packet.setBandwidth((int)buffer.readUnsignedInt());
		
		return packet;
	}
	
	private ZServInterfaceAddressAddPacket decodeZServInterfaceAddressAddPacket(ChannelBuffer buffer, int protocolVersion) throws MalformedPacketException {
		ZServInterfaceAddressAddPacket packet = new ZServInterfaceAddressAddPacket(protocolVersion);
		int readable = buffer.readableBytes();
		
		if(readable < QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_ADD_PKT_MIN)
			throw new MalformedPacketException(QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_ADD_PKT_MIN, readable);
		if(readable > QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_ADD_PKT_MAX)
			throw new MalformedPacketException(QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_ADD_PKT_MAX, readable);
		
		packet.setInterfaceIndex((int)buffer.readUnsignedInt());
		packet.setFlags(buffer.readUnsignedByte());
		
		int addressFamily = buffer.readUnsignedByte();
		int addressLength = 0;
		
		if(addressFamily == os.getAddressFamilyInet4()) {
			addressLength = QuaggaConstants.SIZEOF_IPV4_ADDRESS;
		} else if(addressFamily == os.getAddressFamilyInet6()) {
			addressLength = QuaggaConstants.SIZEOF_IPV6_ADDRESS;			
		} else {
			throw new MalformedPacketException("received unknown address family " + addressFamily 
					+ ", wanted either IPv4(" + os.getAddressFamilyInet4() + ") or IPv6(" + os.getAddressFamilyInet6() + ")");
		}

		// allocate 4 (IPv4) or 16 (IPv6) byte address buffer
		byte[] addressBuffer = new byte[addressLength];
		
		// handle address
		buffer.readBytes(addressBuffer);
		try {
			packet.setAddress(InetAddress.getByAddress(addressBuffer));
		} catch (UnknownHostException e) {
			log.error("bad IP address length for address", e);
			
			throw new MalformedPacketException(e);
		}
		
		// address prefix
		packet.setPrefixLength(buffer.readUnsignedByte());

		// handle destination address
		buffer.readBytes(addressBuffer);
		
		if(!isAllZero(addressBuffer)) {
			try {
				packet.setDestination(InetAddress.getByAddress(addressBuffer));
			} catch (UnknownHostException e) {
				log.error("bad IP address length for destination", e);
				
				throw new MalformedPacketException(e);
			}				
		}

		return packet;
	}
	
	private ZServInterfaceAddressDeletePacket decodeZServInterfaceAddressDeletePacket(ChannelBuffer buffer, int protocolVersion) throws MalformedPacketException {
		ZServInterfaceAddressDeletePacket packet = new ZServInterfaceAddressDeletePacket(protocolVersion);
		int readable = buffer.readableBytes();
		
		if(readable < QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_DELETE_PKT_MIN)
			throw new MalformedPacketException(QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_DELETE_PKT_MIN, readable);
		if(readable > QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_DELETE_PKT_MAX)
			throw new MalformedPacketException(QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_DELETE_PKT_MAX, readable);
		
		packet.setInterfaceIndex((int)buffer.readUnsignedInt());
		packet.setFlags(buffer.readUnsignedByte());
		
		int addressFamily = buffer.readUnsignedByte();
		int addressLength = 0;
		
		if(addressFamily == os.getAddressFamilyInet4()) {
			addressLength = QuaggaConstants.SIZEOF_IPV4_ADDRESS;
		} else if(addressFamily == os.getAddressFamilyInet6()) {
			addressLength = QuaggaConstants.SIZEOF_IPV6_ADDRESS;			
		} else {
			throw new MalformedPacketException("received unknown address family " + addressFamily 
					+ ", wanted either IPv4(" + os.getAddressFamilyInet4() + ") or IPv6(" + os.getAddressFamilyInet6() + ")");
		}

		// allocate 4 (IPv4) or 16 (IPv6) byte address buffer
		byte[] addressBuffer = new byte[addressLength];
		
		// handle address
		buffer.readBytes(addressBuffer);
		try {
			packet.setAddress(InetAddress.getByAddress(addressBuffer));
		} catch (UnknownHostException e) {
			log.error("bad IP address length for address", e);
			
			throw new MalformedPacketException(e);
		}
		
		// address prefix
		packet.setPrefixLength(buffer.readUnsignedByte());

		// handle destination address
		buffer.readBytes(addressBuffer);
		
		if(!isAllZero(addressBuffer)) {
			try {
				packet.setDestination(InetAddress.getByAddress(addressBuffer));
			} catch (UnknownHostException e) {
				log.error("bad IP address length for destination", e);
				
				throw new MalformedPacketException(e);
			}				
		}

		return packet;
	}
	
	/**
	 * decode a string in the packet. The string is either NUL-char terminated or is exactly maxLength bytes long-
	 * The number of bytes consumed from the buffer is maxLength bytes.
	 * 
	 * @param buffer the source buffer
	 * @param maxLength the number of bytes to consume from the buffer
	 * @return the string in the buffer
	 */
	private String decodeString(ChannelBuffer buffer, int maxLength) {
		StringBuilder builder = new StringBuilder();
		byte[] sBuf = new byte[maxLength];
		
		buffer.readBytes(sBuf);
		
		for(int i=0; i<maxLength; i++) {
			byte b = sBuf[i];
			
			if(b == 0)
				break;
			
			builder.append((char)b);
		}
		
		
		return builder.toString();
	}

	/**
	 * Check if a byte array is all zero. Quagga uses zero-filled bytes blocks to signal some information is not set in the packet. 
	 * 
	 * @param array the byte array to check
	 * @return true if the byte array contains NULL-bytes only, false otherwise
	 */
	private boolean isAllZero(byte[] array) {
		boolean result = true;
		
		for(int i=0; i<array.length; i++) {
			if(array[i] != 0) {
				result = false;
				break;
			}
		}
		
		return result;
	}
}
