package org.bgp4j.config.properties;

import lombok.Data;

@Data
public class CapabilitiesProperties {

    private boolean routeRefresh;
    private MultiprotocolCapabilityProperties multiProtocolRouting;
}
