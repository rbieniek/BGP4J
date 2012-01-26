/**
 * 
 */
package org.bgp4j.apps.dumper;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.bgp4j.weld.SeApplicationStartEvent;
import org.slf4j.Logger;

import de.urb.quagga.netty.QuaggaClient;

/**
 * @author rainer
 *
 */
public class DumperApplicationListener {
	private @Inject Logger log;
	private @Inject QuaggaClient quaggaClient;
	
	public void listen(@Observes @DumperApplicationSelector SeApplicationStartEvent event) throws Exception {
		try {
			quaggaClient.startClient();

			quaggaClient.waitForChannelClose();
			quaggaClient.stopClient();
		} catch(Exception e) {
			log.error("failed to run client", e);
			
			throw e;
		}
	}
}
