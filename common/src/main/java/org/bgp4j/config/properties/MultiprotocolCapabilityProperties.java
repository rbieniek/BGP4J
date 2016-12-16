package org.bgp4j.config.properties;

import java.util.List;

import lombok.Data;

@Data
public class MultiprotocolCapabilityProperties {

    private List<AddressFamilyProperties> addressFamilies;
}
