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
 * File: org.bgp4j.netty.protocol.refresh.ORFMatch.java
 */
package org.bgp4j.net;

import java.util.EnumSet;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
@AllArgsConstructor
@Getter
public enum ORFMatch {
    PERMIT(0),
    DENY(1);

    private int code;

    public static ORFMatch fromCode(final int code) {
        return EnumSet.allOf(ORFMatch.class).stream().filter(e -> e.getCode() == code).findAny().orElseThrow(
                () -> new IllegalArgumentException("unknown outbound route filter type code: " + code));
    }

}
