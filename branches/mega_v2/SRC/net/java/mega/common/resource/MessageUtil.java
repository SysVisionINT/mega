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
package net.java.mega.common.resource;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import net.java.sjtools.logging.plus.RLog;
import net.java.sjtools.util.TextUtil;

public class MessageUtil {
	public static String getMessage(List bundleList, String messageKey, Locale locale) {
		if (RLog.isTraceEnabled()) {
			RLog.trace("getMessage([" +TextUtil.toString(bundleList) + "], "+  messageKey + ", " + locale + ")");
		}

		String message = null;
		String bundleName = null;
		ResourceBundle bundle = null;

		for (Iterator i = bundleList.iterator(); i.hasNext();) {
			bundleName = (String) i.next();

			if (RLog.isTraceEnabled()) {
				RLog.trace("bundleName = " + bundleName);
			}

			bundle = ResourceBundle.getBundle(bundleName, locale, Thread.currentThread().getContextClassLoader());

			if (bundle != null) {
				try {
					message = bundle.getString(messageKey);
				} catch (Throwable e) {}

				if (message != null) {
					return message;
				}
			}
		}

		return null;
	}
}
