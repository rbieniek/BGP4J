/**
 * 
 */
package de.urb.quagga.apps.shared;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.jboss.weld.environment.se.bindings.Parameters;

import de.urb.quagga.weld.Configuration;
import de.urb.quagga.weld.ConfigurationProducer;

/**
 * @author rainer
 *
 */
@ApplicationScoped
public class CommandLineConfigurationProducer implements ConfigurationProducer {

	@Inject @Parameters private String[] commandLine;
	
	/* (non-Javadoc)
	 * @see de.urb.quagga.weld.ConfigurationProducer#getConfiguration()
	 */
	@Override
	public Configuration getConfiguration() throws ParseException {
		Configuration config = new Configuration();
		Options options = new Options();
		
		options.addOption("p", "zebra-port", true, "Zebra TCP port number");
		
		CommandLine cl = (new PosixParser()).parse(options, commandLine);
		
		if(cl.hasOption("p")) {
			config.setZebraPort(Integer.parseInt(cl.getOptionValue("p")));
		}
		
		return config;
	}

}
