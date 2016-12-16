package org.bgp4j.config.properties;

import java.net.InetAddress;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PeerConfigurationProperties {

    private static final int BGP_PORT = 179;

    @NotNull
    private InetAddress peerAddress;

    @NotNull
    @Min(value = 1024)
    private int port = BGP_PORT;

    @NotNull
    private String peerName;

    @Min(value = 1)
    private int localAS;

    @Min(value = 1)
    private int remoteAS;

    private long localBgpIdentifier;
    private long remoteBgpIdentifier;

    private int holdTime;
    private int idleHoldTime;
    private int delayOpenTime;
    private int connectRetryTime;
    private int automaticStartInterval;

    private boolean holdTimerDisabled = false;
    private boolean allowAutomaticStart = true;
    private boolean allowAutomaticStop = true;
    private boolean collisionDetectEstablishedState = false;
    private boolean dampPeerOscillation = false;
    private boolean delayOpen = false;
    private boolean passiveTcpEstablishment = false;

    private CapabilitiesProperties capabilities;
}
