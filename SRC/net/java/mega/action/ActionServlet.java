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
import net.java.mega.action.model.WorkflowControl;
import net.java.mega.action.util.Constants;
import net.java.mega.action.util.WorkflowControlUtil;
import net.java.mega.action.xml.ActionConfigReader;
import net.java.mega.common.http.RequestUtil;
import net.java.mega.common.http.ServletContextUtil;
import net.java.mega.common.load.ResponseWrapper;
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

	private void process(HttpServletRequest request, HttpServletResponse response, String doMethod) throws IOException,
			ServletException {
		ActionManager actionManager = ActionManager.getInstance();

		String path = RequestUtil.getAction(request);

		WorkflowControl workflowControl = WorkflowControlUtil.getWorkflowControl(request);

		synchronized (workflowControl) {
			if (workflowControl.isLock()) {
				workflowControl.setResponse(response);

				try {
					workflowControl.wait();
				} catch (InterruptedException e) {
					log.error("Runtime error", e);
				}

				return;
			}

			workflowControl.setLock(true);
		}

		try {
			ResponseProvider responseProvider = null;
			RequestMetaData requestMetaData = actionManager.getRequestMetaData(path, doMethod);
			ResponseMetaData responseMetaData = null;
			
			requestMetaData.setToken(WorkflowControlUtil.getCurrentToken(request));
			WorkflowControlUtil.generateToken(request);

			ActionWrapper actionWrapper = actionManager.getActionWrapper(requestMetaData.getActionConfig()
					.getWrapperChain());

			ResponseWrapper responseWrapper = new ResponseWrapper(response);

			try {
				responseMetaData = actionWrapper.execute(request, responseWrapper, requestMetaData);

				responseProvider = responseMetaData.getResponseProvider();
			} catch (Throwable e) {
				responseProvider = actionManager.getResponseProvider(e);

				if (responseProvider == null) {
					throw e;
				}
			}
			
			responseProvider.process(request, responseWrapper, requestMetaData, responseMetaData);

			if (responseMetaData.isSessionInvalidated()) {
				request.getSession(true).invalidate();
			}

			workflowControl = WorkflowControlUtil.getWorkflowControl(request);

			synchronized (workflowControl) {
				HttpServletResponse lastResponse = workflowControl.getResponse();

				if (lastResponse == null) {
					lastResponse = response;
				}

				responseWrapper.update(lastResponse);

				workflowControl.setResponse(null);
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
		} finally {
			workflowControl = WorkflowControlUtil.getWorkflowControl(request);

			synchronized (workflowControl) {
				workflowControl.setLock(false);
				workflowControl.notifyAll();
			}
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
