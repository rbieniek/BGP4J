package org.bgp4j.net;

import java.util.EnumSet;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Address families as defined in RFC 1700
 *
 * @author rainer
 *
 */
@AllArgsConstructor
@Getter
public enum AddressFamily {
    RESERVED(0, "reserved"),
    IPv4(1, "ipv4"),
    IPv6(2, "ipv6"),
    NSAP(3, "nsap"),
    HDLC(4, "hdlc"),
    BBN1882(5, "bbn1882"),
    IEEE802(6, "ieee802"),
    E163(7, "e163"),
    E164(8, "e164"),
    F69(9, "f69"),
    X121(10, "x121"),
    IPX(11, "ipx"),
    APPLETALK(12, "appletalk"),
    DECNET4(13, "decnet"),
    BANYAN(14, "banyan"),
    RESERVED2(65535, "reserved2");

    private int code;
    private String name;

    public static AddressFamily fromCode(final int code) {
        return EnumSet.allOf(AddressFamily.class).stream().filter(e -> e.getCode() == code).findAny().orElseThrow(
                () -> new IllegalArgumentException("unknown address family code: " + code));
    }

    public static AddressFamily fromString(final String value) {
        return EnumSet.allOf(AddressFamily.class)
                .stream()
                .filter(e -> StringUtils.equalsIgnoreCase(e.getName(), value))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("unknown address family: " + value));
    }
}