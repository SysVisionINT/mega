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

import net.java.mega.action.util.CheckBoxUtil;
import net.java.mega.tags.model.InputBaseTag;
import net.java.sjtools.logging.plus.RLog;

public class CheckBoxTag extends InputBaseTag {
	private static final long serialVersionUID = 4288262712585202635L;

	public int writeStartTag() throws JspException {
		return NOT_INCLUDE_INNER_HTML;
	}

	public void writeEndTag() throws JspException {
		try {
			pageContext.getOut().print("<input type=\"checkbox\" name=\"");
			pageContext.getOut().print(getProperty());
			pageContext.getOut().print("\" value=\"true\"");

			writeAttributes();
			
			Boolean value = (Boolean) getPropertyValue(); 
			
			if (value == null) {
				value = Boolean.FALSE;
			}
			
			if (value.booleanValue()) {
				pageContext.getOut().print(" checked=\"checked\"");
			}

			pageContext.getOut().print("/><input type=\"hidden\" name=\"");
			pageContext.getOut().print(CheckBoxUtil.getHiddenName(getProperty()));
			pageContext.getOut().println("\" value=\"false\"/>");
		} catch (Exception e) {
			RLog.error("Error while writing INPUT checkbox TAG", e);
			throw new JspException(e);
		}
	}

	public void initTag() {
	}
}
