package org.bgp4j.netty;

import java.net.InetSocketAddress;
import java.util.List;

public interface BGPv4Configuration {

	/**
	 * @return the bgpv4Server
	 */
	public abstract InetSocketAddress getBgpv4Server();

	/**
	 * get the list of peers
	 * 
	 * @return a read-only list of peers
	 */
	public abstract List<BGPv4PeerConfiguration> getPeers();

	/**
	 * add a peer to the configuration. Any listener is notified after the peer has been added.
	 * 
	 * @param peer the peer to add
	 */
	public abstract void addPeer(BGPv4PeerConfiguration peer);

	/**
	 * remove a peer from the configuration. If the peer is removed any listener is notified before the peer is removed.
	 * 
	 * @param peer the peer to remove.
	 */
	public abstract void removePeer(BGPv4PeerConfiguration peer);

	/**
	 * add a listener
	 * 
	 * @param listener the listener
	 */
	public abstract void addListener(PeerConfigurationChangedListener listener);

	/**
	 * remove a listener
	 * 
	 * @param listener the listener
	 */
	public abstract void removeListener(
			PeerConfigurationChangedListener listener);

}