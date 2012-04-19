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
package net.java.mega.action.tag.events;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

public class EventInputArgTag extends TagSupport {
	private static final long serialVersionUID = -3853636390856473629L;
	
	private String name = null;
	private String formId = null;
	private String inputName = null;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}
	
	public String getInputName() {
		return inputName;
	}

	public void setInputName(String inputName) {
		this.inputName = inputName;
	}

	public int doEndTag() throws JspException {		
		addEventArg(getFormId(), getInputName());

		return EVAL_PAGE;	
	}

	protected void addEventArg(String form, String input) {
		Tag tag = findAncestorWithClass(this, EventTag.class);
		
		if (tag != null) {			
			StringBuilder buffer = new StringBuilder();
			
			if (form == null) {
				buffer.append("getValue(");
				buffer.append(input);
				buffer.append(")");
			} else {
				buffer.append("getInputValue('");
				buffer.append(form);
				buffer.append("','");
				buffer.append(input);
				buffer.append("')");
			}
			
			((EventTag)tag).addEventArg(getName(), buffer.toString(), false);
		}
	}
}
