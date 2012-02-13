/**
 *  Copyright 2012 Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.bgp4j.netty;


/**
 * This bean contains all information about the BGP connection that needs to be accessible 
 * by channel handlers. It is stored in the ChannelHandlerContext after the OPEN handshake.
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class PeerConnectionInformation {
	private int localAS;
	private int remoteAS;
	private int localBgpIdentifier;
	private int remoteBgpIdentifier;
	private ASType asTypeInUse = ASType.AS_NUMBER_2OCTETS;
	
	public ASType getAsTypeInUse() {
		return asTypeInUse;
	}

	public void setAsTypeInUse(ASType asType) {
		this.asTypeInUse = asType;
	}

	/**
	 * 
	 * @return
	 */
	public int getLocalAS() {
		return localAS;
	}
	
	/**
	 * 
	 * @param localAS
	 */
	public void setLocalAS(int localAS) {
		this.localAS = localAS;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getRemoteAS() {
		return remoteAS;
	}
	
	/**
	 * 
	 * @param remoteAS
	 */
	public void setRemoteAS(int remoteAS) {
		this.remoteAS = remoteAS;
	}

	/**
	 * Test if the connection describes an IBGP connection (peers in the same AS)
	 * 
	 * @return <code>true</code> if IBGP connection, <code>false</code> otherwise
	 */
	public boolean isIBGPConnection() {
		return (getRemoteAS() == getLocalAS());
	}

	/**
	 * Test if the connection describes an EBGP connection (peers in the same AS)
	 * 
	 * @return <code>true</code> if EBGP connection, <code>false</code> otherwise
	 */
	public boolean isEBGPConnection() {
		return (getRemoteAS() != getLocalAS());
	}
	
	/**
	 * Test if this connection uses 4 octet AS numbers
	 * 
	 * @return
	 */
	public boolean isAS4OctetsInUse() {
		return (this.asTypeInUse == ASType.AS_NUMBER_4OCTETS);
	}

	/**
	 * @return the localBgpIdentifier
	 */
	public int getLocalBgpIdentifier() {
		return localBgpIdentifier;
	}

	/**
	 * @param localBgpIdentifier the localBgpIdentifier to set
	 */
	public void setLocalBgpIdentifier(int localBgpIdentifier) {
		this.localBgpIdentifier = localBgpIdentifier;
	}

	/**
	 * @return the remoteBgpIdentifier
	 */
	public int getRemoteBgpIdentifier() {
		return remoteBgpIdentifier;
	}

	/**
	 * @param remoteBgpIdentifier the remoteBgpIdentifier to set
	 */
	public void setRemoteBgpIdentifier(int remoteBgpIdentifier) {
		this.remoteBgpIdentifier = remoteBgpIdentifier;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PeerConnectionInformation [localAS=").append(localAS)
				.append(", remoteAS=").append(remoteAS)
				.append(", localBgpIdentifier=").append(localBgpIdentifier)
				.append(", remoteBgpIdentifier=").append(remoteBgpIdentifier)
				.append(", ");
		if (asTypeInUse != null)
			builder.append("asTypeInUse=").append(asTypeInUse);
		builder.append("]");
		return builder.toString();
	}
}
