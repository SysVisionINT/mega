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

import javax.servlet.jsp.JspException;

import net.java.mega.tags.model.Attribute;
import net.java.mega.tags.model.InputTag;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public class InputTextTag extends InputTag {
	private static final long serialVersionUID = 6592419051534858377L;

	private static Log log = LogFactory.getLog(InputTextTag.class);

	private String size = null;
	private String maxLength = null;

	public String getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public int writeStartTag() throws JspException {
		if (getSize() != null) {
			addAttribute(new Attribute("size", getSize()));
		}

		if (getMaxLength() != null) {
			addAttribute(new Attribute("maxlength", getMaxLength()));
		}
		
		return NOT_INCLUDE_INNER_HTML;
	}

	public void writeEndTag() throws JspException {
		try {
			pageContext.getOut().print("<input type=\"");
			pageContext.getOut().print(getType());
			pageContext.getOut().print("\" name=\"");
			pageContext.getOut().print(getProperty());
			pageContext.getOut().print("\"");

			Object value = getPropertyValue();

			if (value != null) {
				pageContext.getOut().print(" value=\"");
				pageContext.getOut().print(value);
				pageContext.getOut().print("\"");
			}

			writeAttributes();

			pageContext.getOut().println("/>");
		} catch (Exception e) {
			log.error("Error while writing INPUT " + getType() + " TAG", e);
			throw new JspException(e);
		}
	}

	protected String getType() {
		return "text";
	}

	public void initTag() {
	}
}
