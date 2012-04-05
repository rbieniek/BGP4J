package org.bgp4j.rib;

public interface ExtensionRoutingBaseManager {

	public abstract PeerRoutingInformationBase extensionRoutingInformationBase(
			String extensionName, String key);

}