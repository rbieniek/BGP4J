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
 * File: org.bgp4.config.nodes.CapabilitiesDecorator.java
 */
package org.bgp4j.config.nodes;

import java.util.Set;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import org.bgp4j.net.capabilities.Capability;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class CapabilitiesDecorator implements Capabilities {

    private Capabilities decorated;

    public CapabilitiesDecorator(final Capabilities decorated) {
        this.decorated = decorated;
    }

    @Override
    public Set<Capability> getRequiredCapabilities() {
        return this.decorated.getRequiredCapabilities();
    }

    @Override
    public Set<Capability> getOptionalCapabilities() {
        return this.decorated.getOptionalCapabilities();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Capabilities)) {
            return false;
        }

        Set<Capability> otherCaps = ((Capabilities) obj).getRequiredCapabilities();
        Set<Capability> thisCaps = getRequiredCapabilities();

        if (otherCaps.size() != thisCaps.size()) {
            return false;
        }

        for (final Capability cap : thisCaps) {
            if (!otherCaps.contains(cap)) {
                return false;
            }
        }

        otherCaps = ((Capabilities) obj).getOptionalCapabilities();
        thisCaps = getOptionalCapabilities();

        if (otherCaps.size() != thisCaps.size()) {
            return false;
        }

        for (final Capability cap : thisCaps) {
            if (!otherCaps.contains(cap)) {
                return false;
            }
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final HashCodeBuilder hcb = new HashCodeBuilder();

        for (final Capability cap : getRequiredCapabilities()) {
            hcb.append(cap).append(false);
        }

        for (final Capability cap : getOptionalCapabilities()) {
            hcb.append(cap).append(true);
        }

        return hcb.toHashCode();
    }

}
