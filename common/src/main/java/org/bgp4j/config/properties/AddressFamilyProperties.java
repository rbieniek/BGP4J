package org.bgp4j.config.properties;

import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.SubsequentAddressFamily;

import lombok.Data;

@Data
public class AddressFamilyProperties {

    private AddressFamily addressFamily;
    private SubsequentAddressFamily subsequentAddressFamily;
}
