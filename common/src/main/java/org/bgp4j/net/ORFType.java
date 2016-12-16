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
 * File: org.bgp4j.netty.protocol.refresh.ORFType.java
 */
package org.bgp4j.net;

import java.util.EnumSet;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
@Getter
@AllArgsConstructor
public enum ORFType {
    ADDRESS_PREFIX_BASED(ORFType.BGP_OUTBOUND_ROUTE_FILTER_TYPE_ADDRESS_PREFIX_BASED, "addressPrefixBased");

    private int code;
    private String name;

    /** Address prefix based outbound route filter type (RFC 5292) */
    private static final int BGP_OUTBOUND_ROUTE_FILTER_TYPE_ADDRESS_PREFIX_BASED = 64;

    public static ORFType fromCode(final int code) {
        return EnumSet.allOf(ORFType.class).stream().filter(e -> e.getCode() == code).findAny().orElseThrow(
                () -> new IllegalArgumentException("unknown outbound route filter type code: " + code));
    }

    public static ORFType fromString(final String value) {
        return EnumSet.allOf(ORFType.class)
                .stream()
                .filter(e -> StringUtils.equalsIgnoreCase(e.getName(), value))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("unknown aoutbound router filter name: " + value));
    }
}
