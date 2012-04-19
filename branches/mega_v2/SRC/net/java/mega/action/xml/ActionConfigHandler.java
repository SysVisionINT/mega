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
package net.java.mega.action.xml;

import net.java.mega.action.error.ActionAlreadyInUseException;
import net.java.mega.action.model.ActionConfig;
import net.java.mega.action.model.ActionWrapper;
import net.java.mega.action.model.ControllerConfig;
import net.java.mega.action.model.ExceptionConfig;
import net.java.mega.action.model.WrapperChain;
import net.java.mega.action.output.Forward;
import net.java.mega.action.util.Constants;
import net.java.mega.common.util.ClassLoaderUtil;
import net.java.sjtools.logging.plus.RLog;
import net.java.sjtools.util.TextUtil;
import net.java.sjtools.xml.SimpleHandler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


public class ActionConfigHandler extends SimpleHandler {

	public Object proccessElement(String elementType, Object currentObject, Attributes attributes) throws SAXException {
		if (elementType.equals("action-config")) {
			ControllerConfig config =  new ControllerConfig();
			
			config.addWrapperChain(getDefaultWrapperChain());
			
			return config;
		} else if (elementType.equals("property")) {
			String name = attributes.getValue("name");
			String value = attributes.getValue("value");

			if (currentObject instanceof ControllerConfig) {
				ControllerConfig obj = (ControllerConfig) currentObject;

				obj.addProperty(name, value);
			} else if (currentObject instanceof ActionWrapper) {
				ActionWrapper obj = (ActionWrapper) currentObject;

				obj.addProperty(name, value);
			} else if (currentObject instanceof ActionConfig) {
				ActionConfig obj = (ActionConfig) currentObject;

				obj.addProperty(name, value);
			}

			if (RLog.isTraceEnabled()) {
				RLog.trace(" - property " + name + " = " + value);
			}

			return null;
		} else if (elementType.equals("wrapper-chain")) {
			ControllerConfig config = (ControllerConfig) currentObject;

			WrapperChain ret = new WrapperChain();

			ret.setName(attributes.getValue("name"));

			config.addWrapperChain(ret);

			if (RLog.isTraceEnabled()) {
				RLog.trace(" - Wrapper chain " + ret.getName());
			}

			return ret;
		} else if (elementType.equals("default-wrapper-chain")) {
			ControllerConfig config = (ControllerConfig) currentObject;

			WrapperChain ret = getDefaultWrapperChain();

			config.addWrapperChain(ret);

			if (RLog.isTraceEnabled()) {
				RLog.trace(" - Default wrapper chain");
			}

			return ret;
		} else if (elementType.equals("action-wrapper")) {
			String className = attributes.getValue("class-name");

			ActionWrapper ret = null;

			try {
				ret = (ActionWrapper) ClassLoaderUtil.createInstance(className);
			} catch (Exception e) {
				RLog.error("Error creating instance of " + className, e);
				throw new SAXException("Error creating instance of " + className, e);
			}

			if (currentObject instanceof WrapperChain) {
				WrapperChain pack = (WrapperChain) currentObject;

				pack.setActionWrapper(ret);
			} else if (currentObject instanceof ActionWrapper) {
				ActionWrapper father = (ActionWrapper) currentObject;

				father.setNext(ret);
			}

			if (RLog.isTraceEnabled()) {
				RLog.trace(" - ActionWrapper(" + className + ")");
			}

			return ret;
		} else if (elementType.equals("action")) {
			ControllerConfig config = (ControllerConfig) currentObject;

			ActionConfig ret = new ActionConfig();

			ret.setName(attributes.getValue("name"));
			String className = attributes.getValue("class-name");

			try {
				ret.setClazz(ClassLoaderUtil.getClass(className));
			} catch (ClassNotFoundException e) {
				RLog.error("Error loading class " + className, e);
				throw new SAXException("Error loading class " + className, e);
			}

			String forward = attributes.getValue("forward");

			if (!TextUtil.isEmptyString(forward)) {
				ret.setForward(new Forward(forward));
			}

			if (attributes.getValue("wrapper-chain") != null) {
				ret.setWrapperChain(attributes.getValue("wrapper-chain"));
			}

			try {
				config.addAction(ret);
			} catch (ActionAlreadyInUseException e) {
				RLog.error("Class " + className + " already assign to a other action", e);
				throw new SAXException("Class " + className + " already assign to a other action", e);
			}

			if (RLog.isTraceEnabled()) {
				RLog.trace(" - Action " + ret.getName() + " (" + className + ")");
			}

			return ret;
		} else if (elementType.equals("exception")) {
			ControllerConfig config = (ControllerConfig) currentObject;

			ExceptionConfig ret = new ExceptionConfig();

			ret.setClassName(attributes.getValue("class-name"));

			String forward = attributes.getValue("forward");

			ret.setForward(new Forward(forward));

			config.addException(ret);

			if (RLog.isTraceEnabled()) {
				RLog.trace(" - Exception " + ret.getClassName() + " (" + forward + ")");
			}

			return ret;
		} else if (elementType.equals("bundle")) {
			ControllerConfig config = (ControllerConfig) currentObject;

			String bundle = attributes.getValue("name");

			config.addBundle(bundle);

			if (RLog.isTraceEnabled()) {
				RLog.trace(" - Resource " + bundle + ")");
			}

			return null;
		}

		return null;
	}

	private WrapperChain getDefaultWrapperChain() {
		WrapperChain ret = new WrapperChain();

		ret.setName(Constants.DEFAULT_WRAPPER_CHAIN);
		
		return ret;
	}

	public void processPCDATA(String elementType, Object currentObject, String value) {		
		if (elementType.equals("url-pattern")) {
			if (RLog.isTraceEnabled()) {
				RLog.trace("      (" + value + ")");
			}
			
			((WrapperChain)currentObject).addPattern(value);
		}
	}

	public void error(SAXParseException error) throws SAXException {
		RLog.error("SAX Error", error);
		
		throw new SAXException(error);
	}
}
