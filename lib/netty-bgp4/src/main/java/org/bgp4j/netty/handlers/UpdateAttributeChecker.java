/**
 *  Copyright 2012 Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * 
 * File: org.bgp4j.netty.protocol.UpdateAttributeChecker.java 
 */
package org.bgp4j.netty.handlers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bgp4j.netty.BGPv4Constants;
import org.bgp4j.netty.PeerConnectionInformation;
import org.bgp4j.netty.PeerConnectionInformationAware;
import org.bgp4j.netty.protocol.NotificationPacket;
import org.bgp4j.netty.protocol.update.ASPathAttribute;
import org.bgp4j.netty.protocol.update.ASTypeAware;
import org.bgp4j.netty.protocol.update.Attribute;
import org.bgp4j.netty.protocol.update.AttributeFlagsNotificationPacket;
import org.bgp4j.netty.protocol.update.LocalPrefPathAttribute;
import org.bgp4j.netty.protocol.update.MalformedAttributeListNotificationPacket;
import org.bgp4j.netty.protocol.update.MissingWellKnownAttributeNotificationPacket;
import org.bgp4j.netty.protocol.update.NextHopPathAttribute;
import org.bgp4j.netty.protocol.update.OriginPathAttribute;
import org.bgp4j.netty.protocol.update.UpdatePacket;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;

/**
 * @author Rainer Bieniek (rainer@bgp4j.org)
 *
 */
@PeerConnectionInformationAware
@Singleton
public class UpdateAttributeChecker extends SimpleChannelUpstreamHandler {	
	private @Inject Logger log;
	
	private Set<Class<? extends Attribute>> mandatoryIBGPAttributes = new HashSet<Class<? extends Attribute>>();
	private Set<Class<? extends Attribute>> mandatoryEBGPAttributes = new HashSet<Class<? extends Attribute>>();
	private Map<Class<? extends Attribute>, Integer> as2ClazzCodeMap = new HashMap<Class<? extends Attribute>, Integer>();
	private Map<Class<? extends Attribute>, Integer> as4ClazzCodeMap = new HashMap<Class<? extends Attribute>, Integer>();
	
	private UpdateAttributeChecker() {
		mandatoryEBGPAttributes.add(OriginPathAttribute.class);
		mandatoryEBGPAttributes.add(ASPathAttribute.class);
		mandatoryEBGPAttributes.add(NextHopPathAttribute.class);

		mandatoryIBGPAttributes.add(OriginPathAttribute.class);
		mandatoryIBGPAttributes.add(ASPathAttribute.class);
		mandatoryIBGPAttributes.add(NextHopPathAttribute.class);
		mandatoryIBGPAttributes.add(LocalPrefPathAttribute.class);
		
		as2ClazzCodeMap.put(ASPathAttribute.class, BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH);
		as2ClazzCodeMap.put(LocalPrefPathAttribute.class, BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_LOCAL_PREF);
		as2ClazzCodeMap.put(NextHopPathAttribute.class, BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_NEXT_HOP);
		as2ClazzCodeMap.put(OriginPathAttribute.class, BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_ORIGIN);

		as4ClazzCodeMap.put(ASPathAttribute.class, BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH);
		as4ClazzCodeMap.put(LocalPrefPathAttribute.class, BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_LOCAL_PREF);
		as4ClazzCodeMap.put(NextHopPathAttribute.class, BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_NEXT_HOP);
		as4ClazzCodeMap.put(OriginPathAttribute.class, BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_ORIGIN);
}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		boolean sentUpstream = false;
		
