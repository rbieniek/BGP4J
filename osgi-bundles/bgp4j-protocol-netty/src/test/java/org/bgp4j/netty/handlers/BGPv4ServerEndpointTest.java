/**
 * 
 */
package org.bgp4j.netty.handlers;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

import org.bgp4j.definitions.fsm.BGPv4FSM;
import org.bgp4j.definitions.fsm.BGPv4FSMRegistry;
import org.bgp4j.definitions.fsm.FiniteStateMachineAlreadyExistsException;
import org.bgp4j.definitions.fsm.UnknownPeerException;
import org.bgp4j.definitions.peer.EPeerDirection;
import org.bgp4j.definitions.peer.PeerConnectionInformation;
import org.bgp4j.definitions.peer.PeerConnectionInformationRegistry;
import org.bgp4j.net.EChannelDirection;
import org.bgp4j.netty.Attributes;
import org.bgp4j.netty.BGPv4TestBase;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * @author rainer
 *
 */
public class BGPv4ServerEndpointTest extends BGPv4TestBase {
	public static class NotLoopbackAddressMatcher extends BaseMatcher<InetAddress> {

		@Override
		public boolean matches(Object item) {
			return !(InetAddress.getLoopbackAddress().equals(item));
		}

		@Override
		public void describeTo(Description description) {
			description.appendText("not matching loopback address");
		}
		
	}
	
	private BGPv4ServerEndpoint endpoint;
	private PeerConnectionInformation peerConnection;
	private PeerConnectionInformationRegistry peerConnectionRegistry;
	private BGPv4FSMRegistry fsmRegistry;
	private BGPv4FSM fsm;
	private ServerBootstrap bootstrap;
	private EventLoopGroup bossGroup; // (1)
    private EventLoopGroup workerGroup;
    private Channel serverChannel;
    private InetSocketAddress serverAddress;

	@SuppressWarnings("unchecked")
	@Before
	public void before() throws Exception {
		peerConnection = mock(PeerConnectionInformation.class);
		peerConnectionRegistry = mock(PeerConnectionInformationRegistry.class);
		fsm = mock(BGPv4FSM.class);
		fsmRegistry = mock(BGPv4FSMRegistry.class);
		
		when(fsmRegistry.createFsm(InetAddress.getLoopbackAddress(), 
				EChannelDirection.SERVER))
			.thenReturn(fsm)
			.thenThrow(FiniteStateMachineAlreadyExistsException.class);
		when(fsmRegistry.createFsm(argThat(new NotLoopbackAddressMatcher()), 
				eq(EChannelDirection.SERVER)))
			.thenThrow(UnknownPeerException.class);
		when(fsmRegistry.createFsm(any(), eq(EChannelDirection.CLIENT)))
			.thenThrow(UnknownPeerException.class);
		
		when(fsm.peerConnectionInformation()).thenReturn(peerConnection);


		endpoint = new BGPv4ServerEndpoint();
		bossGroup = new NioEventLoopGroup(); // (1)
	    workerGroup = new NioEventLoopGroup();
		bootstrap = new ServerBootstrap();

		bootstrap.group(bossGroup, workerGroup)
         .channel(NioServerSocketChannel.class)
         .childHandler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel ch) throws Exception {
				ch.pipeline().addLast(new BGPv4Reframer());
				ch.pipeline().addLast(new BGPv4Codec());
				ch.pipeline().addLast(new ValidateServerIdentifier());
				ch.pipeline().addLast(new InboundOpenCapabilitiesProcessor());
				ch.pipeline().addLast(new PeerCollisionDetectionHandler());
				ch.pipeline().addLast(new UpdateAttributeChecker());
				ch.pipeline().addLast(endpoint);
			}
		})
		.childAttr(Attributes.channelDirectionKey, EChannelDirection.SERVER)
		.childAttr(Attributes.fsmRegistryKey, fsmRegistry)
		.childAttr(Attributes.peerConnectionInformationRegistry, peerConnectionRegistry)
         .option(ChannelOption.SO_BACKLOG, 128)
         .childOption(ChannelOption.SO_KEEPALIVE, true)
         .childOption(ChannelOption.SO_LINGER, 0);		
		
		ChannelFuture cf = bootstrap.bind(InetAddress.getLoopbackAddress(), 0).sync();
		
		serverChannel = cf.channel();
		serverAddress = (InetSocketAddress)serverChannel.localAddress();
	}
	
	@After
	public void after() throws Exception {
		serverChannel.close().sync();
		serverChannel = null;
		serverAddress = null;
		bootstrap = null;
		bossGroup.shutdownGracefully().sync();
		bossGroup = null;
		workerGroup.shutdownGracefully().sync();
		workerGroup = null;
		peerConnectionRegistry = null;
		fsmRegistry = null;
		endpoint = null;
	}

	@Test
	public void testSuccessfulConnectAndDisconnect() throws Exception {
		Socket clientSocket = new Socket();
		
		when(peerConnection.peerDirection()).thenReturn(EPeerDirection.ClientAndServer);

		clientSocket.connect(serverAddress); 
		Thread.sleep(1000);
		
		verify(fsm).handleConnectionOpened();
		verify(fsm).messageWriter(any());
		verify(fsm,never()).handleConnectionClosed();
		verify(fsm,never()).handleEvent(any());
		verify(fsm,never()).handlePacket(any());

		clientSocket.close();
		Thread.sleep(1000);

		verify(fsm).handleConnectionOpened();
		verify(fsm, times(2)).messageWriter(any());
		verify(fsm).handleConnectionClosed();
		verify(fsm,never()).handleEvent(any());
		verify(fsm,never()).handlePacket(any());
	}	


	@Test
	public void testClientOnlyConnectAndDisconnect() throws Exception {
		Socket clientSocket = new Socket();
		
		when(peerConnection.peerDirection()).thenReturn(EPeerDirection.ClientOnly);

		clientSocket.connect(serverAddress); 
		Thread.sleep(1000);
		
		verify(fsm,never()).handleConnectionOpened();
		verify(fsm,never()).messageWriter(any());
		verify(fsm,never()).handleConnectionClosed();
		verify(fsm,never()).handleEvent(any());
		verify(fsm,never()).handlePacket(any());

		clientSocket.close();
		Thread.sleep(1000);

		verify(fsm,never()).handleConnectionOpened();
		verify(fsm,never()).messageWriter(any());
		verify(fsm,never()).handleConnectionClosed();
		verify(fsm,never()).handleEvent(any());
		verify(fsm,never()).handlePacket(any());
	}	
}
