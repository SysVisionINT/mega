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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.java.mega.action.api.CustomResponseProvider;
import net.java.mega.action.api.Message;
import net.java.mega.action.api.SessionObject;
import net.java.mega.action.api.Validator;
import net.java.mega.action.error.ActionCreationException;
import net.java.mega.action.error.ConfigurationError;
import net.java.mega.action.error.MethodExecuteError;
import net.java.mega.action.error.PropertySetError;
import net.java.mega.action.model.Action;
import net.java.mega.action.model.ActionConfig;
import net.java.mega.action.util.CheckBoxUtil;
import net.java.mega.action.util.Constants;
import net.java.mega.action.util.MethodConstants;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;
import net.java.sjtools.util.BeanUtil;
import net.java.sjtools.util.TextUtil;

public class RequestProcessor {
	private static Log log = LogFactory.getLog(RequestProcessor.class);

	private HttpServletRequest request = null;

	private HttpServletResponse response = null;

	private Map actions = new HashMap();

	private ResponseMetaData currentResponse = new ResponseMetaData();

	private MessageContainer messageContainer = new MessageContainer();

	private boolean sessionInvalidated = false;

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

	public void invalidateSession() {
		sessionInvalidated = true;
	}

	public void addMessage(String key, Message message) {
		messageContainer.addMessage(key, message);
	}

	public HttpServletRequest getHttpServletRequest() {
		if (log.isDebugEnabled()) {
			log.debug("getHttpServletRequest()");
		}

		return (HttpServletRequest) request;
	}

	public HttpSession getHttpSession() {
		if (log.isDebugEnabled()) {
			log.debug("getHttpSession()");
		}

		return (HttpSession) getHttpServletRequest().getSession(true);
	}

	public HttpServletResponse getHttpServletResponse() {
		if (log.isDebugEnabled()) {
			log.debug("getHttpServletResponse()");
		}

		return response;
	}

	public ServletContext getServletContext() {
		if (log.isDebugEnabled()) {
			log.debug("getServletContext()");
		}

		return getHttpSession().getServletContext();
	}

	public void gotoAction(Class clazz) {
		if (log.isDebugEnabled()) {
			log.debug("gotoAction(" + clazz.getName() + ".class)");
		}

		gotoAction(getActionInstance(clazz));
	}

	public void gotoAction(Action action) {
		if (log.isDebugEnabled()) {
			log.debug("gotoAction(" + action.getClass().getName() + ")");
		}

		currentResponse.setAction(action);

		execute(action, MethodConstants.ON_LOAD, new Object[0]);
	}

