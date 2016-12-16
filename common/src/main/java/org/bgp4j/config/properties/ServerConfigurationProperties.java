package org.bgp4j.config.properties;

import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ServerConfigurationProperties {

    @Valid
    @Size(min = 1)
    private List<ListenerConfigurationProperties> listeners = new LinkedList<>();
}
