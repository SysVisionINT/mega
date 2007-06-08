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
package net.java.mega.common.load;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public class LoadControlFilter implements Filter {
	private static Log log = LogFactory.getLog(LoadControlFilter.class);

	public void destroy() {
	}

	public void init(FilterConfig config) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {

		if (!(response instanceof HttpServletResponse)) {
			chain.doFilter(request, response);
			return;
		}
		
		LoadControl control = LoadControlUtil.getLoadControl((HttpServletRequest) request);

		try {
			synchronized (control) {
				if (control.isLock()) {
					if (log.isDebugEnabled()) {
						log.debug("Duplicated request!");
					}
					
					control.setResponse((HttpServletResponse) response);

					try {
						control.wait();
					} catch (InterruptedException e) {
						log.error("Runtime error", e);
					}

					return;
				}

				control.setLock(true);
			}

			ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) response);

			chain.doFilter(request, responseWrapper);
			
			if (log.isDebugEnabled()) {
				log.debug("Request executed!");
			}

			control = LoadControlUtil.getLoadControl((HttpServletRequest) request);

			synchronized (control) {
				HttpServletResponse lastResponse = control.getResponse();

				if (lastResponse == null) {
					lastResponse = (HttpServletResponse) response;
				}

				responseWrapper.update(lastResponse);

				control.setResponse(null);
			}
		} catch (Throwable e) {
			log.error("Runtime error", e);
		} finally {
			control = LoadControlUtil.getLoadControl((HttpServletRequest) request);

			synchronized (control) {
				control.setLock(false);
				control.notifyAll();
			}
		}
	}
}
