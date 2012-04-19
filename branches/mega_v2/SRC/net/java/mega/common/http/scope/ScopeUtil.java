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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

public class ScopeUtil {
	public static Scope getPageScope(PageContext context) {
		return new PageScope(context);
	}

	public static Scope getRequestScope(HttpServletRequest context) {
		return new RequestScope(context);
	}

	public static Scope getSessionScope(HttpServletRequest context) {
		return getRequestScope(context).getNextScope();
	}

	public static Scope getApplicationScope(HttpServletRequest context) {
		return getSessionScope(context).getNextScope();
	}

	public static Scope findAttribute(PageContext context, String name) {
		Scope scope = getPageScope(context);

		while (scope != null && !scope.existsAttribute(name)) {
			scope = scope.getNextScope();
		}

		return scope;
	}
	
	public static Scope findAttribute(HttpServletRequest context, String name) {
		Scope scope = getRequestScope(context);

		while (scope != null && !scope.existsAttribute(name)) {
			scope = scope.getNextScope();
		}

		return scope;
	}	
}
