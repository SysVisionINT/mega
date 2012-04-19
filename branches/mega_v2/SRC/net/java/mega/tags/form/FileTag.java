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
import javax.servlet.jsp.tagext.Tag;

import net.java.mega.tags.model.Attribute;
import net.java.mega.tags.model.InputBaseTag;
import net.java.sjtools.logging.plus.RLog;
import net.java.sjtools.util.TextUtil;

public class FileTag extends InputBaseTag {
	private static final long serialVersionUID = 4288262712585202635L;

	private String size = null;

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public int writeStartTag() throws JspException {
		return NOT_INCLUDE_INNER_HTML;
	}

	public void writeEndTag() throws JspException {
		try {
			pageContext.getOut().print("<input type=\"file\" name=\"");
			pageContext.getOut().print(getProperty());

			if (!TextUtil.isEmptyString(getSize())) {
				pageContext.getOut().print("\" size=\"");
				pageContext.getOut().print(getSize());
			}

			pageContext.getOut().print("\"");

			writeAttributes();

			pageContext.getOut().println("/>");
		} catch (Exception e) {
			RLog.error("Error while writing INPUT file TAG", e);
			throw new JspException(e);
		}
	}

	public void initTag() {
		Tag tag = findAncestorWithClass(this, FormTag.class);
		
		if (tag != null) {
			((FormTag)tag).addAttribute(new Attribute("enctype", "multipart/form-data"));
		}		
	}
}
