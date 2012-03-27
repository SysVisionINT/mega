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
package net.java.mega.tags.events;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import net.java.mega.tags.form.FormTag;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public class EventInputArgTag extends BodyTagSupport {
	private static final long serialVersionUID = -2635263555688921251L;
	
	private static Log log = LogFactory.getLog(EventInputArgTag.class);
	
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
		Tag tag = findAncestorWithClass(this, EventTag.class);
		
		if (tag != null) {
			String form = getFormId();
			String input = getInputName();
			
			if (input == null) {
				form = null;
				input = "this";
			} else {
				if (form == null) {
					Tag formTag = findAncestorWithClass(this, FormTag.class);
					
					if (formTag != null) {
						form = ((FormTag) formTag).getId();
					} else {
						log.error("No FORM TAG for INPUT TAG " + getInputName());
						throw new JspException("No FORM TAG for INPUT TAG " + getInputName());
					}
				}
			}
			
			StringBuffer buffer = new StringBuffer();
			
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

		return EVAL_PAGE;	
	}
}
