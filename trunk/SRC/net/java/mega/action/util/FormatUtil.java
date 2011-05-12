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

import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import net.java.mega.common.http.HTMLUtil;
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
		return format(obj, format, locale, true);
	}

	public static String format(Object obj, String format, Locale locale, boolean filter) {
		if (log.isDebugEnabled()) {
			log.debug("format(" + obj + "," + format + ")");
		}

		if (obj == null) {
			return "";
		}

		String retString = null;

		if (obj instanceof String) {
			retString = (String) obj;
		} else {
			Format formater = null;

			if (obj instanceof Number) {
				String formatString = format;

				if (format == null) {
					formatString = retrieveFormatString(obj.getClass(), locale);

					if (log.isDebugEnabled()) {
						log.debug(obj.getClass().getName() + "  -> formatString = " + formatString);
					}
				}

				if (formatString != null) {
					formater = NumberFormat.getNumberInstance(locale);

					((DecimalFormat) formater).applyLocalizedPattern(formatString);
				}
			} else if (obj instanceof java.util.Date) {
				String formatString = format;

				if (format == null) {
					formatString = retrieveFormatString(obj.getClass(), locale);

					if (log.isDebugEnabled()) {
						log.debug(obj.getClass().getName() + "  -> formatString = " + formatString);
					}
				}

				if (formatString != null) {
					formater = new SimpleDateFormat(formatString, locale);
				}
			}

			if (formater != null) {
				retString = formater.format(obj);
			} else {
				retString = obj.toString();
			}
		}

		if (filter) {
			return HTMLUtil.filter(retString);
		} else {
			return retString;
		}
	}

	private static String retrieveFormatString(Class clazz, Locale locale) {
		String format = null;

		Class objectClass = clazz;

		do {
			format = ActionMessageUtil.getMessage(objectClass.getName(), locale);
			objectClass = objectClass.getSuperclass();
		} while (format == null && objectClass != null);


		return format;
	}
}
