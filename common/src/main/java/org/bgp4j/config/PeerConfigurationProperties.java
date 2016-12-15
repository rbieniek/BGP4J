package org.bgp4j.config;

import java.net.InetSocketAddress;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PeerConfigurationProperties {

    @NotNull
    private InetSocketAddress peerAddress;
}
