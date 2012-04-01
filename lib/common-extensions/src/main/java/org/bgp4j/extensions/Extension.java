/**
 * 
 */
package org.bgp4j.extensions;

import java.util.Collection;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;

/**
 * Main entry point into extensions to the BGP4J core.
 * 
 * This may be used to provide additional sources of routing information and other stuff.
 * 
 * @author rainer
 *
 */
public interface Extension {

	/**
	 * get the extension name
	 * @return
	 */
	public String getName();
	
	/**
	 * Initialize the extension
	 * 
	 * @param beanManager
	 */
	public void initialize(ExtensionBeanFactory beanFactory);
	
	/**
	 * Configure the exception. The configurations framework passes the top level extensions node
	 * into the extensions' configuration mechanism and otherwise treats it as opaque configuration
	 * information
	 * 
	 * @param config top-level extension node
	 * @throws ConfigurationException thrown if the extensions' parser detects any errors
	 */
	public void configure(HierarchicalConfiguration config) throws ConfigurationException;
	
	/**
	 * Start the extension
	 * 
	 * @throws Exception
	 */
	public void startExtension() throws Exception;
	
	/**
	 * Stop the extension
	 * 
	 * @throws Exception
	 */
	public void stopExtension() throws Exception;
	
	/**
	 * get the list of Routing Information Bases provided by this extension. An extensions may define
	 * multiple routing information bases 
	 * 
	 * @return
	 */
	public Collection<ProvidedRIBs> getProvidedRIBs();
	
	/**
	 * Check if the extension is ready for service and should be started
	 */
	public boolean isReadyForService();
}
