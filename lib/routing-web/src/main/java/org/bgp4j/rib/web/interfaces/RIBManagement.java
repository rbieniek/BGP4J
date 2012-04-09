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
 * File: org.bgp4j.rib.web.interfaces.RIBManagement.java 
 */
package org.bgp4j.rib.web.interfaces;

import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.RIBSide;
import org.bgp4j.net.SubsequentAddressFamily;
import org.bgp4j.rib.web.dto.RIBCollection;
import org.bgp4j.rib.web.dto.RouteCollection;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
@Path("/rib")
public interface RIBManagement {

	@GET
	@Path("/list")
	@Produces("application/*+json")
	public RIBCollection ribs();
	
	@GET
	@Path("/routes-by-peer/{peer}/{side}/{afi}/{safi}")
	@Produces("application/*+json")
	public RouteCollection routes(@PathParam("peer") String peer, 
			@PathParam("side") RIBSide side, 
			@PathParam("afi") AddressFamily afi, 
			@PathParam("safi") SubsequentAddressFamily safi);

	
	@GET
	@Path("/routes-by-uuid/{uuid}")
	@Produces("application/*+json")
	public RouteCollection routes(@PathParam("uuid") UUID uuid);
}
