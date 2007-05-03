/*  ------------------
 *  MEGA Web Framework
 *  ------------------
 *  
 *  Copyright 2006 SysVision - Consultadoria e Desenvolvimento em Sistemas de Inform�tica, Lda.
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

import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public class FormatUtil {
	private static Log log = LogFactory.getLog(FormatUtil.class);

	public static String format(Object obj, Locale locale) {
		if (log.isDebugEnabled()) {
			log.debug("format(" + obj + ")");
		}

		return format(obj, null, locale);
	}

	public static String format(Object obj, String format, Locale locale) {
		if (log.isDebugEnabled()) {
			log.debug("format(" + obj + "," + format + ")");
		}

		if (obj == null) {
			return "";
		}

		Format formater = null;

		if (obj instanceof String) {
			return HTMLUtil.filter((String) obj);
		} else if (obj instanceof Number) {
			String formatString = format;

			if (format == null) {
				formatString = retrieveFormatString(obj.getClass().getName(), locale);
				
				if (log.isDebugEnabled()) {
					log.debug(obj.getClass().getName() + "  -> formatString = " + formatString);
				}
			}

			if (formatString != null) {
				formater = NumberFormat.getNumberInstance();

				((DecimalFormat) formater).applyLocalizedPattern(formatString);
			}
		} else if (obj instanceof java.util.Date) {
			String formatString = format;

			if (format == null) {
				formatString = retrieveFormatString(obj.getClass().getName(), locale);
				
				if (log.isDebugEnabled()) {
					log.debug(obj.getClass().getName() + "  -> formatString = " + formatString);
				}
			}

			if (formatString != null) {
				formater = new SimpleDateFormat(formatString);
			}
		}

		if (formater != null) {
			return formater.format(obj);
		} else {
			return obj.toString();
		}
	}

	private static String retrieveFormatString(String key, Locale locale) {
		return ActionMessageUtil.getMessage(key, locale);
	}
}