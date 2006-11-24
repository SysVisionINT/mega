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

import net.java.mega.action.util.Constants;
import net.java.mega.action.util.FormatUtil;
import net.java.mega.action.util.LocaleUtil;
import net.java.mega.common.util.PageContextUtil;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public class ValueTag extends TagSupport {
	private static final long serialVersionUID = 1824165782784735482L;

	private static Log log = LogFactory.getLog(ValueTag.class);

	private String className = Constants.CURRENT_ACTION;
	private String property = null;
	private String format = null;

	public int doEndTag() throws JspException {
		try {
			Object value = PageContextUtil.getObject(pageContext, className, property);

			Locale locale = LocaleUtil.getUserLocate((HttpServletRequest) pageContext.getRequest());

			pageContext.getOut().print(FormatUtil.format(value, format, locale));
		} catch (IOException e) {
			log.error("Error while formating " + className + (property == null ? "." + property : "") + " with "
					+ format, e);

			throw new JspException(e);
		}

		return EVAL_PAGE;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
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
}
