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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.java.mega.action.api.CustomResponseProvider;
import net.java.mega.action.api.Message;
import net.java.mega.action.api.SessionObject;
import net.java.mega.action.api.Validator;
import net.java.mega.action.api.scope.ApplicationScope;
import net.java.mega.action.api.scope.RequestScope;
import net.java.mega.action.api.scope.ResponseScope;
import net.java.mega.action.api.scope.SessionScope;
import net.java.mega.action.error.ActionCreationException;
import net.java.mega.action.error.MethodExecuteError;
import net.java.mega.action.error.PropertySetError;
import net.java.mega.action.model.Action;
import net.java.mega.action.model.ActionConfig;
import net.java.mega.action.util.MethodConstants;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;
import net.java.sjtools.util.BeanUtil;

public class RequestProcessor {
	private static Log log = LogFactory.getLog(RequestProcessor.class);

	private HttpServletRequest request = null;
	private HttpServletResponse response = null;

	private Map actions = new HashMap();
	private ResponseMetaData currentResponse = new ResponseMetaData();
	private MessageContainer messageContainer = new MessageContainer();

	public RequestProcessor(HttpServletRequest request, HttpServletResponse response) {
		if (log.isDebugEnabled()) {
			log.debug("new RequestProcessor(...)");
		}

		this.request = request;
		this.response = response;
	}

	public void addMessage(Message message) {
		messageContainer.addMessage(message);
	}

	public void addMessage(String key, Message message) {
		messageContainer.addMessage(key, message);
	}

	public RequestScope getRequestScope() {
		if (log.isDebugEnabled()) {
			log.debug("getRequestScope()");
		}

		return (RequestScope) request;
	}

	public SessionScope getSessionScope() {
		if (log.isDebugEnabled()) {
			log.debug("getSessionScope()");
		}

		return (SessionScope) getRequestScope().getSession(true);
	}

	public ResponseScope getResponseScope() {
		if (log.isDebugEnabled()) {
			log.debug("getResponseScope()");
		}

		return (ResponseScope) response;
	}
	
	public ApplicationScope getApplicationScope() {
		if (log.isDebugEnabled()) {
			log.debug("getApplicationScope()");
		}

		return (ApplicationScope) getSessionScope().getServletContext();
	}	

