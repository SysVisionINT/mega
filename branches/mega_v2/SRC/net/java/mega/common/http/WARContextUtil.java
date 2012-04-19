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
import net.java.sjtools.logging.plus.RLog;
import net.java.sjtools.util.BeanUtil;

public class WARContextUtil {

	public static Object getObject(Scope scope, String name) {
		if (RLog.isTraceEnabled()) {
			RLog.trace("getObject(Scope, " + name + ")");
		}

		if (scope == null) {
			return null;
		} else {
			return scope.getAttribute(name);
		}
	}

	public static Object getObject(HttpServletRequest request, String name) {
		if (RLog.isTraceEnabled()) {
			RLog.trace("getObject(HttpServletRequest, " + name + ")");
		}

		Scope scope = ScopeUtil.findAttribute(request, name);

		return getObject(scope, name);
	}

	public static Object getValue(Scope scope, String name, String propertyName) {
		if (RLog.isTraceEnabled()) {
			RLog.trace("getValue(Scope, " + name + ", " + propertyName + ")");
		}

		Object ret = null;
		Object obj = scope.getAttribute(name);

		if (obj != null) {
			if (propertyName != null) {
				try {
					ret = BeanUtil.getPropertyValue(MegaCache.getInstance(), obj, propertyName);
				} catch (Exception e) {
					RLog.error("Error while accessing property " + propertyName + " of attribute " + name, e);
				}
			} else {
				ret = obj;
			}
		}

		return ret;
	}
	
	public static Object getValue(PageContext context, String name, String propertyName) {
		if (RLog.isTraceEnabled()) {
			RLog.trace("getValue(PageContext, " + name + ", " + propertyName + ")");
		}

		Scope scope = ScopeUtil.findAttribute(context, name);
		
		if (scope == null) {
			return null;
		} else {
			return getValue(scope, name, propertyName);
		}
	}

	public static Class getPropertyType(PageContext context, String name, String propertyName) {
		if (RLog.isTraceEnabled()) {
			RLog.trace("getPropertyType(PageContext, " + name + ", " + propertyName + ")");
		}

		Scope scope = ScopeUtil.findAttribute(context, name);
		Object obj = getObject(scope, propertyName);
		
		Class ret = null;

		if (obj != null) {
			BeanUtil beanUtil = new BeanUtil(obj);

			String methodName = beanUtil.getGetMethodName(propertyName);

			Method method = MegaCache.getInstance().get(obj.getClass(), methodName);

			if (method == null) {
				List methods = beanUtil.getMethods(methodName);

				if (methods.size() != 1) {
					RLog.error("Property " + propertyName + " not found on bean " + beanUtil.getClassName());
					throw new RuntimeException("Property " + propertyName + " not found on bean " + beanUtil.getClassName());
				}

				method = (Method) methods.get(0);

				MegaCache.getInstance().add(obj.getClass(), methodName, method);
			}

			ret = method.getReturnType();
		} else {
			RLog.error("Object " + name + " not found!");
			throw new RuntimeException("Object " + name + " not found!");
		}

		return ret;
	}
}
