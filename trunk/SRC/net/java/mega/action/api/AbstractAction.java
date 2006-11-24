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

import net.java.mega.action.RequestProcessor;
import net.java.mega.action.api.scope.ApplicationScope;
import net.java.mega.action.api.scope.RequestScope;
import net.java.mega.action.api.scope.ResponseScope;
import net.java.mega.action.api.scope.SessionScope;
import net.java.mega.action.error.ActionException;
import net.java.mega.action.model.Action;
import net.java.mega.action.util.LocaleUtil;


public abstract class AbstractAction implements Action, Serializable{	
	private transient RequestProcessor requestProcessor = null;
	private Properties config = null;

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
	
	public void gotoAction(Class clazz) throws Exception {
		requestProcessor.gotoAction(clazz);
	}
	
	public void gotoAction(Action action) throws Exception {
		requestProcessor.gotoAction(action);
	}
	
	public Action getAction(Class clazz) throws ActionException  {
		return requestProcessor.getActionInstance(clazz);
	}	
	
	public void addMessage(Message message) {
		requestProcessor.addMessage(message);
	}
	
	public void addMessage(String key, Message message) {
		requestProcessor.addMessage(key, message);
	}
	
	public RequestScope getRequestScope() {
		return requestProcessor.getRequestScope();
	}	
	
	public ResponseScope getResponseScope() {
		return requestProcessor.getResponseScope();
	}
	
	public SessionScope getSessionScope() {
		return requestProcessor.getSessionScope();
	}		
	
	public ApplicationScope getApplicationScope() {
		return requestProcessor.getApplicationScope();
	}
	
	public Locale getLocate() {
		return LocaleUtil.getUserLocate(getRequestScope());
	}
	
	public void setLocate(Locale locale) {
		LocaleUtil.setUserLocate(getRequestScope(), locale);
	}
}
