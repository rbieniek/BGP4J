package org.bgp4j.config;

import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ServerConfigurationProperties {

    @Valid
    @Size(min = 1)
    private List<ListenerConfiguration> listeners = new LinkedList<>();
}