	public void gotoAction(Class clazz) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("gotoAction(" + clazz.getName() + ".class)");
		}

		gotoAction(getActionInstance(clazz));
	}

	public void gotoAction(Action action) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("gotoAction(" + action.getClass().getName() + ")");
		}

		currentResponse.setAction(action);

		execute(action, MethodConstants.ON_LOAD);
	}

	public Action getActionInstance(Class clazz) throws ActionCreationException {
		if (log.isDebugEnabled()) {
			log.debug("getActionInstance(" + clazz.getName() + ".class)");
		}

		Action action = (Action) actions.get(clazz.getName());

		if (action == null) {
			action = (Action) getSessionScope().getAttribute(clazz.getName());

			if (action != null) {
				action.setRequestProcessor(this);
			} else {
				try {
					action = (Action) clazz.newInstance();
					action.setRequestProcessor(this);

					ActionConfig actionConfig = ActionManager.getInstance().getActionConfig(clazz);

					if (actionConfig != null) {
						action.setProperties(actionConfig.getProperties());
					}
				} catch (Exception e) {
					log.error("Error while creating instance of " + clazz.getName(), e);
					throw new ActionCreationException(clazz);
				}
			}

			actions.put(clazz.getName(), action);
		}

		return action;
	}

	public ResponseMetaData process(RequestMetaData requestMetaData) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("process(...)");
		}

		Action action = getActionInstance(requestMetaData.getActionConfig().getClazz());

		String attributeName = null;

		for (Enumeration e = getRequestScope().getParameterNames(); e.hasMoreElements();) {
			attributeName = (String) e.nextElement();

			setProperty(action, attributeName, getRequestScope().getParameter(attributeName));
		}

		boolean valid = true;

		if (action instanceof Validator) {
			valid = ((Validator) action).isInputValid();
		}

		currentResponse.setAction(action);

		if (valid) {
			execute(action, requestMetaData.getMethodName());
		}

		for (Iterator i = actions.values().iterator(); i.hasNext();) {
			action = (Action) i.next();

			if (action instanceof SessionObject) {
				getSessionScope().setAttribute(action.getClass().getName(), action);
			} else {
				getRequestScope().setAttribute(action.getClass().getName(), action);
			}
		}

		currentResponse.setMessageContainer(messageContainer);

		if (currentResponse.getAction() instanceof CustomResponseProvider) {
			currentResponse.setResponseProvider(((CustomResponseProvider) currentResponse.getAction())
					.getResponseProvider());
		} else {
			currentResponse.setResponseProvider(ActionManager.getInstance().getResponseProvider(
					currentResponse.getAction()));
		}

		return currentResponse;
	}

	private void setProperty(Action action, String name, String value) throws PropertySetError {
		if (log.isDebugEnabled()) {
			log.debug("setProperty(" + action.getClass().getName() + ", " + name + ", " + value + ")");
		}

		BeanUtil beanUtil = new BeanUtil(action);

		List methods = beanUtil.getMethods(beanUtil.getMethodName("set", name));

		if (!methods.isEmpty()) {
			if (methods.size() > 1) {
				try {
					beanUtil.set(name, value);
				} catch (Exception e) {
					log.error("Error trying to set property " + name + " with the value " + value + " of class "
							+ action.getClass().getName(), e);
					throw new PropertySetError(name, value);
				}
			} else {
				Class clazz = ((Method) methods.get(0)).getParameterTypes()[0];
				Object object = value;

				if (clazz.isPrimitive()) {
					if (clazz.equals(int.class)) {
						object = Integer.valueOf(value);
					} else if (clazz.equals(boolean.class)) {
						object = Boolean.valueOf(value);
					} else if (clazz.equals(char.class)) {
						object = new Character(value.charAt(0));
					} else if (clazz.equals(byte.class)) {
						object = Byte.valueOf(value);
					} else if (clazz.equals(short.class)) {
						object = Short.valueOf(value);
					} else if (clazz.equals(long.class)) {
						object = Long.valueOf(value);
					} else if (clazz.equals(float.class)) {
						object = Float.valueOf(value);
					} else if (clazz.equals(double.class)) {
						object = Double.valueOf(value);
					}
				}

				try {
					beanUtil.set(name, object);
				} catch (Exception e) {
					log.error("Error trying to set property " + name + " with the value " + value + " of class "
							+ action.getClass().getName(), e);
					throw new PropertySetError(name, value);
				}
			}
		}
	}

	private void execute(Action action, String methodName) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("execute(" + action.getClass().getName() + ", " + methodName + ")");
		}

		if (methodName.equals(MethodConstants.ON_LOAD)) {
			action.onLoad();
		} else {
			BeanUtil beanUtil = new BeanUtil(action);

			try {
				beanUtil.invokeMethod(methodName, new Object[0]);
			} catch (SecurityException e) {
				log.error("Error trying to execute method " + methodName + " of class " + action.getClass().getName(),
						e);
				throw new MethodExecuteError(action.getClass().getName(), methodName);
			} catch (IllegalArgumentException e) {
				log.error("Error trying to execute method " + methodName + " of class " + action.getClass().getName(),
						e);
				throw new MethodExecuteError(action.getClass().getName(), methodName);
			} catch (NoSuchMethodException e) {
				log.error("Error trying to execute method " + methodName + " of class " + action.getClass().getName(),
						e);
				throw new MethodExecuteError(action.getClass().getName(), methodName);
			} catch (IllegalAccessException e) {
				log.error("Error trying to execute method " + methodName + " of class " + action.getClass().getName(),
						e);
				throw new MethodExecuteError(action.getClass().getName(), methodName);
			} catch (InvocationTargetException e) {
				log.error("Error trying to execute method " + methodName + " of class " + action.getClass().getName(),
						e);
				throw new MethodExecuteError(action.getClass().getName(), methodName);
			} catch (Exception e) {
				log.error("Error trying to execute method " + methodName + " of class " + action.getClass().getName(),
						e);
				throw e;
			}
		}
	}
}
