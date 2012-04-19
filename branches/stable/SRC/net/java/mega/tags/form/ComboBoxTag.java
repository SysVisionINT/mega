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

import net.java.mega.tags.model.InputBaseTag;
import net.java.mega.tags.model.SelectBox;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public class ComboBoxTag extends InputBaseTag implements SelectBox {
	private static final long serialVersionUID = 1891172739255099324L;

	private static Log log = LogFactory.getLog(ComboBoxTag.class);

	public int writeStartTag() throws JspException {
		try {
			pageContext.getOut().print("<select name=\"");
			pageContext.getOut().print(getProperty());
			pageContext.getOut().print("\"");
			writeAttributes();
			pageContext.getOut().println(">");
		} catch (Exception e) {
			log.error("Error while writing SELECT (comboBox) TAG", e);
			throw new JspException(e);
		}
		
		return INCLUDE_INNER_HTML;
	}
	
	public void writeEndTag() throws JspException {
		try {
			pageContext.getOut().println("</select>");
		} catch (Exception e) {
			log.error("Error while writing SELECT (comboBox) TAG", e);
			throw new JspException(e);
		}
	}

	public void initTag() {		
	}

	public boolean isSelected(String value) {
		Object object = getPropertyValue();
		
		if (object == null) {
			return false;
		}
		
		return value.equals(String.valueOf(object));
	}
}
