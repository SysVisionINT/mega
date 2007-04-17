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

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public class NavigationUtil {
	private static Log log = LogFactory.getLog(NavigationUtil.class);

	public static void forward(HttpServletRequest request, HttpServletResponse response, String url)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);

		if (dispatcher == null) {
			log.error("Can't find url '" + url + "'");

			throw new RuntimeException("Can't find url '" + url + "'");
		}

		if (log.isDebugEnabled()) {
			log.debug("Forward to " + url);
		}

		dispatcher.forward(request, response);
	}

	public static void redirect(HttpServletRequest request, HttpServletResponse response, String url)
			throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("Redirect to " + url);
		}

		response.sendRedirect(url);
	}
}
