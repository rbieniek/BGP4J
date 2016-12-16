package org.bgp4j.config.properties;

import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "bgp")
@Data
public class BgpConfigurationProperties {

    @NotNull
    @Valid
    private ServerConfigurationProperties server;

    private int holdTime;
    private int idleHoldTime;
    private int delayOpenTime;
    private int connectRetryTime;
    private int automaticStartInterval;

    @Valid
    private List<PeerConfigurationProperties> peers = new LinkedList<>();
}
