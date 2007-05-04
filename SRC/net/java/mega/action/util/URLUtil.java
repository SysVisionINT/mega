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
package net.java.mega.action.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.java.mega.action.ActionManager;
import net.java.mega.action.error.ActionException;
import net.java.mega.action.model.Action;
import net.java.mega.action.model.ActionConfig;
import net.java.mega.common.http.WARContextUtil;
import net.java.mega.common.model.ServletMapping;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public class URLUtil {
	private static Log log = LogFactory.getLog(URLUtil.class);

	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	private ServletMapping servletMapping = null;
	private String servletPath = null;
	private String servletExtention = null;

	public URLUtil(HttpServletRequest request, HttpServletResponse response) {
		if (log.isDebugEnabled()) {
			log.debug("new URLUtil(...)");
		}

		this.request = request;
		this.response = response;
	}

	private String getActionURLPart(String actionName) {
		if (log.isDebugEnabled()) {
			log.debug("getActionURLPart(" + actionName + ")");
		}

		StringBuffer buffer = new StringBuffer();

		buffer.append(getContextPath());

		String path = getPath();

		if (path != null) {
			buffer.append(path);
		}

		buffer.append(actionName);

		return buffer.toString();
	}

	private String getContextPath() {
		String contextPath = request.getContextPath();

		if (contextPath.length() > 1) {
			return contextPath;
		} else {
			return "";
		}
	}

	public String getFileURL(String fileName) {
		if (log.isDebugEnabled()) {
			log.debug("getFileURL(" + fileName + ")");
		}

		StringBuffer buffer = new StringBuffer();

		buffer.append(getContextPath());

		if (!fileName.startsWith("/")) {
			buffer.append("/");
		}

		buffer.append(fileName);

		return buffer.toString();
	}

	public String getActionURL(String actionName) throws ActionException {
		if (log.isDebugEnabled()) {
			log.debug("getActionURL(" + actionName + ")");
		}

		StringBuffer buffer = new StringBuffer();

		buffer.append(getActionURLPart(getActionName(actionName)));

		String extention = getExtention();

		if (extention != null) {
			buffer.append(extention);
		}

		return response.encodeURL(buffer.toString());
	}

	private String getExtention() {
		if (log.isDebugEnabled()) {
			log.debug("getExtention()");
		}

		if (servletExtention == null && servletMapping == null) {
			processServletMapping();
		}

		return servletExtention;
	}

	private synchronized void processServletMapping() {
		servletMapping = (ServletMapping) ActionManager.getInstance().getServletConfig().getMapping().get(0);

		String url = servletMapping.getUrl();
		int posDot = url.lastIndexOf(".");
		int posRoot = url.lastIndexOf("/");
		int posMask = url.lastIndexOf("*");

		if (posMask > posRoot && posMask >= 0 && posRoot >= 0) {
			servletPath = url.substring(0, posRoot);
		}

		if (posDot > posMask && posMask >= 0 && posDot >= 0) {
			servletExtention = url.substring(posDot);
		}
	}

	private String getPath() {
		if (log.isDebugEnabled()) {
			log.debug("getPath()");
		}

		if (servletPath == null && servletMapping == null) {
			processServletMapping();
		}

		return servletPath;
	}

	public String getMethodURL(String actionName, String methodName) throws ActionException {
		if (log.isDebugEnabled()) {
			log.debug("getMethodURL(" + actionName + ", " + methodName + ")");
		}
		
		if (methodName == null) {
			return getActionURL(actionName);
		}

		StringBuffer buffer = new StringBuffer();

		buffer.append(getActionURLPart(getActionName(actionName)));
		buffer.append(".");
		buffer.append(methodName);

		String extention = getExtention();

		if (extention != null) {
			buffer.append(extention);
		}

		return response.encodeURL(buffer.toString());
	}
	
	public String getRAWMethodName(String actionName, String methodName) throws ActionException {
		if (log.isDebugEnabled()) {
			log.debug("getRAWMethodName(" + actionName + ", " + methodName + ")");
		}
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(getActionName(actionName));
		
		if (methodName != null) {
			buffer.append(".");
			buffer.append(methodName);
		}
		
		return buffer.toString();
	}

	private String getActionName(String actionName) throws ActionException {
		String action = actionName;
		
		if (action == null) {
			Action actionObject = (Action) WARContextUtil.getObject(request, Constants.CURRENT_ACTION);
			ActionConfig actionConfig = ActionManager.getInstance().getActionConfig(actionObject.getClass());
			action = actionConfig.getName();
		}
		
		return action;
	}
}
