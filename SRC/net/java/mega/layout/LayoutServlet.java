/*  ------------------
 *  MEGA Web Framework
 *  ------------------
 *  
 *  Copyright 2006 SysVision - Consultadoria e Desenvolvimento em Sistemas de Informática, Lda.
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package net.java.mega.layout;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.java.mega.common.util.RequestUtil;
import net.java.mega.common.util.ServletContextUtil;
import net.java.mega.layout.model.Layout;
import net.java.mega.layout.util.Constant;
import net.java.mega.layout.util.LayoutUtil;
import net.java.mega.layout.xml.LayoutConfigReader;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public class LayoutServlet extends HttpServlet {
	private static final long serialVersionUID = 5569343447487377887L;

	private static Log log = LogFactory.getLog(LayoutServlet.class);

	private Layout layout = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if (log.isDebugEnabled()) {
			log.debug("GET from user " + request.getRemoteUser() + " on " + request.getRemoteAddr());
		}

		process(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if (log.isDebugEnabled()) {
			log.debug("POST from user " + request.getRemoteUser() + " on " + request.getRemoteAddr());
		}

		process(request, response);
	}

	private void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String pageName = RequestUtil.getPath(request);

		String layoutLocation = LayoutUtil.getLayoutLocation(layout, pageName);

		if (layoutLocation == null) {
			log.error("Page " + pageName + " not found");

			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} else {
			request.setAttribute(Constant.PAGE_NAME, pageName);

			RequestDispatcher dispatcher = request.getRequestDispatcher(layoutLocation);

			if (dispatcher == null) {
				log.error("LayoutServlet can't find url '" + layoutLocation + "'");

				throw new ServletException("LayoutServlet can't find url '" + layoutLocation + "'.");
			}

			dispatcher.forward(request, response);
		}
	}

	public void init(ServletConfig config) throws ServletException {
		if (log.isDebugEnabled()) {
			log.debug("init(...)");
		}

		String configFileName = config.getInitParameter(Constant.CONFIG_FILE);

		if (configFileName == null) {
			log.fatal("Parameter " + Constant.CONFIG_FILE + " not found");
			new ServletException("Parameter " + Constant.CONFIG_FILE + " not found");
		} else {
			if (log.isDebugEnabled()) {
				log.debug(Constant.CONFIG_FILE + " = " + configFileName);
			}
		}

		try {
			layout = LayoutConfigReader.getLayoutConfig(ServletContextUtil.getResourceInputStream(config
					.getServletContext(), configFileName));
		} catch (Exception e) {
			log.fatal("Error reading " + configFileName, e);
			new ServletException("Error reading " + configFileName, e);
		}

		config.getServletContext().setAttribute(Constant.LAYOUT, layout);
	}

	public void destroy() {
		if (log.isDebugEnabled()) {
			log.debug("destroy()");
		}

		layout = null;
	}
}
