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
 * File: org.bgp4.config.extensions.IPv4ConverterLookupTest.java
 */
package org.bgp4j.config.extensions;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class IPv4ConverterLookupTest {

    private final IPv4ConverterLookup lookup = new IPv4ConverterLookup();

    @Test
    public void testGoodIpv4Address() {
        assertThat(lookup.lookup("192.168.4.1")).isEqualTo("3232236545");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTooLittleParts() {
        lookup.lookup("192.168.4,1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPartTooLarge() {
        lookup.lookup("192.168.256.1");
    }
}
