/**
 *  Copyright 2012 Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * 
 * File: org.bgp4j.netty.fsm.BGPv4FSMTest.java 
 */
package org.bgp4j.netty.fsm;

import org.apache.commons.configuration.XMLConfiguration;
import org.bgp4.config.Configuration;
import org.bgp4.config.ConfigurationParser;
import org.bgp4j.netty.LocalChannelBGPv4TestBase;
import org.junit.After;
import org.junit.Before;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class BGPv4FSMTest extends LocalChannelBGPv4TestBase {

	public class StubbedBGPv4FSM extends BGPv4FSM {
		
	}
	
	@Before
	public void before() {
		parser = obtainInstance(ConfigurationParser.class);
		fsm = obtainInstance(StubbedBGPv4FSM.class);
	}
	
	@After
	public void after() {
		parser = null;
		fsm = null;
	}
	
	private StubbedBGPv4FSM fsm;
	private ConfigurationParser parser;
	
	// -- begin of test messages
	
	// -- end of test messages
	private Configuration loadConfiguration(String fileName) throws Exception {
		return parser.parseConfiguration(new XMLConfiguration(fileName));
	}
	
}
