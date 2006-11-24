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

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import net.java.mega.action.ActionManager;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public class MessageUtil {
	private static Log log = LogFactory.getLog(MessageUtil.class);

	public static String getMessage(String messageKey, Locale locale) {
		if (log.isDebugEnabled()) {
			log.debug("getMessage(" + messageKey + ", " + locale + ")");
		}

		List bundleList = ActionManager.getInstance().getBundleList();

		String message = null;
		String bundleName = null;
		ResourceBundle bundle = null;

		for (Iterator i = bundleList.iterator(); i.hasNext();) {
			bundleName = (String) i.next();

			if (log.isDebugEnabled()) {
				log.debug("bundleName = " + bundleName);
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
