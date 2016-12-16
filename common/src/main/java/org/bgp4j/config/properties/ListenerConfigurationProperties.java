package org.bgp4j.config.properties;

import java.net.InetAddress;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ListenerConfigurationProperties {

    private static final int BGP_PORT = 179;

    @NotNull
    private InetAddress bindAddress;

    @NotNull
    @Min(value = 1024)
    private int port = BGP_PORT;

    private boolean autostartEnabled = true;
}
