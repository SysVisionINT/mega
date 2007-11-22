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

package net.java.mega.action.api;

import java.io.Serializable;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.java.mega.action.ActionManager;
import net.java.mega.action.RequestProcessor;
import net.java.mega.action.error.WorkflowError;
import net.java.mega.action.model.Action;
import net.java.mega.action.util.ActionMessageUtil;
import net.java.mega.action.util.WorkflowControlUtil;
import net.java.mega.common.resource.LocaleUtil;
import net.java.sjtools.util.TextUtil;

public abstract class AbstractAction implements Action, Serializable {
	private transient RequestProcessor requestProcessor = null;
	private Properties config = null;
	
	public void workflowError() throws WorkflowError {
		throw new WorkflowError(this, getRequestToken());
	}

	public void setRequestProcessor(RequestProcessor rp) {
		requestProcessor = rp;
	}

	public void setProperties(Properties config) {
		this.config = config;
	}

	public String getProperty(String name) {
		return config.getProperty(name);
	}

	public abstract void onLoad();

	public void gotoAction(Class clazz) {
		requestProcessor.gotoAction(clazz);
	}

	public void gotoAction(Action action) {
		requestProcessor.gotoAction(action);
	}
	
	public void gotoAction(String path) {
		requestProcessor.gotoAction(path);
	}	

	public Action getAction(Class clazz) {
		return requestProcessor.getActionInstance(clazz);
	}

	public void addMessage(Message message) {
		requestProcessor.addMessage(message);
	}

	public void addMessage(String key, Message message) {
		requestProcessor.addMessage(key, message);
	}

	public HttpServletRequest getHttpServletRequest() {
		return requestProcessor.getHttpServletRequest();
	}

	public HttpServletResponse getHttpServletResponse() {
		return requestProcessor.getHttpServletResponse();
	}

	public HttpSession getHttpSession() {
		return requestProcessor.getHttpSession();
	}

	public ServletContext getServletContext() {
		return requestProcessor.getServletContext();
	}

	public Locale getLocale() {
		return LocaleUtil.getUserLocate(getHttpServletRequest());
	}

	public void setLocale(Locale locale) {
		LocaleUtil.setUserLocate(getHttpServletRequest(), locale);
	}
	
	public void invalidateSession() {
		requestProcessor.invalidateSession();
	}
	
	public boolean containsMessage(Locale locale, String key) {
		return !TextUtil.isEmptyString(ActionMessageUtil.getMessage(key, locale));
	}
	
	public String getRequestToken() {
		return WorkflowControlUtil.getUserToken(requestProcessor.getRequestMetaData().getParameters());
	}
	
	public String getNextRequestToken() {
		return WorkflowControlUtil.getCurrentToken(getHttpServletRequest());
	}
}
