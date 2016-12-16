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
 * File: org.bgp4j.rib.BinaryNextHop.java
 */
package org.bgp4j.net;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class BinaryNextHop implements NextHop {

    private final byte[] address;

    public BinaryNextHop(final byte[] address) {
        this.address = address;
    }

    /**
     * @return the nextHop
     */
    public byte[] getAddress() {
        return address;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getAddress()).toHashCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof BinaryNextHop)) {
            return false;
        }

        final BinaryNextHop o = (BinaryNextHop) obj;

        return new EqualsBuilder().append(getAddress(), o.getAddress()).isEquals();
    }

    @Override
    public int compareTo(final NextHop o) {
        final CompareToBuilder builder = new CompareToBuilder().append(getType(), o.getType());

        if (o.getType() == Type.Binary) {
            builder.append(getAddress(), ((BinaryNextHop) o).getAddress());
        }

        return builder.toComparison();
    }

    @Override
    public Type getType() {
        return Type.Binary;
    }

    private static final char[] chars = new char[] {
            '0',
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9',
            'a',
            'b',
            'c',
            'd',
            'e',
            'f' };

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        for (final byte addres : address) {
            builder.append(chars[addres / 16 & 0x0f]);
            builder.append(chars[addres % 16 & 0x0f]);
        }

        return builder.toString();
    }
}
