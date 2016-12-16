package org.bgp4j.net;

import java.util.EnumSet;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ORFSendReceive {
    RECEIVE(1, "receive"),
    SEND(2, "send"),
    BOTH(3, "both");

    private int code;
    private String name;

    public static ORFSendReceive fromCode(final int code) {
        return EnumSet.allOf(ORFSendReceive.class).stream().filter(e -> e.getCode() == code).findAny().orElseThrow(
                () -> new IllegalArgumentException("unknown outbound route filter type code: " + code));
    }

    public static ORFSendReceive fromString(final String value) {
        return EnumSet.allOf(ORFSendReceive.class)
                .stream()
                .filter(e -> StringUtils.equalsIgnoreCase(e.getName(), value))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("unknown aoutbound router filter name: " + value));
    }
}