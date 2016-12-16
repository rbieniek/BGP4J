package org.bgp4j.net;

import java.util.EnumSet;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Subsequent address family as defined in RFC 2858
 *
 * @author rainer
 *
 */
@AllArgsConstructor
@Getter
public enum SubsequentAddressFamily {
    NLRI_UNICAST_FORWARDING(1, SubsequentAddressFamily.ENCODING_UNICAST),
    NLRI_MULTICAST_FORWARDING(2, SubsequentAddressFamily.ENCODING_MULTICAST),
    NLRI_UNICAST_MULTICAST_FORWARDING(3, SubsequentAddressFamily.ENCODING_UNICAST_MULTICAST);

    private int code;
    private String encoding;

    private static final String ENCODING_UNICAST_MULTICAST = "Unicast+Multicast";
    private static final String ENCODING_MULTICAST = "Multicast";
    private static final String ENCODING_UNICAST = "Unicast";

    public static SubsequentAddressFamily fromCode(final int code) {
        return EnumSet.allOf(SubsequentAddressFamily.class).stream().filter(e -> e.getCode() == code).findAny().orElseThrow(
                () -> new IllegalArgumentException("unknown address family code: " + code));
    }

    public static SubsequentAddressFamily fromString(final String value) {
        return EnumSet.allOf(SubsequentAddressFamily.class)
                .stream()
                .filter(e -> StringUtils.equalsIgnoreCase(e.getEncoding(), value))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("unknown address family: " + value));
    }
}