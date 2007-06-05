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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.java.mega.action.api.ResponseProvider;
import net.java.mega.action.error.ActionAlreadyInUseException;
import net.java.mega.action.error.ActionNotFound;
import net.java.mega.action.error.ConfigurationError;
import net.java.mega.action.model.Action;
import net.java.mega.action.model.ActionConfig;
import net.java.mega.action.model.ActionWrapper;
import net.java.mega.action.model.ControllerConfig;
import net.java.mega.action.model.ExceptionConfig;
import net.java.mega.action.model.WrapperChain;
import net.java.mega.action.output.Forward;
import net.java.mega.action.util.Constants;
import net.java.mega.action.util.MethodConstants;
import net.java.mega.common.model.ServletConfig;
import net.java.mega.common.util.ClassLoaderUtil;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;
import net.java.sjtools.thread.Lock;
import net.java.sjtools.util.TextUtil;

public class ActionManager {
	private static Log log = LogFactory.getLog(ActionManager.class);

	private static ActionManager mySelf = new ActionManager();

	private ControllerConfig controllerConfig = null;

	private Map forwardMap = null;

	private Lock forwardLock = null;

	private ServletConfig servletConfig = null;

	private ActionManager() {
		forwardMap = new HashMap();
		forwardLock = new Lock(forwardMap);
	}

	public static ActionManager getInstance() {
		return mySelf;
	}

	public void setControllerConfig(ControllerConfig controllerConfig) {
		this.controllerConfig = controllerConfig;
	}

	public void clean() {
		controllerConfig = null;

		forwardLock.getWriteLock();
		forwardMap.clear();
		forwardLock.releaseLock();

		servletConfig = null;
	}

	public RequestMetaData getRequestMetaData(String path, String doMethod) throws ActionNotFound, ConfigurationError,
			ActionAlreadyInUseException {
		RequestMetaData requestMetaData = new RequestMetaData();

		requestMetaData.setPath(path);
		requestMetaData.setActionConfig(getActionConfig(getActionPath(path)));
		requestMetaData.setMethodName(getMethod(path));
		requestMetaData.setDoMethod(doMethod);

		return requestMetaData;
	}

	public ActionConfig getActionConfig(String actionPath) throws ActionNotFound, ConfigurationError,
			ActionAlreadyInUseException {
		ActionConfig actionConfig = controllerConfig.getAction(actionPath);

		if (actionConfig == null) {
			actionConfig = createActionConfig(actionPath, null);
		}
		
		if (actionConfig.getWrapperChain() == null) {
			actionConfig.setWrapperChain(findWrapperChain(actionConfig.getName()));
		}

		return actionConfig;
	}

	public ActionConfig getActionConfig(Class clazz) throws ActionAlreadyInUseException, ConfigurationError,
			ActionNotFound {
		ActionConfig actionConfig = controllerConfig.getAction(clazz);

		if (actionConfig == null) {
			actionConfig = createActionConfig(getPathForClass(clazz), clazz);
		}

		return actionConfig;
	}

	private ActionConfig createActionConfig(String path, Class clazz) throws ActionNotFound, ConfigurationError,
			ActionAlreadyInUseException {
		ActionConfig actionConfig = new ActionConfig();

		actionConfig.setName(path);

		if (clazz == null) {
			String className = getClassName(path);

			try {
				actionConfig.setClazz(ClassLoaderUtil.getClass(className));
			} catch (ClassNotFoundException e) {
				log.error("Error loading class " + className, e);
				throw new ActionNotFound(path);
			}
		} else {
			actionConfig.setClazz(clazz);
		}

		try {
			controllerConfig.addAction(actionConfig);
		} catch (ActionAlreadyInUseException e) {
			log.error("Class " + actionConfig.getClazz().getName() + " already assign to a other action", e);
			throw e;
		}

		return actionConfig;
	}

	private String findWrapperChain(String path) {
		WrapperChain chain = null;
		
		for (Iterator i = controllerConfig.getWrapperChains().iterator(); i.hasNext();) {
			chain = (WrapperChain)i.next();
			
			if (chain.matches(path)) {
				return chain.getName();
			}
		}
		
		return Constants.DEFAULT_WRAPPER_CHAIN;
	}

	private String getPathForClass(Class clazz) throws ConfigurationError {
		String rootPackage = getRootPackage();

		StringBuffer buffer = new StringBuffer();

		buffer.append("/");

		String path = TextUtil.replace(clazz.getName().substring(rootPackage.length()), ".", "/");

		if (path.startsWith("/") && path.length() > 1) {
			path = path.substring(1);
		}

		buffer.append(path.toLowerCase());

		return buffer.toString();
	}

	private String getMethod(String path) {
		int pos = path.lastIndexOf(".");

		if ((pos > 0) && (pos != (path.length() - 1))) {
			return path.substring(pos + 1);
		}

		return MethodConstants.ON_LOAD;
	}

