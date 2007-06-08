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
package net.java.mega.action.util;

import javax.servlet.http.HttpServletRequest;

import net.java.mega.common.http.RequestUtil;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;
import net.java.sjtools.util.TextUtil;

public class ActionRequestUtil extends RequestUtil {
	private static Log log = LogFactory.getLog(ActionRequestUtil.class);

	public static String getAction(HttpServletRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("getAction(...)");
		}

		if (!WorkflowControlUtil.isTheSameRequest(request)) {
			String path = request.getParameter(Constants.MEGA_FORM_ACTION);

			if (!TextUtil.isEmptyString(path)) {
				return path;
			}
		}

		return getPath(request);
	}
}
