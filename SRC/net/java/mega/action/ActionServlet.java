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
package net.java.mega.action;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.java.mega.action.api.ResponseProvider;
import net.java.mega.action.error.ActionException;
import net.java.mega.action.error.ActionNotFound;
import net.java.mega.action.error.WorkflowError;
import net.java.mega.action.model.ActionWrapper;
import net.java.mega.action.model.ControllerConfig;
import net.java.mega.action.util.ActionRequestUtil;
import net.java.mega.action.util.ActionRequestWrapper;
import net.java.mega.action.util.Constants;
import net.java.mega.action.util.WorkflowControlUtil;
import net.java.mega.action.xml.ActionConfigReader;
import net.java.mega.common.http.ServletContextUtil;
import net.java.mega.common.xml.ServletConfigReader;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public class ActionServlet extends HttpServlet {
	private static final long serialVersionUID = 5569343447487377887L;

	private static Log log = LogFactory.getLog(ActionServlet.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if (log.isDebugEnabled()) {
			log.debug("GET from user " + request.getRemoteUser() + " on " + request.getRemoteAddr());
		}

		process(request, response, Constants.HTTP_GET);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if (log.isDebugEnabled()) {
			log.debug("POST from user " + request.getRemoteUser() + " on " + request.getRemoteAddr());
		}

		process(request, response, Constants.HTTP_POST);
	}

	private void process(HttpServletRequest servletRequest, HttpServletResponse response, String doMethod) throws IOException,
			ServletException {
		ActionManager actionManager = ActionManager.getInstance();

		try {
			ActionRequestWrapper request = new ActionRequestWrapper(servletRequest);
			String path = ActionRequestUtil.getAction(request);
			boolean sameRequest = WorkflowControlUtil.isTheSameRequest(request);
			
			ResponseProvider responseProvider = null;
			RequestMetaData requestMetaData = actionManager.getRequestMetaData(path, doMethod);
			ResponseMetaData responseMetaData = null;

			if (!sameRequest) {
				requestMetaData.setToken(WorkflowControlUtil.getCurrentToken(request));
				WorkflowControlUtil.generateToken(request);
			}

			ActionWrapper actionWrapper = actionManager.getActionWrapper(requestMetaData.getActionConfig()
					.getWrapperChain());

			try {
				responseMetaData = actionWrapper.execute(request, response, requestMetaData);

				responseProvider = responseMetaData.getResponseProvider();
			} catch (Throwable e) {
				responseProvider = actionManager.getResponseProvider(e);

				if (responseProvider == null) {
					throw e;
				} else {
					if (log.isDebugEnabled()) {
						log.debug("The ResponseProvider for the Throwable " + e.getClass().getName() + " was found");
					}
				}
			}

			responseProvider.process(servletRequest, response, requestMetaData, responseMetaData);

			if (responseMetaData != null && responseMetaData.isSessionInvalidated()) {
				servletRequest.getSession(true).invalidate();
			}
		} catch (ActionNotFound e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} catch (WorkflowError e) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
		} catch (ActionException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} catch (Throwable e) {
			log.error("Runtime error", e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	public void init(ServletConfig config) throws ServletException {
		if (log.isDebugEnabled()) {
			log.debug("init(...)");
		}

		ServletContextUtil wrl = new ServletContextUtil(config.getServletContext());

		String configFile = config.getInitParameter(Constants.ACTION_CONFIG_PARAMETER);

		if (configFile != null) {
			try {
				ControllerConfig controllerConfig = ActionConfigReader.getControllerConfig(wrl
						.getResourceInputStream(configFile));

				ActionManager.getInstance().setControllerConfig(controllerConfig);
			} catch (Exception e) {
				log.error("Error while reading " + configFile, e);
				throw new ServletException(e);
			}
		} else {
			log.error("InitParameter " + Constants.ACTION_CONFIG_PARAMETER + " not defined!");
			throw new ServletException("InitParameter " + Constants.ACTION_CONFIG_PARAMETER + " not defined!");
		}

		try {
			ActionManager.getInstance().setServletConfig(
					ServletConfigReader.getServletConfig(config.getServletName(), wrl
							.getResourceInputStream(Constants.WEB_XML)));
		} catch (Exception e) {
			log.error("Error while reading " + Constants.WEB_XML, e);
			throw new ServletException(e);
		}
	}

	public void destroy() {
		if (log.isDebugEnabled()) {
			log.debug("destroy()");
		}

		ActionManager.getInstance().clean();
	}
}