		if(e.getMessage() instanceof UpdatePacket) {
			PeerConnectionInformation connInfo = (PeerConnectionInformation)ctx.getAttachment();
			UpdatePacket update = (UpdatePacket)e.getMessage();
			List<Attribute> attributeFlagsErrorList = new LinkedList<Attribute>();
			List<Class<? extends Attribute>> missingWellKnownList = new LinkedList<Class<? extends Attribute>>();
			Set<Class<? extends Attribute>> givenAttributes = new HashSet<Class<? extends Attribute>>();
			
			// check if passed optional / transitive bits match the presettings of the attribute type
			for(Attribute attribute : update.getPathAttributes()) {
				boolean badAttr = false;

				givenAttributes.add(attribute.getClass());
				
				switch(attribute.getCategory()) {
				case WELL_KNOWN_MANDATORY:
				case WELL_KNOWN_DISCRETIONARY:
					badAttr = attribute.isOptional() || !attribute.isTransitive();
					break;
				case OPTIONAL_NON_TRANSITIVE:
					badAttr = !attribute.isOptional() || attribute.isTransitive();
					break;
				case OPTIONAL_TRANSITIVE:
					badAttr = !attribute.isOptional() || !attribute.isTransitive();
					break;
				}
				
				if(badAttr) {
					log.info("detected attribute " + attribute + " with invalid flags");
					
					attributeFlagsErrorList.add(attribute);
				}
			}
			
			// if we have any bad attribute, generate notification message and leave
			if(attributeFlagsErrorList.size() > 0) {
				NotificationHelper.sendNotificationAndCloseChannel(ctx, new AttributeFlagsNotificationPacket(serializeAttributes(attributeFlagsErrorList)));
			} else {
				// check presence of mandatory attributes
				Set<Class<? extends Attribute>> mandatoryAttributes;

				if (connInfo.isIBGPConnection())
					mandatoryAttributes = mandatoryIBGPAttributes;
				else
					mandatoryAttributes = mandatoryEBGPAttributes;

				for (Class<? extends Attribute> attrClass : mandatoryAttributes) {
					if (!givenAttributes.contains(attrClass)) {
						missingWellKnownList.add(attrClass);
					}
				}

				if (missingWellKnownList.size() > 0) {
					Map<Class<? extends Attribute>, Integer> codeMap;
					List<NotificationPacket> notifications = new LinkedList<NotificationPacket>();

					if(connInfo.isAS4OctetsInUse())
						codeMap = as4ClazzCodeMap;
					else
						codeMap = as2ClazzCodeMap;

					
					if(connInfo.isAS4OctetsInUse())
						codeMap = as4ClazzCodeMap;
					else
						codeMap = as2ClazzCodeMap;
						
					for(Class<? extends Attribute> attrClass : missingWellKnownList) {
						int code = codeMap.get(attrClass);
						
						log.info("detected missing well-known atribute, type " + code);
						notifications.add(new MissingWellKnownAttributeNotificationPacket(code));
					}
					
					NotificationHelper.sendNotificationsAndCloseChannel(ctx, notifications);
				} else {
					boolean haveBougsWidth = false;
					
					// check path attributes for AS number width (2 or 4) settings which mismatch the connection configuration
					for(Attribute attribute : update.getPathAttributes()) {
						if(attribute instanceof ASTypeAware) {
							if(((ASTypeAware)attribute).getAsType() != connInfo.getAsTypeInUse()) {
								haveBougsWidth = true;
							}
						}
					}
					
					if(haveBougsWidth) {
						NotificationHelper.sendNotificationAndCloseChannel(ctx, new MalformedAttributeListNotificationPacket());
					} else
						sentUpstream = true;
				}
			}
		} else
			sentUpstream = true;
		
		if(sentUpstream)
	        ctx.sendUpstream(e);
	}

	private byte[] serializeAttributes(List<Attribute> attrs) {
		int size = 0;
		
		for(Attribute attr : attrs)
			size += attr.calculateEncodedPathAttributeLength();
		
		ChannelBuffer buffer = ChannelBuffers.buffer(size);
		
		for(Attribute attr : attrs)
			buffer.writeBytes(attr.encodePathAttribute());
		
		byte[] b = new byte[size];
		
		buffer.readBytes(b);
		
		return b;
	}
}
