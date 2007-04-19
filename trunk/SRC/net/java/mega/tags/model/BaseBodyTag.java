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
package net.java.mega.tags.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public abstract class BaseBodyTag extends BodyTagSupport implements AttributeContainer {
	private static final long serialVersionUID = 2098390912252264943L;

	private static Log log = LogFactory.getLog(BaseBodyTag.class);

	private String style = null;
	private String className = null;
	private List attributes = null;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public final int doStartTag() {
		attributes = new ArrayList();

		initTag();

		return EVAL_BODY_BUFFERED;
	}

	public abstract void initTag();

	public abstract void writeStartTag() throws JspException;

	public abstract void writeEndTag() throws JspException;

	public final int doEndTag() throws JspException {
		writeStartTag();

		try {
			if (getBodyContent() != null && getBodyContent().getString() != null) {
				pageContext.getOut().print(getBodyContent().getString().trim());
			}
		} catch (IOException e) {
			log.error("Error while writing the body content", e);

			throw new JspException(e);
		}

		writeEndTag();

		return EVAL_PAGE;
	}

	public void writeAttributes() throws IOException {
		if (getId() != null) {
			writeAttribute("id", getId());
		}

		if (getClassName() != null) {
			writeAttribute("class", getClassName());
		}

		if (getStyle() != null) {
			writeAttribute("style", getStyle());
		}

		AttributeTag tag = null;

		for (Iterator i = attributes.iterator(); i.hasNext();) {
			tag = (AttributeTag) i.next();

			writeAttribute(tag.getName(), tag.getValue());
		}
	}

	public void addAttribute(AttributeTag tag) {
		attributes.add(tag);
	}
	
	private void writeAttribute(String name, String value) throws IOException {
		pageContext.getOut().print(" ");
		pageContext.getOut().print(name);
		pageContext.getOut().print("=\"");
		pageContext.getOut().print(value);
		pageContext.getOut().print("\"");
	}
}
