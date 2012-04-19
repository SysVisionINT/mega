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
import javax.servlet.jsp.tagext.Tag;

import net.java.mega.tags.form.FormTag;
import net.java.sjtools.logging.plus.RLog;

public class EventInputArgTag extends net.java.mega.action.tag.events.EventInputArgTag {
	private static final long serialVersionUID = -2635263555688921251L;

	public int doEndTag() throws JspException {
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
					RLog.error("No FORM TAG for INPUT TAG " + getInputName());
					throw new JspException("No FORM TAG for INPUT TAG " + getInputName());
				}
			}
		}
		
		addEventArg(form, input);

		return EVAL_PAGE;	
	}
}
