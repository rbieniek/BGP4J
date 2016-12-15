package org.bgp4j.config;

import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "bgp")
@Data
public class BgpConfigurationProperties {

    @NotNull
    @Valid
    private ServerConfigurationProperties server;

    @Valid
    private List<PeerConfigurationProperties> peers = new LinkedList<>();
}
