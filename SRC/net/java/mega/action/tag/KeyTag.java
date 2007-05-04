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
import net.java.mega.common.http.HTMLUtil;
import net.java.mega.common.resource.LocaleUtil;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public class KeyTag extends TagSupport {
	private static final long serialVersionUID = 1824165782784735482L;

	private static Log log = LogFactory.getLog(KeyTag.class);

	private String key = null;
	private boolean filter = true;

	public int doEndTag() throws JspException {
		Locale locale = LocaleUtil.getUserLocate((HttpServletRequest) pageContext.getRequest());

		String message = ActionMessageUtil.getMessage(key, locale);
		
		if (filter) {
			message = HTMLUtil.filter(message);
		}

		try {
			pageContext.getOut().print(message);
		} catch (IOException e) {
			log.error("Error while writing message for key = " + key, e);

			throw new JspException(e);
		}

		return EVAL_PAGE;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isFilter() {
		return filter;
	}

	public void setFilter(boolean filter) {
		this.filter = filter;
	}
}
