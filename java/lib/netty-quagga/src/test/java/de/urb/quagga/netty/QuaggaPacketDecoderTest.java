/**
 * 
 */
package de.urb.quagga.netty;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.UpstreamMessageEvent;
import org.junit.Assert;
import org.junit.Test;

import de.urb.quagga.netty.protocol.QuaggaPacket;
import de.urb.quagga.netty.protocol.ZServAddInterfacePacket;
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
		char[] sBuf =  string.toCharArray();
		
		System.arraycopy(sBuf, 0, tBuf, 0, sBuf.length < maxLength ? sBuf.length : maxLength);
		
		buffer.writeBytes(tBuf, 0, maxLength);
	}
	
	@SuppressWarnings("unchecked")
	private <T extends QuaggaPacket> T executeDecode(ChannelBuffer buffer) throws Exception {
		UpstreamMessageEvent me = new UpstreamMessageEvent(null, buffer, null);
		MockChannelHandlerContext ctx = new MockChannelHandlerContext();
		QuaggaPacketDecoder decoder = obtainInstance(QuaggaPacketDecoder.class);
		
		decoder.messageReceived(ctx, me);
		return (T)ctx.getMessage();
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
		buffer.writeLong(1); // interface metric
		buffer.writeLong(1512); // IPv4 mtu
		buffer.writeLong(1496); // IPv6 MTU
		buffer.writeLong(1000); // bandwidth
		
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
	public void testZServInterfaceAddPacketv1status1() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_ADD); // Quagga protocol constant ZEBRA_INTERFACE_ADD
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(1); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeLong(1); // interface metric
		buffer.writeLong(1512); // IPv4 mtu
		buffer.writeLong(1496); // IPv6 MTU
		buffer.writeLong(1000); // bandwidth
		
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
	public void testZServInterfaceAddPacketv1status2() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_ADD); // Quagga protocol constant ZEBRA_INTERFACE_ADD
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(2); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeLong(1); // interface metric
		buffer.writeLong(1512); // IPv4 mtu
		buffer.writeLong(1496); // IPv6 MTU
		buffer.writeLong(1000); // bandwidth
		
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
	public void testZServInterfaceAddPacketv1status4() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion1();
		
		buffer.writeShort(QuaggaConstants.ZEBRA_INTERFACE_ADD); // Quagga protocol constant ZEBRA_INTERFACE_ADD
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(4); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeLong(1); // interface metric
		buffer.writeLong(1512); // IPv4 mtu
		buffer.writeLong(1496); // IPv6 MTU
		buffer.writeLong(1000); // bandwidth
		
		ZServAddInterfacePacket packet = executeDecode(buffer);
		
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

	// ---- Interface add packet, protocol version 0
	
	@Test
	public void testZServInterfaceAddPacketv0status0() throws Exception {
		ChannelBuffer buffer = createQuaggaPacketVersion0();
		
		buffer.writeByte(QuaggaConstants.ZEBRA_INTERFACE_ADD); // Quagga protocol constant ZEBRA_INTERFACE_ADD
		putString(buffer, "eth0", QuaggaConstants.INTERFACE_NAMSIZ);
		buffer.writeInt(1); // Interface index
		buffer.writeByte(0); // status flag
		buffer.writeLong(0xaabbccdd00112233L); // interface flags
		buffer.writeLong(1); // interface metric
		buffer.writeLong(1512); // IPv4 mtu
		buffer.writeLong(1496); // IPv6 MTU
		buffer.writeLong(1000); // bandwidth
		
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
		buffer.writeLong(1); // interface metric
		buffer.writeLong(1512); // IPv4 mtu
		buffer.writeLong(1496); // IPv6 MTU
		buffer.writeLong(1000); // bandwidth
		
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
		buffer.writeLong(1); // interface metric
		buffer.writeLong(1512); // IPv4 mtu
		buffer.writeLong(1496); // IPv6 MTU
		buffer.writeLong(1000); // bandwidth
		
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
		buffer.writeLong(1); // interface metric
		buffer.writeLong(1512); // IPv4 mtu
		buffer.writeLong(1496); // IPv6 MTU
		buffer.writeLong(1000); // bandwidth
		
		ZServAddInterfacePacket packet = executeDecode(buffer);
		
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
}
