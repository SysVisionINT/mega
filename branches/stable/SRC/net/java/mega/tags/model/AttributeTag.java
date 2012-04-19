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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

public class AttributeTag extends BodyTagSupport{
	private static final long serialVersionUID = 8993694992328452439L;
	
	private String name = null;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int doEndTag() throws JspException {		
		if (getBodyContent() != null && getBodyContent().getString() != null) {
			Tag tag = findAncestorWithClass(this, AttributeContainer.class);
			
			if (tag != null) {
				((AttributeContainer)tag).addAttribute(new Attribute(getName(), getBodyContent().getString().trim()));
			}
		}

		return EVAL_PAGE;	
	}
}
