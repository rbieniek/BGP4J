package org.bgp4j.config;

import java.net.InetSocketAddress;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PeerConfigurationProperties {

    @NotNull
    private InetSocketAddress peerAddress;

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