	public Action getActionInstance(Class clazz) {
		if (log.isDebugEnabled()) {
			log.debug("getActionInstance(" + clazz.getName() + ".class)");
		}

		Action action = (Action) actions.get(clazz.getName());

		if (action == null) {
			try {
				action = (Action) getHttpSession().getAttribute(getContextName(clazz.getName()));
			} catch (ConfigurationError e) {
				log.error("Error while looking for session object " + clazz.getName(), e);
				throw new RuntimeException(e);
			}

			if (action != null) {
				action.setRequestProcessor(this);
			} else {
				action = (Action) getHttpSession().getAttribute(Constants.CURRENT_ACTION);

				if (action == null || !action.getClass().equals(clazz)) {
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
				} else {
					action.setRequestProcessor(this);
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

		currentResponse.setAction(action);

		if (requestMetaData.getDoMethod().equals(Constants.HTTP_POST)) {
			processPOST(requestMetaData, action);
		} else {
			processGET(requestMetaData, action);
		}

		if (sessionInvalidated) {
			getHttpSession().invalidate();
		}

		String contextName = null;

		for (Iterator i = actions.values().iterator(); i.hasNext();) {
			action = (Action) i.next();

			contextName = getContextName(action);

			if (!sessionInvalidated && action instanceof SessionObject) {
				getHttpSession().setAttribute(contextName, action);
			} else {
				getHttpServletRequest().setAttribute(contextName, action);
			}
		}

		if (!sessionInvalidated) {
			getHttpSession().setAttribute(Constants.CURRENT_ACTION, currentResponse.getAction());
		} else {
			getHttpServletRequest().setAttribute(Constants.CURRENT_ACTION, currentResponse.getAction());
		}

		currentResponse.setMessageContainer(messageContainer);
		getHttpServletRequest().setAttribute(Constants.MESSAGE_CONTAINER, messageContainer);

		if (currentResponse.getAction() instanceof CustomResponseProvider) {
			currentResponse.setResponseProvider(((CustomResponseProvider) currentResponse.getAction())
					.getResponseProvider());
		} else {
			currentResponse.setResponseProvider(ActionManager.getInstance().getResponseProvider(
					currentResponse.getAction()));
		}

		return currentResponse;
	}

	private void processGET(RequestMetaData requestMetaData, Action action) {
		if (log.isDebugEnabled()) {
			log.debug("processGET(...)");
		}

		List attributeList = new ArrayList();
		String attributeName = null;

		for (Enumeration e = getHttpServletRequest().getParameterNames(); e.hasMoreElements();) {
			attributeName = (String) e.nextElement();

			if (attributeName.startsWith(Constants.GET_ARG)) {
				attributeList.add(attributeName);
			}
		}

		Collections.sort(attributeList);

		String[] parameters = new String[attributeList.size()];
		int index = 0;

		for (Iterator i = attributeList.iterator(); i.hasNext();) {
			attributeName = (String) i.next();

			parameters[index] = getHttpServletRequest().getParameter(attributeName);
		}

		Object[] args = convertArgs(parameters, action, requestMetaData.getMethodName());

		execute(action, requestMetaData.getMethodName(), args);
	}

	private Object[] convertArgs(String[] parameters, Action action, String methodName) {
		Object[] ret = new Object[parameters.length];

		BeanUtil beanUtil = new BeanUtil(action);

		List methods = beanUtil.getMethods(methodName);

		if (methods.size() == 1) {
			if (((Method) methods.get(0)).getParameterTypes().length == 0) {
				ret = new Object[0];
			} else {
				if (parameters.length != ((Method) methods.get(0)).getParameterTypes().length) {
					log.error("Wrong number of arguments for method " + methodName + " of class "
							+ action.getClass().getName());
					throw new MethodExecuteError(action.getClass().getName(), methodName);
				}

				for (int i = 0; i < parameters.length; i++) {
					if (TextUtil.isEmptyString(parameters[i])) {
						ret[i] = null;
					} else {
						ret[i] = convertType(parameters[i], ((Method) methods.get(0)).getParameterTypes()[i]);
					}
				}
			}
		} else {
			ret = parameters;
		}

		return ret;
	}

	private void processPOST(RequestMetaData requestMetaData, Action action) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("processPOST(...)");
		}

		String attributeName = null;
		String attributeValue = null;

		Map parameters = getHttpServletRequest().getParameterMap();

		for (Iterator i = parameters.keySet().iterator(); i.hasNext();) {
			attributeName = (String) i.next();
			attributeValue = attributeName;

			if (CheckBoxUtil.isHiddenCheckBox(attributeName)) {
				attributeName = CheckBoxUtil.getPropertyNameFromHidden(attributeName);

				if (parameters.get(attributeName) != null) {
					continue;
				}
			}

			setProperty(action, attributeName, (String[]) parameters.get(attributeValue));
		}

		boolean valid = true;

		if (!requestMetaData.getMethodName().equals(MethodConstants.ON_LOAD)) {
			if (action instanceof Validator) {
				valid = ((Validator) action).isInputValid(requestMetaData.getMethodName());
			}
		}

		if (valid) {
			execute(action, requestMetaData.getMethodName(), new Object[0]);
		}
	}

	private String getContextName(Object obj) throws ConfigurationError {
		return getContextName(obj.getClass().getName());
	}

	private String getContextName(String name) throws ConfigurationError {
		String rootPackage = ActionManager.getInstance().getRootPackage();

		String newName = name.substring(rootPackage.length());

		newName = newName.replace('.', '/');

		if (!newName.startsWith("/")) {
			newName = "/".concat(newName);
		}

		return newName;
	}

	private void setProperty(Action action, String name, String[] parameterValue) throws PropertySetError {
		if (log.isDebugEnabled()) {
			log.debug("setProperty(" + action.getClass().getName() + ", " + name + ", ["
					+ TextUtil.toString(parameterValue) + "])");
		}

		BeanUtil beanUtil = new BeanUtil(action);

		List methods = beanUtil.getMethods(beanUtil.getMethodName("set", name));

		Object value = parameterValue;

		if (parameterValue != null && parameterValue.length == 1) {
			value = parameterValue[0];
		}

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
				if (value.getClass().isArray()) {
					String[] values = (String[]) value;
					Collection collection = new ArrayList();

					for (int i = 0; i < values.length; i++) {
						collection.add(values[i]);
					}

					try {
						beanUtil.set(name, collection);
					} catch (Exception e) {
						log.error("Error trying to set property " + name + " with the value " + collection
								+ " of class " + action.getClass().getName(), e);
						throw new PropertySetError(name, collection);
					}
				} else {
					if (TextUtil.isEmptyString((String) value)) {
						try {
							beanUtil.set(name, null);
						} catch (Exception e) {
							log.error("Error trying to set property " + name + " with the value NULL of class "
									+ action.getClass().getName(), e);
							throw new PropertySetError(name, null);
						}
					} else {
						Class clazz = ((Method) methods.get(0)).getParameterTypes()[0];
						Object object = convertType((String) value, clazz);

						try {
							beanUtil.set(name, object);
						} catch (Exception e) {
							log.error("Error trying to set property " + name + " with the value " + parameterValue
									+ " of class " + action.getClass().getName(), e);
							throw new PropertySetError(name, parameterValue);
						}
					}
				}
			}
		}
	}

	private Object convertType(String strValue, Class clazz) {
		Object object = strValue;

		if (clazz.equals(int.class) || clazz.equals(Integer.class)) {
			object = Integer.valueOf(strValue);
		} else if (clazz.equals(boolean.class) || clazz.equals(Boolean.class)) {
			object = Boolean.valueOf(strValue);
		} else if (clazz.equals(char.class)) {
			object = new Character(strValue.charAt(0));
		} else if (clazz.equals(byte.class) || clazz.equals(Byte.class)) {
			object = Byte.valueOf(strValue);
		} else if (clazz.equals(short.class) || clazz.equals(Short.class)) {
			object = Short.valueOf(strValue);
		} else if (clazz.equals(long.class) || clazz.equals(Long.class)) {
			object = Long.valueOf(strValue);
		} else if (clazz.equals(float.class) || clazz.equals(Float.class)) {
			object = Float.valueOf(strValue);
		} else if (clazz.equals(double.class) || clazz.equals(Double.class)) {
			object = Double.valueOf(strValue);
		}

		return object;
	}

	private void execute(Action action, String methodName, Object[] parameters) {
		if (log.isDebugEnabled()) {
			log.debug("execute(" + action.getClass().getName() + ", " + methodName + ", ["
					+ TextUtil.toString(parameters) + "])");
		}

		if (methodName.equals(MethodConstants.ON_LOAD)) {
			action.onLoad();
		} else {
			BeanUtil beanUtil = new BeanUtil(action);

			try {
				beanUtil.invokeMethod(methodName, parameters);
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
				throw new MethodExecuteError(e);
			}
		}
	}
}
