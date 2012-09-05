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
package net.java.mega.common.http;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import net.java.mega.common.http.scope.Scope;
import net.java.mega.common.http.scope.ScopeUtil;
import net.java.mega.common.util.MegaCache;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;
import net.java.sjtools.util.BeanUtil;

public class WARContextUtil {

	private static Log log = LogFactory.getLog(WARContextUtil.class);

	public static Object getObject(PageContext context, String name) {
		if (log.isDebugEnabled()) {
			log.debug("getObject(PageContext, " + name + ")");
		}

		Scope scope = ScopeUtil.findAttribute(context, name);

		if (scope == null) {
			return null;
		} else {
			return scope.getAttribute(name);
		}
	}

	public static Object getObject(HttpServletRequest request, String name) {
		if (log.isDebugEnabled()) {
			log.debug("getObject(HttpServletRequest, " + name + ")");
		}

		Scope scope = ScopeUtil.findAttribute(request, name);

		if (scope == null) {
			return null;
		} else {
			return scope.getAttribute(name);
		}
	}

	public static Object getValue(PageContext context, String name, String propertyName) {
		if (log.isDebugEnabled()) {
			log.debug("getValue(PageContext, " + name + ", " + propertyName + ")");
		}

		Object ret = null;
		Object obj = getObject(context, name);

		if (obj != null) {
			if (propertyName != null) {
				try {
					ret = BeanUtil.getPropertyValue(MegaCache.getInstance(), obj, propertyName);
				} catch (Exception e) {
					log.error("Error while accessing property " + propertyName + " of attribute " + name, e);
				}
			} else {
				ret = obj;
			}
		}

		return ret;
	}

	public static Class getPropertyType(PageContext context, String name, String propertyName) {
		if (log.isDebugEnabled()) {
			log.debug("getPropertyType(PageContext, " + name + ", " + propertyName + ")");
		}

		Object obj = getObject(context, name);
		Class ret = null;

		if (obj != null) {
			BeanUtil beanUtil = new BeanUtil(obj);

			String methodName = beanUtil.getGetMethodName(propertyName);

			Method method = MegaCache.getInstance().get(obj.getClass(), methodName);

			if (method == null) {
				List methods = beanUtil.getMethods(methodName);

				if (methods.size() != 1) {
					log.error("Property " + propertyName + " not found on bean " + beanUtil.getClassName());
					throw new RuntimeException("Property " + propertyName + " not found on bean " + beanUtil.getClassName());
				}

				method = (Method) methods.get(0);

				MegaCache.getInstance().add(obj.getClass(), methodName, method);
			}

			ret = method.getReturnType();
		} else {
			log.error("Object " + name + " not found!");
			throw new RuntimeException("Object " + name + " not found!");
		}

		return ret;
	}
}
