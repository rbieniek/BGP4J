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
 * File: org.bgp4.config.IPv4ConverterLookup.java 
 */
package org.bgp4j.config.extensions;

import java.util.StringTokenizer;

import javax.inject.Inject;

import org.apache.commons.lang.text.StrLookup;
import org.slf4j.Logger;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class IPv4ConverterLookup extends StrLookup {

	private @Inject Logger log;
	
	/* (non-Javadoc)
	 * @see org.apache.commons.lang3.text.StrLookup#lookup(java.lang.String)
	 */
	@Override
	public String lookup(String key) {
		StringTokenizer tokenizer = new StringTokenizer(key, ".");
		
		if(tokenizer.countTokens() != 4) {
			log.error("Invalid IPv4 address passed: " + key);
			
			throw new IllegalArgumentException("Invalid IPv4 address passed: " + key);	
		}

		long ipAddr = 0;

		long[] parts = new long[4];
		try {
			for(int i=0; i<4; i++) {
				parts[i] = Integer.parseInt(tokenizer.nextToken());
				
				if(parts[i] < 0 || parts[i] > 255)
					throw new IllegalArgumentException("Invalid IPv4 address passed: " + key);				}
		} catch(NumberFormatException e) {
			log.error("Invalid IPv4 address passed: " + key);
			
			throw new IllegalArgumentException("Invalid IPv4 address passed: " + key, e);			
		}
		
		for(int i=0; i<4; i++)
			ipAddr |= parts[i] << ((3-i)*8);
		
		return Long.toString(ipAddr);
	}

}
