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
package net.java.mega.action.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.java.mega.action.error.ActionAlreadyInUseException;
import net.java.sjtools.thread.Lock;

public class ControllerConfig implements Serializable {
	private static final long serialVersionUID = -4751301817676850672L;

	private Properties config = null;

	private List resources = null;

	private Map chains = null;

	private Map errors = null;

	private Map actionsByPath = null;

	private Map actionsByName = null;

	private Lock lock = null;

	public ControllerConfig() {
		config = new Properties();
		resources = new ArrayList();
		chains = new HashMap();
		errors = new HashMap();

		actionsByPath = new HashMap();
		actionsByName = new HashMap();
		lock = new Lock(actionsByPath);
	}

	public void addProperty(String name, String value) {
		config.setProperty(name, value);
	}

	public String getProperty(String name) {
		return config.getProperty(name);
	}

	public void addBundle(String bundle) {
		resources.add(bundle);
	}

	public List getBundleList() {
		return resources;
	}

	public void addWrapperChain(WrapperChain pac) {
		chains.put(pac.getName(), pac);
	}

	public void addException(ExceptionConfig exceptionConfig) {
		errors.put(exceptionConfig.getClassName(), exceptionConfig);
	}

	public ExceptionConfig getException(String name) {
		return (ExceptionConfig) errors.get(name);
	}

	public WrapperChain getWrapperChain(String name) {
		return (WrapperChain) chains.get(name);
	}
	
	public Collection getWrapperChains() {
		return chains.values();
	}

	public void addAction(ActionConfig action) throws ActionAlreadyInUseException {
		ActionConfig other = getAction(action.getClazz());

		if (other == null) {
			lock.getWriteLock();
			actionsByPath.put(action.getName(), action);
			actionsByName.put(action.getClazz().getName(), action);
			lock.releaseLock();
		} else {
			throw new ActionAlreadyInUseException(action.getClazz());
		}
	}

	public ActionConfig getAction(String name) {
		lock.getReadLock();
		ActionConfig actionConfig = (ActionConfig) actionsByPath.get(name);
		lock.releaseLock();

		return actionConfig;
	}

	public ActionConfig getAction(Class clazz) {
		lock.getReadLock();
		ActionConfig actionConfig = (ActionConfig) actionsByName.get(clazz.getName());
		lock.releaseLock();

		return actionConfig;
	}

	public Map getActionsByName() {
		return actionsByName;
	}

	public void setActionsByName(Map actionsByName) {
		this.actionsByName = actionsByName;
	}
}
