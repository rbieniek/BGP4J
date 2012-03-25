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
 * File: org.bgp4j.management.web.service.PingServlet.java 
 */
package org.bgp4j.management.web.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class PingServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6012378202314428501L;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		StringBuilder builder = new StringBuilder();
		
		builder.append("{ \"Time\": ");
		builder.append(System.currentTimeMillis());
		builder.append(" }");
		
		resp.setContentLength(builder.length());
		resp.setContentType("application/text+json");
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.getWriter().write(builder.toString());
	}

}
