/**
 * 
 */
package de.urb.quagga.netty;

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

/**
 * @author rainer
 *
 */
public class QuaggaPacketDecoder extends SimpleChannelUpstreamHandler {

	@Inject Logger log;
	
	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelUpstreamHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
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
			throw new MalformedPacketException();
		
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
			throw new MalformedPacketException();
		
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
}
