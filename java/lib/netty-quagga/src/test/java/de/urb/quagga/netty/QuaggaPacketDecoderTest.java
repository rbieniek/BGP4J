/**
 * 
 */
package de.urb.quagga.netty;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.UpstreamMessageEvent;
import org.junit.Assert;
import org.junit.Test;

import de.urb.quagga.netty.protocol.QuaggaPacket;
import de.urb.quagga.netty.protocol.ZServAddInterfacePacket;
import de.urb.quagga.netty.protocol.ZServDeleteInterfacePacket;
import de.urb.quagga.netty.protocol.ZServIPv4RouteAddPacket;
import de.urb.quagga.netty.protocol.ZServInterfaceAddressAddPacket;
import de.urb.quagga.netty.protocol.ZServInterfaceAddressDeletePacket;
import de.urb.quagga.netty.protocol.ZServInterfaceDownPacket;
import de.urb.quagga.netty.protocol.ZServInterfaceUpPacket;
import de.urb.quagga.weld.WeldTestCaseBase;

/**
 * @author rainer
 *
 */
public class QuaggaPacketDecoderTest extends WeldTestCaseBase {

	/**
	 * create a protocol version 0 packet header.
	 * 
	 * @return a prepared packet header after which the command can be added
	 */
	private ChannelBuffer createQuaggaPacketVersion0() {
		ChannelBuffer buffer = ChannelBuffers.buffer(4096); // Quaga constant
		
		return buffer;
	}
	
	/**
	 * create a protocol version 1 packet header.
	 * 
	 * @return a prepared packet header after which the command can be added
	 */
	private ChannelBuffer createQuaggaPacketVersion1() {
		ChannelBuffer buffer = ChannelBuffers.buffer(4096); // Quaga constant
		
		buffer.writeByte(QuaggaConstants.ZEBRA_HEADER_MARKER); // Quagga marker constant
		buffer.writeByte(QuaggaConstants.ZEBRA_PROTOCOL_VERSION);  // Quagga protocol version 1
		
		return buffer;
	}

	/**
	 * copy a string into the buffer as a fixed-length byte array.
	 * 
	 * @param buffer
	 * @param string
	 * @param maxLength
	 */
	private void putString(ChannelBuffer buffer, String string, int maxLength) {
		byte[] tBuf = new byte[maxLength];
		
		for(int i=0; i<string.length() && i < maxLength; i++) {
			char c = string.charAt(i);
			
			tBuf[i] = (byte)c;
		}
		
		buffer.writeBytes(tBuf, 0, maxLength);
	}
	
	@SuppressWarnings("unchecked")
	private <T extends QuaggaPacket> T executeDecode(ChannelBuffer buffer) throws Exception {
		MockChannelHandler handler = obtainInstance(MockChannelHandler.class);
		ChannelPipeline pipeline = Channels.pipeline(
				obtainInstance(QuaggaPacketDecoder.class),
				handler
				);
		MockChannelSink sink = obtainInstance(MockChannelSink.class);
		MockChannel channel = new MockChannel(pipeline, sink);
		UpstreamMessageEvent me = new UpstreamMessageEvent(channel, buffer, new InetSocketAddress(InetAddress.getLocalHost(), 1));
		
		pipeline.sendUpstream(me);
		
		MessageEvent result = handler.nextEvent();

		if(result != null)
			return (T)result.getMessage();
		else
			return null;
	}

	// ---- Interface delete packet, protocol version 1
	
