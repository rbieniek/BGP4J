/**
 * 
 */
package de.urb.quagga.weld;

import java.lang.annotation.Annotation;

import org.jboss.weld.environment.se.StartMain;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

/**
 * @author rainer
 *
 */
public class SeApplicationBootstrap {

	public static  void bootstrapApplication(String[] args, Annotation selection) {
		StartMain.PARAMETERS = args;
		
		Weld weld = new Weld();
		WeldContainer weldContainer = weld.initialize();
		
		Runtime.getRuntime().addShutdownHook(new ShutdownHook(weld));
		weldContainer.event().select(SeApplicationStartEvent.class, selection).fire(new SeApplicationStartEvent());
	}

    static class ShutdownHook extends Thread {
        private final Weld weld;

        ShutdownHook(Weld weld) {
            this.weld = weld;
        }

        public void run() {
            weld.shutdown();
        }
    }
}
