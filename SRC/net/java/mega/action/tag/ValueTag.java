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
package net.java.mega.action.tag;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.java.mega.action.util.ActionMessageUtil;
import net.java.mega.action.util.Constants;
import net.java.mega.action.util.FormatUtil;
import net.java.mega.common.http.HTMLUtil;
import net.java.mega.common.http.WARContextUtil;
import net.java.mega.common.http.scope.Scope;
import net.java.mega.common.http.scope.ScopeUtil;
import net.java.mega.common.resource.LocaleUtil;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;
import net.java.sjtools.util.TextUtil;

public class ValueTag extends TagSupport {

	private static final long serialVersionUID = 1824165782784735482L;

	private static Log log = LogFactory.getLog(ValueTag.class);

	private String name = Constants.CURRENT_ACTION;
	private String property = null;
	private String format = null;
	private boolean filter = true;
	private String defaultValue = null;

	public int doEndTag() throws JspException {
		Locale locale = LocaleUtil.getUserLocate((HttpServletRequest) pageContext.getRequest());
		
		try {	
			Scope scope = ScopeUtil.findAttribute(pageContext, name);

			if (scope == null) {
				if (!TextUtil.isEmptyString(defaultValue)) {
					pageContext.getOut().print("[");
					pageContext.getOut().print(name);

					if (property != null) {
						pageContext.getOut().print(".");
						pageContext.getOut().print(property);
					}

					pageContext.getOut().print("]");
				} else {
					pageContext.getOut().print(getKeyValue(defaultValue, locale));
				}
			} else {
				Object value = WARContextUtil.getValue(pageContext, name, property);

				if (value == null && !TextUtil.isEmptyString(defaultValue)) {
					pageContext.getOut().print(getKeyValue(defaultValue, locale));
				} else {
					pageContext.getOut().print(FormatUtil.format(value, format, locale, filter));
				}
			}
		} catch (IOException e) {
			log.error("Error while formating " + name + (property == null ? "." + property : "") + " with " + format, e);

			throw new JspException(e);
		}

		return EVAL_PAGE;
	}

	private String getKeyValue(String key, Locale locale) {
		String message = ActionMessageUtil.getMessage(key, locale);

		if (message != null) {
			if (filter) {
				message = HTMLUtil.filter(message);
			}
		} else {
			message = "{".concat(key).concat("}");
		}
		
		return message;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public boolean isFilter() {
		return filter;
	}

	public void setFilter(boolean filter) {
		this.filter = filter;
	}

	public String getDefault() {
		return defaultValue;
	}

	public void setDefault(String defaultValue) {
		this.defaultValue = defaultValue;
	}
}
