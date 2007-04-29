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
package net.java.mega.common.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;
import net.java.sjtools.util.BeanUtil;

public class WARContextUtil {
	private static Log log = LogFactory.getLog(WARContextUtil.class);

	public static Object getObject(PageContext context, String name) {
		if (log.isDebugEnabled()) {
			log.debug("getObject(PageContext, " + name + ")");
		}

		Object obj = context.getAttribute(name);

		if (obj == null) {
			obj = getObject((HttpServletRequest) context.getRequest(), name);
		}

		return obj;
	}

	public static Object getObject(HttpServletRequest request, String name) {
		if (log.isDebugEnabled()) {
			log.debug("getObject(HttpServletRequest, " + name + ")");
		}

		Object obj = request.getAttribute(name);

		if (obj == null) {
			HttpSession session = request.getSession(true);

			obj = session.getAttribute(name);

			if (obj == null) {
				ServletContext servletContext = session.getServletContext();

				obj = servletContext.getAttribute(name);
			}
		}

		return obj;
	}

	public static Object getValue(PageContext context, String name, String propertyName) {
		if (log.isDebugEnabled()) {
			log.debug("getValue(" + name + ", " + propertyName + ")");
		}

		Object ret = null;
		Object obj = getObject(context, name);

		if (obj != null) {
			if (propertyName != null) {
				BeanUtil beanUtil = new BeanUtil(obj);

				try {
					ret = beanUtil.get(propertyName);
				} catch (Exception e) {
					log.error("Error while accessing property " + propertyName + " of attribute " + name, e);
				}
			} else {
				ret = obj;
			}
		}

		return ret;
	}
}
