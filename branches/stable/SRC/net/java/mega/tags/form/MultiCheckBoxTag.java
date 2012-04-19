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
package net.java.mega.tags.form;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import net.java.mega.action.util.MultiCheckBoxUtil;
import net.java.mega.tags.model.InputBaseTag;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public class MultiCheckBoxTag extends InputBaseTag {
	private static final long serialVersionUID = 4288262712585202635L;

	private static Log log = LogFactory.getLog(MultiCheckBoxTag.class);

	public int writeStartTag() throws JspException {
		return NOT_INCLUDE_INNER_HTML;
	}

	public void writeEndTag() throws JspException {
		String value = getInnerHtml();

		try {
			if (isFirstCheck()) {
				pageContext.getOut().print("<input type=\"hidden\" name=\"");
				pageContext.getOut().print(MultiCheckBoxUtil.getHiddenName(getProperty()));
				pageContext.getOut().println("\" value=\"IN\"/>");
			}

			pageContext.getOut().print("<input type=\"checkbox\" name=\"");
			pageContext.getOut().print(getProperty());
			pageContext.getOut().print("\" value=\"");
			pageContext.getOut().print(value);
			pageContext.getOut().print("\"");

			writeAttributes();

			Collection list = (Collection) getPropertyValue();

			if (list != null && list.contains(value)) {
				pageContext.getOut().print(" checked=\"checked\"");
			}

			pageContext.getOut().println("/>");
		} catch (Exception e) {
			log.error("Error while writing INPUT (multi)checkbox TAG", e);
			throw new JspException(e);
		}
	}

	private boolean isFirstCheck() {
		Tag tag = findAncestorWithClass(this, FormTag.class);

		if (tag != null) {
			FormTag formTag = (FormTag) tag;
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

			String formName = formTag.getId();
			String control = getControlName(formName, getProperty());

			if (request.getAttribute(control) != null) {
				return false;
			} else {
				request.setAttribute(control, "MULTICHECKBOX");

				return true;
			}
		}

		return false;
	}

	private String getControlName(String formName, String property) {
		StringBuffer buffer = new StringBuffer();

		buffer.append(formName);
		buffer.append("_C_");
		buffer.append(property);

		return buffer.toString();
	}

	public void initTag() {
	}
}
