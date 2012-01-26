/**
 * 
 */
package org.bgp4j.weld;

/**
 * @author rainer
 *
 */
public interface ConfigurationProducer {

	/**
	 * create the configuration object
	 * 
	 * @return
	 */
	public Configuration getConfiguration() throws Exception;

}
