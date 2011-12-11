/**
 * 
 */
package de.urb.quagga.apps.dumper;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.slf4j.Logger;

import de.urb.quagga.netty.QuaggaClient;
import de.urb.quagga.weld.SeApplicationStartEvent;

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
			
			while(true) {
				try {
					Thread.sleep(10*1000);
				} catch(InterruptedException e) {}
			}
		} catch(Exception e) {
			log.error("failed to run client", e);
			
			throw e;
		}
	}
}
