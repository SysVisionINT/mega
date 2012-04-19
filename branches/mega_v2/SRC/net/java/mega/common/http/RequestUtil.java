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

import javax.servlet.http.HttpServletRequest;

import net.java.mega.common.util.CommonConstants;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public class RequestUtil {
	private static Log log = LogFactory.getLog(RequestUtil.class);

	public static String getPath(HttpServletRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("getPath(...)");
		}

		String path = (String) request.getAttribute(CommonConstants.PATH_INFO);

		if (path == null) {
			path = request.getPathInfo();
		}

		if ((path != null) && (path.length() > 0)) {
			return path;
		}

		path = (String) request.getAttribute(CommonConstants.SERVLET_PATH);

		if (path == null) {
			path = request.getServletPath();
		}

		int slash = path.lastIndexOf("/");
		int period = path.lastIndexOf(".");

		if ((period >= 0) && (period > slash)) {
			path = path.substring(0, period);
		}

		return path;
	}
}
