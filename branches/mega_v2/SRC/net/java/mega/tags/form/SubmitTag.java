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

import net.java.mega.tags.model.SubmitBaseTag;
import net.java.sjtools.logging.plus.RLog;

public class SubmitTag extends SubmitBaseTag {
	private static final long serialVersionUID = 6684109710319221195L;

	public void initTag() {}

	public int writeStartTag() throws JspException {
		return NOT_INCLUDE_INNER_HTML;
	}

	public void writeEndTag() throws JspException {
		String value = getInnerHtml();

		try {
			pageContext.getOut().print("<input type=\"button\" value=\"");
			pageContext.getOut().print(value);
			pageContext.getOut().print("\"");

			writeAttributes();

			pageContext.getOut().println("/>");
		} catch (Exception e) {
			RLog.error("Error while writing INPUT button TAG", e);
			throw new JspException(e);
		}
	}

	public boolean elementCanBeDisabled() {
		return true;
	}
}
