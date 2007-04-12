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
package net.java.mega.action.wrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.java.mega.action.ActionManager;
import net.java.mega.action.RequestMetaData;
import net.java.mega.action.ResponseMetaData;
import net.java.mega.action.api.AbstractWrapper;


public class SessionValidatorWrapper extends AbstractWrapper {
	private static final long serialVersionUID = 3799931686999152093L;

	public ResponseMetaData execute(HttpServletRequest request, HttpServletResponse response,
			RequestMetaData requestMetaData) throws Exception {
		
		if (sessionObjectExists(request)){
			return executeNext(request, response, requestMetaData);
		} else {
			RequestMetaData forward = ActionManager.getInstance().getRequestMetaData(getProperty("forward-url"));
			
			return executeNext(request, response, forward);
		}
	}

	private boolean sessionObjectExists(HttpServletRequest request) {
		if (request.getSession(true).getAttribute(getProperty("attribute-name")) != null) {
			return true;
		}
		
		return false;
	}

}
