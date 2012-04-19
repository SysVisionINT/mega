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

import net.java.sjtools.logging.plus.RLog;

public abstract class BaseBodyTag extends BodyTagSupport implements AttributeContainer {
	private static final long serialVersionUID = 2098390912252264943L;

	public static int INCLUDE_INNER_HTML = 1;
	public static int NOT_INCLUDE_INNER_HTML = 2;

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

	public abstract int writeStartTag() throws JspException;

	public abstract void writeEndTag() throws JspException;

	public final int doEndTag() throws JspException {
		int include = writeStartTag();

		if (include == INCLUDE_INNER_HTML) {
			try {
				if (getBodyContent() != null && getBodyContent().getString() != null) {
					pageContext.getOut().print(getInnerHtml());
				}
			} catch (IOException e) {
				RLog.error("Error while writing the body content", e);

				throw new JspException(e);
			}
		}

		writeEndTag();

		return EVAL_PAGE;
	}

	public String getInnerHtml() {
		if (getBodyContent() != null && getBodyContent().getString() != null) {
			return getBodyContent().getString().trim();
		}

		return null;
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

		Attribute attribute = null;

		for (Iterator i = attributes.iterator(); i.hasNext();) {
			attribute = (Attribute) i.next();

			writeAttribute(attribute.getName(), attribute.getValue());
		}
	}

	public void addAttribute(Attribute attribute) {
		if (attributes.contains(attribute)) {
			attributes.remove(attribute);
		}

		attributes.add(attribute);
	}

	public String deleteAttribute(String name) {
		Attribute attribute = new Attribute(name, null);
		
		int pos = attributes.indexOf(attribute);
		
		if (pos >= 0) {
			attribute = (Attribute) attributes.get(pos);
			attributes.remove(attribute);
		}
		
		return attribute.getValue();
	}

	private void writeAttribute(String name, String value) throws IOException {
		pageContext.getOut().print(" ");
		pageContext.getOut().print(name);
		pageContext.getOut().print("=\"");
		pageContext.getOut().print(value);
		pageContext.getOut().print("\"");
	}
}