	@Test
	public void testZServInterfaceDeletePacketv1status0() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_DELETE); // Quagga protocol constant ZEBRA_INTERFACE_DELETE
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(0); // status flag
		buffer.writeLong(0x00112233aabbccddL); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServDeleteInterfacePacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 1);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getStatusFlags(), 0);
		Assert.assertFalse(packet.isInterfaceActive());
		Assert.assertFalse(packet.isInterfaceLinkDetection());
		Assert.assertFalse(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0x00112233aabbccddL);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}
	
	@Test
	public void testZServInterfaceDeletePacketv1status1() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_DELETE); // Quagga protocol constant ZEBRA_INTERFACE_ADD
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(1); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServDeleteInterfacePacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 1);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getStatusFlags(), 1);
		Assert.assertTrue(packet.isInterfaceActive());
		Assert.assertFalse(packet.isInterfaceLinkDetection());
		Assert.assertFalse(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0xaabbccdd00112233L);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}
	
	@Test
	public void testZServInterfaceDeletePacketv1status2() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_DELETE); // Quagga protocol constant ZEBRA_INTERFACE_ADD
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(2); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServDeleteInterfacePacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 1);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getStatusFlags(), 2);
		Assert.assertFalse(packet.isInterfaceActive());
		Assert.assertFalse(packet.isInterfaceLinkDetection());
		Assert.assertTrue(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0xaabbccdd00112233L);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}
	
	@Test
	public void testZServInterfaceDeletePacketv1status4() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_DELETE); // Quagga protocol constant ZEBRA_INTERFACE_ADD
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(4); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServDeleteInterfacePacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 1);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getStatusFlags(), 4);
		Assert.assertFalse(packet.isInterfaceActive());
		Assert.assertTrue(packet.isInterfaceLinkDetection());
		Assert.assertFalse(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0xaabbccdd00112233L);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}

	// ---- Interface add packet, protocol version 0
	
	@Test
	public void testZServInterfaceDeletePacketv0status0() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion0();
		
		buffer.writeByte(QuaggaConstants.ZEBRA_INTERFACE_DELETE); // Quagga protocol constant ZEBRA_INTERFACE_ADD
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(0); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServDeleteInterfacePacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 0);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getStatusFlags(), 0);
		Assert.assertFalse(packet.isInterfaceActive());
		Assert.assertFalse(packet.isInterfaceLinkDetection());
		Assert.assertFalse(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0xaabbccdd00112233L);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}
	
	@Test
	public void testZServInterfaceDeletePacketv0status1() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion0();
		
		buffer.writeByte(QuaggaConstants.ZEBRA_INTERFACE_DELETE); // Quagga protocol constant ZEBRA_INTERFACE_ADD
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(1); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServDeleteInterfacePacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 0);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getStatusFlags(), 1);
		Assert.assertTrue(packet.isInterfaceActive());
		Assert.assertFalse(packet.isInterfaceLinkDetection());
		Assert.assertFalse(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0xaabbccdd00112233L);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}
	
	@Test
	public void testZServInterfaceDeletePacketv0status2() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion0();
		
		buffer.writeByte(QuaggaConstants.ZEBRA_INTERFACE_DELETE); // Quagga protocol constant ZEBRA_INTERFACE_ADD
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(2); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServDeleteInterfacePacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 0);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getStatusFlags(), 2);
		Assert.assertFalse(packet.isInterfaceActive());
		Assert.assertFalse(packet.isInterfaceLinkDetection());
		Assert.assertTrue(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0xaabbccdd00112233L);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}
	
	@Test
	public void testZServInterfaceDeletePacketv0status4() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion0();
		
		buffer.writeByte(QuaggaConstants.ZEBRA_INTERFACE_DELETE); // Quagga protocol constant ZEBRA_INTERFACE_ADD
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(4); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServDeleteInterfacePacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 0);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getStatusFlags(), 4);
		Assert.assertFalse(packet.isInterfaceActive());
		Assert.assertTrue(packet.isInterfaceLinkDetection());
		Assert.assertFalse(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0xaabbccdd00112233L);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}
	// ---- Interface add packet, protocol version 1
	
	@Test
	public void testZServInterfaceAddPacketv1status0() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_ADD); // Quagga protocol constant ZEBRA_INTERFACE_ADD
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(0); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServAddInterfacePacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 1);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getStatusFlags(), 0);
		Assert.assertFalse(packet.isInterfaceActive());
		Assert.assertFalse(packet.isInterfaceLinkDetection());
		Assert.assertFalse(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0xaabbccdd00112233L);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}
	
	@Test
	public void testZServInterfaceAddPacketv1status1() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_ADD); // Quagga protocol constant ZEBRA_INTERFACE_ADD
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(1); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServAddInterfacePacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 1);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getStatusFlags(), 1);
		Assert.assertTrue(packet.isInterfaceActive());
		Assert.assertFalse(packet.isInterfaceLinkDetection());
		Assert.assertFalse(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0xaabbccdd00112233L);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}
	
	@Test
	public void testZServInterfaceAddPacketv1status2() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_ADD); // Quagga protocol constant ZEBRA_INTERFACE_ADD
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(2); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServAddInterfacePacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 1);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getStatusFlags(), 2);
		Assert.assertFalse(packet.isInterfaceActive());
		Assert.assertFalse(packet.isInterfaceLinkDetection());
		Assert.assertTrue(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0xaabbccdd00112233L);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}
	
	@Test
	public void testZServInterfaceAddPacketv1status4() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_ADD); // Quagga protocol constant ZEBRA_INTERFACE_ADD
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(4); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServAddInterfacePacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 1);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getStatusFlags(), 4);
		Assert.assertFalse(packet.isInterfaceActive());
		Assert.assertTrue(packet.isInterfaceLinkDetection());
		Assert.assertFalse(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0xaabbccdd00112233L);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}

	// ---- Interface add packet, protocol version 0
	
	@Test
	public void testZServInterfaceAddPacketv0status0() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion0();
		
		buffer.writeByte(QuaggaConstants.ZEBRA_INTERFACE_ADD); // Quagga protocol constant ZEBRA_INTERFACE_ADD
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(0); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServAddInterfacePacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 0);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getStatusFlags(), 0);
		Assert.assertFalse(packet.isInterfaceActive());
		Assert.assertFalse(packet.isInterfaceLinkDetection());
		Assert.assertFalse(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0xaabbccdd00112233L);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}
	
	@Test
	public void testZServInterfaceAddPacketv0status1() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion0();
		
		buffer.writeByte(QuaggaConstants.ZEBRA_INTERFACE_ADD); // Quagga protocol constant ZEBRA_INTERFACE_ADD
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(1); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServAddInterfacePacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 0);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getStatusFlags(), 1);
		Assert.assertTrue(packet.isInterfaceActive());
		Assert.assertFalse(packet.isInterfaceLinkDetection());
		Assert.assertFalse(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0xaabbccdd00112233L);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}
	
	@Test
	public void testZServInterfaceAddPacketv0status2() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion0();
		
		buffer.writeByte(QuaggaConstants.ZEBRA_INTERFACE_ADD); // Quagga protocol constant ZEBRA_INTERFACE_ADD
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(2); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServAddInterfacePacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 0);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getStatusFlags(), 2);
		Assert.assertFalse(packet.isInterfaceActive());
		Assert.assertFalse(packet.isInterfaceLinkDetection());
		Assert.assertTrue(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0xaabbccdd00112233L);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}
	
	@Test
	public void testZServInterfaceAddPacketv0status4() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion0();
		
		buffer.writeByte(QuaggaConstants.ZEBRA_INTERFACE_ADD); // Quagga protocol constant ZEBRA_INTERFACE_ADD
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(4); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServAddInterfacePacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 0);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getInterfaceIndex(), 1);
		Assert.assertEquals(packet.getStatusFlags(), 4);
		Assert.assertFalse(packet.isInterfaceActive());
		Assert.assertTrue(packet.isInterfaceLinkDetection());
		Assert.assertFalse(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0xaabbccdd00112233L);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}
	
	// ---- Interface address add packet, protocol version 0, IPv4 addresses
	@Test
	public void testZServerInterfaceAddressAddPacketV0NoFlagsIpV4prefixNullDestination() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion0();
		OperatingSystem os = obtainInstance(OperatingSystem.class);
		byte[] address = new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x82, (byte)0 };
		byte[] destination = new byte[] { (byte)0, (byte)0, (byte)0, (byte)0 };
		
		buffer.writeByte(QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_ADD); // Quagga protocol constant ZEBRA_INTERFACE_ADDRESS_ADD
		buffer.writeInt(1); // Interface index
		buffer.writeByte(0); // flags
		buffer.writeByte(os.getAddressFamilyInet4()); // write address family value (OS dependent)
		buffer.writeBytes(address); // write binary address
		buffer.writeByte(24); // write prefix length
		buffer.writeBytes(destination); // write binary address
		
		ZServInterfaceAddressAddPacket packet = executeDecode(buffer);
	
		Assert.assertEquals(packet.getProtocolVersion(), 0);
		Assert.assertEquals(packet.getInterfaceIndex(), 1);
		Assert.assertEquals(packet.getFlags(), 0);
		Assert.assertFalse(packet.isSecondaryAddress());
		Assert.assertFalse(packet.isPeerAddress());
		Assert.assertNotNull(packet.getAddress());
		Assert.assertTrue(packet.getAddress() instanceof Inet4Address);
		Assert.assertEquals(packet.getAddress(), InetAddress.getByAddress(address));
		Assert.assertEquals(packet.getPrefixLength(), 24);
		Assert.assertNull(packet.getDestination());
	}

	@Test
	public void testZServerInterfaceAddressAddPacketV0SecondaryAddressIpV4prefixNullDestination() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion0();
		OperatingSystem os = obtainInstance(OperatingSystem.class);
		byte[] address = new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x82, (byte)0 };
		byte[] destination = new byte[] { (byte)0, (byte)0, (byte)0, (byte)0 };
		
		buffer.writeByte(QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_ADD); // Quagga protocol constant ZEBRA_INTERFACE_ADDRESS_ADD
		buffer.writeInt(1); // Interface index
		buffer.writeByte(QuaggaConstants.ZEBRA_IFA_SECONDARY); // flags
		buffer.writeByte(os.getAddressFamilyInet4()); // write address family value (OS dependent)
		buffer.writeBytes(address); // write binary address
		buffer.writeByte(24); // write prefix length
		buffer.writeBytes(destination); // write binary address
		
		ZServInterfaceAddressAddPacket packet = executeDecode(buffer);
	
		Assert.assertEquals(packet.getProtocolVersion(), 0);
		Assert.assertEquals(packet.getInterfaceIndex(), 1);
		Assert.assertEquals(packet.getFlags(), QuaggaConstants.ZEBRA_IFA_SECONDARY);
		Assert.assertTrue(packet.isSecondaryAddress());
		Assert.assertFalse(packet.isPeerAddress());
		Assert.assertNotNull(packet.getAddress());
		Assert.assertTrue(packet.getAddress() instanceof Inet4Address);
		Assert.assertEquals(packet.getAddress(), InetAddress.getByAddress(address));
		Assert.assertEquals(packet.getPrefixLength(), 24);
		Assert.assertNull(packet.getDestination());
	}

	@Test
	public void testZServerInterfaceAddressAddPacketV0PeerAddressIpV4prefixPeerDestination() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion0();
		OperatingSystem os = obtainInstance(OperatingSystem.class);
		byte[] address = new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x82, (byte)0x01 };
		byte[] destination = new byte[] { (byte)0xAB, (byte)0x10, (byte)0xfe, (byte)0x2 };
		
		buffer.writeByte(QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_ADD); // Quagga protocol constant ZEBRA_INTERFACE_ADDRESS_ADD
		buffer.writeInt(1); // Interface index
		buffer.writeByte(QuaggaConstants.ZEBRA_IFA_PEER); // flags
		buffer.writeByte(os.getAddressFamilyInet4()); // write address family value (OS dependent)
		buffer.writeBytes(address); // write binary address
		buffer.writeByte(32); // write prefix length
		buffer.writeBytes(destination); // write binary address
		
		ZServInterfaceAddressAddPacket packet = executeDecode(buffer);
	
		Assert.assertEquals(packet.getProtocolVersion(), 0);
		Assert.assertEquals(packet.getInterfaceIndex(), 1);
		Assert.assertEquals(packet.getFlags(), QuaggaConstants.ZEBRA_IFA_PEER);
		Assert.assertFalse(packet.isSecondaryAddress());
		Assert.assertTrue(packet.isPeerAddress());
		Assert.assertNotNull(packet.getAddress());
		Assert.assertTrue(packet.getAddress() instanceof Inet4Address);
		Assert.assertEquals(packet.getAddress(), InetAddress.getByAddress(address));
		Assert.assertEquals(packet.getPrefixLength(), 32);
		Assert.assertNotNull(packet.getDestination());
		Assert.assertTrue(packet.getDestination() instanceof Inet4Address);
		Assert.assertEquals(packet.getDestination(), InetAddress.getByAddress(destination));
	}

	// ---- Interface address add packet, protocol version 0, IPv6 addresses
	@Test
	public void testZServerInterfaceAddressAddPacketV0NoFlagsIpV6prefixNullDestination() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion0();
		OperatingSystem os = obtainInstance(OperatingSystem.class);
		byte[] address = new byte[] { // represents IPv6 address 1080:0:0:0:8:800:200c:417a 
				(byte)0x10, (byte)0x80, 
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x8,
				(byte)0x8, (byte)0x0,
				(byte)0x20, (byte)0xc,
				(byte)0x41, (byte)0x7a
				};
		byte[] destination = new byte[] { (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, 
				(byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0 };
		
		buffer.writeByte(QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_ADD); // Quagga protocol constant ZEBRA_INTERFACE_ADDRESS_ADD
		buffer.writeInt(1); // Interface index
		buffer.writeByte(0); // flags
		buffer.writeByte(os.getAddressFamilyInet6()); // write address family value (OS dependent)
		buffer.writeBytes(address); // write binary address
		buffer.writeByte(128); // write prefix length
		buffer.writeBytes(destination); // write binary address
		
		ZServInterfaceAddressAddPacket packet = executeDecode(buffer);
	
		Assert.assertEquals(packet.getProtocolVersion(), 0);
		Assert.assertEquals(packet.getInterfaceIndex(), 1);
		Assert.assertEquals(packet.getFlags(), 0);
		Assert.assertFalse(packet.isSecondaryAddress());
		Assert.assertFalse(packet.isPeerAddress());
		Assert.assertNotNull(packet.getAddress());
		Assert.assertTrue(packet.getAddress() instanceof Inet6Address);
		Assert.assertEquals(packet.getAddress(), InetAddress.getByAddress(address));
		Assert.assertEquals(packet.getPrefixLength(), 128);
		Assert.assertNull(packet.getDestination());
	}

	@Test
	public void testZServerInterfaceAddressAddPacketV0SecondaryAddressIpV6prefixNullDestination() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion0();
		OperatingSystem os = obtainInstance(OperatingSystem.class);
		byte[] address = new byte[] { // represents IPv6 address 1080:0:0:0:8:800:200c:417a 
				(byte)0x10, (byte)0x80, 
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x8,
				(byte)0x8, (byte)0x0,
				(byte)0x20, (byte)0xc,
				(byte)0x41, (byte)0x7a
				};
		byte[] destination = new byte[] { (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, 
				(byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0 };
		
		buffer.writeByte(QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_ADD); // Quagga protocol constant ZEBRA_INTERFACE_ADDRESS_ADD
		buffer.writeInt(1); // Interface index
		buffer.writeByte(QuaggaConstants.ZEBRA_IFA_SECONDARY); // flags
		buffer.writeByte(os.getAddressFamilyInet6()); // write address family value (OS dependent)
		buffer.writeBytes(address); // write binary address
		buffer.writeByte(128); // write prefix length
		buffer.writeBytes(destination); // write binary address
		
		ZServInterfaceAddressAddPacket packet = executeDecode(buffer);
	
		Assert.assertEquals(packet.getProtocolVersion(), 0);
		Assert.assertEquals(packet.getInterfaceIndex(), 1);
		Assert.assertEquals(packet.getFlags(), QuaggaConstants.ZEBRA_IFA_SECONDARY);
		Assert.assertTrue(packet.isSecondaryAddress());
		Assert.assertFalse(packet.isPeerAddress());
		Assert.assertNotNull(packet.getAddress());
		Assert.assertTrue(packet.getAddress() instanceof Inet6Address);
		Assert.assertEquals(packet.getAddress(), InetAddress.getByAddress(address));
		Assert.assertEquals(packet.getPrefixLength(), 128);
		Assert.assertNull(packet.getDestination());
	}

	@Test
	public void testZServerInterfaceAddressAddPacketV0PeerAddressIpV6prefixPeerDestination() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion0();
		OperatingSystem os = obtainInstance(OperatingSystem.class);
		byte[] address = new byte[] { // represents IPv6 address 1080:0:0:0:8:800:200c:417a 
				(byte)0x10, (byte)0x80, 
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x8,
				(byte)0x8, (byte)0x0,
				(byte)0x20, (byte)0xc,
				(byte)0x41, (byte)0x7a
				};
		byte[] destination = new byte[] { (byte)0x10, (byte)0x80, 
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x8,
				(byte)0x8, (byte)0x0,
				(byte)0x20, (byte)0xc,
				(byte)0x42, (byte)0x7b
				};
		
		buffer.writeByte(QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_ADD); // Quagga protocol constant ZEBRA_INTERFACE_ADDRESS_ADD
		buffer.writeInt(1); // Interface index
		buffer.writeByte(QuaggaConstants.ZEBRA_IFA_PEER); // flags
		buffer.writeByte(os.getAddressFamilyInet6()); // write address family value (OS dependent)
		buffer.writeBytes(address); // write binary address
		buffer.writeByte(128); // write prefix length
		buffer.writeBytes(destination); // write binary address
		
		ZServInterfaceAddressAddPacket packet = executeDecode(buffer);
	
		Assert.assertEquals(packet.getProtocolVersion(), 0);
		Assert.assertEquals(packet.getInterfaceIndex(), 1);
		Assert.assertEquals(packet.getFlags(), QuaggaConstants.ZEBRA_IFA_PEER);
		Assert.assertFalse(packet.isSecondaryAddress());
		Assert.assertTrue(packet.isPeerAddress());
		Assert.assertNotNull(packet.getAddress());
		Assert.assertTrue(packet.getAddress() instanceof Inet6Address);
		Assert.assertEquals(packet.getAddress(), InetAddress.getByAddress(address));
		Assert.assertEquals(packet.getPrefixLength(), 128);
		Assert.assertNotNull(packet.getDestination());
		Assert.assertTrue(packet.getDestination() instanceof Inet6Address);
		Assert.assertEquals(packet.getDestination(), InetAddress.getByAddress(destination));
	}

	// ---- Interface address add packet, protocol version 1, IPv4 addresses
	@Test
	public void testZServerInterfaceAddressAddPacketV1NoFlagsIpV4prefixNullDestination() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		OperatingSystem os = obtainInstance(OperatingSystem.class);
		byte[] address = new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x82, (byte)0 };
		byte[] destination = new byte[] { (byte)0, (byte)0, (byte)0, (byte)0 };
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_ADD); // Quagga protocol constant ZEBRA_INTERFACE_ADDRESS_ADD
		buffer.writeInt(1); // Interface index
		buffer.writeByte(0); // flags
		buffer.writeByte(os.getAddressFamilyInet4()); // write address family value (OS dependent)
		buffer.writeBytes(address); // write binary address
		buffer.writeByte(24); // write prefix length
		buffer.writeBytes(destination); // write binary address
		
		ZServInterfaceAddressAddPacket packet = executeDecode(buffer);
	
		Assert.assertEquals(packet.getProtocolVersion(), 1);
		Assert.assertEquals(packet.getInterfaceIndex(), 1);
		Assert.assertEquals(packet.getFlags(), 0);
		Assert.assertFalse(packet.isSecondaryAddress());
		Assert.assertFalse(packet.isPeerAddress());
		Assert.assertNotNull(packet.getAddress());
		Assert.assertTrue(packet.getAddress() instanceof Inet4Address);
		Assert.assertEquals(packet.getAddress(), InetAddress.getByAddress(address));
		Assert.assertEquals(packet.getPrefixLength(), 24);
		Assert.assertNull(packet.getDestination());
	}

	@Test
	public void testZServerInterfaceAddressAddPacketV1secondaryAddressIpV4prefixNullDestination() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		OperatingSystem os = obtainInstance(OperatingSystem.class);
		byte[] address = new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x82, (byte)0 };
		byte[] destination = new byte[] { (byte)0, (byte)0, (byte)0, (byte)0 };
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_ADD); // Quagga protocol constant ZEBRA_INTERFACE_ADDRESS_ADD
		buffer.writeInt(1); // Interface index
		buffer.writeByte(QuaggaConstants.ZEBRA_IFA_SECONDARY); // flags
		buffer.writeByte(os.getAddressFamilyInet4()); // write address family value (OS dependent)
		buffer.writeBytes(address); // write binary address
		buffer.writeByte(24); // write prefix length
		buffer.writeBytes(destination); // write binary address
		
		ZServInterfaceAddressAddPacket packet = executeDecode(buffer);
	
		Assert.assertEquals(packet.getProtocolVersion(), 1);
		Assert.assertEquals(packet.getInterfaceIndex(), 1);
		Assert.assertEquals(packet.getFlags(), QuaggaConstants.ZEBRA_IFA_SECONDARY);
		Assert.assertTrue(packet.isSecondaryAddress());
		Assert.assertFalse(packet.isPeerAddress());
		Assert.assertNotNull(packet.getAddress());
		Assert.assertTrue(packet.getAddress() instanceof Inet4Address);
		Assert.assertEquals(packet.getAddress(), InetAddress.getByAddress(address));
		Assert.assertEquals(packet.getPrefixLength(), 24);
		Assert.assertNull(packet.getDestination());
	}

	@Test
	public void testZServerInterfaceAddressAddPacketV1PeerAddressIpV4prefixPeerDestination() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		OperatingSystem os = obtainInstance(OperatingSystem.class);
		byte[] address = new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x82, (byte)0x01 };
		byte[] destination = new byte[] { (byte)0xAB, (byte)0x10, (byte)0xfe, (byte)0x2 };
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_ADD); // Quagga protocol constant ZEBRA_INTERFACE_ADDRESS_ADD
		buffer.writeInt(1); // Interface index
		buffer.writeByte(QuaggaConstants.ZEBRA_IFA_PEER); // flags
		buffer.writeByte(os.getAddressFamilyInet4()); // write address family value (OS dependent)
		buffer.writeBytes(address); // write binary address
		buffer.writeByte(32); // write prefix length
		buffer.writeBytes(destination); // write binary address
		
		ZServInterfaceAddressAddPacket packet = executeDecode(buffer);
	
		Assert.assertEquals(packet.getProtocolVersion(), 1);
		Assert.assertEquals(packet.getInterfaceIndex(), 1);
		Assert.assertEquals(packet.getFlags(), QuaggaConstants.ZEBRA_IFA_PEER);
		Assert.assertFalse(packet.isSecondaryAddress());
		Assert.assertTrue(packet.isPeerAddress());
		Assert.assertNotNull(packet.getAddress());
		Assert.assertTrue(packet.getAddress() instanceof Inet4Address);
		Assert.assertEquals(packet.getAddress(), InetAddress.getByAddress(address));
		Assert.assertEquals(packet.getPrefixLength(), 32);
		Assert.assertNotNull(packet.getDestination());
		Assert.assertTrue(packet.getDestination() instanceof Inet4Address);
		Assert.assertEquals(packet.getDestination(), InetAddress.getByAddress(destination));
	}

	// ---- Interface address add packet, protocol version 1, IPv6 addresses
	@Test
	public void testZServerInterfaceAddressAddPacketV1NoFlagsIpV6prefixNullDestination() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		OperatingSystem os = obtainInstance(OperatingSystem.class);
		byte[] address = new byte[] { // represents IPv6 address 1080:0:0:0:8:800:200c:417a 
				(byte)0x10, (byte)0x80, 
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x8,
				(byte)0x8, (byte)0x0,
				(byte)0x20, (byte)0xc,
				(byte)0x41, (byte)0x7a
				};
		byte[] destination = new byte[] { (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, 
				(byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0 };
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_ADD); // Quagga protocol constant ZEBRA_INTERFACE_ADDRESS_ADD
		buffer.writeInt(1); // Interface index
		buffer.writeByte(0); // flags
		buffer.writeByte(os.getAddressFamilyInet6()); // write address family value (OS dependent)
		buffer.writeBytes(address); // write binary address
		buffer.writeByte(128); // write prefix length
		buffer.writeBytes(destination); // write binary address
		
		ZServInterfaceAddressAddPacket packet = executeDecode(buffer);
	
		Assert.assertEquals(packet.getProtocolVersion(), 1);
		Assert.assertEquals(packet.getInterfaceIndex(), 1);
		Assert.assertEquals(packet.getFlags(), 0);
		Assert.assertFalse(packet.isSecondaryAddress());
		Assert.assertFalse(packet.isPeerAddress());
		Assert.assertNotNull(packet.getAddress());
		Assert.assertTrue(packet.getAddress() instanceof Inet6Address);
		Assert.assertEquals(packet.getAddress(), InetAddress.getByAddress(address));
		Assert.assertEquals(packet.getPrefixLength(), 128);
		Assert.assertNull(packet.getDestination());
	}

	@Test
	public void testZServerInterfaceAddressAddPacketV1SecondaryAddressIpV6prefixNullDestination() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		OperatingSystem os = obtainInstance(OperatingSystem.class);
		byte[] address = new byte[] { // represents IPv6 address 1080:0:0:0:8:800:200c:417a 
				(byte)0x10, (byte)0x80, 
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x8,
				(byte)0x8, (byte)0x0,
				(byte)0x20, (byte)0xc,
				(byte)0x41, (byte)0x7a
				};
		byte[] destination = new byte[] { (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, 
				(byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0 };
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_ADD); // Quagga protocol constant ZEBRA_INTERFACE_ADDRESS_ADD
		buffer.writeInt(1); // Interface index
		buffer.writeByte(QuaggaConstants.ZEBRA_IFA_SECONDARY); // flags
		buffer.writeByte(os.getAddressFamilyInet6()); // write address family value (OS dependent)
		buffer.writeBytes(address); // write binary address
		buffer.writeByte(128); // write prefix length
		buffer.writeBytes(destination); // write binary address
		
		ZServInterfaceAddressAddPacket packet = executeDecode(buffer);
	
		Assert.assertEquals(packet.getProtocolVersion(), 1);
		Assert.assertEquals(packet.getInterfaceIndex(), 1);
		Assert.assertEquals(packet.getFlags(), QuaggaConstants.ZEBRA_IFA_SECONDARY);
		Assert.assertTrue(packet.isSecondaryAddress());
		Assert.assertFalse(packet.isPeerAddress());
		Assert.assertNotNull(packet.getAddress());
		Assert.assertTrue(packet.getAddress() instanceof Inet6Address);
		Assert.assertEquals(packet.getAddress(), InetAddress.getByAddress(address));
		Assert.assertEquals(packet.getPrefixLength(), 128);
		Assert.assertNull(packet.getDestination());
	}

	@Test
	public void testZServerInterfaceAddressAddPacketV1PeerAddressIpV6prefixPeerDestination() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		OperatingSystem os = obtainInstance(OperatingSystem.class);
		byte[] address = new byte[] { // represents IPv6 address 1080:0:0:0:8:800:200c:417a 
				(byte)0x10, (byte)0x80, 
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x8,
				(byte)0x8, (byte)0x0,
				(byte)0x20, (byte)0xc,
				(byte)0x41, (byte)0x7a
				};
		byte[] destination = new byte[] { (byte)0x10, (byte)0x80, 
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x8,
				(byte)0x8, (byte)0x0,
				(byte)0x20, (byte)0xc,
				(byte)0x42, (byte)0x7b
				};
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_ADD); // Quagga protocol constant ZEBRA_INTERFACE_ADDRESS_ADD
		buffer.writeInt(1); // Interface index
		buffer.writeByte(QuaggaConstants.ZEBRA_IFA_PEER); // flags
		buffer.writeByte(os.getAddressFamilyInet6()); // write address family value (OS dependent)
		buffer.writeBytes(address); // write binary address
		buffer.writeByte(128); // write prefix length
		buffer.writeBytes(destination); // write binary address
		
		ZServInterfaceAddressAddPacket packet = executeDecode(buffer);
	
		Assert.assertEquals(packet.getProtocolVersion(), 1);
		Assert.assertEquals(packet.getInterfaceIndex(), 1);
		Assert.assertEquals(packet.getFlags(), QuaggaConstants.ZEBRA_IFA_PEER);
		Assert.assertFalse(packet.isSecondaryAddress());
		Assert.assertTrue(packet.isPeerAddress());
		Assert.assertNotNull(packet.getAddress());
		Assert.assertTrue(packet.getAddress() instanceof Inet6Address);
		Assert.assertEquals(packet.getAddress(), InetAddress.getByAddress(address));
		Assert.assertEquals(packet.getPrefixLength(), 128);
		Assert.assertNotNull(packet.getDestination());
		Assert.assertTrue(packet.getDestination() instanceof Inet6Address);
		Assert.assertEquals(packet.getDestination(), InetAddress.getByAddress(destination));
	}

	// -- marker
	// ---- Interface address add packet, protocol version 0, IPv4 addresses
	@Test
	public void testZServerInterfaceAddressDeletePacketV0NoFlagsIpV4prefixNullDestination() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion0();
		OperatingSystem os = obtainInstance(OperatingSystem.class);
		byte[] address = new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x82, (byte)0 };
		byte[] destination = new byte[] { (byte)0, (byte)0, (byte)0, (byte)0 };
		
		buffer.writeByte(QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_DELETE); // Quagga protocol constant ZEBRA_INTERFACE_ADDRESS_DELETE
		buffer.writeInt(1); // Interface index
		buffer.writeByte(0); // flags
		buffer.writeByte(os.getAddressFamilyInet4()); // write address family value (OS dependent)
		buffer.writeBytes(address); // write binary address
		buffer.writeByte(24); // write prefix length
		buffer.writeBytes(destination); // write binary address
		
		ZServInterfaceAddressDeletePacket packet = executeDecode(buffer);
	
		Assert.assertEquals(packet.getProtocolVersion(), 0);
		Assert.assertEquals(packet.getInterfaceIndex(), 1);
		Assert.assertEquals(packet.getFlags(), 0);
		Assert.assertFalse(packet.isSecondaryAddress());
		Assert.assertFalse(packet.isPeerAddress());
		Assert.assertNotNull(packet.getAddress());
		Assert.assertTrue(packet.getAddress() instanceof Inet4Address);
		Assert.assertEquals(packet.getAddress(), InetAddress.getByAddress(address));
		Assert.assertEquals(packet.getPrefixLength(), 24);
		Assert.assertNull(packet.getDestination());
	}

	@Test
	public void testZServerInterfaceAddressDeletePacketV0SecondaryAddressIpV4prefixNullDestination() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion0();
		OperatingSystem os = obtainInstance(OperatingSystem.class);
		byte[] address = new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x82, (byte)0 };
		byte[] destination = new byte[] { (byte)0, (byte)0, (byte)0, (byte)0 };
		
		buffer.writeByte(QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_DELETE); // Quagga protocol constant ZEBRA_INTERFACE_ADDRESS_DELETE
		buffer.writeInt(1); // Interface index
		buffer.writeByte(QuaggaConstants.ZEBRA_IFA_SECONDARY); // flags
		buffer.writeByte(os.getAddressFamilyInet4()); // write address family value (OS dependent)
		buffer.writeBytes(address); // write binary address
		buffer.writeByte(24); // write prefix length
		buffer.writeBytes(destination); // write binary address
		
		ZServInterfaceAddressDeletePacket packet = executeDecode(buffer);
	
		Assert.assertEquals(packet.getProtocolVersion(), 0);
		Assert.assertEquals(packet.getInterfaceIndex(), 1);
		Assert.assertEquals(packet.getFlags(), QuaggaConstants.ZEBRA_IFA_SECONDARY);
		Assert.assertTrue(packet.isSecondaryAddress());
		Assert.assertFalse(packet.isPeerAddress());
		Assert.assertNotNull(packet.getAddress());
		Assert.assertTrue(packet.getAddress() instanceof Inet4Address);
		Assert.assertEquals(packet.getAddress(), InetAddress.getByAddress(address));
		Assert.assertEquals(packet.getPrefixLength(), 24);
		Assert.assertNull(packet.getDestination());
	}

	@Test
	public void testZServerInterfaceAddressDeletePacketV0PeerAddressIpV4prefixPeerDestination() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion0();
		OperatingSystem os = obtainInstance(OperatingSystem.class);
		byte[] address = new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x82, (byte)0x01 };
		byte[] destination = new byte[] { (byte)0xAB, (byte)0x10, (byte)0xfe, (byte)0x2 };
		
		buffer.writeByte(QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_DELETE); // Quagga protocol constant ZEBRA_INTERFACE_ADDRESS_DELETE
		buffer.writeInt(1); // Interface index
		buffer.writeByte(QuaggaConstants.ZEBRA_IFA_PEER); // flags
		buffer.writeByte(os.getAddressFamilyInet4()); // write address family value (OS dependent)
		buffer.writeBytes(address); // write binary address
		buffer.writeByte(32); // write prefix length
		buffer.writeBytes(destination); // write binary address
		
		ZServInterfaceAddressDeletePacket packet = executeDecode(buffer);
	
		Assert.assertEquals(packet.getProtocolVersion(), 0);
		Assert.assertEquals(packet.getInterfaceIndex(), 1);
		Assert.assertEquals(packet.getFlags(), QuaggaConstants.ZEBRA_IFA_PEER);
		Assert.assertFalse(packet.isSecondaryAddress());
		Assert.assertTrue(packet.isPeerAddress());
		Assert.assertNotNull(packet.getAddress());
		Assert.assertTrue(packet.getAddress() instanceof Inet4Address);
		Assert.assertEquals(packet.getAddress(), InetAddress.getByAddress(address));
		Assert.assertEquals(packet.getPrefixLength(), 32);
		Assert.assertNotNull(packet.getDestination());
		Assert.assertTrue(packet.getDestination() instanceof Inet4Address);
		Assert.assertEquals(packet.getDestination(), InetAddress.getByAddress(destination));
	}

	// ---- Interface address add packet, protocol version 0, IPv6 addresses
	@Test
	public void testZServerInterfaceAddressDeletePacketV0NoFlagsIpV6prefixNullDestination() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion0();
		OperatingSystem os = obtainInstance(OperatingSystem.class);
		byte[] address = new byte[] { // represents IPv6 address 1080:0:0:0:8:800:200c:417a 
				(byte)0x10, (byte)0x80, 
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x8,
				(byte)0x8, (byte)0x0,
				(byte)0x20, (byte)0xc,
				(byte)0x41, (byte)0x7a
				};
		byte[] destination = new byte[] { (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, 
				(byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0 };
		
		buffer.writeByte(QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_DELETE); // Quagga protocol constant ZEBRA_INTERFACE_ADDRESS_DELETE
		buffer.writeInt(1); // Interface index
		buffer.writeByte(0); // flags
		buffer.writeByte(os.getAddressFamilyInet6()); // write address family value (OS dependent)
		buffer.writeBytes(address); // write binary address
		buffer.writeByte(128); // write prefix length
		buffer.writeBytes(destination); // write binary address
		
		ZServInterfaceAddressDeletePacket packet = executeDecode(buffer);
	
		Assert.assertEquals(packet.getProtocolVersion(), 0);
		Assert.assertEquals(packet.getInterfaceIndex(), 1);
		Assert.assertEquals(packet.getFlags(), 0);
		Assert.assertFalse(packet.isSecondaryAddress());
		Assert.assertFalse(packet.isPeerAddress());
		Assert.assertNotNull(packet.getAddress());
		Assert.assertTrue(packet.getAddress() instanceof Inet6Address);
		Assert.assertEquals(packet.getAddress(), InetAddress.getByAddress(address));
		Assert.assertEquals(packet.getPrefixLength(), 128);
		Assert.assertNull(packet.getDestination());
	}

	@Test
	public void testZServerInterfaceAddressDeletePacketV0SecondaryAddressIpV6prefixNullDestination() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion0();
		OperatingSystem os = obtainInstance(OperatingSystem.class);
		byte[] address = new byte[] { // represents IPv6 address 1080:0:0:0:8:800:200c:417a 
				(byte)0x10, (byte)0x80, 
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x8,
				(byte)0x8, (byte)0x0,
				(byte)0x20, (byte)0xc,
				(byte)0x41, (byte)0x7a
				};
		byte[] destination = new byte[] { (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, 
				(byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0 };
		
		buffer.writeByte(QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_DELETE); // Quagga protocol constant ZEBRA_INTERFACE_ADDRESS_DELETE
		buffer.writeInt(1); // Interface index
		buffer.writeByte(QuaggaConstants.ZEBRA_IFA_SECONDARY); // flags
		buffer.writeByte(os.getAddressFamilyInet6()); // write address family value (OS dependent)
		buffer.writeBytes(address); // write binary address
		buffer.writeByte(128); // write prefix length
		buffer.writeBytes(destination); // write binary address
		
		ZServInterfaceAddressDeletePacket packet = executeDecode(buffer);
	
		Assert.assertEquals(packet.getProtocolVersion(), 0);
		Assert.assertEquals(packet.getInterfaceIndex(), 1);
		Assert.assertEquals(packet.getFlags(), QuaggaConstants.ZEBRA_IFA_SECONDARY);
		Assert.assertTrue(packet.isSecondaryAddress());
		Assert.assertFalse(packet.isPeerAddress());
		Assert.assertNotNull(packet.getAddress());
		Assert.assertTrue(packet.getAddress() instanceof Inet6Address);
		Assert.assertEquals(packet.getAddress(), InetAddress.getByAddress(address));
		Assert.assertEquals(packet.getPrefixLength(), 128);
		Assert.assertNull(packet.getDestination());
	}

	@Test
	public void testZServerInterfaceAddressDeletePacketV0PeerAddressIpV6prefixPeerDestination() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion0();
		OperatingSystem os = obtainInstance(OperatingSystem.class);
		byte[] address = new byte[] { // represents IPv6 address 1080:0:0:0:8:800:200c:417a 
				(byte)0x10, (byte)0x80, 
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x8,
				(byte)0x8, (byte)0x0,
				(byte)0x20, (byte)0xc,
				(byte)0x41, (byte)0x7a
				};
		byte[] destination = new byte[] { (byte)0x10, (byte)0x80, 
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x8,
				(byte)0x8, (byte)0x0,
				(byte)0x20, (byte)0xc,
				(byte)0x42, (byte)0x7b
				};
		
		buffer.writeByte(QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_DELETE); // Quagga protocol constant ZEBRA_INTERFACE_ADDRESS_DELETE
		buffer.writeInt(1); // Interface index
		buffer.writeByte(QuaggaConstants.ZEBRA_IFA_PEER); // flags
		buffer.writeByte(os.getAddressFamilyInet6()); // write address family value (OS dependent)
		buffer.writeBytes(address); // write binary address
		buffer.writeByte(128); // write prefix length
		buffer.writeBytes(destination); // write binary address
		
		ZServInterfaceAddressDeletePacket packet = executeDecode(buffer);
	
		Assert.assertEquals(packet.getProtocolVersion(), 0);
		Assert.assertEquals(packet.getInterfaceIndex(), 1);
		Assert.assertEquals(packet.getFlags(), QuaggaConstants.ZEBRA_IFA_PEER);
		Assert.assertFalse(packet.isSecondaryAddress());
		Assert.assertTrue(packet.isPeerAddress());
		Assert.assertNotNull(packet.getAddress());
		Assert.assertTrue(packet.getAddress() instanceof Inet6Address);
		Assert.assertEquals(packet.getAddress(), InetAddress.getByAddress(address));
		Assert.assertEquals(packet.getPrefixLength(), 128);
		Assert.assertNotNull(packet.getDestination());
		Assert.assertTrue(packet.getDestination() instanceof Inet6Address);
		Assert.assertEquals(packet.getDestination(), InetAddress.getByAddress(destination));
	}

	// ---- Interface address add packet, protocol version 1, IPv4 addresses
	@Test
	public void testZServerInterfaceAddressDeletePacketV1NoFlagsIpV4prefixNullDestination() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		OperatingSystem os = obtainInstance(OperatingSystem.class);
		byte[] address = new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x82, (byte)0 };
		byte[] destination = new byte[] { (byte)0, (byte)0, (byte)0, (byte)0 };
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_DELETE); // Quagga protocol constant ZEBRA_INTERFACE_ADDRESS_DELETE
		buffer.writeInt(1); // Interface index
		buffer.writeByte(0); // flags
		buffer.writeByte(os.getAddressFamilyInet4()); // write address family value (OS dependent)
		buffer.writeBytes(address); // write binary address
		buffer.writeByte(24); // write prefix length
		buffer.writeBytes(destination); // write binary address
		
		ZServInterfaceAddressDeletePacket packet = executeDecode(buffer);
	
		Assert.assertEquals(packet.getProtocolVersion(), 1);
		Assert.assertEquals(packet.getInterfaceIndex(), 1);
		Assert.assertEquals(packet.getFlags(), 0);
		Assert.assertFalse(packet.isSecondaryAddress());
		Assert.assertFalse(packet.isPeerAddress());
		Assert.assertNotNull(packet.getAddress());
		Assert.assertTrue(packet.getAddress() instanceof Inet4Address);
		Assert.assertEquals(packet.getAddress(), InetAddress.getByAddress(address));
		Assert.assertEquals(packet.getPrefixLength(), 24);
		Assert.assertNull(packet.getDestination());
	}

	@Test
	public void testZServerInterfaceAddressDeletePacketV1secondaryAddressIpV4prefixNullDestination() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		OperatingSystem os = obtainInstance(OperatingSystem.class);
		byte[] address = new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x82, (byte)0 };
		byte[] destination = new byte[] { (byte)0, (byte)0, (byte)0, (byte)0 };
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_DELETE); // Quagga protocol constant ZEBRA_INTERFACE_ADDRESS_DELETE
		buffer.writeInt(1); // Interface index
		buffer.writeByte(QuaggaConstants.ZEBRA_IFA_SECONDARY); // flags
		buffer.writeByte(os.getAddressFamilyInet4()); // write address family value (OS dependent)
		buffer.writeBytes(address); // write binary address
		buffer.writeByte(24); // write prefix length
		buffer.writeBytes(destination); // write binary address
		
		ZServInterfaceAddressDeletePacket packet = executeDecode(buffer);
	
		Assert.assertEquals(packet.getProtocolVersion(), 1);
		Assert.assertEquals(packet.getInterfaceIndex(), 1);
		Assert.assertEquals(packet.getFlags(), QuaggaConstants.ZEBRA_IFA_SECONDARY);
		Assert.assertTrue(packet.isSecondaryAddress());
		Assert.assertFalse(packet.isPeerAddress());
		Assert.assertNotNull(packet.getAddress());
		Assert.assertTrue(packet.getAddress() instanceof Inet4Address);
		Assert.assertEquals(packet.getAddress(), InetAddress.getByAddress(address));
		Assert.assertEquals(packet.getPrefixLength(), 24);
		Assert.assertNull(packet.getDestination());
	}

	@Test
	public void testZServerInterfaceAddressDeletePacketV1PeerAddressIpV4prefixPeerDestination() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		OperatingSystem os = obtainInstance(OperatingSystem.class);
		byte[] address = new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x82, (byte)0x01 };
		byte[] destination = new byte[] { (byte)0xAB, (byte)0x10, (byte)0xfe, (byte)0x2 };
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_DELETE); // Quagga protocol constant ZEBRA_INTERFACE_ADDRESS_DELETE
		buffer.writeInt(1); // Interface index
		buffer.writeByte(QuaggaConstants.ZEBRA_IFA_PEER); // flags
		buffer.writeByte(os.getAddressFamilyInet4()); // write address family value (OS dependent)
		buffer.writeBytes(address); // write binary address
		buffer.writeByte(32); // write prefix length
		buffer.writeBytes(destination); // write binary address
		
		ZServInterfaceAddressDeletePacket packet = executeDecode(buffer);
	
		Assert.assertEquals(packet.getProtocolVersion(), 1);
		Assert.assertEquals(packet.getInterfaceIndex(), 1);
		Assert.assertEquals(packet.getFlags(), QuaggaConstants.ZEBRA_IFA_PEER);
		Assert.assertFalse(packet.isSecondaryAddress());
		Assert.assertTrue(packet.isPeerAddress());
		Assert.assertNotNull(packet.getAddress());
		Assert.assertTrue(packet.getAddress() instanceof Inet4Address);
		Assert.assertEquals(packet.getAddress(), InetAddress.getByAddress(address));
		Assert.assertEquals(packet.getPrefixLength(), 32);
		Assert.assertNotNull(packet.getDestination());
		Assert.assertTrue(packet.getDestination() instanceof Inet4Address);
		Assert.assertEquals(packet.getDestination(), InetAddress.getByAddress(destination));
	}

	// ---- Interface address add packet, protocol version 1, IPv6 addresses
	@Test
	public void testZServerInterfaceAddressDeletePacketV1NoFlagsIpV6prefixNullDestination() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		OperatingSystem os = obtainInstance(OperatingSystem.class);
		byte[] address = new byte[] { // represents IPv6 address 1080:0:0:0:8:800:200c:417a 
				(byte)0x10, (byte)0x80, 
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x8,
				(byte)0x8, (byte)0x0,
				(byte)0x20, (byte)0xc,
				(byte)0x41, (byte)0x7a
				};
		byte[] destination = new byte[] { (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, 
				(byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0 };
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_DELETE); // Quagga protocol constant ZEBRA_INTERFACE_ADDRESS_DELETE
		buffer.writeInt(1); // Interface index
		buffer.writeByte(0); // flags
		buffer.writeByte(os.getAddressFamilyInet6()); // write address family value (OS dependent)
		buffer.writeBytes(address); // write binary address
		buffer.writeByte(128); // write prefix length
		buffer.writeBytes(destination); // write binary address
		
		ZServInterfaceAddressDeletePacket packet = executeDecode(buffer);
	
		Assert.assertEquals(packet.getProtocolVersion(), 1);
		Assert.assertEquals(packet.getInterfaceIndex(), 1);
		Assert.assertEquals(packet.getFlags(), 0);
		Assert.assertFalse(packet.isSecondaryAddress());
		Assert.assertFalse(packet.isPeerAddress());
		Assert.assertNotNull(packet.getAddress());
		Assert.assertTrue(packet.getAddress() instanceof Inet6Address);
		Assert.assertEquals(packet.getAddress(), InetAddress.getByAddress(address));
		Assert.assertEquals(packet.getPrefixLength(), 128);
		Assert.assertNull(packet.getDestination());
	}

	@Test
	public void testZServerInterfaceAddressDeletePacketV1SecondaryAddressIpV6prefixNullDestination() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		OperatingSystem os = obtainInstance(OperatingSystem.class);
		byte[] address = new byte[] { // represents IPv6 address 1080:0:0:0:8:800:200c:417a 
				(byte)0x10, (byte)0x80, 
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x8,
				(byte)0x8, (byte)0x0,
				(byte)0x20, (byte)0xc,
				(byte)0x41, (byte)0x7a
				};
		byte[] destination = new byte[] { (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, 
				(byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0 };
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_DELETE); // Quagga protocol constant ZEBRA_INTERFACE_ADDRESS_DELETE
		buffer.writeInt(1); // Interface index
		buffer.writeByte(QuaggaConstants.ZEBRA_IFA_SECONDARY); // flags
		buffer.writeByte(os.getAddressFamilyInet6()); // write address family value (OS dependent)
		buffer.writeBytes(address); // write binary address
		buffer.writeByte(128); // write prefix length
		buffer.writeBytes(destination); // write binary address
		
		ZServInterfaceAddressDeletePacket packet = executeDecode(buffer);
	
		Assert.assertEquals(packet.getProtocolVersion(), 1);
		Assert.assertEquals(packet.getInterfaceIndex(), 1);
		Assert.assertEquals(packet.getFlags(), QuaggaConstants.ZEBRA_IFA_SECONDARY);
		Assert.assertTrue(packet.isSecondaryAddress());
		Assert.assertFalse(packet.isPeerAddress());
		Assert.assertNotNull(packet.getAddress());
		Assert.assertTrue(packet.getAddress() instanceof Inet6Address);
		Assert.assertEquals(packet.getAddress(), InetAddress.getByAddress(address));
		Assert.assertEquals(packet.getPrefixLength(), 128);
		Assert.assertNull(packet.getDestination());
	}

	@Test
	public void testZServerInterfaceAddressDeletePacketV1PeerAddressIpV6prefixPeerDestination() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		OperatingSystem os = obtainInstance(OperatingSystem.class);
		byte[] address = new byte[] { // represents IPv6 address 1080:0:0:0:8:800:200c:417a 
				(byte)0x10, (byte)0x80, 
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x8,
				(byte)0x8, (byte)0x0,
				(byte)0x20, (byte)0xc,
				(byte)0x41, (byte)0x7a
				};
		byte[] destination = new byte[] { (byte)0x10, (byte)0x80, 
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x0,
				(byte)0x0, (byte)0x8,
				(byte)0x8, (byte)0x0,
				(byte)0x20, (byte)0xc,
				(byte)0x42, (byte)0x7b
				};
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_ADDRESS_DELETE); // Quagga protocol constant ZEBRA_INTERFACE_ADDRESS_DELETE
		buffer.writeInt(1); // Interface index
		buffer.writeByte(QuaggaConstants.ZEBRA_IFA_PEER); // flags
		buffer.writeByte(os.getAddressFamilyInet6()); // write address family value (OS dependent)
		buffer.writeBytes(address); // write binary address
		buffer.writeByte(128); // write prefix length
		buffer.writeBytes(destination); // write binary address
		
		ZServInterfaceAddressDeletePacket packet = executeDecode(buffer);
	
		Assert.assertEquals(packet.getProtocolVersion(), 1);
		Assert.assertEquals(packet.getInterfaceIndex(), 1);
		Assert.assertEquals(packet.getFlags(), QuaggaConstants.ZEBRA_IFA_PEER);
		Assert.assertFalse(packet.isSecondaryAddress());
		Assert.assertTrue(packet.isPeerAddress());
		Assert.assertNotNull(packet.getAddress());
		Assert.assertTrue(packet.getAddress() instanceof Inet6Address);
		Assert.assertEquals(packet.getAddress(), InetAddress.getByAddress(address));
		Assert.assertEquals(packet.getPrefixLength(), 128);
		Assert.assertNotNull(packet.getDestination());
		Assert.assertTrue(packet.getDestination() instanceof Inet6Address);
		Assert.assertEquals(packet.getDestination(), InetAddress.getByAddress(destination));
	}
	
	// ---- Interface delete packet, protocol version 1
	
	@Test
	public void testZServInterfaceDownPacketv1status0() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_DOWN); // Quagga protocol constant ZEBRA_INTERFACE_DOWN
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(0); // status flag
		buffer.writeLong(0x00112233aabbccddL); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServInterfaceDownPacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 1);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getStatusFlags(), 0);
		Assert.assertFalse(packet.isInterfaceActive());
		Assert.assertFalse(packet.isInterfaceLinkDetection());
		Assert.assertFalse(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0x00112233aabbccddL);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}
	
	@Test
	public void testZServInterfaceDownPacketv1status1() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_DOWN); // Quagga protocol constant ZEBRA_INTERFACE_UP
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(1); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServInterfaceDownPacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 1);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getStatusFlags(), 1);
		Assert.assertTrue(packet.isInterfaceActive());
		Assert.assertFalse(packet.isInterfaceLinkDetection());
		Assert.assertFalse(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0xaabbccdd00112233L);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}
	
	@Test
	public void testZServInterfaceDownPacketv1status2() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_DOWN); // Quagga protocol constant ZEBRA_INTERFACE_UP
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(2); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServInterfaceDownPacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 1);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getStatusFlags(), 2);
		Assert.assertFalse(packet.isInterfaceActive());
		Assert.assertFalse(packet.isInterfaceLinkDetection());
		Assert.assertTrue(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0xaabbccdd00112233L);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}
	
	@Test
	public void testZServInterfaceDownPacketv1status4() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_DOWN); // Quagga protocol constant ZEBRA_INTERFACE_UP
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(4); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServInterfaceDownPacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 1);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getStatusFlags(), 4);
		Assert.assertFalse(packet.isInterfaceActive());
		Assert.assertTrue(packet.isInterfaceLinkDetection());
		Assert.assertFalse(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0xaabbccdd00112233L);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}

	// ---- Interface add packet, protocol version 0
	
	@Test
	public void testZServInterfaceDownPacketv0status0() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion0();
		
		buffer.writeByte(QuaggaConstants.ZEBRA_INTERFACE_DOWN); // Quagga protocol constant ZEBRA_INTERFACE_UP
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(0); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServInterfaceDownPacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 0);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getStatusFlags(), 0);
		Assert.assertFalse(packet.isInterfaceActive());
		Assert.assertFalse(packet.isInterfaceLinkDetection());
		Assert.assertFalse(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0xaabbccdd00112233L);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}
	
	@Test
	public void testZServInterfaceDownPacketv0status1() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion0();
		
		buffer.writeByte(QuaggaConstants.ZEBRA_INTERFACE_DOWN); // Quagga protocol constant ZEBRA_INTERFACE_UP
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(1); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServInterfaceDownPacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 0);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getStatusFlags(), 1);
		Assert.assertTrue(packet.isInterfaceActive());
		Assert.assertFalse(packet.isInterfaceLinkDetection());
		Assert.assertFalse(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0xaabbccdd00112233L);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}
	
	@Test
	public void testZServInterfaceDownPacketv0status2() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion0();
		
		buffer.writeByte(QuaggaConstants.ZEBRA_INTERFACE_DOWN); // Quagga protocol constant ZEBRA_INTERFACE_UP
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(2); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServInterfaceDownPacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 0);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getStatusFlags(), 2);
		Assert.assertFalse(packet.isInterfaceActive());
		Assert.assertFalse(packet.isInterfaceLinkDetection());
		Assert.assertTrue(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0xaabbccdd00112233L);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}
	
	@Test
	public void testZServInterfaceDownPacketv0status4() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion0();
		
		buffer.writeByte(QuaggaConstants.ZEBRA_INTERFACE_DOWN); // Quagga protocol constant ZEBRA_INTERFACE_UP
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(4); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServInterfaceDownPacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 0);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getStatusFlags(), 4);
		Assert.assertFalse(packet.isInterfaceActive());
		Assert.assertTrue(packet.isInterfaceLinkDetection());
		Assert.assertFalse(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0xaabbccdd00112233L);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}
	// ---- Interface add packet, protocol version 1
	
	@Test
	public void testZServInterfaceUpPacketv1status0() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_UP); // Quagga protocol constant ZEBRA_INTERFACE_UP
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(0); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServInterfaceUpPacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 1);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getStatusFlags(), 0);
		Assert.assertFalse(packet.isInterfaceActive());
		Assert.assertFalse(packet.isInterfaceLinkDetection());
		Assert.assertFalse(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0xaabbccdd00112233L);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}
	
	@Test
	public void testZServInterfaceUpPacketv1status1() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_UP); // Quagga protocol constant ZEBRA_INTERFACE_UP
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(1); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServInterfaceUpPacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 1);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getStatusFlags(), 1);
		Assert.assertTrue(packet.isInterfaceActive());
		Assert.assertFalse(packet.isInterfaceLinkDetection());
		Assert.assertFalse(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0xaabbccdd00112233L);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}
	
	@Test
	public void testZServInterfaceUpPacketv1status2() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_UP); // Quagga protocol constant ZEBRA_INTERFACE_UP
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(2); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServInterfaceUpPacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 1);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getStatusFlags(), 2);
		Assert.assertFalse(packet.isInterfaceActive());
		Assert.assertFalse(packet.isInterfaceLinkDetection());
		Assert.assertTrue(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0xaabbccdd00112233L);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}
	
	@Test
	public void testZServInterfaceUpPacketv1status4() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_UP); // Quagga protocol constant ZEBRA_INTERFACE_UP
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(4); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServInterfaceUpPacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 1);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getStatusFlags(), 4);
		Assert.assertFalse(packet.isInterfaceActive());
		Assert.assertTrue(packet.isInterfaceLinkDetection());
		Assert.assertFalse(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0xaabbccdd00112233L);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}

	// ---- Interface add packet, protocol version 0
	
	@Test
	public void testZServInterfaceUpPacketv0status0() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion0();
		
		buffer.writeByte(QuaggaConstants.ZEBRA_INTERFACE_UP); // Quagga protocol constant ZEBRA_INTERFACE_UP
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(0); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServInterfaceUpPacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 0);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getStatusFlags(), 0);
		Assert.assertFalse(packet.isInterfaceActive());
		Assert.assertFalse(packet.isInterfaceLinkDetection());
		Assert.assertFalse(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0xaabbccdd00112233L);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}
	
	@Test
	public void testZServInterfaceUpPacketv0status1() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion0();
		
		buffer.writeByte(QuaggaConstants.ZEBRA_INTERFACE_UP); // Quagga protocol constant ZEBRA_INTERFACE_UP
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(1); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServInterfaceUpPacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 0);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getStatusFlags(), 1);
		Assert.assertTrue(packet.isInterfaceActive());
		Assert.assertFalse(packet.isInterfaceLinkDetection());
		Assert.assertFalse(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0xaabbccdd00112233L);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}
	
	@Test
	public void testZServInterfaceUpPacketv0status2() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion0();
		
		buffer.writeByte(QuaggaConstants.ZEBRA_INTERFACE_UP); // Quagga protocol constant ZEBRA_INTERFACE_UP
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(2); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServInterfaceUpPacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 0);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getStatusFlags(), 2);
		Assert.assertFalse(packet.isInterfaceActive());
		Assert.assertFalse(packet.isInterfaceLinkDetection());
		Assert.assertTrue(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0xaabbccdd00112233L);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}
	
	@Test
	public void testZServInterfaceUpPacketv0status4() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion0();
		
		buffer.writeByte(QuaggaConstants.ZEBRA_INTERFACE_UP); // Quagga protocol constant ZEBRA_INTERFACE_UP
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(4); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeInt(1); // interface metric
		buffer.writeInt(1512); // IPv4 mtu
		buffer.writeInt(1496); // IPv6 MTU
		buffer.writeInt(1000); // bandwidth
		
		ZServInterfaceUpPacket packet = executeDecode(buffer);
		
		Assert.assertEquals(packet.getProtocolVersion(), 0);
		Assert.assertEquals(packet.getInterfaceName(), "eth0");
		Assert.assertEquals(packet.getInterfaceIndex(), 1);
		Assert.assertEquals(packet.getStatusFlags(), 4);
		Assert.assertFalse(packet.isInterfaceActive());
		Assert.assertTrue(packet.isInterfaceLinkDetection());
		Assert.assertFalse(packet.isInterfaceSub());
		Assert.assertEquals(packet.getInterfaceFlags(), 0xaabbccdd00112233L);
		Assert.assertEquals(packet.getInterfaceMetric(), 1);
		Assert.assertEquals(packet.getIpV4Mtu(), 1512);
		Assert.assertEquals(packet.getIpV6Mtu(), 1496);
		Assert.assertEquals(packet.getBandwidth(), 1000);
	}
	
	//-- Add/Delete IPv4/IPv6 protocol version 0 packet
	public void testZServIpV4RouteAddProtocol0SystemFlags0Prefix8Gateway() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion0();
		byte[] prefix = new byte[] { (byte)0x12 };
		byte[] gateway = new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x1, (byte)0x1 };
		
		buffer.writeByte(QuaggaConstants.ZEBRA_IPV4_ROUTE_ADD); // Quagga protocol constant ZEBRA_IPV4_ROUTE_ADD
		buffer.writeByte(0); // route type SYSTEM
		buffer.writeByte(0); // no flags
		buffer.writeByte(QuaggaConstants.ZAPI_MESSAGE_NEXTHOP|QuaggaConstants.ZAPI_MESSAGE_IFINDEX
				|QuaggaConstants.ZAPI_MESSAGE_DISTANCE|QuaggaConstants.ZAPI_MESSAGE_METRIC); // message aka ZAPI flags
		buffer.writeByte(8); // prefix length
		buffer.writeBytes(prefix);
		buffer.writeByte(1); // nexthop number
		buffer.writeBytes(gateway); // IPv4 gateway address
		buffer.writeByte(1); // interface index (??), not explained in zserv.c
		buffer.writeInt(16); // interface index
		buffer.writeByte(1); // distance
		buffer.writeInt(32); // metric
		
		ZServIPv4RouteAddPacket packet = executeDecode(buffer);
		
		// route type
		Assert.assertEquals(EQuaggaRouteType.System, packet.getType());

		// message flags
		Assert.assertEquals(0, packet.getFlags());
		Assert.assertFalse(packet.isInternal());
		Assert.assertFalse(packet.isSelfRoute());
		Assert.assertFalse(packet.isBlackHole());
		Assert.assertFalse(packet.isIbgb());
		Assert.assertFalse(packet.isSelected());
		Assert.assertFalse(packet.isChanged());
		Assert.assertFalse(packet.isStatic());
		Assert.assertFalse(packet.isReject());
		
	}
}
