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

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

public class LocaleUtil {
	private static final String LOCALE = "MEGA_ACTION_LOCALE";
	
	public static Locale getUserLocate(HttpServletRequest request) {
		Locale locale = (Locale) request.getSession(true).getAttribute(LOCALE);
		
		if (locale ==null) {
			locale = request.getLocale();
		}
		
		return locale;
	}
	
	public static void setUserLocate(HttpServletRequest request, Locale locale) {
		request.getSession(true).setAttribute(LOCALE, locale);
	}	
}
