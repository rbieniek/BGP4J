/**
 * 
 */
package de.urb.netty.bgp4;

import java.net.InetSocketAddress;

import org.apache.commons.lang3.builder.EqualsBuilder;


/**
 * @author rainer
 *
 */
public class BGPv4PeerConfigurationBean implements BGPv4PeerConfiguration {
	private int localBgpIdentifier;
	private int localAutonomousSystem;
	private int localHoldTime;
	private int connectRetryInterval;

	private InetSocketAddress remotePeerAddress;
	private int remoteBgpIdentitifer;
	private int remoteAutonomousSystem;

	/* (non-Javadoc)
	 * @see de.urb.netty.bgp4.BGPv4PeerConfiguration#getRemoteBgpIdentitifer()
	 */
	@Override
	public int getRemoteBgpIdentitifer() {
		return remoteBgpIdentitifer;
	}

	/**
	 * @param remoteBgpIdentitifer the remoteBgpIdentitifer to set
	 */
	public void setRemoteBgpIdentitifer(int remoteBgpIdentitifer) {
		this.remoteBgpIdentitifer = remoteBgpIdentitifer;
	}

	/* (non-Javadoc)
	 * @see de.urb.netty.bgp4.BGPv4PeerConfiguration#getRemoteAutonomousSystem()
	 */
	@Override
	public int getRemoteAutonomousSystem() {
		return remoteAutonomousSystem;
	}

	/**
	 * @param remoteAutonomousSystem the remoteAutonomousSystem to set
	 */
	public void setRemoteAutonomousSystem(int remoteAutonomousSystem) {
		this.remoteAutonomousSystem = remoteAutonomousSystem;
	}

	/* (non-Javadoc)
	 * @see de.urb.netty.bgp4.BGPv4PeerConfiguration#getRemotePeerAddress()
	 */
	@Override
	public InetSocketAddress getRemotePeerAddress() {
		return remotePeerAddress;
	}

	/**
	 * @param remotePeerAddress the remotePeerAddress to set
	 */
	public void setRemotePeerAddress(InetSocketAddress remotePeerAddress) {
		this.remotePeerAddress = remotePeerAddress;
	}

	/* (non-Javadoc)
	 * @see de.urb.netty.bgp4.BGPv4PeerConfiguration#getLocalBgpIdentifier()
	 */
	@Override
	public int getLocalBgpIdentifier() {
		return localBgpIdentifier;
	}

	/**
	 * @param localBgpIdentifier the localBgpIdentifier to set
	 */
	public void setLocalBgpIdentifier(int localBgpIdentifier) {
		this.localBgpIdentifier = localBgpIdentifier;
	}

	/* (non-Javadoc)
	 * @see de.urb.netty.bgp4.BGPv4PeerConfiguration#getLocalAutonomousSystem()
	 */
	@Override
	public int getLocalAutonomousSystem() {
		return localAutonomousSystem;
	}

	/**
	 * @param localAutonomousSystem the localAutonomousSystem to set
	 */
	public void setLocalAutonomousSystem(int localAutonomousSystem) {
		this.localAutonomousSystem = localAutonomousSystem;
	}

	/* (non-Javadoc)
	 * @see de.urb.netty.bgp4.BGPv4PeerConfiguration#getLocalHoldTime()
	 */
	@Override
	public int getLocalHoldTime() {
		return localHoldTime;
	}

	/**
	 * @param localHoldTime the localHoldTime to set
	 */
	public void setLocalHoldTime(int localHoldTime) {
		this.localHoldTime = localHoldTime;
	}

	/**
	 * @return the connectRetryInterval
	 */
	public int getConnectRetryInterval() {
		return connectRetryInterval;
	}

	/**
	 * @param connectRetryInterval the connectRetryInterval to set
	 */
	public void setConnectRetryInterval(int connectRetryInterval) {
		this.connectRetryInterval = connectRetryInterval;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		BGPv4PeerConfiguration c = (BGPv4PeerConfiguration)obj;
		
		return (new EqualsBuilder())
				.append(getConnectRetryInterval(), c.getConnectRetryInterval())
				.append(getLocalAutonomousSystem(), c.getLocalAutonomousSystem())
				.append(getLocalBgpIdentifier(), c.getLocalBgpIdentifier())
				.append(getLocalHoldTime(), c.getLocalHoldTime())
				.append(getRemoteAutonomousSystem(), c.getRemoteAutonomousSystem())
				.append(getRemoteBgpIdentitifer(), c.getRemoteBgpIdentitifer())
				.append(getRemotePeerAddress(), c.getRemotePeerAddress())
				.isEquals();
	}
	
	
}
