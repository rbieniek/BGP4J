/**
 * 
 */
package de.urb.netty.bgp4;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The BGP configuration object (bean implementation)
 * 
 * @author rainer
 *
 */
public class BGPv4ConfigurationBean implements BGPv4Configuration {

	private List<PeerConfigurationChangedListener> listeners = new LinkedList<PeerConfigurationChangedListener>();
	private InetSocketAddress localBgpServer = new InetSocketAddress(BGPv4Constants.BGP_PORT);
	private List<BGPv4PeerConfiguration> peers = new LinkedList<BGPv4PeerConfiguration>();

	/* (non-Javadoc)
	 * @see de.urb.netty.bgp4.BGPv4Configuration#getBgpv4Server()
	 */
	@Override
	public InetSocketAddress getBgpv4Server() {
		return localBgpServer;
	}

	/**
	 * @param bgpv4Server the bgpv4Server to set
	 */
	public void setBgpv4Server(InetSocketAddress bgpv4Server) {
		this.localBgpServer = bgpv4Server;
	}

	/* (non-Javadoc)
	 * @see de.urb.netty.bgp4.BGPv4Configuration#getPeers()
	 */
	@Override
	public List<BGPv4PeerConfiguration> getPeers() {
		return Collections.unmodifiableList(this.peers);
	}
	
	/* (non-Javadoc)
	 * @see de.urb.netty.bgp4.BGPv4Configuration#addPeer(de.urb.netty.bgp4.BGPv4PeerConfiguration)
	 */
	@Override
	public void addPeer(BGPv4PeerConfiguration peer) {
		this.peers.add(peer);
	
		for(PeerConfigurationChangedListener listener : listeners) {
			listener.peerAdded(peer);
		}
			
	}
	
	/* (non-Javadoc)
	 * @see de.urb.netty.bgp4.BGPv4Configuration#removePeer(de.urb.netty.bgp4.BGPv4PeerConfiguration)
	 */
	@Override
	public void removePeer(BGPv4PeerConfiguration peer) {
		if(this.peers.contains(peer)) {
			for(PeerConfigurationChangedListener listener : listeners) {
				listener.peerRemoved(peer);
			}
			
			this.peers.remove(peer);
		}
	}
	
	/* (non-Javadoc)
	 * @see de.urb.netty.bgp4.BGPv4Configuration#addListener(de.urb.netty.bgp4.PeerConfigurationChangedListener)
	 */
	@Override
	public void addListener(PeerConfigurationChangedListener listener) {
		this.listeners.add(listener);
	}
	
	/* (non-Javadoc)
	 * @see de.urb.netty.bgp4.BGPv4Configuration#removeListener(de.urb.netty.bgp4.PeerConfigurationChangedListener)
	 */
	@Override
	public void removeListener(PeerConfigurationChangedListener listener) {
		this.listeners.remove(listener);
	}
}
