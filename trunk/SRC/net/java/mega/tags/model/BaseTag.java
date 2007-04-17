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

import javax.servlet.jsp.tagext.TagSupport;

public class BaseTag extends TagSupport {
	private static final long serialVersionUID = -4278177955235598606L;

	private String style = null;
	private String className = null;

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

	public void writeAttributes() throws IOException {
		if (getId() != null) {
			addAttribute("id", getId());
		}

		if (getClassName() != null) {
			addAttribute("class", getClassName());
		}

		if (getStyle() != null) {
			addAttribute("style", getStyle());
		}
	}

	private void addAttribute(String name, String value) throws IOException {
		pageContext.getOut().print(" ");
		pageContext.getOut().print(name);
		pageContext.getOut().print("=\"");
		pageContext.getOut().print(value);
		pageContext.getOut().print("\"");
	}
}
