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
 * 
 * File: org.bgp4j.netty.MockPeerConnectionInformation.java 
 */
package org.bgp4j.netty;

import org.bgp4j.net.ASType;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class MockPeerConnectionInformation implements PeerConnectionInformation {

	private ASType asTypeInUse;
	private int localAS;
	private int remoteAS;
	private long localBgpIdentifier;
	private long remoteBgpIdentifier;
	
	/* (non-Javadoc)
	 * @see org.bgp4j.netty.PeerConnectionInformation#getAsTypeInUse()
	 */
	@Override
	public ASType getAsTypeInUse() {
		return asTypeInUse;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.PeerConnectionInformation#getLocalAS()
	 */
	@Override
	public int getLocalAS() {
		return localAS;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.PeerConnectionInformation#getRemoteAS()
	 */
	@Override
	public int getRemoteAS() {
		// TODO Auto-generated method stub
		return remoteAS;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.PeerConnectionInformation#isIBGPConnection()
	 */
	@Override
	public boolean isIBGPConnection() {
		return (localAS == remoteAS);
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.PeerConnectionInformation#isEBGPConnection()
	 */
	@Override
	public boolean isEBGPConnection() {
		return (localAS != remoteAS);
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.PeerConnectionInformation#isAS4OctetsInUse()
	 */
	@Override
	public boolean isAS4OctetsInUse() {
		return (asTypeInUse == ASType.AS_NUMBER_4OCTETS);
	}


	/**
	 * @return the localBGPIdentifier
	 */
	public long getLocalBgpIdentifier() {
		return localBgpIdentifier;
	}

	/**
	 * @param localBGPIdentifier the localBGPIdentifier to set
	 */
	public void setLocalBgpIdentifier(long localBGPIdentifier) {
		this.localBgpIdentifier = localBGPIdentifier;
	}

	/**
	 * @return the remoteBGPIdentifier
	 */
	public long getRemoteBgpIdentifier() {
		return remoteBgpIdentifier;
	}

	/**
	 * @param remoteBGPIdentifier the remoteBGPIdentifier to set
	 */
	public void setRemoteBgpIdentifier(long remoteBGPIdentifier) {
		this.remoteBgpIdentifier = remoteBGPIdentifier;
	}

	/**
	 * @param asTypeInUse the asTypeInUse to set
	 */
	public void setAsTypeInUse(ASType asTypeInUse) {
		this.asTypeInUse = asTypeInUse;
	}

	/**
	 * @param localAS the localAS to set
	 */
	public void setLocalAS(int localAS) {
		this.localAS = localAS;
	}

	/**
	 * @param remoteAS the remoteAS to set
	 */
	public void setRemoteAS(int remoteAS) {
		this.remoteAS = remoteAS;
	}

}
