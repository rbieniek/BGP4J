/**
 * 
 */
package org.bgp4j.apps.easybox;

import javax.enterprise.util.AnnotationLiteral;

import org.bgp4j.weld.SeApplicationBootstrap;

/**
 * @author rainer
 *
 */
public class EasyboxDaemon {
	public static void main(String[] args) {
		SeApplicationBootstrap.bootstrapApplication(args, new AnnotationLiteral<EasyboxDaemonApplicationSelector>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 4333688934371320506L;
			
		});
	}
}
