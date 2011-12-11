/**
 * 
 */
package de.urb.quagga.apps.dumper;

import javax.enterprise.util.AnnotationLiteral;

import de.urb.quagga.weld.SeApplicationBootstrap;

/**
 * @author rainer
 *
 */
public class PacketDumper {

	public static void main(String[] args) {
		SeApplicationBootstrap.bootstrapApplication(args, new AnnotationLiteral<DumperApplicationSelector>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 4333688934371320506L;
			
		});
	}
}
