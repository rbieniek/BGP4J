package org.bgp4j.net;

/**
 * Discrete origin types as specified in RFC 4271
 * 
 * @author rainer
 *
 */
public enum Origin {
	
	/** NLRI is interior to the originating AS (RFC 4271) */
	IGP,

	/** NLRI learned via EGP protocol (RFC 4271, RFC 904) */
	EGP,
	
	/** NLRI learned by some other means (RFC 4271)*/
	INCOMPLETE
}