package org.bgp4j.config;

import java.net.InetAddress;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ListenerConfiguration {

    @NotNull
    private InetAddress bindAddress;

    @NotNull
    @Min(value = 1024)
    private int port;

    private boolean autostartEnabled = true;
}