	private String getClassName(String path) throws ConfigurationError {
		String rootPackage = getRootPackage();

		StringBuffer buffer = new StringBuffer();

		buffer.append(rootPackage);

		if (!rootPackage.endsWith(".")) {
			buffer.append(".");
		}

		String requestPath = TextUtil.replace(path, "/", ".");

		if (requestPath.startsWith(".")) {
			requestPath = requestPath.substring(1);
		}

		int pos = requestPath.lastIndexOf(".");

		if (pos >= 0) {
			buffer.append(requestPath.substring(0, pos));
			buffer.append(".");

			requestPath = requestPath.substring(pos + 1);
		}

		buffer.append(requestPath.substring(0, 1).toUpperCase());

		if (requestPath.length() > 0) {
			buffer.append(requestPath.substring(1));
		}

		return buffer.toString();
	}

	public String getRootPackage() throws ConfigurationError {
		String rootPackage = controllerConfig.getProperty(Constants.ROOT_PACKAGE_PROPERTY);

		if (rootPackage == null) {
			log.error("Property " + Constants.ROOT_PACKAGE_PROPERTY + " not found");
			throw new ConfigurationError(Constants.ROOT_PACKAGE_PROPERTY);
		}

		return rootPackage;
	}

	private String getActionPath(String path) {
		String actionPath = path;

		int pos = actionPath.lastIndexOf(".");

		if (pos >= 0) {
			actionPath = actionPath.substring(0, pos);
		}

		return actionPath;
	}

	public ActionWrapper getActionWrapper(String wrapperChain) {
		WrapperChain chain = controllerConfig.getWrapperChain(wrapperChain);

		return chain.getActionWrapper();
	}

	public ResponseProvider getResponseProvider(Action action) throws ConfigurationError, ActionNotFound,
			ActionAlreadyInUseException {
		ResponseProvider provider = null;

		forwardLock.getReadLock();
		provider = (ResponseProvider) forwardMap.get(action.getClass().getName());
		forwardLock.releaseLock();

		if (provider == null) {
			ActionConfig actionConfig = getActionConfig(action.getClass());

			if (actionConfig.getForward() != null) {
				provider = actionConfig.getForward();
			}

			if (provider == null) {
				provider = createResponseProvider(action.getClass().getName());
			}

			if (provider != null) {
				forwardLock.getWriteLock();
				forwardMap.put(action.getClass().getName(), provider);
				forwardLock.releaseLock();
			}
		}

		return provider;
	}

	private ResponseProvider createResponseProvider(String className) throws ConfigurationError {
		if (log.isDebugEnabled()) {
			log.debug("createResponseProvider(" + className + ")");
		}

		String rootPackage = getRootPackage();
		String renderExtention = controllerConfig.getProperty(Constants.DEFAULT_OUTPUT_RENDER_EXTENTION_PROPERTY);
		String renderRoot = controllerConfig.getProperty(Constants.OUTPUT_RENDER_ROOT_PROPERTY);

		if (renderRoot == null) {
			renderRoot = Constants.OUTPUT_RENDER_ROOT_PROPERTY_VALUE;
		}

		StringBuffer buffer = new StringBuffer();

		if (!renderRoot.startsWith("/")) {
			buffer.append("/");
		}

		buffer.append(renderRoot);

		if (!renderRoot.endsWith("/")) {
			buffer.append("/");
		}

		String path = TextUtil.replace(className.substring(rootPackage.length()), ".", "/");

		if (path.startsWith("/") && path.length() > 1) {
			path = path.substring(1);
		}

		int pos = path.lastIndexOf("/");
		
		if (pos != -1) {
			buffer.append(path.substring(0, pos + 1));
			
			path = path.substring(pos + 1);
		}
		
		buffer.append(path.substring(0, 1).toLowerCase());
		buffer.append(path.substring(1));

		if (renderExtention == null) {
			buffer.append(Constants.DEFAULT_OUTPUT_RENDER_EXTENTION_PROPERTY_VALUE);
		} else {
			if (!renderExtention.startsWith(".")) {
				buffer.append(".");
			}

			buffer.append(renderExtention);
		}

		ResponseProvider provider = new Forward(buffer.toString());

		return provider;
	}

	public ResponseProvider getResponseProvider(Throwable e) {
		ExceptionConfig exceptionConfig = controllerConfig.getException(e.getClass().getName());

		if (exceptionConfig != null) {
			return exceptionConfig.getForward();
		}

		return null;
	}

	public void setServletConfig(ServletConfig servletConfig) {
		this.servletConfig = servletConfig;
	}

	public ServletConfig getServletConfig() {
		return servletConfig;
	}

	public List getBundleList() {
		return controllerConfig.getBundleList();
	}
}
