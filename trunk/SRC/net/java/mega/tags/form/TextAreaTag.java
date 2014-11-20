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

import net.java.mega.common.http.HTMLUtil;
import net.java.mega.tags.model.Attribute;
import net.java.mega.tags.model.InputBaseTag;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public class TextAreaTag extends InputBaseTag {

	private static final long serialVersionUID = 6220131178083682726L;

	private static Log log = LogFactory.getLog(TextAreaTag.class);

	private String cols = null;
	private String rows = null;

	public String getCols() {
		return cols;
	}

	public void setCols(String cols) {
		this.cols = cols;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public int writeStartTag() throws JspException {
		if (getCols() != null) {
			addAttribute(new Attribute("cols", getCols()));
		}

		if (getRows() != null) {
			addAttribute(new Attribute("rows", getRows()));
		}

		return NOT_INCLUDE_INNER_HTML;
	}

	public void writeEndTag() throws JspException {
		try {
			pageContext.getOut().print("<textarea name=\"");
			pageContext.getOut().print(getProperty());
			pageContext.getOut().print("\"");

			writeAttributes();

			pageContext.getOut().print(">");

			Object value = getPropertyValue();

			if (value != null) {
				if (value instanceof String) {
					value = HTMLUtil.filter((String) value);
				}

				pageContext.getOut().print(value);
			}

			pageContext.getOut().println("</textarea>");
		} catch (Exception e) {
			log.error("Error while writing INPUT textarea TAG", e);
			throw new JspException(e);
		}
	}

	public void initTag() {}
}
