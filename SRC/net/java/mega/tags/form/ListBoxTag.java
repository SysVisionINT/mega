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

import javax.servlet.jsp.JspException;

import net.java.mega.action.util.Constants;
import net.java.mega.common.util.WARContextUtil;
import net.java.mega.tags.model.InputBaseTag;
import net.java.mega.tags.model.SelectBox;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public class ListBoxTag extends InputBaseTag implements SelectBox {
	private static final long serialVersionUID = 9074354870725252355L;

	private static Log log = LogFactory.getLog(ListBoxTag.class);

	private String size = null;

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public int writeStartTag() throws JspException {
		try {
			pageContext.getOut().print("<select name=\"");
			pageContext.getOut().print(getProperty());
			pageContext.getOut().print("\" size=\"");
			pageContext.getOut().print(getSize());
			pageContext.getOut().print("\"");
			writeAttributes();

			if (isMultiSelect()) {
				pageContext.getOut().print(" multiple=\"multiple\"");
			}

			pageContext.getOut().println(">");
		} catch (Exception e) {
			log.error("Error while writing SELECT (listBox) TAG", e);
			throw new JspException(e);
		}

		return INCLUDE_INNER_HTML;
	}

	private boolean isMultiSelect() {
		Class returnType = WARContextUtil.getPropertyType(pageContext, Constants.CURRENT_ACTION, getProperty());
		return (Collection.class.isAssignableFrom(returnType));
	}

	public void writeEndTag() throws JspException {
		try {
			pageContext.getOut().println("</select>");
		} catch (Exception e) {
			log.error("Error while writing SELECT (listBox) TAG", e);
			throw new JspException(e);
		}
	}

	public void initTag() {
	}

	public boolean isSelected(String value) {		
		if (isMultiSelect()) {
			Collection list = (Collection) getPropertyValue();

			if (list != null && list.contains(value)) {
				return true;
			}

			return false;
		} else {
			Object object = getPropertyValue();

			if (object == null) {
				return false;
			}

			return value.equals(String.valueOf(object));
		}
	}
}
