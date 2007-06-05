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
package net.java.mega.common.http.scope;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

public class RequestScope implements Scope {
	private HttpServletRequest context = null;
	
	public RequestScope(HttpServletRequest context) {
		this.context = context;
	}

	public void setAttribute(String name, Object value) {
		context.setAttribute(name, value);
	}

	public Object getAttribute(String name) {
		return context.getAttribute(name);
	}

	public void removeAttribute(String name) {
		context.removeAttribute(name);
	}

	public boolean existsAttribute(String name) {
		Enumeration names = context.getAttributeNames();
		
		while (names.hasMoreElements()) {
			if (name.equals(names.nextElement())) {
				return true;
			}
		}
		
		return false;
	}

	public Scope getNextScope() {
		return new SessionScope(context.getSession(true));
	}
}
