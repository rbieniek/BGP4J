/**
 * 
 */
package org.bgp4j.extension.snmp4j.web;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author rainer
 *
 */
@XmlRootElement(name="instances")
public class ListInstancesResult {

	private List<String> names = new LinkedList<String>();
	
	@XmlElement
	public List<String> getNames() {
		return names;
	}
	
	void addName(String name) {
		names.add(name);
	}
}
